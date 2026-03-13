package com.retailplatform.dto;
import lombok.Data;
@Data
public class RetailerRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String shopName;
    private String shopAddress;
    private String shopCategory;
    private Double latitude;
    private Double longitude;
    private String gstin;
}
