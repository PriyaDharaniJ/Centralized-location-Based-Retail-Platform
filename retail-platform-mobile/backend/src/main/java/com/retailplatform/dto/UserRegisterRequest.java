package com.retailplatform.dto;
import lombok.Data;
@Data
public class UserRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Double latitude;
    private Double longitude;
}
