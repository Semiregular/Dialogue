package cn.org.dialogue.service;

import cn.org.dialogue.dto.PageDTO;
import cn.org.dialogue.dto.UserMessageDTO;
import cn.org.dialogue.mapper.UserContactMapper;
import cn.org.dialogue.mapper.UserMessageMapper;
import cn.org.dialogue.po.UserContactPo;
import cn.org.dialogue.po.UserMessagePo;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.vo.UserContactVo;
import cn.org.dialogue.vo.UserMessageVo;
import cn.org.dialogue.websocket.WebSocketServer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author 周正明
 * @date 2024/2/2
 */
@Service
@Component
public class UserMessageService {
    @Autowired
    public UserMessageMapper userMessageMapper;
    @Autowired
    public UserContactMapper userContactMapper;
    @Autowired
    public ImageService imageService;

    public ResVo getUserContact(PageDTO pageDTO){
        Page<UserContactPo> page = new Page<>(1,8);
        QueryWrapper<UserContactPo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_to",pageDTO.getIdxUser())
                .or()
                .eq("idx_from",pageDTO.getIdxUser())
                .orderBy(true,true,"is_check")
                .orderBy(true,false,"gmt_modify");
        page = userContactMapper.selectPage(page,wrapper);
        List<UserContactPo> list = page.getRecords();
        List<UserContactVo> volist = new ArrayList<>();
        for(UserContactPo item : list){
            UserContactVo userContactVo = UserContactVo.convert(item);
            String photoFromUrl = imageService.getUserImageUrl(item.getPhotoFrom());
            userContactVo.setPhotoFromUrl(photoFromUrl);
            String photoToUrl = imageService.getUserImageUrl(item.getPhotoTo());
            userContactVo.setPhotoToUrl(photoToUrl);
            volist.add(userContactVo);
        }
        return ResVo.success(volist);
    }

    public ResVo updateUserContact(UserMessageDTO userMessageDTO){
        QueryWrapper<UserContactPo> wrapper = new QueryWrapper<>();
        UpdateWrapper<UserContactPo> updateWrapper = new UpdateWrapper<>();
        wrapper.and(wr->wr.eq("idx_to",userMessageDTO.getIdxTo())
                .eq("idx_from",userMessageDTO.getIdxFrom()));
        wrapper.or(wr->wr.eq("idx_from",userMessageDTO.getIdxTo())
                .eq("idx_to",userMessageDTO.getIdxFrom()));
        UserContactPo oldContact = userContactMapper.selectOne(wrapper);
        if(oldContact == null){
            oldContact = UserContactPo.insert(userMessageDTO);
            userContactMapper.insert(oldContact);
            return ResVo.success();
        }else{
            updateWrapper.eq("id",oldContact.getId())
                    .set("is_check",0);
            userContactMapper.update(null,updateWrapper);
            return ResVo.success();
        }
    }

    public ResVo getUserMessage(PageDTO pageDTO){
        Page<UserMessagePo> page = new Page<>(1,50);
        QueryWrapper<UserMessagePo> wrapper = new QueryWrapper<>();
        wrapper.and(wr->wr.eq("idx_to",pageDTO.getIdxTo())
                .eq("idx_from",pageDTO.getIdxFrom()));
        wrapper.or(wr->wr.eq("idx_from",pageDTO.getIdxTo())
                .eq("idx_to",pageDTO.getIdxFrom()));
        wrapper.orderBy(true,false,"gmt_create");
        page = userMessageMapper.selectPage(page,wrapper);
        List<UserMessagePo> list = page.getRecords();
        //list实现反序
        Collections.reverse(list);
        List<UserMessageVo> volist = new ArrayList<>();
        for(UserMessagePo item : list){
            UserMessageVo userMessageVo = UserMessageVo.convert(item);
            String photoFromUrl = imageService.getUserImageUrl(item.getPhotoFrom());
            userMessageVo.setPhotoFromUrl(photoFromUrl);
            volist.add(userMessageVo);
        }
        return ResVo.success(volist);
    }

    public int getUserMessageUncheck(Integer idxUser){
        QueryWrapper<UserMessagePo> wrapper = new QueryWrapper<>();
        wrapper.select("id")
                .eq("idx_to",idxUser)
                .eq("is_check",0);
        return userMessageMapper.selectCount(wrapper).intValue();
    }

    public ResVo sendUserMessage(UserMessageDTO userMessageDTO){
        //插入数据库
        UserMessagePo userMessagePo = UserMessagePo.insert(userMessageDTO);
        int isInsertSuccess = userMessageMapper.insert(userMessagePo);
        //更新用户权重
        QueryWrapper<UserContactPo> wrapper = new QueryWrapper<>();
        wrapper.and(wr->wr.eq("idx_to",userMessageDTO.getIdxTo())
                           .eq("idx_from",userMessageDTO.getIdxFrom()));
        wrapper.or(wr->wr.eq("idx_from",userMessageDTO.getIdxTo())
                .eq("idx_to",userMessageDTO.getIdxFrom()));
        UserContactPo userContactPo = userContactMapper.selectOne(wrapper);
        if(userContactPo == null){
            userContactPo = UserContactPo.insert(userMessageDTO);
            userContactMapper.insert(userContactPo);
        }else{
            userContactPo.setContent(userMessageDTO.getContent());
            userContactPo.setType(userMessageDTO.getType());
            userContactPo.setGmtModify(userMessageDTO.getGmtCreate());
            userContactPo.setIsCheck(0);
            userContactMapper.updateById(userContactPo);
        }
        //窗口推送消息
        int isReceiveSuccess = 0;
        try{
            isReceiveSuccess = WebSocketServer.sendByIdxUser(userMessageDTO.getIdxFrom(),userMessageDTO.getIdxTo());
        }catch (IOException e){
            e.printStackTrace();
        }
        HashMap<String,Integer> map = new HashMap<>();
        map.put("insert",isInsertSuccess);
        map.put("receive",isReceiveSuccess);
        return ResVo.success(map);
    }

    public ResVo checkMessage(int id){
        UpdateWrapper<UserContactPo> contactWrapper = new UpdateWrapper<>();
        contactWrapper.eq("id",id)
                .set("is_check",1);
        userContactMapper.update(null,contactWrapper);
        return ResVo.success();
    }
}
