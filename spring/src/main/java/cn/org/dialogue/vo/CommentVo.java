package cn.org.dialogue.vo;

import cn.org.dialogue.po.ArticleCommentPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;


/**
 * @author 周正明
 * @date 2024/3/7
 */
@Data
@ApiModel(value = "文章评论返回类")
public class CommentVo {
    private Integer id;
    private int idxArticle;
    private int idxComment;
    private int idxSpan;
    private int idxUser;
    private String userName;
    private String userPhotoUrl;
    private String ip;
    private String content;
    private int commentLike;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    private Object sublist;
    private Object total;
    private int isLike;

    public static CommentVo convert(ArticleCommentPo articleCommentPo){
        CommentVo commentVo = new CommentVo();
        commentVo.id = articleCommentPo.getId();
        commentVo.idxArticle = articleCommentPo.getIdxArticle();
        commentVo.idxComment = articleCommentPo.getIdxComment();
        commentVo.idxSpan = articleCommentPo.getIdxSpan();
        commentVo.idxUser = articleCommentPo.getIdxUser();
        commentVo.userName = articleCommentPo.getUserName();
        commentVo.ip = articleCommentPo.getIp();
        commentVo.content = articleCommentPo.getContent();
        commentVo.commentLike = articleCommentPo.getCommentLike();
        commentVo.gmtCreate = articleCommentPo.getGmtCreate();
        return commentVo;
    }


}
