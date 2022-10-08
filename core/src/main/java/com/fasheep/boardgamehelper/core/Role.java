package com.fasheep.boardgamehelper.core;

public class Role {
    private final String name;
    private final String imagePath;
    private final int defNumber;
    private int number;

    public Role(String name, int def_number, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
        this.defNumber = def_number;
        number = def_number;
    }

    public String getName() {
        return name;
    }

    public int getDefNumber() {
        return defNumber;
    }

    public int getNumber() {
        return number;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void addNumber() {
        number++;
    }

    public void subNumber() {
        number--;
    }

    @Override
    public Role clone() throws CloneNotSupportedException {
        return (Role) super.clone();
    }
}
