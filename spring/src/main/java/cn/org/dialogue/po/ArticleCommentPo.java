package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
@TableName("article_comment")
public class ArticleCommentPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private int idxArticle;
    private int idxComment;
    private int idxSpan;
    private int idxUser;
    private String userName;
    private int userPhoto;
    private String ip;
    private String content;
    private int commentLike;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private int isDelete;
}
