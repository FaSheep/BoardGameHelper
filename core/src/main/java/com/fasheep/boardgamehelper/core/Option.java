package com.fasheep.boardgamehelper.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Option {
    private final String optionStr;
    private final Set<Integer> votes = new HashSet<>();

    protected Option(String optionStr) {
        this.optionStr = optionStr;
    }

    public String getOptionStr() {
        return optionStr;
    }

    protected Set<Integer> getVotes() {
        return Collections.unmodifiableSet(votes);
    }

    protected void voted(int who) {
        votes.add(who);
    }
}
