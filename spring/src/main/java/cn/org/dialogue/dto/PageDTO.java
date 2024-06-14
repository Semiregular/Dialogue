package cn.org.dialogue.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 周正明
 * @date 2024/2/2
 */
@Data
@ApiModel(value = "分页数据类")
public class PageDTO {
    private int id;
    private int idxUser;
    private int idxFrom;
    private int idxTo;
    private String key;
    @ApiModelProperty("当前页容量")
    private int pageSize;
    @ApiModelProperty("当前页数")
    private int pageCurrent;
    private int pageStart;

    public void setStart(){
        this.pageStart = this.getPageSize() * (this.pageCurrent - 1);
    }
}
