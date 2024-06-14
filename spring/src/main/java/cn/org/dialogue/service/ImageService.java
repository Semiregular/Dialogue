package cn.org.dialogue.service;

import cn.org.dialogue.mapper.ArticlePhotoMapper;
import cn.org.dialogue.mapper.UserPhotoMapper;
import cn.org.dialogue.po.ArticlePhotoPo;
import cn.org.dialogue.po.UserPhotoPo;
import cn.org.dialogue.res.ResStatusEnum;
import cn.org.dialogue.res.ResVo;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author 周正明
 * @date 2024/2/6
 */
@Component
@Service
@Slf4j
public class ImageService {
    @Autowired
    public ArticlePhotoMapper articlePhotoMapper;
    @Autowired
    public UserPhotoMapper userPhotoMapper;
    public String insertOSS(MultipartFile file){
        String endpoint = "https://oss-cn-wulanchabu.aliyuncs.com";
        String accessKeyId = "xxxxxxxxxxxxxxx";
        String accessKeySecret = "xxxxxxxxxxxxxxxxxxxxxx";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "dialogue-oss";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        /**修改文件名称和创建一个新的文件路径名*/
        //创建日期目录
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        String datepath = dateFormat.format(new Date());
        /**创建文件路径*/
        String name = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String suffixName = name.substring(name.lastIndexOf("."));
        String newName= uuid + suffixName;
        String fileUrl= datepath+"/"+newName;
        try {

            //解决访问图片路径时导致直接下载问题
            /*阿里云默认存储图片的请求头为 image/jpeg,此时打开图片地址就会直接下载。将请求头改为image/jpg，*/
            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileUrl, file.getInputStream(),objectMetadata);
            ossClient.putObject(putObjectRequest);
            String url = "https://dialogue-oss.oss-cn-wulanchabu.aliyuncs.com/" + fileUrl;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public String getArticleImageUrl(int idxPhoto){
        QueryWrapper<ArticlePhotoPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("url")
                .eq("id",idxPhoto);
        ArticlePhotoPo articlePhotoPo = articlePhotoMapper.selectOne(queryWrapper);
        return articlePhotoPo.getUrl();
    }

    public ResVo insertArticleImg(MultipartFile file){
        String url = insertOSS(file);
        ArticlePhotoPo articlePhotoPo = ArticlePhotoPo.insert(url);
        int isInsert = articlePhotoMapper.insert(articlePhotoPo);
        if(isInsert == 1){
            return ResVo.success(articlePhotoPo);
        }else{
            return ResVo.failure();
        }
    }

    public String getUserImageUrl(int idxPhoto){
        QueryWrapper<UserPhotoPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("url")
                .eq("id",idxPhoto);
        UserPhotoPo userPhotoPo = userPhotoMapper.selectOne(queryWrapper);
        return userPhotoPo.getUrl();
    }

    public ResVo insertUserImg(MultipartFile file){
        String url = insertOSS(file);
        UserPhotoPo userPhotoPo = UserPhotoPo.insert(url);
        int isInsert = userPhotoMapper.insert(userPhotoPo);
        if(isInsert == 1){
            return ResVo.success(userPhotoPo);
        }else{
            return ResVo.failure();
        }
    }

    public ResVo insertLocalImg(MultipartFile file){
        if (file.isEmpty()) {
            return ResVo.failure(ResStatusEnum.ERROR);
        }
        String originalFilename = file.getOriginalFilename();
        String ext = null;
        if (originalFilename != null) {
            ext = "."+ originalFilename.split("\\.")[1];
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newName = uuid + ext;
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\";
        String path = pre + newName;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResVo.success(path);
    }

}
