package cn.org.dialogue.vo;

import cn.org.dialogue.po.UserPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 周正明
 * @date 2024/2/9
 */
@Data
public class UserInfoVo {
    private Integer id;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    private String userName;
    private int idxPhoto;
    private String idxPhotoUrl;
    private String userIntro;
    private String userTel;
    private String userEmail;
    private String userCity;
    private String ip;
    private int userLevel;
    private int totalArticleView;
    private int totalArticleLike;
    private int totalArticleCollect;
    private int totalArticleNum;
    private int totalFollowerNum;

    public static UserInfoVo convert(UserPo userPo){
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.id = userPo.getId();
        userInfoVo.ip = userPo.getIp();
        userInfoVo.userName = userPo.getUserName();
        userInfoVo.idxPhoto = userPo.getIdxPhoto();
        userInfoVo.gmtCreate = userPo.getGmtCreate();
        userInfoVo.gmtModify = userPo.getGmtModify();
        userInfoVo.userIntro = userPo.getUserIntro();
        userInfoVo.userLevel = userPo.getUserLevel();
        userInfoVo.userCity = userPo.getUserCity();
        userInfoVo.userTel = userPo.getUserTel();
        userInfoVo.userEmail = userPo.getUserEmail();
        userInfoVo.totalArticleNum = userPo.getTotalArticleNum();
        userInfoVo.totalArticleLike = userPo.getTotalArticleLike();
        userInfoVo.totalArticleView = userPo.getTotalArticleView();
        userInfoVo.totalArticleCollect = userPo.getTotalArticleCollect();
        userInfoVo.totalFollowerNum = userPo.getTotalFollowerNum();
        return userInfoVo;
    }
}
