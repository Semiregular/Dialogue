package cn.org.dialogue.mapper;

import cn.org.dialogue.po.ArticlePhotoPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author 周正明
 * @date 2024/2/5
 */
@Mapper
@Component
public interface ArticlePhotoMapper extends BaseMapper<ArticlePhotoPo> {
}
