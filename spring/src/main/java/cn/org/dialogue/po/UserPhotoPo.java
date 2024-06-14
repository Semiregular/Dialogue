package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author 周正明
 * @date 2024/2/8
 */
@Data
@TableName("user_photo")
public class UserPhotoPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String url;
    private String gmtCreate;
    private String gmtModify;

    public static UserPhotoPo insert(String url){
        UserPhotoPo userPhotoPo = new UserPhotoPo();
        userPhotoPo.name = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
        userPhotoPo.gmtCreate = dateFormat.format(date);
        userPhotoPo.gmtModify = dateFormat.format(date);
        userPhotoPo.url = url;
        return userPhotoPo;
    }
}
