package cn.org.dialogue.service;

import cn.org.dialogue.mapper.ArticleCommentMapper;
import cn.org.dialogue.po.ArticleCommentPo;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.vo.CommentVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
public class ArticleCommentService {
    @Autowired
    public ArticleCommentMapper articleCommentMapper;
    @Autowired
    public ImageService imageService;
    @Autowired
    public UserService userService;

    public ResVo getMainComment(int idxArticle, int idxUser){
        Page<ArticleCommentPo> page = new Page<>(1,50);
        QueryWrapper<ArticleCommentPo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_article",idxArticle)
                .eq("idx_comment",0)
                .eq("is_delete",0)
                .orderBy(true,false,"comment_like")
                .orderBy(true,true,"gmt_create");
        page = articleCommentMapper.selectPage(page,wrapper);
        List<ArticleCommentPo> list = page.getRecords();
        List<CommentVo> volist = new ArrayList<>();
        for(ArticleCommentPo item : list){
            CommentVo commentVo = CommentVo.convert(item);
            String userPhotoUrl = imageService.getUserImageUrl(item.getUserPhoto());
            int isLike = userService.getUserCommentHistory(idxUser,item.getId());
            commentVo.setIsLike(isLike);
            commentVo.setUserPhotoUrl(userPhotoUrl);
            Map<String, Object> map = getSubComment(item.getId(),idxUser);
            commentVo.setSublist(map.get("sublist"));
            commentVo.setTotal(map.get("total"));
            volist.add(commentVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("record",volist);
        map.put("total",page.getPages());
        return ResVo.success(map);
    }

    public Map<String, Object> getSubComment(int idxComment, int idxUser){
        Page<ArticleCommentPo> page = new Page<>(1,5);
        QueryWrapper<ArticleCommentPo> wrapper = new QueryWrapper<>();
        wrapper.eq("idx_comment",idxComment)
                .eq("is_delete",0)
                .orderBy(true,false,"comment_like")
                .orderBy(true,true,"gmt_create");
        page = articleCommentMapper.selectPage(page,wrapper);
        List<ArticleCommentPo> list = page.getRecords();
        List<CommentVo> volist = new ArrayList<>();
        for(ArticleCommentPo item : list){
            String userPhotoUrl = imageService.getUserImageUrl(item.getUserPhoto());
            CommentVo commentVo = CommentVo.convert(item);
            commentVo.setUserPhotoUrl(userPhotoUrl);
            int isLike = userService.getUserCommentHistory(idxUser,item.getId());
            commentVo.setIsLike(isLike);
            volist.add(commentVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("sublist",volist);
        map.put("total",page.getPages());
        return map;
    }

    public ResVo insertComment(ArticleCommentPo articleCommentPo){
        int isSuccess = articleCommentMapper.insert(articleCommentPo);
        if(isSuccess == 1){
            return ResVo.success();
        }else{
            return ResVo.failure();
        }
    }

}
