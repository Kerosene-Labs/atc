package io.kerosenelabs.atc.configuration.pojo;

import java.util.List;

import io.kerosenelabs.atc.configuration.pojo.service.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Configuration {
    private List<Service> services;
}