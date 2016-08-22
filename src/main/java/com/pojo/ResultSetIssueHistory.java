package com.pojo;

/**
 * Created by Shirel Azulay on 4/08/2016.
 */

public class ResultSetIssueHistory {

    String dateCreated;
    String userId;
    String userName;
    String description;
    int IssueId;
    int IssueHistoryId;
    String fieldChanged;
    String oldValue;


    String newValue;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(/*Date*/String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFieldChanged() {
        return fieldChanged;
    }

    public void setFieldChanged(String fieldChanged) {
        this.fieldChanged = fieldChanged;
    }

    public int getIssueId() {
        return IssueId;
    }

    public void setIssueId(int issueId) {
        IssueId = issueId;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public int getIssueHistoryId() {
        return IssueHistoryId;
    }

    public void setIssueHistoryId(int issueHistoryId) {
        IssueHistoryId = issueHistoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
}
