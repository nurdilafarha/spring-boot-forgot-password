package com.nurdila.controller;

import com.hendisantika.entity.PasswordResetToken;
import com.nurdila.service.framework.PasswordResetTokenService;
import com.nurdila.service.framework.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Locale;

@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {
    private final PasswordResetTokenService tokenService;
    private final MessageSource messageSource;
    private final UserService userService;

    @Autowired
    public ResetPasswordController(PasswordResetTokenService tokenService, MessageSource messageSource,
                                   UserService userService) {
        this.tokenService = tokenService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @GetMapping
    public String viewPage(@RequestParam(name = "token", required = false) String token,
                           Model model) {
        PasswordResetToken passwordResetToken = tokenService.findByToken(token);
        if (passwordResetToken == null) {
            model.addAttribute("error", messageSource.getMessage("TOKEN_NOT_FOUND", new Object[]{}, Locale.ENGLISH));
        } else if (passwordResetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", messageSource.getMessage("TOKEN_EXPIRED", new Object[]{}, Locale.ENGLISH));
        } else {
            model.addAttribute("token", passwordResetToken.getToken());
        }
        return "reset-password";
    }
}
