package cn.org.dialogue.controller;

import cn.org.dialogue.po.ArticleCommentPo;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.service.ArticleCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags="文章评论接口")
@RestController
@RequestMapping("/comment")
public class ArticleCommentController {
    @Autowired
    public ArticleCommentService articleCommentService;

    @ApiOperation(value = "获取文章一级评论")
    @GetMapping("get/main")
    public ResVo getMainComment(@RequestParam("idxArticle") int idxArticle, @RequestParam("idxUser") int idxUser){
        return articleCommentService.getMainComment(idxArticle, idxUser);
    }

    @ApiOperation(value = "插入文章评论")
    @PostMapping("insert")
    public ResVo insertComment(@RequestBody ArticleCommentPo articleCommentPo){
        return articleCommentService.insertComment(articleCommentPo);
    }


}
