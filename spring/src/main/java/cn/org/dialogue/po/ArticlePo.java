package cn.org.dialogue.po;

import cn.org.dialogue.dto.ArticleDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.io.IOException;
import java.util.Date;


@Data
@TableName("article")
public class ArticlePo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private int idxUser;
    private String userName;
    private int userPhoto;
    private String title;
    private String subtitle;
    private String summary;
    private int articleView;
    private int articleLike;
    private int articleCollect;
    private int articleShare;
    private int articlePhoto;
    private int totalMark;
    private String markData;
    private int isCheck;
    private int isDelete;
    public static ArticlePo insert(ArticleDTO articleDTO) throws IOException {
        ArticlePo articlePO = new ArticlePo();
        articlePO.gmtCreate = articleDTO.getGmtCreate();
        articlePO.gmtModify = articleDTO.getGmtCreate();
        articlePO.idxUser = articleDTO.getIdxUser();
        articlePO.userName = articleDTO.getUserName();
        articlePO.title = articleDTO.getTitle();
        articlePO.subtitle = articleDTO.getSubtitle();
        articlePO.summary = articleDTO.getSummary();
        articlePO.articlePhoto = articleDTO.getArticlePhoto();
        articlePO.userPhoto = articleDTO.getUserPhoto();
        return articlePO;
    }

    public static ArticlePo update(ArticleDTO articleDTO) throws IOException {
        ArticlePo articlePO = new ArticlePo();
        articlePO.gmtCreate = articleDTO.getGmtCreate();
        articlePO.gmtModify = articleDTO.getGmtModify();
        articlePO.idxUser = articleDTO.getIdxUser();
        articlePO.userName = articleDTO.getUserName();
        articlePO.title = articleDTO.getTitle();
        articlePO.subtitle = articleDTO.getSubtitle();
        articlePO.summary = articleDTO.getSummary();
        articlePO.articlePhoto = articleDTO.getArticlePhoto();
        articlePO.userPhoto = articleDTO.getUserPhoto();
        return articlePO;
    }
}
