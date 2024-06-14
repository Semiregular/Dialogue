package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
@TableName("user_collect")
public class UserCollectPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String gmtCreate;
    private String gmtModify;
    private int idxUser;
    private int idxArticle;
    private String title;
    private int isDelete;
}
