package com.email.sender.api.controller;

import com.email.sender.api.dto.MailRequestDTO;
import com.email.sender.api.mapper.MailMapper;
import com.email.sender.api.service.RecoveryMailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/v1/recovery-mail", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recovery Mail", description = "Recovery Mail API")
public class RecoveryMailController {

    private final RecoveryMailService recoveryMailService;
    private final MailMapper mailMapper;

    @PostMapping
    @Operation(summary = "Send recovery mail",
            description = "Send recovery mail to the user with the provided email address",
            tags = {"Recovery Mail"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recovery mail sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email address"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    public ResponseEntity<Void> sendRecoveryMail(@Valid @RequestBody MailRequestDTO mailRequestDTO) throws Exception {

        recoveryMailService.sendRecoveryMail(mailMapper.toDTO(mailRequestDTO));
        return ResponseEntity.ok().build();
    }
}
