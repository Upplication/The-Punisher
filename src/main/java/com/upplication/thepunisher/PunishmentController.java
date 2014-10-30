package com.upplication.thepunisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        if (punishmentRepository.getByTitle(form.getTitle()) != null){
            throw new IllegalArgumentException("title already exists: " + form.getTitle());
        }
        Punishment punishment = punishmentRepository.create(form.getTitle(), form.getDescription());
        return new Success(true, punishment.getId(), form.getTitle(), form.getDescription());
    }


    @RequestMapping(value = "create-punishment")
    public String savePunishment() {
        return "thepunisher/create";
    }

    @RequestMapping(value = "delete-punishment",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean deletePunishment(@Valid @RequestBody PunishmentDeleteForm form) {
        punishmentRepository.delete(form.getId());
        return true;
    }

    @RequestMapping(value = "edit-punishment",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean editPunishment(@Valid @RequestBody PunishmentEditForm form) {
        if (punishmentRepository.getByTitle(form.getTitle()) != null){
            throw new IllegalArgumentException("title already exists: " + form.getTitle());
        }
        Punishment punishment = new Punishment();
        punishment.setId(form.getId());
        punishment.setTitle(form.getTitle());
        punishment.setDescription(form.getDescription());
        punishmentRepository.edit(punishment);
        return true;
    }


    @RequestMapping(value = "list-punishment")
    @ResponseBody
    public List<Punishment> listPunishment() {
        return punishmentRepository.list();
    }

    @RequestMapping(value = "roulette-punishments")
    public String roulettePunishment(Map<String, Object> model) {

        model.put("punishments", punishmentRepository.list());

        return "thepunisher/roulette";
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="data are not valid")
    @ExceptionHandler(IllegalArgumentException.class)
    public void dataNotValid() {
        // Nothing to do
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="data are not valid")
    @ExceptionHandler(PersistenceException.class)
    public void bdNotValid() {
        // Nothing to do
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

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getId() {
            return id;
        }
    }

}
