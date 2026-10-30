package com.foodxever.config;

import com.foodxever.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object usuarioSesion = request.getSession(false) == null
                ? null : request.getSession(false).getAttribute("usuarioActual");

        if (!(usuarioSesion instanceof Usuario usuario)) {
            response.sendRedirect(request.getContextPath() + "/login?acceso=administracion");
            return false;
        }

        if (!"ADMIN".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/?acceso=denegado");
            return false;
        }
        return true;
    }
}
