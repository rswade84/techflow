package org.taskntech.tech_flow.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String login(HttpServletRequest request, Model model) {
        model.addAttribute("requestUri", request.getRequestURI());
        return "index";
    }

}
