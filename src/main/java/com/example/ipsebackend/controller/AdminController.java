package com.example.ipsebackend.controller;

import com.example.ipsebackend.dto.SignupRequest;
import com.example.ipsebackend.dto.UserDto;
import com.example.ipsebackend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor

@RequestMapping("/api/admin/users")
@CrossOrigin("*")
@SecurityRequirement(name = "bearer-key")

public class AdminController {
  private  AdminService adminService;

  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }
    @PostMapping("/create-user")
    @Operation(summary = "Create a new user", security = @SecurityRequirement(name = "bearer-key"))

  public ResponseEntity<?> createUser(@RequestBody SignupRequest signupRequest) {
    UserDto createdUserDto = adminService.createUser(signupRequest);
    if (createdUserDto == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account not created");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
  }

  @GetMapping("/users")
  @Operation(summary = "Get users", security = @SecurityRequirement(name = "bearer-key"))

  public ResponseEntity<?> getUser(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "1") int size,
          @RequestParam(defaultValue = "") String searchTerm) {
    Pageable pageable = PageRequest.of(page, size);
    Page<UserDto> vendorPage = adminService.getUsers(pageable, searchTerm);
    return ResponseEntity.ok(vendorPage);
  }

  @PatchMapping("/toggle-user/{userId}")
  @Operation(summary = "Toggle user activation status", security = @SecurityRequirement(name = "bearer-key"))
  public ResponseEntity<?> toggleUserEnabled(@PathVariable Long userId) {
    boolean toggled = adminService.toggleUserEnabled(userId);
    if (!toggled) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or cannot be modified");
    }
    return ResponseEntity.ok().body("{\"message\": \"User activation status toggled successfully\"}");
  }


  @PutMapping("/update-user/{userId}")

  public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDto updateRequest) {
    UserDto updatedUser = adminService.updateUser(userId, updateRequest);
    if (updatedUser == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/delete-user/{userId}")

  public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
    boolean deleted = adminService.deleteUser(userId);
    if (!deleted) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Vendor not found or not a vendor account\"}");
    }
    return ResponseEntity.ok().body("{\"message\": \"User account deleted successfully\"}");
  }

  }