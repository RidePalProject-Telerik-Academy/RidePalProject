package com.example.ridepalapplication.controllers.mvc;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admins")
public class AdminMvcController {

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AdminMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public String getAdminPanel(Authentication authentication, Model model) {
        try {
            User user = authenticationHelper.tryGetUser(authentication);

            if (!authenticationHelper.isAdmin(authentication)) {
                throw new AuthorizationException("You are not allowed to enter Admin portal.");
            }

            model.addAttribute("user", user);
            return "AdminView";

        }catch (AuthorizationException e){
            model.addAttribute("statusCode",
                    HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

}
