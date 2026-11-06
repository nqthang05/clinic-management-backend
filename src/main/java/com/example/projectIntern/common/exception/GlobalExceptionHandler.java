package com.example.projectIntern.common.exception;

import com.example.projectIntern.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Bắt các lỗi RuntimeException chưa được định nghĩa (VD: NullPointer, Database lỗi...)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handlingRuntimeException(Exception exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage() + ": " + exception.getMessage());
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    // 2. Bắt các lỗi nghiệp vụ chủ động ném ra (AppException)
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<String>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // 3. Bắt lỗi từ @Valid (Validation DTO)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handlingValidation(MethodArgumentNotValidException exception) {
        String validationMessage = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();

        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(validationMessage); // Trả về câu thông báo lỗi bạn định nghĩa trong DTO (VD: "Username không được để trống")

        return ResponseEntity.badRequest().body(apiResponse);
    }
}