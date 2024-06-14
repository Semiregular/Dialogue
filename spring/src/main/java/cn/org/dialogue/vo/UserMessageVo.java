package cn.org.dialogue.vo;

import cn.org.dialogue.po.UserMessagePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 周正明
 * @date 2024/3/8
 */
@Data
public class UserMessageVo {
    private Integer id;
    private int idxTo;
    private int idxFrom;
    private String photoFromUrl;
    private String nameTo;
    private String nameFrom;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private String content;
    private int isCheck;
    private int type;

    public static UserMessageVo convert(UserMessagePo userMessagePo){
        UserMessageVo userMessageVo = new UserMessageVo();
        userMessageVo.idxTo = userMessagePo.getIdxTo();
        userMessageVo.idxFrom = userMessagePo.getIdxFrom();
        userMessageVo.nameFrom = userMessagePo.getNameFrom();
        userMessageVo.nameTo = userMessagePo.getNameTo();
        userMessageVo.content = userMessagePo.getContent();
        userMessageVo.gmtCreate = userMessagePo.getGmtCreate();
        userMessageVo.gmtModify = userMessagePo.getGmtModify();
        userMessageVo.isCheck = userMessagePo.getIsCheck();
        userMessageVo.type = userMessagePo.getType();
        return userMessageVo;
    }
}
