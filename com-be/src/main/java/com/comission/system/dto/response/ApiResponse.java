package com.comission.system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private Integer code = 200;

    @Builder.Default
    private String message = "Success";

    private T data;

    public static <T> ApiResponse<T> success(T data){
        return ApiResponse.<T>builder()
                .data(data)
                .build();
    }

    public static ApiResponse<?> error(int code, String message){
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
