package com.odc.Apiodkerp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/swagger")
public class SwaggerController {
    @RequestMapping(method = RequestMethod.GET)
    public String swagger() {
        return "redirect:/swagger-ui.html";
    }
}
