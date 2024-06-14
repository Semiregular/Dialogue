package cn.org.dialogue.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 周正明
 * @date 2024/2/13
 */
@Data
@TableName("user_comment_history")
public class UserCommentHistoryPo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private int idxUser;
    private int idxComment;
    private int isLike;
    private int isDelete;
    private String gmtCreate;
    private String gmtModify;
}
