package com.comission.system.controller;

import com.comission.system.dto.request.auth.LoginReqDTO;
import com.comission.system.dto.request.auth.RegisterReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.auth.AuthResDTO;
import com.comission.system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResDTO> login(@Valid @RequestBody LoginReqDTO reqDTO) {
        return ApiResponse.success(authService.login(reqDTO));
    }

    @PostMapping("/register")
    public ApiResponse<AuthResDTO> register(@Valid @RequestBody RegisterReqDTO reqDTO) {
        return ApiResponse.success(authService.register(reqDTO));
    }
}
