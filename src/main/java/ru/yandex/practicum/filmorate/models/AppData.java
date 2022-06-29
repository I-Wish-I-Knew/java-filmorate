package ru.yandex.practicum.filmorate.models;

public abstract class AppData {
    protected int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
