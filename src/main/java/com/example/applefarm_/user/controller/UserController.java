package com.example.applefarm_.user.controller;

import com.example.applefarm_.product.dto.ProductResponse;
import com.example.applefarm_.seller.dto.SellerProfileResponseDto;
import com.example.applefarm_.user.dto.UserOrderDto;
import com.example.applefarm_.user.dto.SellerRegistrationDto;
import com.example.applefarm_.security.user.UserDetailsImpl;
import com.example.applefarm_.user.dto.LoginRequestDto;
import com.example.applefarm_.user.dto.SignupRequestDto;
import com.example.applefarm_.user.dto.UserProfileRequestDto;
import com.example.applefarm_.user.dto.UserProfileResponseDto;
import com.example.applefarm_.user.service.UserService;
import com.example.applefarm_.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return "signup";
    }

    @PostMapping("/signin")
    public void signin(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response,HttpServletRequest request) {
        userService.signin(loginRequestDto, response,request);
    }

    @GetMapping("/customer/profile")
    public UserProfileResponseDto getCustomerProfile(@AuthenticationPrincipal UserDetailsImpl user) {
        return new UserProfileResponseDto(user.getUser());
    }

    @GetMapping("/customer/sellerprofile/{id}")
    public SellerProfileResponseDto getSellerProfile(@PathVariable Long id) {
        return userService.getSellerProfile(id);
    }
//
    @GetMapping("/productlist")
    public ResponseEntity getProductList(@RequestParam("page") int page, @RequestParam("size") int size) {
        return userService.getProductList(page, size);
    }

    @GetMapping("/sellerlist")
    public ResponseEntity getSellerList(@RequestParam("page") int page, @RequestParam("size") int size) {
        return userService.getSellerList(page, size);
    }


    @PutMapping("/profile")
    public UserProfileResponseDto editUserProfile (@RequestBody UserProfileRequestDto
    userProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.editUserProfile(userProfileRequestDto, userDetails.getUser().getId());
    }

    @PostMapping("/signout")
    public String signout(HttpServletRequest request) {
        userService.signout(request);
        return "signout";
    }


    @GetMapping("/keyword")
    public List<ProductResponse> getProductByKeyword(@RequestParam String keyword, @RequestParam int page) {
        return userService.getProductsByKeyword(keyword, page);
    }

    @GetMapping("/nickname")
    public List<ProductResponse> getProductByNickname(@RequestParam String nickname, @RequestParam int page) {
        return userService.getProductsByNickname(nickname, page);
    }
}