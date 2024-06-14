package cn.org.dialogue.mapper;

import cn.org.dialogue.po.BlockPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Mapper
@Component
public interface BlockMapper extends BaseMapper<BlockPo> {
}
