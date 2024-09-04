package com.TaiNguyen.ACBBank.Controller;

import com.TaiNguyen.ACBBank.Config.JwtProvider;
import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Repository.UserStaffRepository;
import com.TaiNguyen.ACBBank.Request.ChangePasswordRequest;
import com.TaiNguyen.ACBBank.Request.Login_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Response.AuthResponse;
import com.TaiNguyen.ACBBank.Response.ErrorResponse;
import com.TaiNguyen.ACBBank.Service.UserService;
import com.TaiNguyen.ACBBank.Service.UserServiceImpl;
import com.TaiNguyen.ACBBank.Service.UserStaffDetailsImpl;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag( name = "AuthController", description = "To perform operations on students")
public class AuthController {

    @Autowired
    private UserStaffRepository userStaffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserStaffDetailsImpl userStaffDetails;

    @Autowired
    private UserService userService;



    @Operation(
            summary = "POST Signup Operation for User Registration",
            description = "This endpoint allows ADMIN users to register new staff members by providing their employee code, full name, email, password, and role. The role determines the level of access the user will have in the system. If any required field is missing or invalid, an appropriate error message will be returned."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<?> createUserHandler(@RequestBody User_Staff_ACBBank user_Staff_ACBBank) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User_Staff_ACBBank currentUser = userStaffRepository.findByemployeeCode(authentication.getName());



        //chi cho phep ADMIN tao nguoi dung moi
        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            ErrorResponse errorResponse = new ErrorResponse("You are not authorized for this function");
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
        User_Staff_ACBBank isUserExistingUser = userStaffRepository.findByemployeeCode(user_Staff_ACBBank.getEmployeeCode());

        if(isUserExistingUser != null){
            throw new Exception("User already exists");
        }
        User_Staff_ACBBank CreatedUser = new User_Staff_ACBBank();
        CreatedUser.setEmployeeCode(user_Staff_ACBBank.getEmployeeCode());
        CreatedUser.setFullName(user_Staff_ACBBank.getFullName());
        CreatedUser.setEmail(user_Staff_ACBBank.getEmail());
        CreatedUser.setPassword(passwordEncoder.encode(user_Staff_ACBBank.getPassword()));
        CreatedUser.setRole(user_Staff_ACBBank.getRole());

        User_Staff_ACBBank savedUser = userStaffRepository.save(CreatedUser);

//        Authentication authentication = new UsernamePasswordAuthenticationToken(user_Staff_ACBBank.getEmployeeCode(), user_Staff_ACBBank.getPassword());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setEmployeeCode(savedUser.getEmployeeCode());
        res.setFullName(savedUser.getFullName());
        res.setEmail(savedUser.getEmail());
        res.setRole(savedUser.getRole());
        res.setMessage("Đăng ký tài khoản thành công");
//        res.setToken(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    private Authentication authenticate(String Code, String Password){
        UserDetails userDetails = userStaffDetails.loadUserByUsername(Code);
        if(userDetails == null){
            throw new UsernameNotFoundException("Không tìm thấy nhân viên có mã nhân viên: " + Code);
        }
        if(!passwordEncoder.matches(Password, userDetails.getPassword())){
            throw new BadCredentialsException("Mật khẩu bị sai");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Operation(
            summary = "POST Login Operation for Staff Authentication",
            description = "This endpoint allows staff members to log in to the system. Users must provide their email and password. Upon successful authentication, a JWT token will be returned, which can be used for subsequent authenticated requests. If the credentials are invalid, an appropriate error message will be returned."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signing(@RequestBody Login_Staff_ACBBank loginRequest){
        String Code = loginRequest.getEmployeeCode();
        String Password = loginRequest.getPassword();

        Authentication authentication = authenticate(Code, Password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        User_Staff_ACBBank user = userStaffRepository.findByemployeeCode(Code);

        AuthResponse res = new AuthResponse();
        res.setMessage("Đăng nhập thành công");
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setEmployeeCode(user.getEmployeeCode());
        res.setRole(user.getRole());
        res.setToken(jwt);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(
            summary = "POST Forgot Password Operation",
            description = "This endpoint allows users to initiate the password reset process by providing their email address. A password reset token will be sent to the specified email address if it is associated with a registered user. Users can then use this token to reset their password. If the email address is not found or there is any issue with sending the reset email, an appropriate error message will be returned."
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) throws Exception {
        try{
            String response = userService.forgotPassword(email);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
//        return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
    }

    @Operation(
            summary = "POST Reset Password Operation",
            description = "This endpoint allows users to reset their password using a password reset token that was previously sent to their email address. Users must provide the reset token, new password, and confirm the new password. The reset token must be valid and match the one sent to the user's email. If the token is invalid or expired, or if the passwords do not match, an appropriate error message will be returned."
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String otp) throws Exception {
        if(userService.resetPassword(email, newPassword, otp)){
            return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Password to reset failed", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "POST Change Password Operation",
            description = "This endpoint allows authenticated users to change their current password. Users must provide their current password, new password, and confirm the new password. The current password must be correct, and the new passwords must match. If the current password is incorrect or the new passwords do not match, an appropriate error message will be returned."
    )
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = (String) authentication.getPrincipal(); // Email được lưu trong Principal

            User_Staff_ACBBank user = userStaffRepository.findByemployeeCode(email);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // Kiểm tra mật khẩu cũ
            if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
            }

            // Cập nhật mật khẩu mới
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userStaffRepository.save(user);

            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Hoặc logger.error("Error changing password", e);
            return new ResponseEntity<>("Password change failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view-viewers")
    public ResponseEntity<Object> viewUserWithRoleViewer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User_Staff_ACBBank currentUser = userStaffRepository.findByemployeeCode(authentication.getName());

        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            ErrorResponse res = new ErrorResponse();
            res.setError("You are not authorized to perform this action");
            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
        }

        List<User_Staff_ACBBank> viewers = userStaffRepository.findByRole("VIEWER");
        return new ResponseEntity<>(viewers, HttpStatus.OK);

    }

    @Operation(
            summary = "GET View Viewers Operation",
            description = "This endpoint allows authenticated users to retrieve a list of all users with the VIEWER role. Only users with ADMIN role can access this endpoint. The response will include details of users who have view-only permissions. If the user does not have sufficient permissions, an appropriate error message will be returned."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view-admin")
    public ResponseEntity<Object> viewUserWithRoleAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User_Staff_ACBBank currentUser = userStaffRepository.findByemployeeCode(authentication.getName());

        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            ErrorResponse res = new ErrorResponse();
            res.setError("You are not authorized to perform this action");
            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
        }

        List<User_Staff_ACBBank> viewers = userStaffRepository.findByRole("ADMIN");
        return new ResponseEntity<>(viewers, HttpStatus.OK);

    }
}
