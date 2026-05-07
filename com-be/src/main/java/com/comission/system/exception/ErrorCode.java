package com.comission.system.exception;

public enum ErrorCode {
    // PRODUCT
    PRODUCT_001("PRODUCT_001", "Không tìm thấy sản phẩm!"),
    PRODUCT_002("PRODUCT_002", "Tên sản phẩm đã tồn tại!"),
    PRODUCT_003("PRODUCT_003", "Sản phẩm đã hết hàng hoặc không đủ số lượng!"),
    PRODUCT_004("PRODUCT_004", "Không thể xóa sản phẩm vì đã có đơn hàng hoặc dữ liệu liên quan!"),


    // AFFILIATE_LINK
    LINK_001("LINK_001", "Tạo link affiliate không thành công!"),
    LINK_002("LINK_002", "Mã link affiliate đã tồn tại!"),
    LINK_003("LINK_003", "Không tìm thấy link affiliate này!"),

    // EMPLOYEE
    EMPLOYEE_001("EMPLOYEE_001", "Không tìm thấy nhân viên!"),
    EMPLOYEE_002("EMPLOYEE_002", "Nhân viên đã tồn tại!"),
    EMPLOYEE_003("EMPLOYEE_003", "Không thể xóa nhân viên vì đã có giao dịch hoặc dữ liệu liên quan!"),


    // CUSTOMER
    CUSTOMER_001("CUSTOMER_001", "Không tìm thấy khách hàng!"),
    CUSTOMER_002("CUSTOMER_002", "Khách hàng đã tồn tại!"),
    CUSTOMER_003("CUSTOMER_003", "Không thể xóa khách hàng vì đã có đơn hàng hoặc dữ liệu liên quan!"),


    // CUSTOMER_ORDER
    ORDER_001("ORDER_001", "Đơn hàng đã tồn tại!"),
    ORDER_002("ORDER_002", "Không tìm thấy đơn hàng!"),

    // COMMISSION_POLICY
    POLICY_001("POLICY_001", "Đã có cấu hình cho sản phẩm này!"),
    POLICY_002("POLICY_002", "Cấu hình lợi nhuận không tồn tại!"),
    POLICY_003("POLICY_003", "Không thể xóa cấu hình này vì đã có giao dịch phát sinh!"),


    // COMMISSION_TRANSACTION
    TRANSACTION_001("TRANSACTION_001", "Lợi nhuận thực tế của đơn hàng không tìm thấy!"),
    TRANSACTION_002("TRANSACTION_002", "Lợi nhuận thực tế của đơn hàng đã tồn tại!"),

    // LOGIN
    AUTH_001("AUTH_001", "Đăng nhập thất bại, kiểm tra lại tên đăng nhập và mật khẩu!");

    private final String errorCode;
    private final String errorMessage;

    ErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
