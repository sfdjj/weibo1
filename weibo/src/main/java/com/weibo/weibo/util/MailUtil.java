package com.weibo.weibo.util;

import com.weibo.weibo.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jwc on 2017/7/28.
 */
@Component
public class MailUtil implements InitializingBean{

    private JavaMailSenderImpl mailSender;
 /*   public static void main(String args[]) {
        MailUtil mailUtil = new MailUtil();
        mailUtil.sendWithHTMLTemplate("991157472@qq.com","登陆通知");
    }
*/
    public boolean sendWithHTMLTemplate(Map<String,Object> map) {
        try {
            User user = null;
            if(!map.containsKey("user")) {
                return false;
            }
            user = (User)map.get("user");
            String to = user.getMail();
            String subject = null;
            if(map.containsKey("subject")) {
                subject = map.get("subject").toString();
            }
            String nick = MimeUtility.encodeText("O(∩_∩)O！");
            InternetAddress from = new InternetAddress(nick + "<3051639559@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String result = "欢迎登陆";
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();

        // 请输入自己的邮箱和密码，用于发送邮件
        mailSender.setUsername("3051639559@qq.com");
        mailSender.setPassword("sxejzxhwvwkvdhdd");
        mailSender.setHost("smtp.qq.com");
        // 请配置自己的邮箱和密码

        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
