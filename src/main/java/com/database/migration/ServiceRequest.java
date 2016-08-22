package com.database.migration;


/**
 * Created by Shirel Azulay 24.7.16.
 */

import com.utils.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class ServiceRequest {
    private int id;
    private int sr_cust_issueid;
    private String title;
    private String description;
    private String resolution;
    private String problem_type;
    private String request_user;
    private String submit_user;
    private String responsibility;
    private String insert_time;
    private Date close_time;
    private Date due_date;
    private Date update_time;
    private int status;
    private int urgency;
    private int impact;
    private int priority;
    private String sr_cust_Milestone;
    private String sr_cust_affMilestone;
    private String sr_cust_issuetype;
    private String sr_cust_partner;
    private String sr_cust_oldcompany;
    private float sr_cust_issest;
    private float sr_cust_isstime;
    private int parent_link;
    private String sr_cust_parent;
    private String sr_cust_related;
    private int source;
    private int sr_type;
    private int sr_sub_type;
    private String account_id;
    private int version;
    private String notes;
    private String sr_cust_IssHistory;


    /**
     * Persist record
     * @param ps
     * @param isHistory
     * @throws SQLException
     */
    public void save(PreparedStatement ps, boolean isHistory) throws SQLException {
        try {
            ps.setInt(1, this.sr_cust_issueid);
            ps.setString(2, this.title);
            if (this.sr_cust_issueid == 20672 || this.sr_cust_issueid == 20674) {
                ps.setString(3, getValidString(this.description));
            } else {
                ps.setString(3, this.description);
            }
            ps.setString(4, this.resolution);
            ps.setString(5, this.problem_type);
            ps.setString(6, this.request_user);
            ps.setString(7, this.submit_user);
            ps.setString(8, this.responsibility);
            ps.setString(9, this.insert_time);
            ps.setTimestamp(10, this.close_time == null ? null : new Timestamp(this.close_time.getTime()));
            ps.setTimestamp(11, this.due_date == null ? null : new Timestamp(this.due_date.getTime()));
            ps.setTimestamp(12, this.update_time == null ? null : new Timestamp(this.update_time.getTime()));
            ps.setInt(13, this.status);
            ps.setInt(14, this.priority);
            ps.setInt(15, 5);
            ps.setInt(16, 5);
            ps.setString(17, this.sr_cust_Milestone);
            ps.setString(18, this.sr_cust_affMilestone);
            ps.setString(19, this.sr_cust_issuetype);
            ps.setFloat(20, this.sr_cust_issest);
            ps.setFloat(21, this.sr_cust_isstime);
            ps.setString(22, this.sr_cust_partner);
            ps.setString(23, this.sr_cust_oldcompany);
            ps.setString(24, this.sr_cust_parent);
            ps.setString(25, this.sr_cust_related);
            ps.setInt(26, 1);
            ps.setInt(27, 1);
            ps.setInt(28, 20);
            ps.setString(29, "its");
            ps.setInt(30, 1);
            ps.setString(31, this.notes);
            ps.setString(32, this.sr_cust_IssHistory);
            if (isHistory) {
                ps.setInt(33, this.id);
            }

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getCause());
        }

    }


    public ResultSet getAllRecordThatWasInsertedToServiceReq(PreparedStatement ps) throws SQLException {
        try {
            System.out.println("this.sr_cust_issueid = " + this.sr_cust_issueid);
            ps.setInt(1, this.sr_cust_issueid);
            return ps.executeQuery();

        } catch (Exception e) {
            System.out.println(description);
        }
        return null;
    }


    public int getSr_cust_issueid() {
        return sr_cust_issueid;
    }

    public void setSr_cust_issueid(int sr_cust_issueid) {
        this.sr_cust_issueid = sr_cust_issueid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getProblem_type() {
        return problem_type;
    }

    public void setProblem_type(String problem_type) {
        this.problem_type = problem_type;
    }

    public String getRequest_user() {
        return request_user;
    }

    public void setRequest_user(String request_user) {
        this.request_user = request_user;
    }

    public String getSubmit_user() {
        return submit_user;
    }

    public void setSubmit_user(String submit_user) {
        this.submit_user = submit_user;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public Date getClose_time() {
        return close_time;
    }

    public void setClose_time(Date close_time) {
        this.close_time = close_time;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public int getImpact() {
        return impact;
    }

    public void setImpact(int impact) {
        this.impact = impact;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSr_cust_Milestone() {
        return sr_cust_Milestone;
    }

    public void setSr_cust_Milestone(String sr_cust_Milestone) {
        this.sr_cust_Milestone = sr_cust_Milestone;
    }

    public String getSr_cust_affMilestone() {
        return sr_cust_affMilestone;
    }

    public void setSr_cust_affMilestone(String sr_cust_affMilestone) {
        this.sr_cust_affMilestone = sr_cust_affMilestone;
    }

    public String getSr_cust_issuetype() {
        return sr_cust_issuetype;
    }

    public void setSr_cust_issuetype(String sr_cust_issuetype) {
        this.sr_cust_issuetype = sr_cust_issuetype;
    }

    public String getSr_cust_partner() {
        return sr_cust_partner;
    }

    public void setSr_cust_partner(String sr_cust_partner) {
        this.sr_cust_partner = sr_cust_partner;
    }

    public float getSr_cust_issest() {
        return sr_cust_issest;
    }

    public void setSr_cust_issest(float sr_cust_issest) {
        this.sr_cust_issest = sr_cust_issest;
    }

    public float getSr_cust_isstime() {
        return sr_cust_isstime;
    }

    public void setSr_cust_isstime(float sr_cust_isstime) {
        this.sr_cust_isstime = sr_cust_isstime;
    }

    public String getSr_cust_oldcompany() {
        return sr_cust_oldcompany;
    }

    public void setSr_cust_oldcompany(String sr_cust_oldcompany) {
        this.sr_cust_oldcompany = sr_cust_oldcompany;
    }

    public int getParent_link() {
        return parent_link;
    }

    public void setParent_link(int parent_link) {
        this.parent_link = parent_link;
    }

    public String getSr_cust_related() {
        return sr_cust_related;
    }

    public void setSr_cust_related(String sr_cust_related) {
        this.sr_cust_related = sr_cust_related;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getSr_type() {
        return sr_type;
    }

    public void setSr_type(int sr_type) {
        this.sr_type = sr_type;
    }

    public int getSr_sub_type() {
        return sr_sub_type;
    }

    public void setSr_sub_type(int sr_sub_type) {
        this.sr_sub_type = sr_sub_type;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static String getInsertServiceReqSql() {
        return Queries.INSERT_SERVICE_REQ_SQL;
    }

    public String getSr_cust_parent() {
        return sr_cust_parent;
    }

    public void setSr_cust_parent(String sr_cust_parent) {
        this.sr_cust_parent = sr_cust_parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSr_cust_IssHistory() {
        return sr_cust_IssHistory;
    }

    public void setSr_cust_IssHistory(String sr_cust_IssHistory) {
        this.sr_cust_IssHistory = sr_cust_IssHistory;
    }


    public String getValidString(String invalidString) {
        return invalidString.replace("\uD83D\uDE03", " ");
    }

}
