package com.pojo;

/**
 * Created by shirel on 25/07/2016.
 */

    /*  "Notes Mapping:
    id        bugnet_issueCommentsView.issuecommentid
    userName    bugnet_issueCommentsView.creatorusername
    date    bugnet_issueCommentsView.datecreated
    text     bugnet_issueCommentsView.fieldChanged"*/



public class ResultSetIssueCommentsView {
    String issueCommentId;
    String IssueId;
    String creatorUserName;
    String dateCreated;
    String comment;


    public String getIssueCommentId() {
        return issueCommentId;
    }

    public void setIssueCommentId(String issueCommentId) {
        this.issueCommentId = issueCommentId;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(/*Date*/String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIssueId() {
        return IssueId;
    }

    public void setIssueId(String issueId) {
        IssueId = issueId;
    }


}
