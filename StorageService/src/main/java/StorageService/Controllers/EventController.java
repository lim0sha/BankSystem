package StorageService.Controllers;

import StorageService.DTO.AccountEventDTO;
import StorageService.DTO.UserEventDTO;
import StorageService.Models.AccountEvent;
import StorageService.Models.UserEvent;
import StorageService.Repositories.AccountEventRepository;
import StorageService.Repositories.UserEventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final UserEventRepository userEventRepository;
    private final AccountEventRepository accountEventRepository;

    @Operation(summary = "Получить все события пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "События получены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEventDTO.class)))
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserEventDTO>> getAllUserEvents() {
        List<UserEventDTO> result = userEventRepository.findAll()
                .stream()
                .map(UserEventDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получить все события аккаунтов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "События получены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountEventDTO.class)))
    })
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountEventDTO>> getAllAccountEvents() {
        List<AccountEventDTO> result = accountEventRepository.findAll()
                .stream()
                .map(AccountEventDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получить событие пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Событие найдено",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEventDTO.class))),
            @ApiResponse(responseCode = "404", description = "Событие не найдено")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserEventDTO> getUserEventById(@Parameter(description = "ID пользователя") @PathVariable Long id) {
        return userEventRepository.findById(id)
                .map(event -> ResponseEntity.ok(new UserEventDTO(event)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить событие аккаунта по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Событие найдено",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountEventDTO.class))),
            @ApiResponse(responseCode = "404", description = "Событие не найдено")
    })
    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountEventDTO> getAccountEventById(@Parameter(description = "ID аккаунта") @PathVariable Long id) {
        return accountEventRepository.findById(id)
                .map(event -> ResponseEntity.ok(new AccountEventDTO(event)))
                .orElse(ResponseEntity.notFound().build());
    }
}
