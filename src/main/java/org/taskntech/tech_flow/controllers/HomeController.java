package org.taskntech.tech_flow.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class HomeController {

//    @RequestMapping("/")
//    public String index(Model model, @AuthenticationPrincipal OAuth2User principal){
//
//        if (principal != null) {
//            String name = principal.getAttribute("name");
//            String username = principal.getAttribute("login");
//            String email = principal.getAttribute("email");
//            //for github private emails
//            if(email == null) {
//                email = "Set to private";
//            }
//            //google doesn't have login/username
//            if(username == null) {
//                username = email;
//            }
//            Map<String,Object> all = principal.getAttributes();
//            model.addAttribute("authenticated", true);
//            model.addAttribute("name", name);
//            model.addAttribute("username", username);
//            model.addAttribute("email", email);
//            model.addAttribute("all", all);
//        } else {
//            model.addAttribute("authenticated", false);
//        }
//
//        return "index";
//    }

    @RequestMapping("/logoutTest")
    public String index() {
        return "logout-tester";
    }
}
