package com.app.banking.docs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/docs")
    public String docs() {
        return "forward:/index.html";
    }
}