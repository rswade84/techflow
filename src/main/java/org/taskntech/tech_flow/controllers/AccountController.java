package org.taskntech.tech_flow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.taskntech.tech_flow.data.UserRepository;
import org.taskntech.tech_flow.models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/manage-account")
    public String showManageAccountPage() {
        return "manage-account";
    }

    // Unified method to find user by email, GitHub ID, or Google Sub ID.
    private User findUserByOAuth(OAuth2AuthenticationToken authentication, OAuth2User principal) {
        String provider = authentication.getAuthorizedClientRegistrationId();
        String email = principal.getAttribute("email");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            if ("github".equals(provider)) {
                Integer githubIdInt = principal.getAttribute("id");
                String githubId = (githubIdInt != null) ? String.valueOf(githubIdInt) : null;
                user = userRepository.findByGithubId(githubId);
            } else if ("google".equals(provider)) {
                String googleSubId = principal.getAttribute("sub");
                user = userRepository.findByGoogleSubId(googleSubId);
            }
        }
        return user;
    }

    // Handles updating the display name of a user based on their authentication provider.
    @PostMapping("/manage-account/update")
    public String updateDisplayName(@RequestParam("displayName") String displayName,
                                    @AuthenticationPrincipal OAuth2User principal,
                                    OAuth2AuthenticationToken authentication) {

        User user = findUserByOAuth(authentication, principal);

        if (user == null) {
            return "redirect:/manage-account?error=UserNotFound";
        }

        user.setDisplayName(displayName);
        userRepository.save(user);

        return "redirect:/manage-account?success=true";
    }

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/";

    // Handles user profile picture uploads and saves the path in the database.
    @PostMapping("/manage-account/upload-profile-picture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                       @AuthenticationPrincipal OAuth2User principal,
                                       OAuth2AuthenticationToken authentication) {
        if (file.isEmpty()) {
            return "redirect:/manage-account?error=NoFileUploaded";
        }

        try {
            User user = findUserByOAuth(authentication, principal);

            if (user == null) {
                return "redirect:/manage-account?error=UserNotFound";
            }

            String fileName = "profile-" + user.getUserId() + ".png";
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY, fileName);

            Files.createDirectories(uploadPath.getParent());
            Files.write(uploadPath, file.getBytes());

            user.setProfilePicturePath("/uploads/" + fileName);
            userRepository.save(user);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/manage-account?error=FileUploadFailed";
        }
        return "redirect:/manage-account?profilePictureUploaded=true";
    }
}