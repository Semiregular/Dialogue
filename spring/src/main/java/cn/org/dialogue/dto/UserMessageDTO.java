package cn.org.dialogue.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * @author 周正明
 * @date 2024/2/2
 */
@Data
@ApiModel(value = "用户消息请求类")
public class UserMessageDTO {
    @ApiModelProperty("接收用户id")
    private int idxTo;
    @ApiModelProperty("发送用户id")
    private int idxFrom;
    private int photoFrom;
    private int photoTo;
    @ApiModelProperty("接收用户名")
    private String nameTo;
    @ApiModelProperty("发送用户名")
    private String nameFrom;
    @ApiModelProperty("发送时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("当前页数")
    private int pageCurrent;
    @ApiModelProperty("当前页最大记录数")
    private int pageSize;
    private int type;
}
