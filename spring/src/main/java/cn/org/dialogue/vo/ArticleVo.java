package cn.org.dialogue.vo;

import cn.org.dialogue.po.ArticlePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;


import java.util.Date;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Data
@ApiModel(value = "文章简要信息返回类")
public class ArticleVo {
    private Integer id;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private int idxUser;
    private String userName;
    private String userPhotoUrl;
    private String title;
    private String subtitle;
    private String summary;
    private int articleView;
    private int articleLike;
    private int articleCollect;
    private String articlePhotoUrl;


    public static ArticleVo convert(ArticlePo articlePo){
        ArticleVo articleVo = new ArticleVo();
        articleVo.id = articlePo.getId();
        articleVo.gmtCreate = articlePo.getGmtCreate();
        articleVo.gmtModify = articlePo.getGmtModify();
        articleVo.title = articlePo.getTitle();
        articleVo.subtitle = articlePo.getSubtitle();
        articleVo.summary = articlePo.getSummary();
        articleVo.articleView = articlePo.getArticleView();
        articleVo.articleLike = articlePo.getArticleLike();
        articleVo.articleCollect = articlePo.getArticleCollect();
        articleVo.idxUser = articlePo.getIdxUser();
        articleVo.userName = articlePo.getUserName();
        return articleVo;
    }
}
