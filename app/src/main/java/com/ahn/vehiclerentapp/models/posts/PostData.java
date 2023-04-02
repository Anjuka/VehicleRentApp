package com.ahn.vehiclerentapp.models.posts;

import java.util.ArrayList;

public class PostData {

    private ArrayList<PostsDataList> postsDataLists;

    public PostData(ArrayList<PostsDataList> postsDataLists) {
        this.postsDataLists = postsDataLists;
    }

    public PostData() {
    }

    public ArrayList<PostsDataList> getPostsDataLists() {
        return postsDataLists;
    }

    public void setPostsDataLists(ArrayList<PostsDataList> postsDataLists) {
        this.postsDataLists = postsDataLists;
    }
}
