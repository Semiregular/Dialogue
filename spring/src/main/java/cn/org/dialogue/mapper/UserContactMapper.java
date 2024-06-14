package cn.org.dialogue.mapper;

import cn.org.dialogue.po.UserContactPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author 周正明
 * @date 2024/2/2
 */
@Mapper
@Component
public interface UserContactMapper extends BaseMapper<UserContactPo> {
}
