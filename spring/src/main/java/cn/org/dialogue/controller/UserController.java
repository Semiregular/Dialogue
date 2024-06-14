package cn.org.dialogue.controller;

import cn.org.dialogue.dto.UserLoginDTO;
import cn.org.dialogue.ip.IpParseUtil;
import cn.org.dialogue.po.UserCommentHistoryPo;
import cn.org.dialogue.po.UserFollowerPo;
import cn.org.dialogue.po.UserHistoryPo;
import cn.org.dialogue.res.ResStatusEnum;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.service.UserService;
import cn.org.dialogue.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Api(tags="用户接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    public UserService userService;
    @Autowired
    public StringRedisTemplate stringRedisTemplate;


    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    private ResVo userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        switch (userLoginDTO.getLoginType()) {
            case PASSWORD:
                return userService.userLoginByPassword(userLoginDTO);
            case TEL:
                return userService.userLoginByTel(userLoginDTO);
            case EMAIL:
                return userService.userLoginByEmail(userLoginDTO);
            default:
                return ResVo.failure(ResStatusEnum.PARAM_IS_INVALID);
        }
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("insert")
    private ResVo userInsert(@RequestBody UserLoginDTO userLoginDTO){
        switch (userLoginDTO.getLoginType()) {
            case PASSWORD:
                return userService.userInsertByPassword(userLoginDTO);
            default:
                return ResVo.failure(ResStatusEnum.PARAM_IS_INVALID);
        }
    }

    @ApiOperation(value = "获取手机验证码")
    @GetMapping("tel/send")
    private ResVo getTelVerify(@RequestParam("tel")String tel){
        try {
            return userService.sendTelVerify(tel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResVo.failure();
        }
    }

    @ApiOperation(value = "更新用户密码")
    @PostMapping("update/password")
    private ResVo updateUserPassword(@RequestBody UserLoginDTO userLoginDTO){
        return userService.updateUserPassword(userLoginDTO);
    }

    @ApiOperation(value = "更新用户名")
    @PostMapping("update/username")
    private ResVo updateUserName(@RequestBody UserLoginDTO userLoginDTO){
        return userService.updateUserName(userLoginDTO);
    }

    @ApiOperation(value = "获取用户实时Ip")
    @GetMapping("get/ip")
    private ResVo getUserIp(HttpServletRequest request){
        String ip = request.getHeader("X-Real-IP");
        int[] index = {2,3};
        if(ip != null){
            List<String> list = IpParseUtil.parse(ip,index);
            return ResVo.success(list);
        }else {
            ip = request.getRemoteAddr();
            if(Objects.equals(ip, "127.0.0.1") || Objects.equals(ip, "0:0:0:0:0:0:0:1")){
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                List<String> list = IpParseUtil.parse(ip,index);
                return ResVo.success(list);
            }else {
                List<String> list = IpParseUtil.parse(ip, index);
                return ResVo.success(list);
            }
        }
    }
    @ApiOperation(value = "更新用户Ip")
    @GetMapping("update/ip")
    private ResVo updateUserIp(@RequestParam("id")int id,@RequestParam("ip")String ip){
        return userService.updateUserIp(id,ip);
    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("update/photo")
    private ResVo updateUserPhoto(@RequestBody UserLoginDTO userLoginDTO){
        return userService.updateUserPhoto(userLoginDTO);
    }


    @ApiOperation(value = "更新用户资料信息")
    @PostMapping("update/info")
    private ResVo updateUserInfo(@RequestBody UserInfoVo userInfoVo){
        return userService.updateUserInfo(userInfoVo);
    }

    @ApiOperation(value = "获取用户空间信息")
    @GetMapping("get/info")
    private ResVo getUserInfo(@RequestParam("idxUser")int idxUser){
        return userService.getUserInfo(idxUser);
    }

    @ApiOperation(value = "获取用户发布信息")
    @GetMapping("get/article")
    private ResVo getUserArticle(@RequestParam("idxUser")int idxUser,
                                 @RequestParam("pageCurrent")int pageCurrent,
                                 @RequestParam("pageSize")int pageSize){
        return userService.getUserArticle(idxUser,pageCurrent,pageSize);
    }

    @ApiOperation(value = "获取用户收藏信息")
    @GetMapping("get/collect")
    private ResVo getUserCollect(@RequestParam("idxUser")int idxUser,
                                 @RequestParam("pageCurrent")int pageCurrent,
                                 @RequestParam("pageSize")int pageSize){
        return userService.getUserCollect(idxUser,pageCurrent,pageSize);
    }

    @ApiOperation(value = "更新用户收藏信息")
    @GetMapping("update/collect")
    private ResVo updateUserCollect(@RequestParam("idxUser")int idxUser,@RequestParam("idxArticle")int idxArticle){
        return userService.updateUserCollect(idxUser,idxArticle);
    }

    @ApiOperation(value = "获取用户历史信息")
    @GetMapping("list/history")
    public ResVo listUserHistory(@RequestParam("idxUser")int idxUser,
                                @RequestParam("pageCurrent")int pageCurrent,
                                @RequestParam("pageSize")int pageSize){
        return userService.listUserHistory(idxUser,pageCurrent,pageSize);
    }

    @ApiOperation(value = "删除用户历史信息")
    @GetMapping("delete/history")
    public ResVo deleteUserHistory(@RequestParam("id") int id){
        return userService.deleteUserHistory(id);
    }

    @ApiOperation(value = "获取用户文章历史信息")
    @GetMapping("get/history/read")
    public ResVo getUserHistory(@RequestParam("idxUser")int idxUser,
                                @RequestParam("idxArticle")int idxArticle){
        return userService.getUserHistoryRead(idxUser,idxArticle);
    }

    @ApiOperation(value = "更新用户历史信息")
    @PostMapping("update/history")
    public ResVo updateUserHistory(@RequestBody UserHistoryPo userHistoryPo){
        return userService.updateUserHistory(userHistoryPo);
    }

    @ApiOperation(value = "更新用户历史收藏信息")
    @PostMapping("update/history/collect")
    public ResVo updateUserHistoryCollect(@RequestBody UserHistoryPo userHistoryPo){
        return userService.updateUserHistoryCollect(userHistoryPo);
    }

    @ApiOperation(value = "更新用户评论历史信息")
    @PostMapping("update/comment/history")
    public ResVo updateUserCommentHistory(@RequestBody UserCommentHistoryPo userCommentHistoryPo){
        return userService.updateUserCommentHistory(userCommentHistoryPo);
    }
    @ApiOperation(value = "更新用户文章标记信息")
    @PostMapping("update/mark")
    public ResVo updateUserMark(@RequestBody UserHistoryPo userHistoryPo){
        return userService.updateUserMark(userHistoryPo);
    }

    @ApiOperation(value = "获取用户关注信息")
    @GetMapping("get/follower")
    public ResVo getFollower(@RequestParam("idxUser") int idxUser,
                             @RequestParam("idxFollower")int idxFollower){
        return userService.getUserFollower(idxUser,idxFollower);
    }

    @ApiOperation(value = "更新用户关注信息")
    @PostMapping("update/follower")
    public ResVo updateFollower(@RequestBody UserFollowerPo userFollowerPo){
        return userService.updateUserFollower(userFollowerPo);
    }


}

