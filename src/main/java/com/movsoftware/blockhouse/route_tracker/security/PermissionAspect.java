package com.movsoftware.blockhouse.route_tracker.security;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.movsoftware.blockhouse.route_tracker.exceptions.PermissionsException;
import com.movsoftware.blockhouse.route_tracker.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private JwtService jwtService;

    @Pointcut("@annotation(requiresPermission)")
    public void callAt(RequiresPermission requiresPermission) {}

    @Before("callAt(requiresPermission)")
    public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) {
            throw new PermissionsException("No JWT found in session");
        }

        List<String> permissions = jwtService.getPermissionsFromJwt(jwt);
        List<String> requiredPermissions = Arrays.asList(requiresPermission.value());

        if (!permissions.containsAll(requiredPermissions)) {
            throw new PermissionsException("You don't have necessary permissions");
        }
    }
}

