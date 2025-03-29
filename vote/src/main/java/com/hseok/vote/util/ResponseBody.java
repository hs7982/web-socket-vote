package com.hseok.vote.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBody {
    private ResponseStatus status;
    private String message;
    private Object data;
}
