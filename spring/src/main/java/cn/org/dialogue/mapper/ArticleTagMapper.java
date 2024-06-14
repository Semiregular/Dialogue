package cn.org.dialogue.mapper;

import cn.org.dialogue.po.ArticleTagPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author 周正明
 * @date 2024/1/25
 */
@Mapper
@Component
public interface ArticleTagMapper extends BaseMapper<ArticleTagPo> {
}
