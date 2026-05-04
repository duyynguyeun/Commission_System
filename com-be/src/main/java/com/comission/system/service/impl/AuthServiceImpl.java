package com.comission.system.service.impl;

import com.comission.system.dto.request.auth.LoginReqDTO;
import com.comission.system.dto.request.auth.RegisterReqDTO;
import com.comission.system.dto.response.auth.AuthResDTO;
import com.comission.system.entity.Account;
import com.comission.system.entity.Customer;
import com.comission.system.entity.Employee;
import com.comission.system.enums.EmployeeEnum;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.repository.AccountRepository;
import com.comission.system.repository.CustomerRepository;
import com.comission.system.repository.EmployeeRepository;
import com.comission.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.ttl-seconds}")
    private long ttlSeconds;

    @Override
    public AuthResDTO login(LoginReqDTO reqDTO) {
        Account account = accountRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_001));

        boolean valid = passwordEncoder.matches(reqDTO.getPassword(), account.getPassword())
                || reqDTO.getPassword().equals(account.getPassword());
        if (!valid) {
            throw new BusinessException(ErrorCode.AUTH_001);
        }

        Long userId = resolveUserId(account.getId());
        return buildAuthResponse(account, userId);
    }

    @Override
    public AuthResDTO register(RegisterReqDTO reqDTO) {
        if (accountRepository.existsByUsername(reqDTO.getUsername())) {
            throw new BusinessException(ErrorCode.CUSTOMER_002);
        }

        Account account = Account.builder()
                .username(reqDTO.getUsername())
                .password(passwordEncoder.encode(reqDTO.getPassword()))
                .role(reqDTO.getRole())
                .createAt(Instant.now())
                .updateAt(Instant.now())
                .build();
        accountRepository.save(account);

        Long userId;
        if (reqDTO.getRole() == EmployeeEnum.CUSTOMER) {
            Customer customer = Customer.builder()
                    .fullName(reqDTO.getFullName())
                    .address("")
                    .account(account)
                    .createAt(Instant.now())
                    .updateAt(Instant.now())
                    .build();
            customerRepository.save(customer);
            userId = customer.getId();
        } else {
            Employee employee = Employee.builder()
                    .fullName(reqDTO.getFullName())
                    .parentId(reqDTO.getParentId())
                    .account(account)
                    .createAt(Instant.now())
                    .updateAt(Instant.now())
                    .build();
            employeeRepository.save(employee);
            userId = employee.getId();
        }

        return buildAuthResponse(account, userId);
    }

    private AuthResDTO buildAuthResponse(Account account, Long userId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("commission-system")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ttlSeconds))
                .subject(account.getUsername())
                .claim("roles", java.util.List.of(account.getRole().name()))
                .claim("accountId", account.getId())
                .claim("userId", userId)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        return AuthResDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .accountId(account.getId())
                .userId(userId)
                .username(account.getUsername())
                .role(account.getRole())
                .build();
    }

    private Long resolveUserId(Long accountId) {
        return employeeRepository.findByAccount_Id(accountId).map(Employee::getId)
                .orElseGet(() -> customerRepository.findByAccount_Id(accountId).map(Customer::getId).orElse(null));
    }
}
