package cn.org.dialogue.vo;

import cn.org.dialogue.po.UserContactPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 周正明
 * @date 2024/2/2
 */
@Data
public class UserContactVo {
    private Integer id;
    private Integer idxFrom;
    private Integer idxTo;
    private String photoFromUrl;
    private String photoToUrl;
    private String nameTo;
    private String nameFrom;
    private String content;
    private int isDelete;
    private int isCheck;
    private int type;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;

    public static UserContactVo convert(UserContactPo userContactPo){
        UserContactVo userContactVo = new UserContactVo();
        userContactVo.id = userContactPo.getId();
        userContactVo.idxFrom = userContactPo.getIdxFrom();
        userContactVo.idxTo = userContactPo.getIdxTo();
        userContactVo.gmtCreate = userContactPo.getGmtCreate();
        userContactVo.gmtModify = userContactPo.getGmtModify();
        userContactVo.content = userContactPo.getContent();
        userContactVo.type = userContactPo.getType();
        userContactVo.nameFrom = userContactPo.getNameFrom();
        userContactVo.nameTo = userContactPo.getNameTo();
        userContactVo.isCheck = userContactPo.getIsCheck();
        return userContactVo;
    }
}
