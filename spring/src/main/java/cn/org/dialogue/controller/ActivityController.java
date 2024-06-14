package cn.org.dialogue.controller;

import cn.org.dialogue.dto.PageDTO;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Api(tags="活动接口")
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    public ActivityService activityService;

    @ApiOperation(value = "获取活动文章")
    @PostMapping("list")
    private ResVo listActivityArticle(@RequestBody PageDTO pageDTO){
        return activityService.listArticleActivity(pageDTO);
    }

}
