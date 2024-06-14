package cn.org.dialogue.mapper;

import cn.org.dialogue.dto.PageDTO;
import cn.org.dialogue.po.ArticleActivityPo;
import cn.org.dialogue.po.ArticlePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Mapper
@Component
public interface ArticleActivityMapper extends BaseMapper<ArticleActivityPo> {
    @Select("select article.id, article.article_photo, article.title, article.subtitle, " +
            "article.summary, article.article_view, article.article_like, article.gmt_create, " +
            "article.gmt_modify ,article.idx_user, article.user_name, article.user_photo " +
            "from article_activity " +
            "join article on article_activity.idx_article = article.id " +
            "where article_activity.idx_activity = #{id} " +
            "limit #{pageStart}, #{pageSize}")
    List<ArticlePo> listArticleByActivityId(PageDTO pageDTO);


}
