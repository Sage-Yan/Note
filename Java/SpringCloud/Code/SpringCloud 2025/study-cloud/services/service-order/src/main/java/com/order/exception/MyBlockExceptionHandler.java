package com.order.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.common.AjaxResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;


@Component // 只需要将异常放到容器中就能生效
public class MyBlockExceptionHandler implements BlockExceptionHandler {
    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String s, BlockException e) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        // too many requests
        response.setStatus(429);
        AjaxResult<String> error = AjaxResult.error("系统繁忙，请稍后再试：" + e.getClass());
        writer.write(objectMapper.writeValueAsString(error));

        writer.flush();
        writer.close();
    }
}
