package com.codesanook.service;

import com.codesanook.dto.emails.EmailDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    private Log log = LogFactory.getLog(MailService.class);
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public MailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private void sendWithSimpleMailSender(EmailDto emailDto, WebContext ctx) {

        SimpleMailMessage message = new SimpleMailMessage();
        String from = emailDto.getFromEmail();
        String to = emailDto.getToEmail();
        String subject = emailDto.getSubject();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);

        final String body = templateEngine.process(emailDto.getTemplate(), ctx);
        message.setText(body);
        mailSender.send(message);
    }

    public boolean sendMail(final EmailDto emailDto, final WebContext ctx) {

        try {
            sendMailWithJavaMailSender(emailDto, ctx);
            return true;

        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }


    private void sendMailWithJavaMailSenderImpl(EmailDto emailDto, WebContext ctx) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(emailDto.getFromEmail(), emailDto.getFromName());
        helper.setTo(emailDto.getToEmail());

        helper.setSubject(emailDto.getSubject());
        final String htmlContent = templateEngine.process(emailDto.getTemplate(), ctx);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private void sendMailWithJavaMailSender(final EmailDto emailDto, final WebContext ctx) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {


                InternetAddress from = new InternetAddress(emailDto.getFromEmail(),emailDto.getFromName(),"UTF-8");
                mimeMessage.setFrom(from);
                mimeMessage.setRecipients(Message.RecipientType.TO, emailDto.getToEmail());

                //mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
                mimeMessage.setSubject(emailDto.getSubject(), "UTF-8");
                final String htmlContent = templateEngine.process(emailDto.getTemplate(), ctx);
                mimeMessage.setContent(htmlContent, "text/html; charset=UTF-8");
            }
        };

        mailSender.send(preparator);
        log.debug("email sent");
    }

}
