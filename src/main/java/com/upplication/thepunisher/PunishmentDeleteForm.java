package com.upplication.thepunisher;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PunishmentDeleteForm implements Serializable {
    @NotNull
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
