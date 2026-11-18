package org.example.gotogether.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponseDto {
    private LocalDateTime timeStamp ;
    private int codeStatus;
    private String error ;
    private String message;
    private String path ;
}
