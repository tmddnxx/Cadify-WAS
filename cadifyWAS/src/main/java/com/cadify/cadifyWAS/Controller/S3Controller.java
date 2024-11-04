package com.cadify.cadifyWAS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/s3")
public class S3Controller {

    @GetMapping("")
    public String main(){
        return "/s3/main";
    }
}
