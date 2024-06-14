package cn.org.dialogue.po;

import cn.org.dialogue.dto.UserMessageDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.util.Date;

/**
 * @author 周正明
 * @date 2024/2/2
 */
@Data
@TableName("user_contact")
public class UserContactPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private int idxTo;
    private int idxFrom;
    private String nameTo;
    private String nameFrom;
    private int photoFrom;
    private int photoTo;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private String content;
    private int isDelete;
    private int isCheck;
    private int type;
    public static UserContactPo insert(UserMessageDTO userMessageDTO){
        UserContactPo userContactPo = new UserContactPo();
        userContactPo.idxFrom = userMessageDTO.getIdxFrom();
        userContactPo.idxTo = userMessageDTO.getIdxTo();
        userContactPo.photoFrom = userMessageDTO.getPhotoFrom();
        userContactPo.photoTo = userMessageDTO.getPhotoTo();
        userContactPo.nameFrom = userMessageDTO.getNameFrom();
        userContactPo.nameTo = userMessageDTO.getNameTo();
        userContactPo.gmtCreate = userMessageDTO.getGmtCreate();
        userContactPo.gmtModify = userMessageDTO.getGmtCreate();
        userContactPo.content = userMessageDTO.getContent();
        userContactPo.isCheck = 0;
        userContactPo.isDelete = 0;
        userContactPo.type = userMessageDTO.getType();
        return userContactPo;
    }
}
