package com.TaiNguyen.ACBBank.Controller;

import com.TaiNguyen.ACBBank.Config.JwtProvider;
import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Repository.UserStaffRepository;
import com.TaiNguyen.ACBBank.Request.Login_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Response.AuthResponse;
import com.TaiNguyen.ACBBank.Service.UserStaffDetailsImpl;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserStaffRepository userStaffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserStaffDetailsImpl userStaffDetails;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User_Staff_ACBBank user_Staff_ACBBank) throws Exception {
        User_Staff_ACBBank isUserExistingUser = userStaffRepository.findByemployeeCode(user_Staff_ACBBank.getEmployeeCode());

        if(isUserExistingUser != null){
            throw new Exception("Mã nhân viên trên đã tồn tại");
        }

        User_Staff_ACBBank CreatedUser = new User_Staff_ACBBank();
        CreatedUser.setEmployeeCode(user_Staff_ACBBank.getEmployeeCode());
        CreatedUser.setFullName(user_Staff_ACBBank.getFullName());
        CreatedUser.setEmail(user_Staff_ACBBank.getEmail());
        CreatedUser.setPassword(passwordEncoder.encode(user_Staff_ACBBank.getPassword()));

        User_Staff_ACBBank savedUser = userStaffRepository.save(CreatedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user_Staff_ACBBank.getEmployeeCode(), user_Staff_ACBBank.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setEmployeeCode(user_Staff_ACBBank.getEmployeeCode());
        res.setFullName(user_Staff_ACBBank.getFullName());
        res.setEmail(user_Staff_ACBBank.getEmail());
        res.setMessage("Đăng ký tài khoản thành công");
        res.setToken(jwt);

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
        res.setToken(jwt);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
