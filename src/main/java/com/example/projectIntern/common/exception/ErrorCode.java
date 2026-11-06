package com.example.projectIntern.common.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Lỗi cấu hình validation", HttpStatus.BAD_REQUEST),

    // User & Role
    USER_EXISTED(1002, "Tên đăng nhập đã tồn tại", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1003, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1005, "Không tìm thấy phân quyền", HttpStatus.NOT_FOUND),

    // Doctor
    DOCTOR_NOT_FOUND(1006, "Không tìm thấy hồ sơ bác sĩ", HttpStatus.NOT_FOUND),
    USER_ALREADY_HAS_DOCTOR_PROFILE(1007, "Tài khoản này đã có hồ sơ Bác sĩ", HttpStatus.BAD_REQUEST),
    USER_NOT_DOCTOR_ROLE(1008, "Tài khoản này không có quyền Bác sĩ", HttpStatus.BAD_REQUEST),

    // Patient
    PATIENT_NOT_FOUND(1009, "Không tìm thấy hồ sơ bệnh nhân", HttpStatus.NOT_FOUND),
    USER_ALREADY_HAS_PATIENT_PROFILE(1010, "Tài khoản này đã có hồ sơ Bệnh nhân", HttpStatus.BAD_REQUEST),

    // Appointment
    APPOINTMENT_NOT_FOUND(1011, "Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND),

    // Medical Record
    MEDICAL_RECORD_EXISTED(1012, "Lịch hẹn này đã có bệnh án", HttpStatus.BAD_REQUEST),
    MEDICAL_RECORD_NOT_FOUND(1013, "Không tìm thấy bệnh án", HttpStatus.NOT_FOUND),

    // Medicine & Prescription
    MEDICINE_NOT_FOUND(1014, "Không tìm thấy thuốc", HttpStatus.NOT_FOUND),
    OUT_OF_STOCK(1015, "Thuốc trong kho không đủ số lượng", HttpStatus.BAD_REQUEST),
    PRESCRIPTION_EXISTED(1016, "Bệnh án này đã được kê đơn thuốc", HttpStatus.BAD_REQUEST),
    PRESCRIPTION_NOT_FOUND(1017, "Không tìm thấy đơn thuốc", HttpStatus.NOT_FOUND),

    // Invoice
    INVOICE_EXISTED(1018, "Hóa đơn cho lịch hẹn này đã tồn tại", HttpStatus.BAD_REQUEST),
    INVOICE_NOT_FOUND(1019, "Không tìm thấy hóa đơn", HttpStatus.NOT_FOUND),
    INVOICE_ALREADY_PAID(1020, "Hóa đơn này đã được thanh toán", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED(1021, "Chưa đăng nhập hoặc Token không hợp lệ/hết hạn", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1022, "Bạn không có quyền truy cập chức năng này", HttpStatus.FORBIDDEN),

    APPOINTMENT_NOT_CONFIRMED(1023, "Lịch hẹn phải được Xác Nhận (CONFIRMED) mới có thể lập bệnh án", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_DEFAULT_ADMIN(1024, "Không thể khóa/xóa tài khoản Quản trị viên mặc định của hệ thống", HttpStatus.FORBIDDEN);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}