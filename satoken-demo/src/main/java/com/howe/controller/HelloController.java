package com.howe.controller;

import com.howe.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author howe
 */
@RequestMapping("/hello")
@RestController
public class HelloController {
    /**
     * 测试接口
     * @return hello
     */
    @Operation(summary = "测试接口" ,description = "发一个hello，会返回一个hello")
    @GetMapping()
    public Result hello() {
        return Result.success("hello");
    }

}