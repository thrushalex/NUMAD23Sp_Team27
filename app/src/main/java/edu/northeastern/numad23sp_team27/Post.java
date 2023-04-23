package edu.northeastern.numad23sp_team27;

public class Post {
    String postAuthor;
    String postId;
    String postTitle;
    String postBody;
    String postDiagram;
    String postDatetime;
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getPostDiagram() {
        return postDiagram;
    }

    public void setPostDiagram(String postDiagram) {
        this.postDiagram = postDiagram;
    }

    public void setPostDatetime(String dt) { this.postDatetime = dt;}

    public String getPostDatetime() { return this.postDatetime;}

    public void setPostAuthor(String author) { this.postAuthor = author;}

    public String getPostAuthor() { return this.postAuthor;}

    public Post(String postTitle, String postAuthor, String postBody, String postDiagram, String postDatetime) {
        this.postId = null;
        this.postAuthor = postAuthor;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postDiagram = postDiagram;
        this.postDatetime = postDatetime;
    }

    public Post() {
        this.postAuthor = null;
        this.postId = null;
        this.postTitle = null;
        this.postBody = null;
        this.postDiagram = null;
        this.postDatetime = null;
    }
}
