package io.kerosenelabs.atc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionErrorResponse {
    private String message;
    private List<String> stackTrace;
}
