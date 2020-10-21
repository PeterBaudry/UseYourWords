package com.peton.useyourwords.models;

import com.peton.useyourwords.models.enums.FunnyTypes;

import javax.persistence.*;

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

    //</editor-fold>

}
