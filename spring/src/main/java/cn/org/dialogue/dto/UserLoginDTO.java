package cn.org.dialogue.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 周正明
 * @date 2024/1/23
 */
@Data
@ApiModel(value = "用户登录请求类")
public class UserLoginDTO {
    /**
     * PASSWORD代表账号密码登录，TEL代表手机号登录，
     * EMAIL代表邮箱登录，WECHAT代表微信扫码登录，
     * QQ代表QQ扫码登录，APP代表对话App扫码登录
     */
    public enum LoginType{
        PASSWORD,
        TEL,
        EMAIL,
        WECHAT,
        QQ,
        APP
    }
    private int id;
    @ApiModelProperty("登录类型")
    private LoginType loginType;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String userPassword;
    @ApiModelProperty("邮箱")
    private String userEmail;
    @ApiModelProperty("电话")
    private String userTel;
    private Integer userPhoto;
    /**
     * 请求时间
     */
    @ApiModelProperty("请求时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    public boolean isPasswordLogin(){
        return getLoginType()==LoginType.PASSWORD;
    }
    public boolean isTelLogin(){
        return getLoginType()==LoginType.TEL;
    }
    public boolean isEmailLogin(){
        return getLoginType()==LoginType.EMAIL;
    }
    public boolean isWechatLogin(){
        return getLoginType()==LoginType.WECHAT;
    }
    public boolean isQQLogin(){
        return getLoginType()==LoginType.QQ;
    }
    public boolean isAppLogin(){
        return getLoginType()==LoginType.APP;
    }
}
