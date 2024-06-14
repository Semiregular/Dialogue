package cn.org.dialogue.controller;

import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 周正明
 * @date 2024/2/6
 */

@Api(tags="图片接口")
@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    public ImageService imageService;

    @ApiOperation(value = "上传图片到云端")
    @PostMapping("insert/oss")
    public ResVo insertMessage(@RequestBody MultipartFile file){
        return ResVo.success(imageService.insertOSS(file));
    }

    @ApiOperation(value = "上传图片到本地")
    @PostMapping("insert/local")
    public ResVo insertLocalImg(@RequestBody MultipartFile file){
        return imageService.insertLocalImg(file);
    }

    @ApiOperation(value = "上传文章封面")
    @PostMapping("insert/article")
    public ResVo insertArticleImg(@RequestBody MultipartFile file){
        return imageService.insertArticleImg(file);
    }

    @ApiOperation(value = "上传用户头像")
    @PostMapping("insert/user")
    public ResVo insertUserImg(@RequestBody MultipartFile file){
        return imageService.insertUserImg(file);
    }

}
