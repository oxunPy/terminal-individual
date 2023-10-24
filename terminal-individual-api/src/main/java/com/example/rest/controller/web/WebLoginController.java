package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebLoginController {

    @GetMapping( value = {ConstEndpoints.URL_AJAX_LOGIN_PAGE, ConstEndpoints.URL_AJAX_LOGIN})
    @Operation(hidden = true)
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "logout", required = false) String logout,
                        @RequestParam(name = "denied", required = false) String denied){
        return "user/login";
    }

    @PostMapping(ConstEndpoints.URL_AJAX_LOGIN_PAGE)
    @Operation(hidden = true)
    public String loginPost(Model model, @RequestParam(name = "error", required = false) String error,
                            @RequestParam(name = "logout", required = false) String logout,
                            @RequestParam(name = "denied", required = false) String denied){
        if(!StringUtils.isEmpty(error)){
            model.addAttribute("error", true);
        }
        if(!StringUtils.isEmpty(logout)){
            model.addAttribute("logout", true);
        }
        if(!StringUtils.isEmpty(denied)){
            model.addAttribute("denied", true);
        }
        return "user/login";
    }
}
