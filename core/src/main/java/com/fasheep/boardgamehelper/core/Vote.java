package com.fasheep.boardgamehelper.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Vote {
    private final int num;
    private List<Option> optionList = new ArrayList<>();
    private boolean shuffle = false;
    // private List resultList = new ArrayList<>();

    public Vote(int num) {
        this.num = num;
    }

    public Vote(int num, boolean shuffle) {
        this.num = num;
        this.shuffle = shuffle;
    }

    private Vote addOption(String optionStr) {
        optionList.add(new Option(optionStr));
        return this;
    }

    // public Vote addOption(Option option) {
    // optionList.add(option);
    // return this;
    // }

    public int getNum() {
        return num;
    }

    public static Vote getInstanceFromJson(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, Vote.class);
    }

    public synchronized void voted(Option option, int who) {
        if (who >= 0 && who < num) {
            option.voted(who);
        }
    }

    public synchronized Option attribute() {
        Option max;
        max = optionList.get(0);
        for (int i = 1; i < optionList.size(); i++) {
            if (max.getVotes().size() < optionList.get(i).getVotes().size()) {
                max = optionList.get(i);
            }
        }
        return max;
    }

    public synchronized List<Option> getOptions() {
        if (shuffle) {
            List<Option> temp = List.copyOf(optionList);
            Collections.shuffle(temp, new Random());
            return Collections.unmodifiableList(temp);
        } else {
            return Collections.unmodifiableList(optionList);
        }
    }
}
