package top.yeonon.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentDetailVo {
    private Integer commentId;

    private Integer userId;

    private String userName;

    private String userAvatar;

    private Integer noticeId;

    private String commentDesc;

    private String createTime;

    private String updateTime;

}
