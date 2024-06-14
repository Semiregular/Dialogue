package cn.org.dialogue.po;

import cn.org.dialogue.dto.UserMessageDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.util.Date;


@Data
@TableName("user_message")
public class UserMessagePo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private int idxTo;
    private int idxFrom;
    private int photoFrom;
    private String nameTo;
    private String nameFrom;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private String content;
    private int isDelete;
    private int isCheck;
    private int type;
    public static UserMessagePo insert(UserMessageDTO userMessageDTO){
        UserMessagePo userMessagePo = new UserMessagePo();
        userMessagePo.idxTo = userMessageDTO.getIdxTo();
        userMessagePo.idxFrom = userMessageDTO.getIdxFrom();
        userMessagePo.content = userMessageDTO.getContent();
        userMessagePo.nameFrom = userMessageDTO.getNameFrom();
        userMessagePo.nameTo = userMessageDTO.getNameTo();
        userMessagePo.gmtCreate = userMessageDTO.getGmtCreate();
        userMessagePo.gmtModify = userMessageDTO.getGmtCreate();
        userMessagePo.photoFrom = userMessageDTO.getPhotoFrom();
        userMessagePo.isCheck = 0;
        userMessagePo.isDelete = 0;
        userMessagePo.type = userMessageDTO.getType();
        return userMessagePo;
    }
}
