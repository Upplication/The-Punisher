package com.upplication.thepunisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
class PunishmentController {

    @Autowired
    private PunishmentRepository punishmentRepository;


    @RequestMapping(value = "save-punishment",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Success savePunishment(@Valid @RequestBody Punishment punishment) {
        return new Success(true, punishment.getTitle(), punishment.getDescription());
    }


    public static class Success {

        private boolean success;
        private String title;
        private String description;

        public Success(boolean success, String title, String descrption){
            this.success = success;
            this.title = title;
            this.description = descrption;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }



    @RequestMapping(
            value = "/index",
            method = RequestMethod.GET
    )
    public String index() {
        return "punisher/index";
    }

}
