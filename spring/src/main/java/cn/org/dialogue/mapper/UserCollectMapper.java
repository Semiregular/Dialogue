package cn.org.dialogue.mapper;

import cn.org.dialogue.po.UserCollectPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;



@Mapper
@Component
public interface UserCollectMapper extends BaseMapper<UserCollectPo> {

}
