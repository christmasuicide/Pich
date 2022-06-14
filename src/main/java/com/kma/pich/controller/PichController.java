package com.kma.pich.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class PichController {

    @RequestMapping("/")
    public String index() {
        return "start_page";
    }

}
