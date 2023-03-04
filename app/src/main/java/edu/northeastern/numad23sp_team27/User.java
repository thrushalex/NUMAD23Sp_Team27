package edu.northeastern.numad23sp_team27;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private Map<String, Integer> countOfStickersSent;
    private ArrayList<Integer> historyOfStickersReceived;

    User(String username) {
        this.username = username;

        // count of stickers sent
        this.countOfStickersSent = new HashMap<>();

        // history of stickers received
        this.historyOfStickersReceived = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getCountOfStickersSent() {
        return countOfStickersSent;
    }

    public ArrayList<Integer> getHistoryOfStickersReceived() {
        return historyOfStickersReceived;
    }
}
