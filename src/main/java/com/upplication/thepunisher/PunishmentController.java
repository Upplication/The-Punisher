package com.upplication.thepunisher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PunishmentController {

    @RequestMapping(value = "punish/create")
    public String savePunishment() {
        return "thepunisher/create";
    }
}
