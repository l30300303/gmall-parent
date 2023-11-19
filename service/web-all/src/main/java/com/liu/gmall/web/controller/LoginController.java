package com.liu.gmall.web.controller;
/*
 *@title LoginController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 8:37
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/login.html")
    public String login(@RequestParam("originUrl") String originUrl, Model model) {
        model.addAttribute("originUrl",originUrl);
        return "login";
    }
}
