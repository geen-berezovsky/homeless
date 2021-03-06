package ru.homeless.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by maxim on 28.06.14.
 */
@Entity
@Table(name = "Room")
public class Room implements Serializable {

    private Integer id;

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    private String roomnumber;
    private Integer roommaxlivers;
    private String roomnotes;

    public Integer getCurrentnumoflivers() {
        if (currentnumoflivers == null) currentnumoflivers = 0;
            return currentnumoflivers;
    }

    public void setCurrentnumoflivers(Integer currentnumoflivers) {
        this.currentnumoflivers = currentnumoflivers;
    }

    private Integer currentnumoflivers;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getRoommaxlivers() {
        return roommaxlivers;
    }

    public void setRoommaxlivers(Integer roommaxlivers) {
        this.roommaxlivers = roommaxlivers;
    }

    public String getRoomnotes() {
        return roomnotes;
    }

    public void setRoomnotes(String roomnotes) {
        this.roomnotes = roomnotes;
    }




    public Room() {

    }

}
