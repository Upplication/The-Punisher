package com.upplication.thepunisher.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class PunishmentRequest implements Serializable {

    @NotNull
    @Size(max=100, min=1)
    private String title;

    @NotNull
    @Size(max=100, min=1)
    private String description;

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
