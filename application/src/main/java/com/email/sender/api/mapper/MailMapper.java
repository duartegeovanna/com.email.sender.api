package com.email.sender.api.mapper;

import com.email.sender.api.dto.MailRequestDTO;
import com.email.sender.api.model.MailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailMapper {

    MailDTO toDTO(MailRequestDTO mailRequestDTO);
}
