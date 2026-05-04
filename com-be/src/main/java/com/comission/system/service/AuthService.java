package com.comission.system.service;

import com.comission.system.dto.request.auth.LoginReqDTO;
import com.comission.system.dto.request.auth.RegisterReqDTO;
import com.comission.system.dto.response.auth.AuthResDTO;

public interface AuthService {
    AuthResDTO login(LoginReqDTO reqDTO);
    AuthResDTO register(RegisterReqDTO reqDTO);
}
