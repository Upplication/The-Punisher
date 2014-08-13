package com.upplication.thepunisher;

import com.upplication.thepunisher.requests.PunishmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PunishmentController {

    @Autowired
    private PunishmentRepository punishmentRepository;

    @RequestMapping(
            value = "/punishment/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Punishment createPunishment(@Valid @RequestBody PunishmentRequest req) {
        return punishmentRepository.create(req.getTitle(), req.getDescription());
    }

}
