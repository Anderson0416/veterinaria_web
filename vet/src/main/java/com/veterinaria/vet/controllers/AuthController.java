package com.veterinaria.vet.controllers;

import com.veterinaria.vet.config.UserAuthenticationProvider;
import com.veterinaria.vet.dtos.CredentialsDto;
import com.veterinaria.vet.dtos.SignUpDto;
import com.veterinaria.vet.dtos.UpdatePasswordDto;
import com.veterinaria.vet.dtos.UserDto;
import com.veterinaria.vet.entities.User;
import com.veterinaria.vet.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin()));
        return ResponseEntity.ok(userDto);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
        System.out.println("Datos recibidos para registro controller: " + user);
        UserDto createdUser = userService.register(user);
        System.out.println("foto generado: " + user.getFoto());
        createdUser.setToken(userAuthenticationProvider.createToken(user.getLogin()));
        System.out.println("usuario creado controller " + createdUser);
        return ResponseEntity.created(URI.create("/usuarios/" + createdUser.getId())).body(createdUser);
    }

    @PutMapping("/update/{login}")
    public ResponseEntity<?> updateUser(
        @PathVariable String login,
        @RequestParam(required = true) String id,
        @RequestParam(required = true) String nombre,
        @RequestParam(required = true) String apellido,
        @RequestParam(required = false) MultipartFile foto
    ) {
        try {
            // Convertir el ID a Long de manera segura
            Long userId;
            try {
                userId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return ResponseEntity
                    .badRequest()
                    
                    .body("ID de usuario inválido "+id);
            }

            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty() ||
                apellido == null || apellido.trim().isEmpty() ) {
                return ResponseEntity
                    .badRequest()
                    .body("Todos los campos son requeridos");
            }

            User updatedUser = userService.updateUser(login, userId, nombre, apellido, foto);
        System.out.println("Usuario actualizado CONTROLLER: " + updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar los datos del usuario: " + e.getMessage());
        }
    }
    
    @PutMapping("/update-password") 
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto) { 
        try { 
            userService.updatePassword(updatePasswordDto); 
            return ResponseEntity.ok("Contraseña actualizada correctamente"); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la contraseña: " + e.getMessage()); 
        } 
    }
    /* 
    @PostMapping("/request-password-reset") 
public ResponseEntity<?> requestPasswordReset(@RequestBody @Valid RequestPasswordResetDto requestPasswordResetDto) {        
        try { 
            userService.requestPasswordReset(requestPasswordResetDto.getEmail()); 
            return ResponseEntity.ok("Se ha enviado un enlace de restablecimiento de contraseña a su correo electrónico"); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al solicitar el restablecimiento de contraseña: " + e.getMessage()); 
        } 
    }
    @PutMapping("/reset-password") 
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetRequestDto passwordResetRequestDto) {        
        try { 
            userService.updatePasswordWithToken(passwordResetRequestDto); 
            return ResponseEntity.ok("Contraseña actualizada correctamente"); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la contraseña: " + e.getMessage()); 
        } 
    }
    */
    @GetMapping("/profile") 
    public ResponseEntity<UserDto> getUserProfile(Authentication authentication) { 
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Autenticación fallida");
            return ResponseEntity.status(401).build();
        }
    
        String userDtoString = authentication.getName();
        System.out.println("Usuario autenticado CONTROLLER: " + userDtoString);

        String login = extractLoginFromUserDto(userDtoString); 
        
        System.out.println("Login extraído: " + login);
        UserDto user = userService.findByLogin(login);
        
        if (user == null) {
            System.out.println("Perfil de usuario no encontrado");
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(user); 
    }

    @GetMapping("/receptionists")
    public ResponseEntity<List<UserDto>> getReceptionists() {
        try {
            List<UserDto> receptionists = userService.findAllReceptionists();
            System.out.println("Recepcionistas encontrados: " + receptionists);
            return ResponseEntity.ok(receptionists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/veterinarians")
    public ResponseEntity<List<UserDto>> getVeterinarians() {
        try {
            List<UserDto> receptionists = userService.findAllveterinarians();
            System.out.println("Veterinarios encontrados: " + receptionists);
            return ResponseEntity.ok(receptionists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDto> getUserProfileById(@PathVariable Long id) {
        try {
            System.out.println("Buscando usuario con ID: " + id);
            UserDto user = userService.findById(id);
            
            if (user == null) {
                System.out.println("Perfil de usuario no encontrado para el ID: " + id);
                return ResponseEntity.status(404).build();
            }
            
            System.out.println("Usuario encontrado: " + user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (id == 1) {
            return ResponseEntity.badRequest().body("No se puede eliminar el usuario");
        }
        try {
            System.out.println("Intentando eliminar usuario con ID: " + id);
            boolean deleted = userService.deleteUser(id);
            
            if (deleted) {
                System.out.println("Usuario eliminado exitosamente");
                return ResponseEntity.ok().body("Usuario eliminado exitosamente");
            } else {
                System.out.println("Usuario no encontrado con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error al eliminar usuario: " + e.getMessage());
        }
    }

    private String extractLoginFromUserDto(String userDtoString) { 
        String loginPrefix = "login="; 
        int loginStart = userDtoString.indexOf(loginPrefix) + loginPrefix.length(); 
        int loginEnd = userDtoString.indexOf(',', loginStart); 
        if (loginEnd == -1) { loginEnd = userDtoString.indexOf(')', loginStart); 
        } 
        return userDtoString.substring(loginStart, loginEnd).trim(); 
    }

}
