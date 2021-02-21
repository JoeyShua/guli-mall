package com.jxs.product.exception;

import com.jxs.common.exception.BizCodeEnum;
import com.jxs.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.jxs.product.controller")
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidateException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item)->{
            errorMap.put(item.getField(),item.getDefaultMessage());
        });
        log.error("数据校验出现异常{}。异常类型：{}",e.getMessage(),e.getStackTrace());
        return R.error(BizCodeEnum.VALIDATE_EXCEPTION.getCode(),BizCodeEnum.VALIDATE_EXCEPTION.getMsg()).put("data",errorMap);
    }


    @ExceptionHandler(value = Throwable.class)
    public R handleValidateException(Throwable e){
        log.error("未知错误{},{}",e.getMessage(),e.getStackTrace());
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(),BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }

}
