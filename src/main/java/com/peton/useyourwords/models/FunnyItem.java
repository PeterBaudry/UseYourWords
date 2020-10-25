package com.peton.useyourwords.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.peton.useyourwords.models.enums.FunnyTypes;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "funny_items")
public class FunnyItem {

    //<editor-fold desc="Fields">

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private FunnyTypes type;

    @JsonBackReference
    @ManyToMany(mappedBy = "funnyItems")
    private List<Room> rooms;

    //</editor-fold>

    //<editor-fold desc="Accessors">

    public FunnyItem(FunnyTypes type, String content){
        this.content = content;
        this.type = type;
    }
    public FunnyItem(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FunnyTypes getType() {
        return type;
    }

    public void setType(FunnyTypes type) {
        this.type = type;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    //</editor-fold>

}
