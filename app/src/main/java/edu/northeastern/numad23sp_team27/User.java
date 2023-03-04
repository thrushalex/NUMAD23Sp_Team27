package edu.northeastern.numad23sp_team27;

/**
 * Created by aniru on 2/18/2017.
 */

public class User {

    public String username;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
    }

}
