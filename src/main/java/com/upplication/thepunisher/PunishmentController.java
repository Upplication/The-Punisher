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
    public Success savePunishment(@Valid @RequestBody PunishmentForm form) {
        Punishment punishment = punishmentRepository.create(form.getTitle(), form.getDescription());
        return new Success(true, punishment.getId(), form.getTitle(), form.getDescription());
    }



    public static class Success {

        private boolean success;
        private int id;
        private String title;
        private String description;

        public Success(boolean success, int id, String title, String descrption){
            this.success = success;
            this.title = title;
            this.description = descrption;
            this.id = id;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
