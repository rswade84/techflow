package org.taskntech.tech_flow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.taskntech.tech_flow.data.UserRepository;
import org.taskntech.tech_flow.models.User;

//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Map;

@Controller
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/manage-account")
    public String showManageAccountPage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            String email = principal.getAttribute("email");
            User user = userRepository.findByEmail(email);

            if (user == null) {
                user = new User(email, principal.getAttribute("name"));
                userRepository.save(user);
            }

            model.addAttribute("name", user.getDisplayName());
            model.addAttribute("email", email);
        }
        return "manage-account";
    }

    @PostMapping("/manage-account/update")
    public String updateDisplayName(@RequestParam("displayName") String displayName,
                                    @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setDisplayName(displayName);
            userRepository.save(user);
        }
        return "redirect:/manage-account?success=true";
    }

    private static final String UPLOAD_DIRECTORY = "uploads/";

//    @PostMapping("/manage-account/upload-profile-picture")
//    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
//                                       @AuthenticationPrincipal OAuth2User principal) {
//        if (file.isEmpty()) {
//            return "redirect:/manage-account?error=NoFileUploaded";
//        }
//
//        try {
//            String username = principal.getAttribute("login");
//            if (username == null || username.isEmpty()) {
//                username = principal.getAttribute("email");
//            }
//
//            String fileName = username + ".profile-image";
//            Path uploadPath = Paths.get(UPLOAD_DIRECTORY, fileName);
//            Files.createDirectories(uploadPath.getParent());
//            Files.write(uploadPath, file.getBytes());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "redirect:/manage-account?error=FileUploadFailed";
//        }
//        return "redirect:/manage-account?profilePictureUploaded=true";
//    }

}