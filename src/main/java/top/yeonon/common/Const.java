package top.yeonon.common;

public class Const {
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String STUDENT_ID = "STUDENT_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String EMAIL = "EMAIL";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }
}
