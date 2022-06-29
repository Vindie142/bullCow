package ru.kazberov.bullCow;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Controllers {
	@GetMapping("/")
    public String homePage(@CookieValue(value = "nickname", required = false) String cookieNickname,
    						HttpServletRequest request, Model model){
		// if the user is logged in
		if (cookieNickname != null) {
			// if the user with which cookie is not found in the database
			if (!Base.checkUser(cookieNickname)) {
				return "redirect:/Log-out";
			}
			
			String butt = request.getParameter("butt");
			// check if it need to create a new game
			Base.getUserWithNickname(cookieNickname).ifNewGame();
			// writing the entered number to the game
			if (butt != null) {
				Base.getUserWithNickname(cookieNickname).getCurrentGame().takeEntered(butt);
			}
			// if there was a victory
			if (Base.getUserWithNickname(cookieNickname).getCurrentGame().getVictory()) {
				return "redirect:/Victory";
			} else {
				model.addAttribute("topPlayers",Base.getTopPlayers());
				model.addAttribute("input",Base.getUserWithNickname(cookieNickname).getCurrentGame().getLine());
				model.addAttribute("attempts",Base.getUserWithNickname(cookieNickname).getCurrentGame().getAttempts());
				return "Game";
			}
		} else { // if the user isn't logged in
			return "redirect:/Registration";
		}
		
    }
    
    @GetMapping("/Registration")
    public String registrationPage(HttpServletRequest request, Model model){
    	return "Registration";
    }
    
    @PostMapping("/Registration")
    public String postRegistrationPage(@RequestParam("nickname") String nickname,
    									@RequestParam("password") String password,
    									Model model){
    	// if there is already such a user
    	if (Base.checkUser(nickname.trim())) {
    		model.addAttribute("output","This nickname is already registered!!!");
        	return "Registration";
		} else {
			new User (nickname.trim(), password.trim());
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
    public String postLoggingPage(HttpServletResponse response,
									@RequestParam("nickname") String nickname,
									@RequestParam("password") String password,
									Model model){
    	// if there is no such user
    	if (!Base.checkUser(nickname.trim())) {
    		model.addAttribute("output","This nickname isn't registered!!!");
        	return "Logging-into-account";
		}
    	// if the password does not fit
    	else if (!Base.getUserWithNickname(nickname.trim()).getPassword().equals(password)) {
			model.addAttribute("output","Incorrect password!!!");
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
    	model.addAttribute("topPlayers",Base.getTopPlayers());
    	model.addAttribute("amountAttempts"," "+Base.getUserWithNickname(cookieNickname).getCurrentGame().getAttempts().size()+" ");
		model.addAttribute("attempts",Base.getUserWithNickname(cookieNickname).getCurrentGame().getAttempts());
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