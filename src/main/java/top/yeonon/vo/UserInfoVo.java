package top.yeonon.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class UserInfoVo {
    private Integer userId;
    private String studentId;
    private String userName;
    private String avatar;
    private Integer role;
    private String createTime;
    private String updateTime;

}
