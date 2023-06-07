package com.photoshop.photoshopgui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PhotoshopGuiController {
    
    @GetMapping("/")
    public String getForm(Model model) {
        return "index";
    }
}
