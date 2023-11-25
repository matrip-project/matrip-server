package com.v1.matripserver.util.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.v1.matripserver.util.exceptions.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;

    // 요청에 성공한 경우
    public BaseResponse(Object result) {
        this(SUCCESS.isSuccess(), SUCCESS.getMessage(), SUCCESS.getCode(), result);
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this(status.isSuccess(), status.getMessage(), status.getCode(), null);
    }
}
