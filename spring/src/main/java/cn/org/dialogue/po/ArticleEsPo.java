package cn.org.dialogue.po;

import cn.org.dialogue.dto.ArticleDTO;
import lombok.Data;

/**
 * @author 周正明
 * @date 2024/1/25
 */
@Data
public class ArticleEsPo {
    private Integer id;
    private String title;
    private String subtitle;
    private String summary;
    public static ArticleEsPo insert(int id, ArticleDTO articleDTO){
        ArticleEsPo articleESPO = new ArticleEsPo();
        articleESPO.id = id;
        articleESPO.title = articleDTO.getTitle();
        articleESPO.subtitle = articleDTO.getSubtitle();
        articleESPO.summary = articleDTO.getSummary();
        return articleESPO;
    }
}
