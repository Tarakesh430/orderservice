package com.order.response;

public class ApiResponse<T> {
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";

    private String status;
    private String message;
    private T data;

    // Constructors
    public ApiResponse() {
    }

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // Convenience methods for creating success and error responses

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(STATUS_SUCCESS, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(STATUS_ERROR, message, null);
    }
}
