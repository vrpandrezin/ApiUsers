package br.com.matias.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.matias.todolist.repository.IUserRepository;
import br.com.matias.todolist.Response.ErroResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

            // Verifica se a rota requer autenticação
            if (servletPath.startsWith("/users/remUser") || servletPath.startsWith("/users/updateUser")) {
                var authorization = request.getHeader("Authorization");

                // Valida a presença do cabeçalho Authorization
                if (authorization == null || !authorization.startsWith("Basic ")) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Credenciais ausentes ou inválidas.");
                    return;
                }

                // Decodifica o cabeçalho Authorization
                var authEncoded = authorization.substring("Basic".length()).trim();
                byte[] authDecode = Base64.getDecoder().decode(authEncoded);
                var authString = new String(authDecode);
                String[] credentials = authString.split(":");

                if (credentials.length != 2) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Formato de credenciais inválido.");
                    return;
                }

                String username = credentials[0];
                String password = credentials[1];

                // Busca o usuário no banco de dados
                var user = this.userRepository.findByUsername(username);
                if (user == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não encontrado.");
                    return;
                }

                // Verifica a senha
                var storedPassword = user.getPassword();
                if (storedPassword == null || !BCrypt.verifyer().verify(password.toCharArray(), storedPassword).verified) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Senha incorreta.");
                    return;
                }
            }

            // Continua o fluxo se todas as verificações forem bem-sucedidas
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Log para depuração (opcional)
            e.printStackTrace();

            // Retorna erro 500 com uma mensagem amigável
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor.");
        }
    }
}
