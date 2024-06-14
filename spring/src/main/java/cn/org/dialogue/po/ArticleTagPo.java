package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 周正明
 * @date 2024/1/23
 */
@Data
@TableName("article_tag")
public class ArticleTagPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private int idxArticle;
    private String tag;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    public static ArticleTagPo insert(int idxArticle, String tag, Date gmtCreate){
        ArticleTagPo articleTagPO = new ArticleTagPo();
        articleTagPO.idxArticle = idxArticle;
        articleTagPO.tag = tag;
        articleTagPO.gmtCreate = gmtCreate;
        articleTagPO.gmtModify = gmtCreate;
        return articleTagPO;
    }
}
