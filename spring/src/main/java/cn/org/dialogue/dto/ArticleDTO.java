package cn.org.dialogue.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

/**
 * @author 周正明
 * @date 2024/1/25
 */
@Data
@ApiModel(value = "文章请求类")
public class ArticleDTO {
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("用户id")
    private int idxUser;
    @ApiModelProperty("用户名")
    private String userName;
    private int userPhoto;
    private int articlePhoto;
    private String[] tag;
    private Map<Integer,String> updateTagMap;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("副标题")
    private String subtitle;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("内容")
    private String content;
    private String markData;
    private int totalMark;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;
    @ApiModelProperty("文档格式类型")
    private Integer editType;
}
