package cn.org.dialogue.vo;

import cn.org.dialogue.po.ArticlePo;
import cn.org.dialogue.po.ArticleTagPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Data
@ApiModel(value = "文章详细信息返回类")
public class ArticleDetailVo {

    private Integer id;
    private int idxUser;
    private String userName;
    private String userPhotoUrl;
    private int articlePhoto;
    private String title;
    private String subtitle;
    private String summary;
    private int articleView;
    private int articleLike;
    private int articleCollect;
    private String articlePhotoUrl;
    private String content;
    private List<ArticleTagPo> tagList;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private Integer editType;
    private String markData;
    private int totalMark;

    public static ArticleDetailVo convert(ArticlePo articlePo){
        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        articleDetailVo.id = articlePo.getId();
        articleDetailVo.gmtCreate = articlePo.getGmtCreate();
        articleDetailVo.gmtModify = articlePo.getGmtModify();
        articleDetailVo.title = articlePo.getTitle();
        articleDetailVo.subtitle = articlePo.getSubtitle();
        articleDetailVo.summary = articlePo.getSummary();
        articleDetailVo.articleView = articlePo.getArticleView();
        articleDetailVo.articleLike = articlePo.getArticleLike();
        articleDetailVo.articleCollect = articlePo.getArticleCollect();
        articleDetailVo.idxUser = articlePo.getIdxUser();
        articleDetailVo.userName = articlePo.getUserName();
        articleDetailVo.markData = articlePo.getMarkData();
        articleDetailVo.totalMark = articlePo.getTotalMark();
        return articleDetailVo;
    }
}
