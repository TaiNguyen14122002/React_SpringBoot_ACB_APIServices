package com.TaiNguyen.ACBBank.Controller;

import com.TaiNguyen.ACBBank.Config.JwtProvider;
import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Repository.UserStaffRepository;
import com.TaiNguyen.ACBBank.Request.ChangePasswordRequest;
import com.TaiNguyen.ACBBank.Request.ChangeRoleUserRequest;
import com.TaiNguyen.ACBBank.Request.Login_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Response.AuthResponse;
import com.TaiNguyen.ACBBank.Response.ErrorResponse;
import com.TaiNguyen.ACBBank.Service.UserService;
import com.TaiNguyen.ACBBank.Service.UserServiceImpl;
import com.TaiNguyen.ACBBank.Service.UserStaffDetailsImpl;
import com.TaiNguyen.ACBBank.utill.CorrectPassword;
import com.TaiNguyen.ACBBank.utill.EmailUtil;
import com.TaiNguyen.ACBBank.utill.PasswordUtil;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
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

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
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
    @Autowired
    private EmailUtil emailUtil;

    public boolean isValidEmailSyntax(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }


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

        ErrorResponse ress = new ErrorResponse();
        if(isUserExistingUser != null){

            ress.setError("User already exists");
            return new ResponseEntity<>(ress, HttpStatus.FORBIDDEN);
        }

        User_Staff_ACBBank isEmailExistingUser = userStaffRepository.findByEmail(user_Staff_ACBBank.getEmail());
        if(isEmailExistingUser != null){

            ress.setError("Email already exists");
            return new ResponseEntity<>(ress, HttpStatus.FORBIDDEN);
        }



        // Kiểm tra cú pháp email
        if (!isValidEmailSyntax(user_Staff_ACBBank.getEmail())) {
            ress.setError("Invalid email format: " + user_Staff_ACBBank.getEmail());
            return new ResponseEntity<>(ress, HttpStatus.FORBIDDEN);
        } else if (!isValidEmail(user_Staff_ACBBank.getEmail())) {
            ress.setError("Email does not exist: " + user_Staff_ACBBank.getEmail());
            return new ResponseEntity<>(ress, HttpStatus.FORBIDDEN);
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
    public ResponseEntity<?> signing(@RequestBody Login_Staff_ACBBank loginRequest){
        String Code = loginRequest.getEmployeeCode();
        String Password = loginRequest.getPassword();
        User_Staff_ACBBank user = userStaffRepository.findByemployeeCode(Code);

//        ErrorResponse errorResponse = new ErrorResponse();
        if(user == null){
            ErrorResponse errorResponse = new ErrorResponse("User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        if(!CorrectPassword.verifyPassword(Password, user.getPassword())){
            ErrorResponse errorResponse = new ErrorResponse("Password incorrect");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

        }

        Authentication authentication = authenticate(Code, Password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);



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

    @Operation(
            summary = "GET VIEW VIEWERS",
            description = "Retrieve a list of viewers"
    )
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

    @Operation(
            summary = "Update a user's role",
            description = "This endpoint allows users with the ADMIN role to update the role of another user. "
                    + "The current user must have the ADMIN role to perform this action. "
                    + "If the user does not have ADMIN privileges, a 403 FORBIDDEN error will be returned."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update-role/{userId}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @RequestBody ChangeRoleUserRequest changeRoleUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User_Staff_ACBBank currentUser = userStaffRepository.findByemployeeCode(authentication.getName());

        //Kiem tra xem nguoi dung hien tai co phai la ADMIN khong
        if(currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            ErrorResponse res = new ErrorResponse();
            res.setError("You are not authorized to perform this action");
            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
        }

        //Nguoi dung can thay doi quyen
        User_Staff_ACBBank userToUpdate = userStaffRepository.findById(userId).orElse(null);

        userToUpdate.setRole(changeRoleUserRequest.getNewRole());
//        userToUpdate.setRole("ADMIN");
        userStaffRepository.save(userToUpdate);

        AuthResponse res = new AuthResponse();
        res.setMessage("User role updated successfully");
        res.setEmployeeCode(userToUpdate.getEmployeeCode());
        res.setFullName(userToUpdate.getFullName());
        res.setEmail(userToUpdate.getEmail());
        res.setRole(userToUpdate.getRole());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(
            summary = "Create multiple users",
            description = "This endpoint allows users with the ADMIN role to create multiple user accounts at once. "
                    + "Each user will be assigned the VIEWER role by default. "
                    + "If a user with the same employee code already exists, the creation process will be halted, and an error will be returned. "
                    + "A welcome email with login details will be sent to each successfully created user."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-multiple-users")
    public ResponseEntity<?> createUserMore(@RequestBody List<User_Staff_ACBBank> users) throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User_Staff_ACBBank currentUser = userStaffRepository.findByemployeeCode(authentication.getName());

        ErrorResponse resres = new ErrorResponse();
        if(currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            ErrorResponse res = new ErrorResponse("You are not authorized for this function");
            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
        }

        List<ErrorResponse> errors = new ArrayList<>();
        List<User_Staff_ACBBank> createdUsers = new ArrayList<>();

        for(User_Staff_ACBBank user : users) {



            // Kiểm tra EmployeeCode có tồn tại hay không
            User_Staff_ACBBank existingUser = userStaffRepository.findByemployeeCode(user.getEmployeeCode());
            if(existingUser != null) {
                errors.add(new ErrorResponse("User with Employee Code " + user.getEmployeeCode() + " already exists"));
                continue;
            }

            // Kiểm tra Email có tồn tại hay không
            User_Staff_ACBBank existingUserByEmail = userStaffRepository.findByEmail(user.getEmail());
            if(existingUserByEmail != null) {
                errors.add(new ErrorResponse("Email " + user.getEmail() + " already exists"));
                continue;
            }

            // Kiểm tra cú pháp email
            if (!isValidEmailSyntax(user.getEmail())) {
                errors.add(new ErrorResponse("Invalid email format: " + user.getEmail()));
                continue;
            } else if (!isValidEmail(user.getEmail())) {
                errors.add(new ErrorResponse("Email does not exist: " + user.getEmail()));
                continue;
            }



            User_Staff_ACBBank addUser = new User_Staff_ACBBank();
            addUser.setEmployeeCode(user.getEmployeeCode());
            addUser.setFullName(user.getFullName());
            addUser.setEmail(user.getEmail());
            String randomPassword = PasswordUtil.generateRandomPassword();
            addUser.setPassword(passwordEncoder.encode(randomPassword));
            addUser.setRole("VIEWER");
//            List<User_Staff_ACBBank> viewers = userStaffRepository.findByRole("VIEWER");
            userStaffRepository.save(addUser);
            createdUsers.add(addUser);

            String subject = "Welcome to" + addUser.getFullName();
            String message = String.format(
                    "<html>" +
                            "<head>" +
                            "<style>" +
                            "    .header {" +
                            "        width: 100%%;" +
                            "        text-align: center;" +
                            "        margin-bottom: 20px;" +
                            "    }" +
                            "    .content {" +
                            "        text-align: center;" +
                            "        margin: 20px;" +
                            "    }" +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            "<div class='header'>" +
                            "    <img src='https://scontent.fsgn2-7.fna.fbcdn.net/v/t39.30808-6/415026555_1782510785526442_1580498241229081176_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=bdeb5f&_nc_ohc=G7AKWIG8_IkQ7kNvgHrYZs7&_nc_ht=scontent.fsgn2-7.fna&oh=00_AYD7KTieYeNJvs8KtElCdoxcU3PYuej7hnyrDICxC5djYA&oe=66DDFC08' alt='ACBBank Logo' style='width: 100%%; height: auto;'/>" +  // Hình ảnh chiếm toàn bộ phần header
                            "</div>" +
                            "<div class='content'>" +
                            "    <p>Dear %s,</p>" +
                            "    <p>Your account has been created successfully.</p>" +
                            "    <p>Employee Code: %s</p>" +
                            "    <p>Password: %s</p>" +
                            "    <p>We are delighted to have you with us and look forward to supporting your financial journey.</p>" +
                            "    <p>Best Regards,</p>" +
                            "    <p>Tai Nguyen</p>" +
                            "</div>" +
                            "</body>" +
                            "</html>",
                    addUser.getFullName(),
                    addUser.getEmployeeCode(),
                    randomPassword
            );


            emailUtil.sendEmail(addUser.getEmail(), subject, message);
        }
//        // Trả về phản hồi thành công
//        AuthResponse response = new AuthResponse();
//        response.setMessage("Users created successfully");
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
        // Tạo phản hồi chi tiết
        if (errors.isEmpty()) {
            AuthResponse response = new AuthResponse();
            response.setMessage("Users created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            // Phản hồi với thông tin lỗi
            ErrorResponse errorResponseList = new ErrorResponse();
            errorResponseList.setError(errors.toString());
            return new ResponseEntity<>(errorResponseList, HttpStatus.BAD_REQUEST);
        }
    }


//    @PreAuthorize("hasRole('MANAGER')")
//    @PutMapping("/delete-user/{userId}")
//    public ResponseEntity<?> softDeleteUser(@PathVariable Long userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User_Staff_ACBBank currentUser = userStaffRepository.findByemployeeCode(authentication.getName());
//
//        // Check if the current user has the MANAGER role
//        if (currentUser == null || !currentUser.getRole().equals("MANAGER")) {
//            ErrorResponse res = new ErrorResponse();
//            res.setError("You are not authorized to perform this action");
//            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
//        }
//
//        // Retrieve the user to delete
//        User_Staff_ACBBank userToDelete = userStaffRepository.findById(userId).orElse(null);
//
//        if (userToDelete == null || userToDelete.getAction() == -1) {
//            return new ResponseEntity<>("User not found or already deleted", HttpStatus.NOT_FOUND);
//        }
//
//        // Set the action field to -1 to mark the user as deleted
//        userToDelete.setAction(-1);
//        userStaffRepository.save(userToDelete);
//
//        AuthResponse res = new AuthResponse();
//        res.setMessage("User soft-deleted successfully");
//        res.setEmployeeCode(userToDelete.getEmployeeCode());
//        res.setFullName(userToDelete.getFullName());
//        res.setEmail(userToDelete.getEmail());
//        res.setRole(userToDelete.getRole());
//
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }

    @Operation(
            summary = "Soft delete a user",
            description = "This endpoint allows users with the ADMIN role to soft delete a user by updating the `action` field to -1."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/delete-user/{userId}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User_Staff_ACBBank currentUser = userStaffRepository.findByemployeeCode(authentication.getName());

        //Kiem tra xem nguoi dung hien tai co phai la ADMIN khong
        if(currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            ErrorResponse res = new ErrorResponse();
            res.setError("You are not authorized to perform this action");
            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
        }

        //Nguoi dung can thay doi quyen
        User_Staff_ACBBank userToUpdate = userStaffRepository.findById(userId).orElse(null);

        if (userToUpdate == null || userToUpdate.getAction() == -1) {
            return new ResponseEntity<>("User not found or already deleted", HttpStatus.NOT_FOUND);
        }
        userToUpdate.setAction(0);
//        userToUpdate.setRole("ADMIN");
        userStaffRepository.save(userToUpdate);

        AuthResponse res = new AuthResponse();
        res.setMessage("User soft-deleted successfully");
        res.setEmployeeCode(userToUpdate.getEmployeeCode());
        res.setFullName(userToUpdate.getFullName());
        res.setEmail(userToUpdate.getEmail());
        res.setRole(userToUpdate.getRole());


        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
