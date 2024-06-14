package cn.org.dialogue.mapper;

import cn.org.dialogue.po.ArticlePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface ArticleMapper extends BaseMapper<ArticlePo> {

}
