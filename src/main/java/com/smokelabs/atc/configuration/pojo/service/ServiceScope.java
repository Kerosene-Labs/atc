package com.smokelabs.atc.configuration.pojo.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceScope {
    private List<String> methods;
    private String description;
}
