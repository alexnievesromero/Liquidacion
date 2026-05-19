package Liquidacion.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorMsg = "credenciales";

        if (exception instanceof BadCredentialsException) {
            errorMsg = "credenciales";
        } else if (exception instanceof DisabledException) {
            errorMsg = "deshabilitado";
        } else if (exception instanceof LockedException) {
            errorMsg = "bloqueado";
        }

        response.sendRedirect("/login?msg=" + errorMsg);
    }
}

