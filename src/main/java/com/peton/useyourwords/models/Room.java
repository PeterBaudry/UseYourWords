package com.peton.useyourwords.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    //<editor-fold desc="Fields">

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "max_places")
    private int maxPlaces;

    @Column(name = "is_open")
    private boolean isOpen;

    @Column(name = "currentRound")
    private int currentRound;

    @Column(name = "currentState")
    private String currentState;

    @JsonIgnore
    @Column(name = "currentUserActionsCount")
    private int currentUserActionsCount;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<User> users;

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "room_items",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "funny_item_id")}
    )
    private List<FunnyItem> funnyItems;

    //</editor-fold>

    //<editor-fold desc="Methods">

    public boolean addUser(User u) {
        if (users.size() >= maxPlaces) {
            return false;
        }

        return users.add(u);
    }

    public boolean removeUser(User u) {
        return users.remove(u);
    }

    public void incrementCurrentUserActionsCount() {
        currentUserActionsCount++;
    }

    public void incrementCurrentRound() {
        currentRound++;
    }

    //</editor-fold>

    //<editor-fold desc="Accessors">

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public int getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(int maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getCurrentUserActionsCount() {
        return currentUserActionsCount;
    }

    public void setCurrentUserActionsCount(int currentUserActionsCount) {
        this.currentUserActionsCount = currentUserActionsCount;
    }

    public List<FunnyItem> getFunnyItems() {
        return funnyItems;
    }

    public void setFunnyItems(List<FunnyItem> funnyItems) {
        this.funnyItems = funnyItems;
    }

    //</editor-fold>
}
