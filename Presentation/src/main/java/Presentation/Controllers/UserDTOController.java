package Presentation.Controllers;

import Application.Models.Entities.User;
import Application.ResultTypes.UserResult;
import Presentation.DTO.UserDTO;
import Presentation.Interfaces.IUserController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserDTOController {

    private final IUserController userController;

    @Autowired
    public UserDTOController(IUserController userController) {
        this.userController = userController;
    }

    @Operation(summary = "Получить пользователя по ID", description = "Получает информацию о пользователе по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "ID пользователя") @PathVariable int id) {
        User user = userController.GetUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new UserDTO(user));
    }

    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        UserResult result = userController.CreateUser(user);
        if (result instanceof UserResult.Success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @Operation(summary = "Обновить данные пользователя", description = "Обновляет данные о пользователе по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Parameter(description = "ID пользователя") @PathVariable int id, @RequestBody User user) {
        User existingUser = userController.GetUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        UserResult result = userController.UpdateUser(user);
        if (result instanceof UserResult.Success) {
            return ResponseEntity.ok(new UserDTO(user));
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Parameter(description = "ID пользователя") @PathVariable int id) {
        User user = userController.GetUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResult result = userController.DeleteUser(user);
        if (result instanceof UserResult.Success) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}