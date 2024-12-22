package org.example.avtodiller.services;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.example.avtodiller.models.RoleModel;
import org.example.avtodiller.models.UserModel;
import org.example.avtodiller.repositories.RoleRepository;
import org.example.avtodiller.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.avtodiller.utils.JWTUtils;
import org.example.avtodiller.utils.UsersAndRoles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final Gson gson;

    public boolean register(String login, String password)
    {
        if(login == null || login.isEmpty() || password == null || password.isEmpty()) { return false; }

        if (userRepository.findByLogin(login) != null) { return false; }

        try {
            int sizeOfUsers = userRepository.findAll().size();
            List<RoleModel> roles = roleRepository.findAll();
            UserModel newUser = null;
            if (sizeOfUsers == 0) {
                newUser = new UserModel(login, passwordEncoder.encode(password), roles.get(0));
            } else {
                newUser = new UserModel(login, passwordEncoder.encode(password), roles.get(1));
            }

            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean registerUserWithApi(Map<String, String> regData)
    {
        String login = regData.get("login");
        String password = regData.get("password");
        return register(login, password);
    }

    public Cookie login(String login, String password)
    {
        if(login == null || login.isEmpty() || password == null || password.isEmpty()) { return null; }

        UserModel foundUser = userRepository.findByLogin(login);
        if(foundUser != null) {
            if(passwordEncoder.matches(password, foundUser.getPassword()))
            {
                Cookie cookie = new Cookie("token",new JWTUtils().generateToken(foundUser));
                cookie.setPath("/");
                cookie.setMaxAge((int) Duration.ofMinutes(30).toSeconds());
                cookie.setHttpOnly(true);
//                cookie.setSecure(true);
                return cookie;
            }
        }
        return null;
    }

    public Cookie loginUserWithApi(Map<String, String> loginData) throws Exception {
        String login = loginData.get("login");
        String password = loginData.get("password");
        return login(login, password);
    }
    public UserModel getUser(HttpServletRequest request) {
        Cookie tokenCookie = jwtUtils.findCookie(request);
        String token = tokenCookie.getValue();
        String userData = jwtUtils.tokenData(token);
        return gson.fromJson(userData, UserModel.class);
    }

    public UsersAndRoles list(HttpServletRequest request) {
        UserModel currentUser = getUser(request);
        if(currentUser.getRole().equals("ROLE_ADMIN")){
            List<UserModel> users = userRepository.findAll()
                    .stream()
                    .filter(el-> !Objects.equals(el.getId(), currentUser.getId()))
                    .toList();
            List<RoleModel> roles = roleRepository.findAll();

            return new UsersAndRoles(users, roles);
        }else{
            throw new RuntimeException("nizya");
        }

    }

    public void setUserRole(HttpServletRequest request, Map<String, String> userAndRole) {
        UserModel reqFromUser = getUser(request);
        if(reqFromUser.getRole().equals("ROLE_ADMIN"))
        {
            String currentUser = userAndRole.get("login");
            Long currentRole = Long.valueOf(userAndRole.get("role"));
            UserModel user = userRepository.findByLogin(currentUser);
            RoleModel role = roleRepository.findById(currentRole).orElseThrow();
            user.setRole(role);
            userRepository.save(user);
        }
        else{
            throw new RuntimeException("Not allowed to set user role");
        }

    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token","");
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.ofMinutes(30).toSeconds());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
