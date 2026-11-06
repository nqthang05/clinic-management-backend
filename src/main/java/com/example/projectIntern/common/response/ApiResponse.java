package com.example.projectIntern.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Những trường nào null thì không parse ra JSON
public class ApiResponse<T> {

    @Builder.Default
    private int code = 1000; // 1000 mặc định là thành công

    private String message;

    private T result; // Object dữ liệu trả về (User, Bệnh án, Đơn thuốc...)
}