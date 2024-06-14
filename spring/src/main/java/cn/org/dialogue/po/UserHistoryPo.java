package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



/**
 * @author 周正明
 * @date 2024/1/23
 */
@Data
@TableName("user_history")
public class UserHistoryPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private int idxUser;
    private int idxArticle;
    private String title;
    private int isLike;
    private int isCollect;
    private int isShare;
    private int isComment;
    private int isDelete;
    private String markData;
    private String gmtCreate;
    private String gmtModify;
}
