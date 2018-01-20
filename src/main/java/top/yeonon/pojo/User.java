package top.yeonon.pojo;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;

    private String studentId;

    private String userName;

    private String password;

    private String email;

    private String avatar;

    private String question;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;

    private Byte banned;

    private String profile;


}