package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AdminDashboardController {

    @GetMapping(ConstEndpoints.URL_MAIN_PAGE)
    @Operation(hidden = true)
    public String main(){
        return "dashboard/main";
    }
}
