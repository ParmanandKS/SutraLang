package com.sutralang.ide.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "snippets")
public class Snippet {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String userId;
    private String title;
    private String code;
    private long timestamp;

    public Snippet(String userId, String title, String code, long timestamp) {
        this.userId = userId;
        this.title = title;
        this.code = code;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
