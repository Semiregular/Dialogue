package cn.org.dialogue.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import cn.org.dialogue.res.ResVo;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Random;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;
    @RequestMapping("send")
    public ResVo sendSimpleMail(@RequestParam(value = "receiver") String emailReceiver) throws MessagingException {
        MailUtil emailUtil = new MailUtil();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Mail codeEntity = new Mail();
        helper.setFrom(from);
        helper.setTo(emailReceiver);
        helper.setSubject("验证码");
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(10);
            code.append(r);
        }
        String text = emailUtil.buildContent(code.toString());
        helper.setText(text,true);

        try {
            javaMailSender.send(message);
            codeEntity.setEmail(Arrays.toString(message.getFrom()));
            codeEntity.setCode(code.toString());
            codeEntity.setMsg("success");
            return ResVo.success(codeEntity);
        } catch (MailException e) {
            e.printStackTrace();
        }
        return ResVo.failure();
    }
}
