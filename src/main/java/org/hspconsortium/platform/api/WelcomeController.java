package org.hspconsortium.platform.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @RequestMapping("/")
    public String helloWorld() {
        return "Welcome to the HSPC Reference API Server!";
    }

}
