package cn.org.dialogue.mapper;

import cn.org.dialogue.po.UserMessagePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMessageMapper extends BaseMapper<UserMessagePo> {

}
