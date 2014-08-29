package com.upplication.thepunisher;

import com.upplication.thepunisher.requests.PunishmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Punishment createPunishment(@Valid @RequestBody PunishmentRequest req) {
        return punishmentRepository.create(req.getTitle(), req.getDescription());
    }

    @RequestMapping(
            value = "/punishment/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Punishment editPunishment(@Valid @RequestBody PunishmentRequest req, @PathVariable int id) {
        return punishmentRepository.edit(id, req.getTitle(), req.getDescription());
    }

    @RequestMapping(
            value = "/punishment/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity removePunishment(@PathVariable int id) {
        ResponseEntity response;

        if (punishmentRepository.remove(id)) {
            response = new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            response = new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return response;
    }

}
