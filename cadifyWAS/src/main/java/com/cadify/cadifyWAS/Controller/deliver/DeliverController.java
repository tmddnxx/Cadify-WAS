package com.cadify.cadifyWAS.Controller.deliver;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/deliver")
public class DeliverController {

    @GetMapping("")
    public String main(){

        return "deliver/main";
    }
}
