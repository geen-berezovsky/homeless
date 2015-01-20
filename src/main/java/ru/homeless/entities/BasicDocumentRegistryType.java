package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by maxim on 28.06.14.
 */
@Entity
@Table(name = "BasicDocumentRegistryType")
public class BasicDocumentRegistryType implements Serializable {

    private Integer id;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    private String caption;

    public BasicDocumentRegistryType(String caption) {
        this.caption = caption;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public BasicDocumentRegistryType() {

    }

}
