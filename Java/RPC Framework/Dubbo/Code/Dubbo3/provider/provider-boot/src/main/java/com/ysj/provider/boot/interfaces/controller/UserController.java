package com.ysj.provider.interfaces.controller;


import com.ysj.provider.application.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UserController
 * @Description 控制器
 * @Date 2025/10/24 16:47
 * @Created by YanShijie
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userApplicationService;

    @GetMapping("/user")
    public String getUser() {
        return userApplicationService.getUser();
    }

}
