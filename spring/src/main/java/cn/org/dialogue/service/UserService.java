package cn.org.dialogue.service;

import cn.org.dialogue.dto.UserLoginDTO;
import cn.org.dialogue.jwt.TokenUtil;
import cn.org.dialogue.mapper.*;
import cn.org.dialogue.po.*;
import cn.org.dialogue.res.ResStatusEnum;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.vo.ArticleVo;
import cn.org.dialogue.vo.UserInfoVo;
import cn.org.dialogue.vo.UserLoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.aliyun.tea.*;


import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@Component
public class UserService {
    @Autowired
    public UserMapper userMapper;
    @Autowired
    public UserMessageService userMessageService;
    @Autowired
    public ArticleMapper articleMapper;
    @Autowired
    public UserCollectMapper userCollectMapper;
    @Autowired
    public UserHistoryMapper userHistoryMapper;
    @Autowired
    public UserFollowerMapper userFollowerMapper;
    @Autowired
    public UserCommentHistoryMapper userCommentHistoryMapper;
    @Autowired
    public ArticleCommentMapper articleCommentMapper;
    @Autowired
    public UserContactMapper userContactMapper;
    @Autowired
    public UserMessageMapper userMessageMapper;
    @Autowired
    public ImageService imageService;


    public ResVo userLoginByPassword(UserLoginDTO userLoginDTO){
        QueryWrapper<UserPo> wrapper = new QueryWrapper<>();
        wrapper.select("id","is_login","idx_photo")
                .eq("user_name",userLoginDTO.getUserName())
                .eq("user_password",userLoginDTO.getUserPassword());
        UserPo userPo = userMapper.selectOne(wrapper);
        if(userPo == null){
            return ResVo.failure(ResStatusEnum.USER_NOT_EXIST);
        }else{
            if(userPo.getIsLogin() == 1){
                return ResVo.failure(ResStatusEnum.USER_HAS_LOGIN);
            }else {
                //生成token
                Map<String, Object> jwtInfo = new HashMap<>();
                jwtInfo.put("uid", userPo.getId());
                String token = TokenUtil.sign(jwtInfo);
                UserLoginVo userLoginVo = new UserLoginVo();
                String idxPhotoUrl = imageService.getUserImageUrl(userPo.getIdxPhoto());
                userLoginVo.setIdxPhotoUrl(idxPhotoUrl);
                userLoginVo.setIdxPhoto(userPo.getIdxPhoto());
                userLoginVo.setIdxUser(userPo.getId());
                userLoginVo.setMessageUncheck(userMessageService.getUserMessageUncheck(userPo.getId()));
                userLoginVo.setToken(token);
                return ResVo.success(userLoginVo);
            }
        }
    }

    public ResVo userLoginByEmail(UserLoginDTO userLoginDTO){
        //查询用户
        QueryWrapper<UserPo> wrapper = new QueryWrapper<>();
        wrapper.select("id","user_name","is_login","idx_photo")
                .eq("user_email",userLoginDTO.getUserEmail());
        UserPo userPo = userMapper.selectOne(wrapper);
        //用户不存在
        if(userPo == null){
            userPo = new UserPo();
            userPo.setUserEmail(userLoginDTO.getUserEmail());
            userPo.setGmtCreate(userLoginDTO.getGmtCreate());
            userPo.setGmtModify(userLoginDTO.getGmtCreate());
            userPo.setIdxPhoto(16);
            //插入新用户
            int isInsertSuccess = userMapper.insert(userPo);
            if(isInsertSuccess == 1){
                //生成token
                Map<String, Object> jwtInfo = new HashMap<>();
                jwtInfo.put("uid", userPo.getId());
                String token = TokenUtil.sign(jwtInfo);
                UserLoginVo userLoginVo = new UserLoginVo();
                userLoginVo.setIdxUser(userPo.getId());
                String idxPhotoUrl = imageService.getUserImageUrl(16);
                userLoginVo.setIdxPhotoUrl(idxPhotoUrl);
                userLoginVo.setIdxPhoto(16);
                userLoginVo.setUserName("小可爱用户");
                userLoginVo.setMessageUncheck(userMessageService.getUserMessageUncheck(userPo.getId()));
                userLoginVo.setToken(token);
                //用户注册成功
                return ResVo.success(userLoginVo);
            }else{
                //用户注册失败
                return ResVo.failure();
            }
        }else{
            if(userPo.getIsLogin() == 1){
                //用户存在且已登录
                return ResVo.failure(ResStatusEnum.USER_HAS_LOGIN);
            }else {
                //生成token
                Map<String, Object> jwtInfo = new HashMap<>();
                jwtInfo.put("uid", userPo.getId());
                String token = TokenUtil.sign(jwtInfo);
                UserLoginVo userLoginVo = new UserLoginVo();
                String idxPhotoUrl = imageService.getUserImageUrl(userPo.getIdxPhoto());
                userLoginVo.setIdxPhotoUrl(idxPhotoUrl);
                userLoginVo.setIdxPhoto(userPo.getIdxPhoto());
                userLoginVo.setIdxUser(userPo.getId());
                userLoginVo.setUserName(userPo.getUserName());
                userLoginVo.setMessageUncheck(userMessageService.getUserMessageUncheck(userPo.getId()));
                userLoginVo.setToken(token);
                //用户存在且未登录
                return ResVo.success(userLoginVo);
            }
        }
    }

    public ResVo userLoginByTel(UserLoginDTO userLoginDTO){
        //查询用户
        QueryWrapper<UserPo> wrapper = new QueryWrapper<>();
        wrapper.select("id","user_name","is_login","idx_photo")
                .eq("user_tel",userLoginDTO.getUserTel());
        UserPo userPo = userMapper.selectOne(wrapper);
        //用户不存在
        if(userPo == null){
            userPo = new UserPo();
            userPo.setUserTel(userLoginDTO.getUserTel());
            userPo.setIdxPhoto(16);
            userPo.setGmtCreate(userLoginDTO.getGmtCreate());
            userPo.setGmtModify(userLoginDTO.getGmtCreate());
            //插入新用户
            int isInsertSuccess = userMapper.insert(userPo);
            if(isInsertSuccess == 1){
                //生成token
                Map<String, Object> jwtInfo = new HashMap<>();
                jwtInfo.put("uid", userPo.getId());
                String token = TokenUtil.sign(jwtInfo);
                UserLoginVo userLoginVo = new UserLoginVo();
                userLoginVo.setIdxUser(userPo.getId());
                String idxPhotoUrl = imageService.getUserImageUrl(16);
                userLoginVo.setIdxPhotoUrl(idxPhotoUrl);
                userLoginVo.setUserName("小可爱用户");
                userLoginVo.setIdxPhoto(16);
                userLoginVo.setMessageUncheck(userMessageService.getUserMessageUncheck(userPo.getId()));
                userLoginVo.setToken(token);
                //用户注册成功
                return ResVo.success(userLoginVo);
            }else{
                //用户注册失败
                return ResVo.failure();
            }
        }else{
            if(userPo.getIsLogin() == 1){
                //用户存在且已登录
                return ResVo.failure(ResStatusEnum.USER_HAS_LOGIN);
            }else {
                //生成token
                Map<String, Object> jwtInfo = new HashMap<>();
                jwtInfo.put("uid", userPo.getId());
                String token = TokenUtil.sign(jwtInfo);
                UserLoginVo userLoginVo = new UserLoginVo();
                String idxPhotoUrl = imageService.getUserImageUrl(userPo.getIdxPhoto());
                userLoginVo.setIdxPhotoUrl(idxPhotoUrl);
                userLoginVo.setIdxUser(userPo.getId());
                userLoginVo.setIdxPhoto(userPo.getIdxPhoto());
                userLoginVo.setUserName(userPo.getUserName());
                userLoginVo.setMessageUncheck(userMessageService.getUserMessageUncheck(userPo.getId()));
                userLoginVo.setToken(token);
                //用户存在且未登录
                return ResVo.success(userLoginVo);
            }
        }
    }

    public ResVo userInsertByPassword(UserLoginDTO userLoginDTO){
        UserPo userPo = UserPo.insertByPassword(userLoginDTO);
        userPo.setIdxPhoto(16);
        int isInsertSuccess = userMapper.insert(userPo);
        if(isInsertSuccess == 1){
            int id = userPo.getId();
            Map<String, Object> jwtInfo = new HashMap<>();
            jwtInfo.put("uid", id);
            String token = TokenUtil.sign(jwtInfo);
            UserLoginVo userLoginVo = new UserLoginVo();
            String idxPhotoUrl = imageService.getUserImageUrl(16);
            userLoginVo.setIdxPhotoUrl(idxPhotoUrl);
            userLoginVo.setIdxUser(id);
            userLoginVo.setIdxPhoto(userPo.getIdxPhoto());
            userLoginVo.setMessageUncheck(userMessageService.getUserMessageUncheck(id));
            userLoginVo.setToken(token);
            //注册成功
            return ResVo.success(userLoginVo);
        }else{
            //注册失败
            return ResVo.failure();
        }
    }

    public  com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId("xxxxxxxxxxxxxxx")
                .setAccessKeySecret("xxxxxxxxxxxxxxxxxxx");
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public  ResVo sendTelVerify(String tel) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = createClient();
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(9) + 1;
            code.append(r);
        }
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("Dialogue阅读社区")
                .setTemplateCode("xxxxxxxxxx")
                .setPhoneNumbers(tel)
                .setTemplateParam("{\"code\":" + code.toString() + "}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            com.aliyun.dysmsapi20170525.models.SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
            com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(resp));
            return ResVo.success(code);
        } catch (TeaException error) {
            error.printStackTrace();
            return ResVo.failure();
        } catch (Exception error) {
            error.printStackTrace();
            return ResVo.failure();
        }
    }

    public ResVo updateUserPassword(UserLoginDTO userLoginDTO){
        UpdateWrapper<UserPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",userLoginDTO.getId())
                .set("user_password",userLoginDTO.getUserPassword())
                .set("gmt_modify",userLoginDTO.getGmtModify());
        userMapper.update(null,wrapper);
        return ResVo.success();
    }

    public ResVo updateUserName(UserLoginDTO userLoginDTO){
        UpdateWrapper<UserPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",userLoginDTO.getId())
                .set("user_name",userLoginDTO.getUserName())
                .set("gmt_modify",userLoginDTO.getGmtModify());
        userMapper.update(null,wrapper);
        UpdateWrapper<ArticlePo> articleUpdateWrapper = new UpdateWrapper<>();
        articleUpdateWrapper.eq("idx_user",userLoginDTO.getId())
                .set("user_name",userLoginDTO.getUserName())
                .set("gmt_modify",userLoginDTO.getGmtModify());
        articleMapper.update(null,articleUpdateWrapper);
        UpdateWrapper<ArticleCommentPo> articleCommentUpdateWrapper = new UpdateWrapper<>();
        articleCommentUpdateWrapper.eq("idx_user",userLoginDTO.getId())
                .set("user_name",userLoginDTO.getUserName())
                .set("gmt_modify",userLoginDTO.getGmtModify());
        articleCommentMapper.update(null,articleCommentUpdateWrapper);

        UpdateWrapper<UserContactPo> contactUpdateFromWrapper = new UpdateWrapper<>();
        contactUpdateFromWrapper.eq("idx_from",userLoginDTO.getId())
                .set("name_from",userLoginDTO.getUserName());
        userContactMapper.update(null,contactUpdateFromWrapper);

        UpdateWrapper<UserContactPo> contactUpdateToWrapper = new UpdateWrapper<>();
        contactUpdateToWrapper.eq("idx_to",userLoginDTO.getId())
                .set("name_to",userLoginDTO.getUserName());
        userContactMapper.update(null,contactUpdateToWrapper);

        UpdateWrapper<UserMessagePo> messageUpdateFromWrapper = new UpdateWrapper<>();
        messageUpdateFromWrapper.eq("idx_from",userLoginDTO.getId())
                .set("name_from",userLoginDTO.getUserName());
        userMessageMapper.update(null,messageUpdateFromWrapper);

        UpdateWrapper<UserMessagePo> messageUpdateToWrapper = new UpdateWrapper<>();
        messageUpdateToWrapper.eq("idx_to",userLoginDTO.getId())
                .set("name_to",userLoginDTO.getUserName());
        userMessageMapper.update(null,messageUpdateToWrapper);

        UpdateWrapper<UserFollowerPo> followerUpdateFromWrapper = new UpdateWrapper<>();
        followerUpdateFromWrapper.eq("idx_user",userLoginDTO.getId())
                .set("user_name",userLoginDTO.getUserName());
        userFollowerMapper.update(null,followerUpdateFromWrapper);

        UpdateWrapper<UserFollowerPo> followerUpdateToWrapper = new UpdateWrapper<>();
        followerUpdateToWrapper.eq("idx_follower",userLoginDTO.getId())
                .set("follower_name",userLoginDTO.getUserName());
        userFollowerMapper.update(null,followerUpdateToWrapper);
        return ResVo.success();
    }

    public ResVo updateUserPhoto(UserLoginDTO userLoginDTO){
        UpdateWrapper<UserPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",userLoginDTO.getId())
                .set("idx_photo",userLoginDTO.getUserPhoto())
                .set("gmt_modify",userLoginDTO.getGmtModify());
        userMapper.update(null,wrapper);
        UpdateWrapper<ArticlePo> articleUpdateWrapper = new UpdateWrapper<>();
        articleUpdateWrapper.eq("idx_user",userLoginDTO.getId())
                .set("user_photo",userLoginDTO.getUserPhoto())
                .set("gmt_modify",userLoginDTO.getGmtModify());
        articleMapper.update(null,articleUpdateWrapper);

        UpdateWrapper<ArticleCommentPo> articleCommentUpdateWrapper = new UpdateWrapper<>();
        articleCommentUpdateWrapper.eq("idx_user",userLoginDTO.getId())
                .set("user_photo",userLoginDTO.getUserPhoto())
                .set("gmt_modify",userLoginDTO.getGmtModify());
        articleCommentMapper.update(null,articleCommentUpdateWrapper);

        UpdateWrapper<UserContactPo> contactUpdateFromWrapper = new UpdateWrapper<>();
        contactUpdateFromWrapper.eq("idx_from",userLoginDTO.getId())
                .set("photo_from",userLoginDTO.getUserPhoto());
        userContactMapper.update(null,contactUpdateFromWrapper);

        UpdateWrapper<UserContactPo> contactUpdateToWrapper = new UpdateWrapper<>();
        contactUpdateToWrapper.eq("idx_to",userLoginDTO.getId())
                .set("photo_to",userLoginDTO.getUserPhoto());
        userContactMapper.update(null,contactUpdateToWrapper);

        UpdateWrapper<UserMessagePo> messageUpdateFromWrapper = new UpdateWrapper<>();
        messageUpdateFromWrapper.eq("idx_from",userLoginDTO.getId())
                .set("photo_from",userLoginDTO.getUserPhoto());
        userMessageMapper.update(null,messageUpdateFromWrapper);
        return ResVo.success();
    }

    public ResVo updateUserIp(int id, String ip){
        UpdateWrapper<UserPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id)
                .set("ip",ip);
        userMapper.update(null,wrapper);
        return ResVo.success();
    }

    public ResVo updateUserInfo(UserInfoVo userInfoVo){
        UpdateWrapper<UserPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",userInfoVo.getId())
                .set("user_tel",userInfoVo.getUserTel())
                .set("user_email",userInfoVo.getUserEmail())
                .set("user_intro",userInfoVo.getUserIntro())
                .set("user_city",userInfoVo.getUserCity())
                .set("gmt_modify",userInfoVo.getGmtModify());
        userMapper.update(null,wrapper);
        return ResVo.success();
    }

    public ResVo getUserInfo(int id){
        UserPo userPo = userMapper.selectById(id);
        UserInfoVo userInfoVo = UserInfoVo.convert(userPo);
        String idxPhotoUrl = imageService.getUserImageUrl(userPo.getIdxPhoto());
        userInfoVo.setIdxPhotoUrl(idxPhotoUrl);
        return ResVo.success(userInfoVo);
    }

    public ResVo getUserArticle(int idxUser, int pageCurrent,int pageSize){
        Page<ArticlePo> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<ArticlePo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_user",idxUser)
                .eq("is_delete",0)
                .orderBy(true,false,"gmt_create");
        page = articleMapper.selectPage(page,wrapper);
        List<ArticlePo> list = page.getRecords();
        List<ArticleVo> volist = new ArrayList<>();
        for(ArticlePo item : list){
            String articleImageUrl = imageService.getArticleImageUrl(item.getArticlePhoto());
            String userImageUrl = imageService.getUserImageUrl(item.getUserPhoto());
            ArticleVo articleVo = ArticleVo.convert(item);
            articleVo.setArticlePhotoUrl(articleImageUrl);
            articleVo.setUserPhotoUrl(userImageUrl);
            volist.add(articleVo);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("record",volist);
        map.put("total",page.getPages());
        return ResVo.success(map);
    }

    public ResVo getUserCollect(int idxUser, int pageCurrent,int pageSize){
        Page<UserCollectPo> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<UserCollectPo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_user",idxUser)
                .eq("is_delete",0)
                .orderBy(true,false,"gmt_create");
        page = userCollectMapper.selectPage(page,wrapper);
        return ResVo.success(page);
    }

    public ResVo updateUserCollect(int idxUser, int idxArticle){
        QueryWrapper<UserCollectPo> queryWrapper = new QueryWrapper<>();
        UpdateWrapper<UserCollectPo> updateWrapper = new UpdateWrapper<>();
        queryWrapper.eq("idx_user",idxUser)
                .eq("idx_article",idxArticle);
        UserCollectPo userCollectPo = userCollectMapper.selectOne(queryWrapper);
        if(userCollectPo != null){
            updateWrapper.eq("id",userCollectPo.getId())
                    .set("is_delete",1 - userCollectPo.getIsDelete());
            userCollectMapper.update(null,updateWrapper);
            return ResVo.success();
        }else{
            ArticlePo articlePo = articleMapper.selectById(idxArticle);
            userCollectPo = new UserCollectPo();
            userCollectPo.setTitle(articlePo.getTitle());
            userCollectPo.setIdxUser(idxUser);
            userCollectPo.setIdxArticle(idxArticle);
            userCollectPo.setIsDelete(0);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userCollectPo.setGmtCreate(formatter.format(date));
            userCollectPo.setGmtModify(formatter.format(date));
            userCollectMapper.insert(userCollectPo);
            return ResVo.success();
        }
    }

    public ResVo listUserHistory(int idxUser, int pageCurrent,int pageSize){
        Page<UserHistoryPo> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<UserHistoryPo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_user",idxUser)
                .eq("is_delete",0)
                .orderBy(true,false,"gmt_modify");
        page = userHistoryMapper.selectPage(page,wrapper);
        return ResVo.success(page);
    }

    public ResVo getUserHistoryRead(int idxUser, int idxArticle){
        QueryWrapper<UserHistoryPo> wrapper = new QueryWrapper<>();
        UpdateWrapper<UserHistoryPo> updateWrapper = new UpdateWrapper<>();
        UpdateWrapper<ArticlePo> articleWrapper = new UpdateWrapper<>();
        wrapper.eq("idx_user",idxUser)
                .eq("idx_article",idxArticle);
        UserHistoryPo userHistoryPo = userHistoryMapper.selectOne(wrapper);
        if(userHistoryPo != null){
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            updateWrapper.eq("id",userHistoryPo.getId())
                    .set("gmt_modify",formatter.format(date));
            userHistoryMapper.update(null,updateWrapper);
            ArticlePo oldArticle = articleMapper.selectById(userHistoryPo.getIdxArticle());
            articleWrapper.eq("id",oldArticle.getId())
                    .set("article_view",oldArticle.getArticleView() + 1);
            articleMapper.update(null,articleWrapper);
            return ResVo.success(userHistoryPo);
        }else{
            ArticlePo articlePo = articleMapper.selectById(idxArticle);
            userHistoryPo = new UserHistoryPo();
            userHistoryPo.setTitle(articlePo.getTitle());
            userHistoryPo.setIdxUser(idxUser);
            userHistoryPo.setIdxArticle(idxArticle);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userHistoryPo.setGmtCreate(formatter.format(date));
            userHistoryPo.setGmtModify(formatter.format(date));
            int isInsertSuccess = userHistoryMapper.insert(userHistoryPo);
            if(isInsertSuccess == 1){
                return ResVo.success(userHistoryPo);
            }else{
                return ResVo.failure();
            }
        }
    }

    public ResVo updateUserMark(UserHistoryPo userHistoryPo){
        UpdateWrapper<UserHistoryPo> userHistoryWrapper = new UpdateWrapper<>();
        userHistoryWrapper.eq("id",userHistoryPo.getId())
                .set("mark_data",userHistoryPo.getMarkData())
                .set("gmt_modify",userHistoryPo.getGmtModify());
        userHistoryMapper.update(null,userHistoryWrapper);
        return ResVo.success();
    }

    public ResVo updateUserHistory(UserHistoryPo userHistoryPo){
        UpdateWrapper<UserHistoryPo> userHistoryWrapper = new UpdateWrapper<>();
        UpdateWrapper<ArticlePo> articleWrapper = new UpdateWrapper<>();
        //查询旧值
        UserHistoryPo oldHistory = userHistoryMapper.selectById(userHistoryPo.getId());
        //更新文章数据
        if(oldHistory.getIsLike() > userHistoryPo.getIsLike()){
            ArticlePo oldArticle = articleMapper.selectById(oldHistory.getIdxArticle());
            articleWrapper.eq("id",oldArticle.getId())
                    .set("article_like",oldArticle.getArticleLike() - 1);
            articleMapper.update(null,articleWrapper);
        } else if (oldHistory.getIsLike() < userHistoryPo.getIsLike()) {
            ArticlePo oldArticle = articleMapper.selectById(oldHistory.getIdxArticle());
            articleWrapper.eq("id",oldArticle.getId())
                    .set("article_like",oldArticle.getArticleLike() + 1);
            articleMapper.update(null,articleWrapper);
        }
        if(oldHistory.getIsCollect() > userHistoryPo.getIsCollect()){
            ArticlePo oldArticle = articleMapper.selectById(oldHistory.getIdxArticle());
            articleWrapper.eq("id",oldArticle.getId())
                    .set("article_collect",oldArticle.getArticleCollect() - 1);
            articleMapper.update(null,articleWrapper);
        } else if (oldHistory.getIsCollect() < userHistoryPo.getIsCollect()) {
            ArticlePo oldArticle = articleMapper.selectById(oldHistory.getIdxArticle());
            articleWrapper.eq("id",oldArticle.getId())
                    .set("article_collect",oldArticle.getArticleCollect() + 1);
            articleMapper.update(null,articleWrapper);
        }
        //更新history
        userHistoryWrapper.eq("id",userHistoryPo.getId())
                .set("is_like",userHistoryPo.getIsLike())
                .set("is_collect",userHistoryPo.getIsCollect())
                .set("is_comment",userHistoryPo.getIsComment())
                .set("is_share",userHistoryPo.getIsShare())
                .set("gmt_modify",userHistoryPo.getGmtModify());
        int isSuccess = userHistoryMapper.update(null,userHistoryWrapper);
        if(isSuccess == 1){
            return ResVo.success();
        }
        else
            return ResVo.failure();
    }

    public ResVo updateUserHistoryCollect(UserHistoryPo userHistoryPo){
        //查询旧值
        QueryWrapper<UserHistoryPo> userHistoryQueryWrapper = new QueryWrapper<>();
        userHistoryQueryWrapper.eq("idx_user",userHistoryPo.getIdxUser())
                .eq("idx_article",userHistoryPo.getIdxArticle());
        UserHistoryPo oldHistory = userHistoryMapper.selectOne(userHistoryQueryWrapper);

        UpdateWrapper<UserHistoryPo> userHistoryWrapper = new UpdateWrapper<>();
        UpdateWrapper<ArticlePo> articleWrapper = new UpdateWrapper<>();

        //更新文章数据
        if(oldHistory.getIsCollect() > userHistoryPo.getIsCollect()){
            ArticlePo oldArticle = articleMapper.selectById(oldHistory.getIdxArticle());
            articleWrapper.eq("id",oldArticle.getId())
                    .set("article_collect",oldArticle.getArticleCollect() - 1);
            articleMapper.update(null,articleWrapper);
        } else if (oldHistory.getIsCollect() < userHistoryPo.getIsCollect()) {
            ArticlePo oldArticle = articleMapper.selectById(oldHistory.getIdxArticle());
            articleWrapper.eq("id",oldArticle.getId())
                    .set("article_collect",oldArticle.getArticleCollect() + 1);
            articleMapper.update(null,articleWrapper);
        }
        //更新history
        userHistoryWrapper.eq("id",oldHistory.getId())
                .set("is_collect",userHistoryPo.getIsCollect())
                .set("gmt_modify",userHistoryPo.getGmtModify());
        int isSuccess = userHistoryMapper.update(null,userHistoryWrapper);
        if(isSuccess == 1){
            return ResVo.success();
        }
        else
            return ResVo.failure();
    }

    public ResVo deleteUserHistory(int id){
        UpdateWrapper<UserHistoryPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id)
                .set("is_delete",1);
        int isSuccess = userHistoryMapper.update(null,wrapper);
        if(isSuccess == 1){
            return ResVo.success();
        }
        else
            return ResVo.failure();
    }

    public int getUserCommentHistory(int idxUser, int idxComment){
        QueryWrapper<UserCommentHistoryPo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_user",idxUser)
                .eq("idx_comment",idxComment);
        UserCommentHistoryPo userCommentHistoryPo = userCommentHistoryMapper.selectOne(wrapper);
        if(userCommentHistoryPo != null){
            return userCommentHistoryPo.getIsLike();
        }else{
            return 2;
        }
    }

    public ResVo updateUserCommentHistory(UserCommentHistoryPo commentHistoryPo){
        QueryWrapper<UserCommentHistoryPo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_user",commentHistoryPo.getIdxUser())
                .eq("idx_comment",commentHistoryPo.getIdxComment());
        UserCommentHistoryPo userCommentHistoryPo = userCommentHistoryMapper.selectOne(wrapper);

        if(userCommentHistoryPo != null) {
            UpdateWrapper<ArticleCommentPo> articleWrapper = new UpdateWrapper<>();
            UpdateWrapper<UserCommentHistoryPo> userHistoryWrapper = new UpdateWrapper<>();
            //更新评论数据
            if (userCommentHistoryPo.getIsLike() > commentHistoryPo.getIsLike()) {
                ArticleCommentPo oldArticle = articleCommentMapper.selectById(userCommentHistoryPo.getIdxComment());
                articleWrapper.eq("id", oldArticle.getId())
                        .set("comment_like", oldArticle.getCommentLike() - 1);
                articleCommentMapper.update(null, articleWrapper);
            } else if (userCommentHistoryPo.getIsLike() < commentHistoryPo.getIsLike()) {
                ArticleCommentPo oldArticle = articleCommentMapper.selectById(userCommentHistoryPo.getIdxComment());
                articleWrapper.eq("id", oldArticle.getId())
                        .set("comment_like", oldArticle.getCommentLike() + 1);
                articleCommentMapper.update(null, articleWrapper);
            }
            //更新history
            userHistoryWrapper.eq("id", userCommentHistoryPo.getId())
                    .set("is_like", commentHistoryPo.getIsLike())
                    .set("gmt_modify", commentHistoryPo.getGmtModify());
            int isSuccess = userCommentHistoryMapper.update(null, userHistoryWrapper);
            if(isSuccess == 1){
                return ResVo.success();
            }
            else
                return ResVo.failure();
        }else{
            userCommentHistoryPo = new UserCommentHistoryPo();
            userCommentHistoryPo.setIdxUser(commentHistoryPo.getIdxUser());
            userCommentHistoryPo.setIdxComment(commentHistoryPo.getIdxComment());
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userCommentHistoryPo.setGmtCreate(formatter.format(date));
            userCommentHistoryPo.setGmtModify(formatter.format(date));
            userCommentHistoryPo.setIsLike(1);
            int isInsertSuccess = userCommentHistoryMapper.insert(userCommentHistoryPo);
            ArticleCommentPo oldArticle = articleCommentMapper.selectById(commentHistoryPo.getIdxComment());
            UpdateWrapper<ArticleCommentPo> articleWrapper = new UpdateWrapper<>();
            articleWrapper.eq("id", oldArticle.getId())
                    .set("comment_like", oldArticle.getCommentLike() + 1);
            articleCommentMapper.update(null, articleWrapper);
            if(isInsertSuccess == 1){
                return ResVo.success();
            }else{
                return ResVo.failure();
            }
        }

    }

    public ResVo getUserFollower(int idxUser, int idxFollower){
        QueryWrapper<UserFollowerPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idx_user",idxUser)
                .eq("idx_follower",idxFollower);
        UserFollowerPo oldFollower = userFollowerMapper.selectOne(queryWrapper);
        if(oldFollower == null){
            return ResVo.failure();
        }else{
            return ResVo.success(oldFollower.getIsDelete());
        }
    }

    public ResVo updateUserFollower(UserFollowerPo userFollowerPo){
        QueryWrapper<UserFollowerPo> queryWrapper = new QueryWrapper<>();
        UserPo oldUser = userMapper.selectById(userFollowerPo.getIdxUser());
        UpdateWrapper<UserPo> updateUserWrapper = new UpdateWrapper<>();
        queryWrapper.eq("idx_user",userFollowerPo.getIdxUser())
                .eq("idx_follower",userFollowerPo.getIdxFollower());
        UserFollowerPo oldFollower = userFollowerMapper.selectOne(queryWrapper);
        if(oldFollower == null){
            userFollowerMapper.insert(userFollowerPo);
            updateUserWrapper.eq("id",oldUser.getId())
                    .set("total_follower_num",oldUser.getTotalFollowerNum() + 1);
            userMapper.update(null,updateUserWrapper);
            return ResVo.success();
        }else{
            UpdateWrapper<UserFollowerPo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",oldFollower.getId())
                    .set("is_delete",1 - oldFollower.getIsDelete())
                    .set("gmt_modify",userFollowerPo.getGmtModify());
            userFollowerMapper.update(null,updateWrapper);
            if(oldFollower.getIsDelete() == 0){
                updateUserWrapper.eq("id",oldUser.getId())
                        .set("total_follower_num",oldUser.getTotalFollowerNum() - 1);
                userMapper.update(null,updateUserWrapper);
            }else{
                updateUserWrapper.eq("id",oldUser.getId())
                        .set("total_follower_num",oldUser.getTotalFollowerNum() + 1);
                userMapper.update(null,updateUserWrapper);
            }
            return ResVo.success();
        }
    }


}
