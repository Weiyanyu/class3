package top.yeonon.service;

import com.github.pagehelper.PageInfo;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Notice;
import top.yeonon.vo.NoticeDetailVo;

public interface INoticeService {

    ServerResponse addNotice(Notice notice);

    ServerResponse batchDeleteNotice(String noticeIds);

    ServerResponse<PageInfo> getNoticeList(int pageNum, int pageSize, Integer topicId,String orderBy);

    ServerResponse<NoticeDetailVo> getDetail(Integer noticeId);

    ServerResponse updateNotice(Integer noticeId, Notice notice);

    ServerResponse<PageInfo> searchNotice(int pageNum, int pageSize, String noticeTitle, String orderBy);
}
