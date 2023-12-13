package com.example.ridepalapplication.controllers.mvc;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.SynchronizationDetails;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
import com.example.ridepalapplication.services.SynchronizationService;
import com.example.ridepalapplication.services.UserService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final PlaylistService playlistService;
    private final UserService userService;
    private final SynchronizationService synchronizationService;

    @Autowired
    public AdminMvcController(AuthenticationHelper authenticationHelper, PlaylistService playlistService, UserService userService, SynchronizationService synchronizationService) {
        this.authenticationHelper = authenticationHelper;
        this.playlistService = playlistService;
        this.userService = userService;
        this.synchronizationService = synchronizationService;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated() {
        return authenticationHelper.isAuthenticated();
    }
    @GetMapping
    public String getAdminPanel(Authentication authentication, Model model) {
        try {
            User user = verifyAuthority(authentication);
            List<Playlist> playLists = playlistService.getAll();
            model.addAttribute("playlists",playLists);
            model.addAttribute("user", user);
            return "AdminView";

        }catch (AuthorizationException e){
            model.addAttribute("statusCode",
                    HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @GetMapping("/synchronize")
    public String synchronizeGenres(Authentication authentication,Model model) throws ParseException{
       try {
           User user = verifyAuthority(authentication);
          synchronizationService.synchronize();
           return "redirect:/admins/sync";
       }catch (AuthorizationException e){
           model.addAttribute("error",e.getMessage());
           return "ErrorView";
       }
    }

    @GetMapping("/sync")
    public String getSyncView(Authentication authentication,Model model){
        User user = verifyAuthority(authentication);
        List<SynchronizationDetails> mostRecent = synchronizationService.mostRecent();
        model.addAttribute("user",user);
        model.addAttribute("mostRecent",mostRecent);
        return "SyncView";
    }

    @GetMapping("/users")
    public String getUsersView(Authentication authentication,Model model){
        User user = verifyAuthority(authentication);
        List<User> allUsers = userService.getAll();
        model.addAttribute("user",user);
        model.addAttribute("allUsers",allUsers);
        return "UsersView";
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(Authentication authentication, Model model, @PathVariable Long id){
        User user = verifyAuthority(authentication);

        userService.deleteUser(user, id);
        if(user.getId()==id){
            return "redirect:/logout";
        }
        model.addAttribute("user",user);
        return "redirect:/admins/users";
    }

    private User verifyAuthority(Authentication authentication) {
        User user = authenticationHelper.tryGetUser(authentication);

        if (!authenticationHelper.isAdmin(authentication)) {
            throw new AuthorizationException("You are not allowed to enter Admin portal.");
        }
        return user;
    }
}
