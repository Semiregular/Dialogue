package cn.org.dialogue.po;

import cn.org.dialogue.dto.ArticleDTO;
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
@TableName("article_detail")
public class ArticleDetailPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String content;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private Integer editType;
    public static ArticleDetailPo insert(ArticleDTO articleDTO){
        ArticleDetailPo articleDetailPO = new ArticleDetailPo();
        articleDetailPO.content = articleDTO.getContent();
        articleDetailPO.gmtCreate = articleDTO.getGmtCreate();
        articleDetailPO.gmtModify = articleDTO.getGmtCreate();
        articleDetailPO.editType = articleDTO.getEditType();
        return articleDetailPO;
    }

    public static ArticleDetailPo update(ArticleDTO articleDTO){
        ArticleDetailPo articleDetailPO = new ArticleDetailPo();
        articleDetailPO.content = articleDTO.getContent();
        articleDetailPO.gmtCreate = articleDTO.getGmtCreate();
        articleDetailPO.gmtModify = articleDTO.getGmtModify();
        articleDetailPO.editType = articleDTO.getEditType();
        return articleDetailPO;
    }
}
