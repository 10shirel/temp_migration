package com.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shirel Azulay on 25/07/2016.
 */

public class MappingValues {

public static Map <String, Integer> priority = new HashMap();
    static {
        priority.put("Emergency Fix", 1);
        priority.put("Unassigned", 5);
        priority.put("1 - Emergency Fix", 1);
        priority.put("3 - Low", 5);
        priority.put("3 - Medium", 4);
        priority.put("1 - High", 3);
        priority.put("Low", 5);
        priority.put("High", 3);
        priority.put("2 - Medium", 4);
        priority.put("0 - Emergency Fix", 1);
        priority.put("2 - High", 3);
        priority.put("Medium", 4);
        priority.put("4 - Low", 5);
        priority.put("Critical", 2);
    }

    public static Map <String, Integer> status = new HashMap();
    static {
        status.put("Closed - Cannot replicate", 3);
        status.put("Pending", 13);
        status.put("Unassigned", 3);
        status.put("QA Complete", 3);
        status.put("Closed - Fixed", 3);
        status.put("Closed", 3);
        status.put("Closed- No Response from Client", 3);
        status.put("Code Review", 3);
        status.put("Waiting on approval", 13);
        status.put("Closed - Not An Issue", 3);
        status.put("Pending - On Hold", 13);
        status.put("Closed-Completed", 3);
        status.put("Closed - Forwarded to Partner", 3);
        status.put("Dev - curriculum", 3);
        status.put("Pending - Waiting For More Info", 13);
        status.put("Delayed", 6);
        status.put("Closed - Duplicate Issue", 3);
        status.put("Closed-Duplicate Issue", 3);
        status.put("Dev - design", 3);
        status.put("Waiting on client", 14);
        status.put("Pending- waiting for more info", 13);
        status.put("QA", 3);
        status.put("Ready for QA", 3);
        status.put("QA Ready", 13);
        status.put("Closed- Done", 3);
        status.put("Closed-Other", 3);
        status.put("Closed - Resolved", 3);
        status.put("In Progress", 2);
        status.put("Open", 2);
    }


    public static Map <String, String> descritionIssueHistory = new HashMap();
    static {
        descritionIssueHistory.put("Description", "has been modified");
        descritionIssueHistory.put("Title", "has been modified");
        descritionIssueHistory.put("Status", "has been changed to _X_");
        descritionIssueHistory.put("Priority", "has been changed to _X_");
        descritionIssueHistory.put("Estimation", "has been changed to _X_");
        descritionIssueHistory.put("Due Date", "has been changed to _X_");
        descritionIssueHistory.put("Milestone", "has been changed to _X_");
        descritionIssueHistory.put("Issue Type", "has been changed to _X_");
        descritionIssueHistory.put("Affected Milestone", "has been changed to _X_");
        descritionIssueHistory.put("Time Logged", "has been changed to _X_");
        descritionIssueHistory.put("Category", "has been changed to _X_");
        descritionIssueHistory.put("Assigned to", "has been changed to _X_");
        descritionIssueHistory.put("Progress", "has been changed to _X_");
        descritionIssueHistory.put("Owner", "has been changed to _X_");
        descritionIssueHistory.put("Visibility to", "has been changed to _X_");
        descritionIssueHistory.put("Resolution", "has been changed to _X_");

        descritionIssueHistory.put("Parent issue", "has been _X_");
        descritionIssueHistory.put("Related issue", "has been _X_");
        descritionIssueHistory.put("Child issue", "has been _X_");
        descritionIssueHistory.put("Attachment", "has been added");
        descritionIssueHistory.put("Comment", "has been added");
    }
}
