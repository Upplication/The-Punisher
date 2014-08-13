package com.upplication.thepunisher;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PunishmentForm implements Serializable {

    @NotNull
    private String title;
    @NotNull
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
