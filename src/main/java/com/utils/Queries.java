package com.utils;

/**
 * Created by shirel on 25/07/2016.
 */

public class Queries {

    /*
    IssueId,
    IssueTitle,
    IssueDescription,
    ResolutionName,
    CategoryName,
    OwnerUserName,
    IV.CreatorUserName,
    AssignedUserName,
    IV.DateCreated,
    LastUpdate,
    IssueDueDate,
    StatusName,
    PriorityName,
    MilestoneName,
    AffectedMilestoneName,
    IssueTypeName,
    IssueEstimation,
    TimeLogged
   */
    public static String buildQueryBugnetIssuesView(String concatenateQuery) {
        String query = "select " +
                "IV.IssueId," +
                "IssueTitle," +
                "IssueDescription," +
                "ResolutionName," +
                "CategoryName," +
                "OwnerUserName," +
                "IV.CreatorUserName," +
                "AssignedUserName," +
                "IV.DateCreated," +
                "LastUpdate," +
                "IssueDueDate," +
                "StatusName," +
                "PriorityName," +
                "MilestoneName," +
                "AffectedMilestoneName," +
                "IssueTypeName," +
                "IssueEstimation," +
                "TimeLogged" +
                " from bugnet_issuesView AS IV " +
                concatenateQuery;
        return query;
    }

    public static String buildQueryProjectCustomFieldValues(String concatenateQuery) {
        String query = "select " +
                " issueId," +
                " CustomFieldId," +
                " CustomFieldValue" +
                " from BugNet_ProjectCustomFieldValues " +
                concatenateQuery;

        return query;
    }

    public static String buildQueryRelatedIssues() {
        String query = "select " +
                " RI.RelationType," +
                " RI.PrimaryIssueId," +
                " RI.SecondaryIssueId" +
                " from shirel_BugNet_RelatedIssues AS RI";


        return query;
    }


    public static String buildQueryIssueCommentsView(String concateneteQuery) {
        String query = "select " +
                "IssueCommentId," +
                "IssueId," +
                "CreatorUserName," +
                "DateCreated," +
                "Comment" +
                " from BugNet_IssueCommentsView AS ICV "+
                concateneteQuery;

        return query;
    }

//TODO: Change the name
    public static String buildQueryIdOfServiceRec() {
        String query = "select " +
                "id," +
                "sr_cust_issueid" +
                " from shirel_service_req_temp" +
                " where id >= (SELECT id from shirel_service_req_temp WHERE sr_cust_issueid = ? )";


        return query;
    }


    public static String buildQueryIssuehistory(String concatenateQuery) {
        String query = "select " +
                "BIH.*, U.UserName" +
                " from BugNet_Issuehistory as BIH, Users as U" +
                " where BIH.UserId=U.UserId " +
                concatenateQuery;

        return query;
    }


    public static String buildQueryIssueAttachment(String concatenateQuery) {
        String query = "SELECT IssueId, " +
                " FileName, " +
                "Attachment, " +
                "DateCreated " +
                "from BugNet_IssueAttachments "
                + concatenateQuery;

        return query;
    }
    //todo CHANGE THE NAME
    public static String buildQueryServiceRecId() {
        String query = "SELECT sr_cust_issueid, id from shirel_service_req_temp";

        return query;
    }

    public static String maxIssueIdNumber() {
        String query = "select max(IssueId) from BugNet_Issues";

        return query;
    }


    /*//Todo : change the name shirel...
    final public static String insertServiceReqSql =
            "insert into service_req (sr_cust_issueid, title, description, resolution, problem_type," +
                    "request_user, submit_user, responsibility, insert_time, close_time, due_date, update_time," +
                    "status, priority, impact, urgency, sr_cust_Milestone, sr_cust_affMilestone, sr_cust_issuetype, sr_cust_issest, sr_cust_isstime," +
                    "sr_cust_partner, sr_cust_oldcompany, sr_cust_parent, sr_cust_related, source, sr_type, sr_sub_type, account_id, version, Notes, sr_cust_IssHistory) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";


    //TODO:change name of the table
    final public static String insertServiceReqHistorySql =
            "insert into service_req_history (sr_cust_issueid, title, description, resolution, problem_type," +
                    "request_user, submit_user, responsibility, insert_time, close_time, due_date, update_time," +
                    "status, priority, impact, urgency, sr_cust_Milestone, sr_cust_affMilestone, sr_cust_issuetype, sr_cust_issest, sr_cust_isstime," +
                    "sr_cust_partner, sr_cust_oldcompany, sr_cust_parent, sr_cust_related, source, sr_type, sr_sub_type, account_id, version, Notes, sr_cust_IssHistory, id) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

    final public static String insertServiceReqFilesSql =
            "insert into service_req_files (id, account_id, file_id, file_name, file_date," +
                    "chat_session_id, file_content, real_file_name) " +
                    "values (?,?,?,?,?,?,?,?) ";

    */

    //Todo : change the name shirel...
    final public static String insertServiceReqSql =
            "insert into shirel_service_req_temp (sr_cust_issueid, title, description, resolution, problem_type," +
                    "request_user, submit_user, responsibility, insert_time, close_time, due_date, update_time," +
                    "status, priority, impact, urgency, sr_cust_Milestone, sr_cust_affMilestone, sr_cust_issuetype, sr_cust_issest, sr_cust_isstime," +
                    "sr_cust_partner, sr_cust_oldcompany, sr_cust_parent, sr_cust_related, source, sr_type, sr_sub_type, account_id, version, Notes, sr_cust_IssHistory) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";


    //TODO:change name of the table
    final public static String insertServiceReqHistorySql =
            "insert into shirel_service_req__history_temp (sr_cust_issueid, title, description, resolution, problem_type," +
                    "request_user, submit_user, responsibility, insert_time, close_time, due_date, update_time," +
                    "status, priority, impact, urgency, sr_cust_Milestone, sr_cust_affMilestone, sr_cust_issuetype, sr_cust_issest, sr_cust_isstime," +
                    "sr_cust_partner, sr_cust_oldcompany, sr_cust_parent, sr_cust_related, source, sr_type, sr_sub_type, account_id, version, Notes, sr_cust_IssHistory, id) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";


    //TODO:change name of the table
    final public static String insertServiceReqFilesSql =
            "insert into shirel_service_req_files (id, account_id, file_id, file_name, file_date," +
                    "chat_session_id, file_content, real_file_name) " +
                    "values (?,?,?,?,?,?,?,?) ";

}
