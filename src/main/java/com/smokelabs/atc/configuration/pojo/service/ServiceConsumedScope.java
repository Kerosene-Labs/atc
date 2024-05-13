package com.smokelabs.atc.configuration.pojo.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceConsumedScope {
    private List<String> methods;
}
