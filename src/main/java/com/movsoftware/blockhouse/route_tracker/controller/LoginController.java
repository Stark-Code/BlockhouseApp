package com.movsoftware.blockhouse.route_tracker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.movsoftware.blockhouse.route_tracker.service.ValidationService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Value("${main.server.url}")
    private String mainServerUrl;

    @Value("${auth.server.url}")
    private String authServerUrl;

    @Autowired
    private ValidationService validationService;

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }

    @GetMapping("/get_login")
    public ModelAndView login(Model model) {
        model.addAttribute("login", new String());
        model.addAttribute("authServerUrl", authServerUrl);
        model.addAttribute("mainServerUrl", mainServerUrl);
        return new ModelAndView("login");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("home");
    }

    @GetMapping("/register")
    public ModelAndView register(Model model,
            @RequestParam(value = "invite_code", required = false) String invite_code) {
                    
        if (invite_code != null && validationService.validateJWTFormat(invite_code)) {
            model.addAttribute("invite_code", invite_code);
        }

        model.addAttribute("authServerUrl", authServerUrl);
        model.addAttribute("mainServerUrl", mainServerUrl);
        model.addAttribute("register", new String());
        return new ModelAndView("register");
    }

    @PostMapping("/add_session_token")
    public void submitToken(@RequestBody Map<String, String> payload, HttpSession session) {
        System.out.println("Request received at /add_session_token");
        String jwt = payload.get("jwt");
        String uid = payload.get("uid");
        String name = payload.get("name");

        session.setAttribute("uid", uid);
        session.setAttribute("name", name);
        session.setAttribute("jwt", jwt);
    }
}
