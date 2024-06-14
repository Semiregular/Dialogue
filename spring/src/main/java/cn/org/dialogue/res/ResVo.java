package cn.org.dialogue.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 周正明
 * @date 2024/1/25
 * 返回结果包装类
 */
@Data
public class ResVo implements Serializable {

    private static final long SerialVersionUID = 1L;
    private int code;
    private String message;
    private Object data;

    /**
     * 操作成功，没有返回的数据
     * */
    public static ResVo success() {
        ResVo resVo = new ResVo();
        resVo.setCode(ResStatusEnum.SUCCESS.getCode());
        resVo.setMessage(ResStatusEnum.SUCCESS.getMessage());
        return resVo;
    }

    /**
     * 操作成功，有返回的数据
     * */
    public static ResVo success(Object data) {
        ResVo resVo = new ResVo();
        resVo.setCode(ResStatusEnum.SUCCESS.getCode());
        resVo.setMessage(ResStatusEnum.SUCCESS.getMessage());
        resVo.setData(data);
        return resVo;
    }

    /**
     * 操作失败，默认信息有误
     */
    public static ResVo failure(){
        ResVo resVo = new ResVo();
        resVo.setCode(ResStatusEnum.ERROR.getCode());
        resVo.setMessage(ResStatusEnum.ERROR.getMessage());
        return resVo;
    }

    /**
     * 操作失败，没有返回的数据，返回特定状态错误码
     * */
    public static ResVo failure(ResStatusEnum resStatusEnum) {
        ResVo resVo = new ResVo();
        resVo.setCode(resStatusEnum.getCode());
        resVo.setMessage(resStatusEnum.getMessage());
        return resVo;
    }

    /**
     * 操作失败，有返回的数据，返回特定状态错误码
     * */
    public static ResVo failure(ResStatusEnum resStatusEnum, Object data) {
        ResVo resVo = new ResVo();
        resVo.setCode(resStatusEnum.getCode());
        resVo.setMessage(resStatusEnum.getMessage());
        resVo.setData(data);
        return resVo;
    }

}

