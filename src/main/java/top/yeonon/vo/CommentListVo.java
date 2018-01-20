package top.yeonon.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentListVo {
    private Integer commentId;
    private String commentDesc;
    private Integer userId;
    private Integer noticeId;
    private String noticeTitle;
    private String userName;
    private String userAvatar;

}
