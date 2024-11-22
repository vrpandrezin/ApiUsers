package br.com.matias.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.matias.todolist.Repository.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterUser extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            var servletPath = request.getServletPath();

            if (servletPath.startsWith("/users/remUser") || servletPath.startsWith("/users/updateUser")) {
                var authorization = request.getHeader("Authorization");

                if (authorization == null || !authorization.startsWith("Basic ")) {
                    writeErrorResponse(response, "Não autorizado.", "Usuário inválido ou sem permissão.");
                    return;
                }

                var authEncoded = authorization.substring("Basic".length()).trim();
                byte[] authDecode = Base64.getDecoder().decode(authEncoded);
                var authString = new String(authDecode);
                String[] credentials = authString.split(":");

                if (credentials.length != 2) {
                    writeErrorResponse(response, "Não autorizado", "Formato de credenciais inválido.");
                    return;
                }

                String username = credentials[0];
                String password = credentials[1];

                var user = this.userRepository.findByUsername(username);
                if (user == null) {
                    writeErrorResponse(response, "Não autorizado.", "Usuário inválido ou sem permissão.");
                    return;
                }

                var storedPassword = user.getPassword();
                if (storedPassword == null || !BCrypt.verifyer().verify(password.toCharArray(), storedPassword).verified) {
                    writeErrorResponse(response, "Não autorizado.", "Senha inválida ou sem permissão.");
                    return;
                }

                if (!"ADMIN".equalsIgnoreCase(user.getType())) {
                    writeErrorResponse(response, "Autenticação negada.", "Usuário sem permissão.");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            writeErrorResponse(response, "Erro interno", "Ocorreu um erro interno no servidor.");
        }
    }
    private void writeErrorResponse(HttpServletResponse response, String status, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String json = String.format("{\"status\": \"%s\", \"message\": \"%s\"}", status, message);
        response.getWriter().write(json);
    }
}
