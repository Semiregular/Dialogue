package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Data
@TableName("activity")
public class ActivityPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String activityName;
    private String gmtCreate;
    private String gmtModify;
    private String startTime;
    private String endTime;
    private int isDelete;
    private String description;
}
