package com.database.migration;


/**
 * Created by Shirel Azulay 9.8.16.
 */


import java.sql.*;

public class ServiceRequestFiles {
    private int id;
    private String accountId;
    private String fileId;
    private String fileName;
    private String fileDate;
    private String chatSession;
    private String fileContent;
    private String realFileName;


    /**
     * Persist service request file record
     * @param ps
     * @throws SQLException
     */
    public void save(PreparedStatement ps) throws SQLException {
        try {
            ps.setInt(1, this.id);
            ps.setString(2, this.accountId);
            ps.setString(3, this.fileId);
            ps.setString(4, this.fileName);
            ps.setString(5, this.fileDate);
            ps.setString(6, this.chatSession);
            ps.setString(7, this.fileContent);
            ps.setString(8, this.realFileName);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getChatSession() {
        return chatSession;
    }

    public void setChatSession(String chatSession) {
        this.chatSession = chatSession;
    }

    public /*Blob*/String getFileContent() {
        return fileContent;
    }

    public void setFileContent(/*Blob*/String fileContent) {
        this.fileContent = fileContent;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }
}
