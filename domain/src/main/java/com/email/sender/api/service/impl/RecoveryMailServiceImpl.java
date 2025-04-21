package com.email.sender.api.service.impl;

import com.email.sender.api.model.MailDTO;
import com.email.sender.api.properties.MailProperties;
import com.email.sender.api.service.MailService;
import com.email.sender.api.service.RecoveryMailService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class RecoveryMailServiceImpl implements RecoveryMailService {

    private final MailService mailService;
    private final MailProperties mailProperties;

    public void sendRecoveryMail(MailDTO mailDTO) throws Exception {
        var templateFinal = getString(mailDTO);
        mailProperties.setMaskUser("Recovery Mail");
        mailService.sendMessageMail(mailDTO, templateFinal, mailProperties);
    }

    private String getString(MailDTO mailDTO) throws IOException {
        var resource = new ClassPathResource("template/recovery-mail-template.html");
        String recoveryTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return recoveryTemplate
                .replace("{{TOKEN_AQUI}}", mailDTO.getToken())
                .replace("{{URL_DE_REDEFINICAO}}", mailProperties.getUrlRecovery());
    }
}