package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class CustomerCreateRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Gender is required")
    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String alternatePhone;
    private String identityCardNumber;
    private String address;
    private String provinceCode;
    private String districtCode;
    private String communeCode;
    private String villageCode;
    private String occupation;
    private String employerName;
    private BigDecimal monthlyIncome;
    private String guarantorName;
    private String guarantorPhone;
}
