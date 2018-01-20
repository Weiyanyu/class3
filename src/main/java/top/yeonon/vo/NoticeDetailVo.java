package top.yeonon.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.yeonon.pojo.Comment;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
public class NoticeDetailVo {
    private Integer noticeId;

    private Integer userId;

    private Integer topicId;

    private String noticeTitle;

    private String noticeDesc;

    private String createTime;

    private String updateTime;

    private List<CommentDetailVo> commentDetailVoList;

}
