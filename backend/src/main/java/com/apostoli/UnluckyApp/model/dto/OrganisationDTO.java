package com.apostoli.UnluckyApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> memberNames;
}
