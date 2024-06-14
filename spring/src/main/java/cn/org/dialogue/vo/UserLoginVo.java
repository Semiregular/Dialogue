package cn.org.dialogue.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 周正明
 * @date  2024/1/26
 */
@Data
@ApiModel(value = "用户登录请求返回类")
public class UserLoginVo {
    @ApiModelProperty(value = "用户id")
    private int idxUser;
    private String idxPhotoUrl;
    private int idxPhoto;
    private String userName;
    private int messageUncheck;
    private String token;

}
