package cn.org.dialogue.res;


import lombok.Getter;

@Getter
public enum ResStatusEnum {
    /* 默认成功状态码 */
    SUCCESS(1, "success"),
    /* 默认失败状态码 */
    ERROR(0, "failure"),
    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),
    /* 用户错误：20001-29999*/
    USER_NOT_LOGIN(20001, "用户未登录"),
    USER_HAS_LOGIN(20002,"用户已登录"),
    USER_LOGIN_ERROR(20003, "账号或密码信息有误"),
    USER_ACCOUNT_FORBIDDEN(20004, "账号已被封禁"),
    USER_NOT_EXIST(20005, "用户不存在"),
    USER_HAS_EXISTED(20006, "用户已存在"),
    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "业务逻辑出现问题"),
    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统内部错误，请稍后重试"),
    /* 数据错误：50001-599999 */
    DATA_NONE(50001, "数据不存在"),
    DATA_WRONG(50002, "数据错误"),
    DATA_EXISTED(50003, "数据已存在"),
    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限");

    private final Integer code;
    private final String message;
    ResStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}

