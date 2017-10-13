package top.yeonon.service;

import top.yeonon.common.ServerResponse;

public interface IMailSenderService {
    ServerResponse sendMail(String toEmail, String content);
}
