package cn.org.dialogue.controller;


import cn.org.dialogue.dto.ArticleDTO;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags="文章接口")
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    public ArticleService articleService;

    @ApiOperation(value = "获取文章阅读")
    @GetMapping("get/read")
    public ResVo getArticleForRead(@RequestParam("idxArticle") int idxArticle){
        return articleService.getArticleForRead(idxArticle);
    }

    @ApiOperation(value = "获取文章更新")
    @GetMapping("get/update")
    public ResVo getArticleForUpdate(@RequestParam("idxArticle") int idxArticle){
        return articleService.getArticleForUpdate(idxArticle);
    }

    @ApiOperation(value = "文章关键词检索 ES")
    @GetMapping("search/es")
    public ResVo searchArticleByES(@RequestParam("key") String key) {
        return articleService.searchArticleByEs(key);
    }

    @ApiOperation(value = "文章关键词检索 Mysql")
    @GetMapping("search/mysql")
    public ResVo searchArticleByMysql(@RequestParam("key")String key,
                                      @RequestParam("pageCurrent")int pageCurrent,
                                      @RequestParam("pageSize")int pageSize,
                                      @RequestParam("orderType")int orderType){
        return articleService.searchArticleByMysql(key,pageCurrent,pageSize,orderType);
    }


    @ApiOperation(value = "文章投稿 Mysql")
    @PostMapping("insert/mysql")
    public ResVo insertArticleToMysql(@RequestBody ArticleDTO articleDTO){
        return articleService.insertArticleToMysql(articleDTO);
    }

    @ApiOperation(value = "文章投稿 ES")
    @PostMapping("insert/es")
    public ResVo insertArticleToEs(@RequestBody ArticleDTO articleDTO){
        return articleService.insertArticleToEs(articleDTO);
    }

    @ApiOperation(value = "文章更新 Mysql")
    @PostMapping("update/mysql")
    public ResVo updateArticleInMysql(@RequestBody ArticleDTO articleDTO){
        return articleService.updateArticleInMysql(articleDTO);
    }

    @ApiOperation(value = "文章删除 Mysql")
    @GetMapping("delete/mysql")
    public ResVo deleteArticleInMysql(@RequestParam("id") int id){
        return articleService.deleteArticleInMysql(id);
    }


    @ApiOperation(value = "更新文章评论标记")
    @PostMapping("update/mark")
    public ResVo updateArticleCommentMark(@RequestBody ArticleDTO articleDTO){
        return articleService.updateArticleCommentMark(articleDTO);
    }





}
