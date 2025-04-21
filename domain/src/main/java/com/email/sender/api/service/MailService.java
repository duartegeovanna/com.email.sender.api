package com.email.sender.api.service;

import com.email.sender.api.model.MailDTO;
import com.email.sender.api.properties.MailProperties;
import jakarta.mail.MessagingException;

public interface MailService {

    void sendMessageMail(MailDTO mailDTO, String template, MailProperties mailProperties) throws Exception;
}
