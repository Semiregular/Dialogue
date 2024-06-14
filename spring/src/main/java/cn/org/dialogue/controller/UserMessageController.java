package cn.org.dialogue.controller;

import cn.org.dialogue.dto.PageDTO;
import cn.org.dialogue.dto.UserMessageDTO;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.service.UserMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags="用户消息接口")
@RestController
@RequestMapping("/message")
public class UserMessageController {
    @Autowired
    public UserMessageService userMessageService;

    @ApiOperation(value = "获取联系人")
    @PostMapping("get/contact")
    public ResVo getContact(@RequestBody PageDTO pageDTO){
        return userMessageService.getUserContact(pageDTO);
    }

    @ApiOperation(value = "更新联系人")
    @PostMapping("update/contact")
    public ResVo updateContact(@RequestBody UserMessageDTO userMessageDTO){
        return userMessageService.updateUserContact(userMessageDTO);
    }

    @ApiOperation(value = "获取联系人最近消息")
    @PostMapping("get")
    public ResVo getMessage(@RequestBody PageDTO pageDTO){
        return userMessageService.getUserMessage(pageDTO);
    }

    @ApiOperation(value = "发送消息")
    @PostMapping("send")
    public ResVo sendMessage(@RequestBody UserMessageDTO userMessageDTO){
        return userMessageService.sendUserMessage(userMessageDTO);
    }

    @ApiOperation(value = "更新消息状态")
    @GetMapping("check")
    public ResVo checkMessage(@RequestParam("id")int id){
        return userMessageService.checkMessage(id);
    }


}