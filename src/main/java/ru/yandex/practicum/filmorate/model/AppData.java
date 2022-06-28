package ru.yandex.practicum.filmorate.model;

public abstract class AppData {
    protected int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
