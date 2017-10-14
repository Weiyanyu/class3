package top.yeonon.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;

import top.yeonon.service.IMailSenderService;
import top.yeonon.util.PropertiesUtil;

import java.util.Date;

@Service
public class MailSenderService implements IMailSenderService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;


    private static final String SUBJECT = "这是一个邮箱注册激活链接";

    @Override
    public ServerResponse sendMail(String toEmail,String content) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(PropertiesUtil.getProperty("mail.username"));
        smm.setTo(toEmail);
        smm.setSubject(SUBJECT);
        smm.setText(content);
        smm.setSentDate(new Date());
        javaMailSender.send(smm);
        return ServerResponse.createBySuccessMessage("发送成功");
    }

}
