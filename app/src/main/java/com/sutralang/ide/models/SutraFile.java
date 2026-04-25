package com.sutralang.ide.models;

import java.io.Serializable;

/**
 * SutraFile - Model class representing a .sutra file
 */
public class SutraFile implements Serializable {
    private String fileName;
    private String content;
    private long createdAt;
    private long modifiedAt;
    
    public SutraFile(String fileName) {
        this.fileName = fileName;
        this.content = "";
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }
    
    public SutraFile(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
        this.modifiedAt = System.currentTimeMillis();
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public long getModifiedAt() {
        return modifiedAt;
    }
    
    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    
    @Override
    public String toString() {
        return "SutraFile{" +
                "fileName='" + fileName + '\'' +
                ", content length=" + (content != null ? content.length() : 0) +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
