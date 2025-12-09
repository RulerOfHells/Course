package com.rohan.dev.course.controller;

import com.rohan.dev.course.dto.UserDTO;
import com.rohan.dev.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null && error.equals("bad_credentials")) {
            model.addAttribute("error", "Incorrect email or password");
        }
        return "loginform";
    }

    @GetMapping("/mfa-login")
    public String mfaLogin(Model model) {
        System.out.println("mfa-login arrived");
        boolean mfaRequired = model.containsAttribute("mfaRequired");
        String email = Objects.requireNonNull(model.getAttribute("email")).toString();
        if(mfaRequired) {
            UserDTO userDTO = userService.getUserFromEmail(email);
            userService.sendVerificationCode(userDTO);
            model.addAttribute("username", userDTO.getFirstName()+" "+userDTO.getLastName());
            return "mfaform";
        }
        return "redirect:/login";
    }
}
