package com.upplication.thepunisher;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
class PunishmentController {


    @RequestMapping(value = "save-punishment", method = RequestMethod.POST,  headers = {"content-type=application/json"}, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Success savePunishment(@RequestBody PunishmentForm form) {
        return new Success(true, form.getTitle(), form.getDescription());
    }

    public static class Success{
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
    }

}
