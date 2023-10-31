package com.movsoftware.blockhouse.route_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @GetMapping("/")
    public RedirectView redirectToLoginPage() {
        return new RedirectView("/login/get_login");
    }
}

