package com.pingan.life.micromall.product.advice;

import com.pingan.life.common.enums.ExceptionCodeEnum;
import com.pingan.life.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局集中处理异常
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.pingan.life.micromall.product.controller")
public class GlobalExceptionHandler {

    /**
     * 处理参数检查异常
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public R handleParamCheckException(WebExchangeBindException e) {
        log.error("参数检查发生异常：[{}], 异常类型：[{}]", e.getMessage(), e.getClass());
        Map<String, String> errorData = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(error -> errorData.put(error.getField(), error.getDefaultMessage()));
        return R.error(ExceptionCodeEnum.PARAMETER_EXCEPTION.getCode(), ExceptionCodeEnum.PARAMETER_EXCEPTION.getMsg())
                .put("data", errorData);
    }

    /**
     * 处理系统未知异常
     */
    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("系统发生异常：[{}]", throwable);
        return R.error(ExceptionCodeEnum.UNKNOWN_EXCEPTION.getCode(), ExceptionCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }

}
