package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author 周正明
 * @date 2024/2/5
 */
@Data
@TableName("article_photo")
public class ArticlePhotoPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String url;
    private String gmtCreate;
    private String gmtModify;
    public static ArticlePhotoPo insert(String url){
        ArticlePhotoPo articlePhotoPo = new ArticlePhotoPo();
        articlePhotoPo.name = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
        articlePhotoPo.gmtCreate = dateFormat.format(date);
        articlePhotoPo.gmtModify = dateFormat.format(date);
        articlePhotoPo.url = url;
        return articlePhotoPo;
    }
}
