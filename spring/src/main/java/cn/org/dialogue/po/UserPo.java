package cn.org.dialogue.po;

import cn.org.dialogue.dto.UserLoginDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class UserPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private int isLogin;
    private int isDelete;
    private int idxPhoto;
    private String ip;
    private String userName;
    private String userPassword;
    private String userIntro;
    private int userAge;
    private String userCity;
    private int userLevel;
    private String userTel;
    private String userEmail;
    private int totalArticleView;
    private int totalArticleLike;
    private int totalArticleCollect;
    private int totalArticleNum;
    private int totalFollowerNum;

    public static UserPo insertByPassword(UserLoginDTO userLoginDTO){
        UserPo userPo = new UserPo();
        userPo.userName = userLoginDTO.getUserName();
        userPo.userPassword = userLoginDTO.getUserPassword();
        userPo.gmtCreate = userLoginDTO.getGmtCreate();
        userPo.gmtModify = userLoginDTO.getGmtCreate();
        userPo.idxPhoto = 0;
        userPo.isLogin = 0;
        return userPo;
    }
}
