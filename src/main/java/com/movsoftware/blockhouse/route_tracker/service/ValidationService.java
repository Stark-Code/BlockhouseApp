package com.movsoftware.blockhouse.route_tracker.service;

import org.springframework.stereotype.Service;

@Service
public interface ValidationService {
   void validateName(String name);
   void validateUUID(String uuid);
   void validateDescription(String description);
   boolean validateJWTFormat(String jwt);
   boolean validateEmailFormat(String email);
}
