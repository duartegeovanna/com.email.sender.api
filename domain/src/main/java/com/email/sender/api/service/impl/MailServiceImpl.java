package com.email.sender.api.service.impl;

import com.email.sender.api.model.MailDTO;
import com.email.sender.api.properties.MailProperties;
import com.email.sender.api.service.MailService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@AllArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    @Override
    public void sendMessageMail(MailDTO mailDTO, String template, MailProperties mailProperties) throws Exception {
        JavaMailSender mailSender = getJavaMailSender(mailProperties);
        MimeMessage message = buildMimeMessage(mailSender, mailDTO, template, mailProperties);
        mailSender.send(message);
    }

    private JavaMailSender getJavaMailSender(MailProperties mailProperties) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding());
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUser());
        mailSender.setPassword(mailProperties.getPassword());

        var props = buildMailProperties(mailSender, mailProperties);
        mailSender.setJavaMailProperties(props);
        return mailSender;
    }

    private Properties buildMailProperties(JavaMailSenderImpl mailSender, MailProperties mailProperties) {
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.host", mailProperties.getHost());
        props.put("mail.smtp.port", mailProperties.getPort());
        props.put("mail.transport.protocol", mailProperties.getProtocol());
        props.put("mail.smtp.auth", mailProperties.getUseAuth());
        props.put("mail.smtp.starttls.enable", mailProperties.getStarttlsEnable());
        return props;
    }

    private MimeMessage buildMimeMessage(JavaMailSender mailSender, MailDTO mailDTO,
                                        String template, MailProperties mailProperties) throws Exception {
        var message = mailSender.createMimeMessage();
        message.setContent(template,"text/html; charset=UTF-8");
        message.setFrom(new InternetAddress(mailProperties.getUser(), mailProperties.getMaskUser()));
        message.setSubject(mailDTO.getSubject());
        message.setRecipients(Message.RecipientType.TO, mailDTO.getEmail());
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setText(template, true);
        return message;
    }
}