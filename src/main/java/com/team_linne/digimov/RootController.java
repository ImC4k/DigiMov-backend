package com.team_linne.digimov;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marco")
public class RootController {
    @GetMapping
    public String polo() {
        return "polo";
    }
}
