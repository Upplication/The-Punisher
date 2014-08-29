package com.upplication.thepunisher;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class PunishmentForm implements Serializable {

    @NotNull
    @NotEmpty
    @Size(max = 100)
    private String title;
    @NotNull
    @NotEmpty
    @Size(max = 100)
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
