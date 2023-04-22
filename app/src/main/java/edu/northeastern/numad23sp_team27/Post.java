package edu.northeastern.numad23sp_team27;

public class Post {
    String postId;
    String postTitle;
    String postBody;
    String postDiagram;

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

    public Post(String postTitle, String postBody, String postDiagram) {
        this.postId = null;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postDiagram = postDiagram;
    }

    public Post() {
        this.postId = null;
        this.postTitle = null;
        this.postBody = null;
        this.postDiagram = null;
    }
}
