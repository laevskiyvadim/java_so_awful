package org.example.avtodiller.interceptors;

import jakarta.servlet.http.Cookie;
import org.example.avtodiller.utils.JWTUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class HandlerInterceptorAdapter implements HandlerInterceptor {

    private final JWTUtils jwtUtils;
    private final Gson gson;

    public HandlerInterceptorAdapter(JWTUtils jwtUtils, Gson gson)
    {
        this.jwtUtils = jwtUtils;
        this.gson = gson;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/") && !requestURI.startsWith("/api/auth/")) {

            try
            {
                Cookie tokenCookie = jwtUtils.findCookie(request);
                if (tokenCookie == null || !new JWTUtils().validateToken(tokenCookie.getValue()))
                {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
            }
            catch (Exception e)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
//        if (!(request.getRequestURI().equals("/login")
//                || request.getRequestURI().equals("/reg")
//                || request.getRequestURI().equals("/api/auth/reg")
//                || request.getRequestURI().equals("/api/auth/login"))
//        ) {
//            try {
//                    Cookie tokenCookie = jwtUtils.findCookie(request);
//                    if (tokenCookie != null)
//                    {
//                        String token = tokenCookie.getValue();
//                        if (jwtUtils.validateToken(token))
//                        {
//                            String userData = jwtUtils.tokenData(token);
//                            UserModel userModel = gson.fromJson(userData, UserModel.class);
//                            if (modelAndView != null)
//                            {
//                                modelAndView.addObject("role", userModel.getRole());
//                            }
//                        }
//                        else
//                        {
//                            response.sendRedirect("/login");
//                        }
//                    }
//                    else
//                    {
//                        response.sendRedirect("/login");
//                    }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                response.sendRedirect("/login");
//            }
//        }
    }
}
