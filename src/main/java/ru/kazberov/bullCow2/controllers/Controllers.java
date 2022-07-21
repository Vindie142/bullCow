package ru.kazberov.bullCow2.controllers;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.kazberov.bullCow2.BullCow2Application;
import ru.kazberov.bullCow2.models.Game;
import ru.kazberov.bullCow2.models.User;
import ru.kazberov.bullCow2.repo.UserRepo;

@Controller
public class Controllers {
	
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/")
    public String homePage(@CookieValue(value = "nickname", required = false) String cookieNickname,
    						HttpServletRequest request, Model model){
		// if the user is logged in
		if (cookieNickname != null) {
			// if the user with which cookie is not found in the database
			if (!userRepo.checkUser(cookieNickname)) {
				return "redirect:/Log-out";
			}
			// check if it need to create a new game
			if (userRepo.getUserWithNickname(cookieNickname).ifNewGame()) {
				User user = userRepo.getUserWithNickname(cookieNickname);
				new Game(user);
				userRepo.save(user);
			}
			// writing the entered number to the game
			String butt = request.getParameter("butt");
			if (butt != null) {
				userRepo.getUserWithNickname(cookieNickname).getCurrentGame().takeEntered(butt);
			}
			// saves changes
			userRepo.save(userRepo.getUserWithNickname(cookieNickname));
			// if there was a victory
			if (userRepo.getUserWithNickname(cookieNickname).getCurrentGame().getVictory()) {
				return "redirect:/Victory";
			} else {
				model.addAttribute("topPlayers",userRepo.getTopPlayers());
				model.addAttribute("input",userRepo.getUserWithNickname(cookieNickname).getCurrentGame().getLine());
				model.addAttribute("attempts",userRepo.getUserWithNickname(cookieNickname).getCurrentGame().getAttempts());
				return "Game";
			}
		} else { // if the user isn't logged in
			return "redirect:/Logging-into-account";
		}
		
    }
    
    @GetMapping("/Registration")
    public String registrationPage(HttpServletRequest request, Model model){
    	return "Registration";
    }
    
    @PostMapping("/Registration")
    public String registrationPage(@RequestParam("nickname") String nickname,
    									@RequestParam("password") char[] password,
    									Model model){
    	// if there is already such user
    	if (userRepo.checkUser(nickname.trim())) {
    		model.addAttribute("output","This nickname is already registered!!!");
        	return "Registration";
		} else {
			User user = new User (nickname.trim(), Arrays.hashCode(password));
			BullCow2Application.resetPassword(password);
			userRepo.save(user);
	    	return "redirect:/Successful-registration";
		}
    	
    }
    
    @GetMapping("/Successful-registration")
    public String successfulRegistrationPage(){
    	return "Successful-registration";
    }
    
    @GetMapping("/Logging-into-account")
    public String loggingPage(){
    	return "Logging-into-account";
    }
    
    @PostMapping("/Logging-into-account")
    public String loggingPage(HttpServletResponse response,
									@RequestParam("nickname") String nickname,
									@RequestParam("password") char[] password,
									Model model){
    	// if there is no such user
    	if (!userRepo.checkUser(nickname.trim())) {
    		model.addAttribute("output","This nickname isn't registered!!!");
        	return "Logging-into-account";
		}
    	// if the password does not fit
    	else if (userRepo.getUserWithNickname(nickname.trim()).getPassword() != Arrays.hashCode(password)) {
    		BullCow2Application.resetPassword(password);
			model.addAttribute("output","Incorrect password!!!");
			model.addAttribute("nickname",nickname);
			return "Logging-into-account";
		} else {
			Cookie cookie = new Cookie("nickname", nickname.trim());
		     cookie.setPath("/");
		     cookie.setMaxAge(86400);
		     response.addCookie(cookie);
	    	return "redirect:/";
		}
    }
    
    @GetMapping("/Victory")
    public String victoryPage(@CookieValue(value = "nickname", required = false) String cookieNickname,
    							Model model){
    	model.addAttribute("topPlayers",userRepo.getTopPlayers());
    	model.addAttribute("amountAttempts"," "+userRepo.getUserWithNickname(cookieNickname).getCurrentGame().getAttempts().size()+" ");
		model.addAttribute("attempts",userRepo.getUserWithNickname(cookieNickname).getCurrentGame().getAttempts());
    	return "Victory";
    }
    
    @GetMapping("/Log-out")
    public String LogOut(HttpServletResponse response, Model model){
    	// reset cookies
    	Cookie cookie = new Cookie("nickname", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    	return "redirect:/Logging-into-account";
    }
}