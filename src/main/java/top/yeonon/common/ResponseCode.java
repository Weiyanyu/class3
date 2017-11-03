package top.yeonon.common;

public enum ResponseCode {

    SUCCESS(0,"SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    NEED_LOGIN_ADMIN(20, "NEED_LOGIN_ADMIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    ILLEGAL_REQUEST(30, "ILLEGAL_REQUEST"),
    GOTO_USER_SHOW(100, "ILLEGAL_REQUEST");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

}
