package com.movsoftware.blockhouse.route_tracker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.service.EntityService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;
import com.movsoftware.blockhouse.route_tracker.service.ValidationService;

@RestController
@RequestMapping("/gym")
public class GymController {
    
    @Value("${main.server.url}")
    private String mainServerUrl;

    @Value("${developer.api.key}")
    private String developerApiKey;

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }

    // private final EntityService entityService;
    private final ValidationService validationService;
    private final GymService gymService;

    public GymController(EntityService entityService, ValidationService validationService, GymService gymService) {
        // this.entityService = entityService;
        this.validationService = validationService;
        this.gymService = gymService;
    }

    @Transactional
    @PostMapping("/save_gym") // Status: Production Ready. API Access Only
    public ResponseEntity<Gym> createOrganization(@RequestParam("name") String name,
            @RequestHeader("x-developer-key") String key) {
        if (!key.equals(developerApiKey)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        validationService.validateName(name);
        Gym gym = new Gym(name);
        Gym savedGym = gymService.saveGym(gym);

        return new ResponseEntity<>(savedGym, HttpStatus.CREATED);
    }
}
