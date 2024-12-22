package org.example.avtodiller.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.avtodiller.services.UserService;
import org.example.avtodiller.utils.UsersAndRoles;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
class UserApiController {
    private final UserService userService;

    @PostMapping(value = "/api/auth/login", consumes = {"application/x-www-form-urlencoded", "application/json"})
    ResponseEntity<String> logined(@RequestBody Map<String, String> loginData, HttpServletRequest request) throws Exception {

            Cookie cookie = userService.loginUserWithApi(loginData);
            if(cookie != null)
            {
                ResponseCookie responseCookie = ResponseCookie.from(cookie.getName(),cookie.getValue())
                        .maxAge(Duration.ofMinutes(30))
                        .path("/")
                        .httpOnly(true)
                        .secure(request.isSecure())
                        .build();
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
                return new ResponseEntity<>("Login successfully!", headers, HttpStatus.OK);
            }
        return ResponseEntity.badRequest().body("Login failed");
    }

    @PostMapping(value = "/api/auth/reg")
    ResponseEntity<String> registered(@RequestBody Map<String, String> regData) throws Exception {

        boolean res = userService.registerUserWithApi(regData);
        if(res)
        {
            return ResponseEntity.ok().body("Register successful");
        }
        else
        {
            return ResponseEntity.badRequest().body("Register failed");
        }
    }

    @GetMapping(value = "/api/users")
    ResponseEntity<UsersAndRoles> users(HttpServletRequest request) throws Exception {
        try {
            UsersAndRoles usersAndRoles = userService.list(request);
            return ResponseEntity.ok(usersAndRoles);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/api/users", consumes = {"application/x-www-form-urlencoded", "application/json"})
    ResponseEntity<String> setUserRole(@RequestBody Map<String, String> userAndRole, HttpServletRequest request) throws Exception {
        try {
            userService.setUserRole(request, userAndRole);
            return ResponseEntity.ok().body("update");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/api/auth/logout")
    ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            userService.logout(request, response);
            return ResponseEntity.ok().body("logout");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
