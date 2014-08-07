package com.upplication.thepunisher;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
class PunishmentController {


    @RequestMapping(value = "save-punishment", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void accounts(@RequestBody PunishmentForm punish) {
    }
}
