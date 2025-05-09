package Controllers;

import JWT.JwtUtil;
import Requests.LoginRequest;
import Responses.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> getLoginInfo() {
        return ResponseEntity.status(HttpStatus.OK).body("Please send a POST request to /login with your credentials.");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Проверка данных
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtResponse("Username and password are required"));
            }

            // Аутентификация пользователя
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
            );

            authenticationManager.authenticate(auth); // Попытка аутентификации

            // Загрузка данных пользователя
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwt = jwtUtil.generateToken(userDetails); // Генерация токена

            // Возвращаем токен в ответе
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            // Неверные учетные данные
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponse("Invalid username or password"));
        } catch (Exception e) {
            // Любые другие исключения
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JwtResponse("An unexpected error occurred"));
        }
    }
}
