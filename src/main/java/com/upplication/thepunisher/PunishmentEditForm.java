package com.upplication.thepunisher;

import javax.validation.constraints.NotNull;

public class PunishmentEditForm {
    @NotNull
    private int id;
    @NotNull
    private String description;
    @NotNull
    private String title;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
