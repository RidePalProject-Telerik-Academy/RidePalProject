package com.example.ridepalapplication.controllers.mvc;

import com.example.ridepalapplication.dtos.RegisterDto;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.mappers.UserMapper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
import com.example.ridepalapplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;
    private final PlaylistService playlistService;

    @Autowired
    public UserMvcController(UserService userService, UserMapper userMapper, AuthenticationHelper authenticationHelper, PlaylistService playlistService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
        this.playlistService = playlistService;
    }

    @GetMapping("/myProfile")
    public String getMyProfilePage(Authentication authentication,Model model) {
        try {

            User user = authenticationHelper.tryGetUser(authentication);
            List<Playlist> userPlaylists = playlistService.getUserPlaylists(user.getId());
            model.addAttribute("userPlaylists",userPlaylists);
            model.addAttribute("user",user);

           return "MyProfileView";
        }catch (AuthorizationException e){
            model.addAttribute("statusCode",
                    HttpStatus.UNAUTHORIZED.getReasonPhrase());

            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated() {
        return authenticationHelper.isAuthenticated();
    }

    @GetMapping("/register")
    public String getRegisterView(Model model) {
        model.addAttribute("user", new RegisterDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }
        try {
            User user = userMapper.fromRegisterDtoToUser(registerDto);
            userService.createUser(user);
            return "redirect:/playlists";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "RegisterView";
        }
    }

}
