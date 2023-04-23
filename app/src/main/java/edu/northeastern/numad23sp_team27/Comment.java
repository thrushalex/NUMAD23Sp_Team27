package edu.northeastern.numad23sp_team27;

public class Comment {
    String author;
    String comment_body;
    String commentId;
    String dateTime;

    public Comment() {
        this.author = null;
        this.comment_body = null;
        this.commentId = null;
        this.dateTime = null;
    }

    public Comment(String author, String body, String commentId, String datetime) {
        this.author = author;
        this.comment_body = body;
        this.commentId = commentId;
        this.dateTime = datetime;
    }


}
