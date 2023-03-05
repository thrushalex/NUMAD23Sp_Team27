package edu.northeastern.numad23sp_team27;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private ArrayList<Integer> countOfStickersSent;
    private ArrayList<Integer> historyOfStickersReceived;

    User(String username) {
        this.username = username;

        // count of stickers sent
        // adding 0 as temp fix, if array has no value it doesn't get added to DB
        this.countOfStickersSent = new ArrayList<>();
        this.countOfStickersSent.add(0);

        // history of stickers received
        this.historyOfStickersReceived = new ArrayList<>();
        this.historyOfStickersReceived.add(0);
    }

    User(String username, ArrayList<Integer> countOfStickersSent, ArrayList<Integer> historyOfStickersReceived) {
        this.username = username;

        // count of stickers sent
        this.countOfStickersSent = countOfStickersSent;

        // history of stickers received
        this.historyOfStickersReceived = historyOfStickersReceived;
    }

    User(User user) {
        this.username = user.getUsername();

        // count of stickers sent
        this.countOfStickersSent = user.getCountOfStickersSent();

        // history of stickers received
        this.historyOfStickersReceived = user.getHistoryOfStickersReceived();
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Integer> getCountOfStickersSent() {
        return countOfStickersSent;
    }

    public ArrayList<Integer> getHistoryOfStickersReceived() {
        return historyOfStickersReceived;
    }

    public void addStickerToArray(int sticker) {
        historyOfStickersReceived.add(sticker);
    }
}
