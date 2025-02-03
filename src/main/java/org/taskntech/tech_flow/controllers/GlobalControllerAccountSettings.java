package org.taskntech.tech_flow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.taskntech.tech_flow.data.UserRepository;
import org.taskntech.tech_flow.models.User;
import org.springframework.ui.Model;

@Controller
@RestControllerAdvice
public class GlobalControllerAccountSettings {

    @Autowired
    private UserRepository userRepository;

    private static final String DEFAULT_PROFILE_PICTURE_URL = "https://www.tenforums.com/attachments/user-accounts-family-safety/322690d1615743307-user-account-image-log-user.png";

    // Finds user based on the authentication provider
    private User findUserByOAuth(OAuth2AuthenticationToken authentication, OAuth2User principal) {
        String provider = authentication.getAuthorizedClientRegistrationId();

        if ("github".equals(provider)) {
            Integer githubIdInt = principal.getAttribute("id");
            String githubId = (githubIdInt != null) ? String.valueOf(githubIdInt) : null;
            return userRepository.findByGithubId(githubId);
        } else if ("google".equals(provider)) {
            String googleSubId = principal.getAttribute("sub");
            return userRepository.findByGoogleSubId(googleSubId);
        }
        return null;
    }

    @ModelAttribute
    public void addGlobalAttributes(@AuthenticationPrincipal OAuth2User principal,
                                    OAuth2AuthenticationToken authentication, Model model) {
        if (principal != null) {
            String provider = authentication.getAuthorizedClientRegistrationId();
            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");

            User user = findUserByOAuth(authentication, principal);

            if (user == null) {
                // If user doesn't exist, create a new one
                if ("github".equals(provider)) {
                    Integer githubIdInt = principal.getAttribute("id");
                    String githubId = (githubIdInt != null) ? String.valueOf(githubIdInt) : null;
                    user = new User(email, name, githubId, null);
                } else if ("google".equals(provider)) {
                    String googleSubId = principal.getAttribute("sub");
                    user = new User(email, name, null, googleSubId);
                }
                userRepository.save(user);
            } else {
                if ("github".equals(provider) && user.getGithubId() == null) {
                    Integer githubIdInt = principal.getAttribute("id");
                    user.setGithubId((githubIdInt != null) ? String.valueOf(githubIdInt) : null);
                } else if ("google".equals(provider) && user.getGoogleSubId() == null) {
                    user.setGoogleSubId(principal.getAttribute("sub"));
                }
                userRepository.save(user);
            }

            model.addAttribute("name", user.getDisplayName());
            model.addAttribute("profilePicturePath",
                    user.getProfilePicturePath() != null ? user.getProfilePicturePath() : DEFAULT_PROFILE_PICTURE_URL);
        } else {
            model.addAttribute("name", "Not Signed In");
            model.addAttribute("profilePicturePath", DEFAULT_PROFILE_PICTURE_URL);
        }
    }
}