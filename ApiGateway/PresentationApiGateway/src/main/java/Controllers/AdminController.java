package Controllers;

import DTO.BankAccountDTO;
import DTO.OperationDTO;
import DTO.UserDTO;
import Requests.UserFilterRequest;
import Requests.UserRequest;
import Services.AdminService;
import Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/create")
    public ResponseEntity<String> CreateUser(@RequestBody UserRequest userRequest) {
        userService.CreateUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRole());
        return ResponseEntity.ok("User created successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersFiltered(
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String hairColor
    ) {
        UserFilterRequest request = new UserFilterRequest();
        request.setSex(sex);
        request.setHairColor(hairColor);
        return ResponseEntity.ok(adminService.getFilteredUsers(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts")
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts() {
        return ResponseEntity.ok(adminService.getAllBankAccounts());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts/user/{userId}")
    public ResponseEntity<List<BankAccountDTO>> getAccountsByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(adminService.getAccountsByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts/{id}/operations")
    public ResponseEntity<List<OperationDTO>> getOperationsByAccountId(
            @PathVariable int id,
            @RequestParam(required = false) String type
    ) {
        return ResponseEntity.ok(adminService.getOperationsByAccountId(id, type));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> Logout() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.ok("Admin " + authentication.getName() + " logged out.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during logout: " + e.getMessage());
        }
    }
}
