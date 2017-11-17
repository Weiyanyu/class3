package top.yeonon.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String STUDENT_ID = "STUDENT_ID";
    public static final String EMAIL = "EMAIL";

    public static final Integer FORGET_TOKEN_EXPIRES_HOUR = 6;
    public static final Integer LOGIN_TOKEN_EXPIRES_HOUR = 12;


    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface TopicOrderBy {
        Set<String> NAME_ASC_DESC = Sets.newHashSet("name_asc", "name_desc");
        Set<String> ID_ASC_DESC = Sets.newHashSet("id_asc", "id_desc");
    }

    public interface NoticeOrderBy {
        Set<String> TITLE_ASC_DESC = Sets.newHashSet("title_asc", "title_desc");
        Set<String> ID_ASC_DESC = Sets.newHashSet("id_asc", "id_desc");
    }

}
