package com.apostoli.UnluckyApp.model.dto;


import com.apostoli.UnluckyApp.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDTO {

    private Long id;
    private String username;
    private String email;
    private RoleType role;
    private String organisationName;
    private String orgRank;

}
