package edu.lehigh.cse216.slj222.backend;

public class Preference {
    boolean followsMe;
    boolean commentsOnPost;
    boolean followingPost;

    public Preference(boolean followsMe, boolean commentsOnPost, boolean followingPost) {
        this.followsMe = followsMe;
        this.commentsOnPost = commentsOnPost;
        this.followingPost = followingPost;
    }
}