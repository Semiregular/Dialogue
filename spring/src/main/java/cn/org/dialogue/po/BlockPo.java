package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Data
@TableName("block")
public class BlockPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String blockName;
    private String gmtCreate;
    private String gmtModify;
    private int isDelete;
    private String description;
}
