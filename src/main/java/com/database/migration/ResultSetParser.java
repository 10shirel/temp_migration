package com.database.migration;

import com.pojo.ResultSetIssueHistory;
import com.utils.DateUtils;
import com.utils.MappingValues;
import com.pojo.ResultSetIssueCommentsView;
import com.pojo.ResultSetRelatedIssues;
import org.jsoup.Jsoup;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

import static com.sysaid.Main.*;

/**
 * Created by Shirel Azulay 24.7.16
 */

public class ResultSetParser {

    public static void parseResultSetToServiceRequest(ResultSet rsIssuesView, ResultSet rsRelatedIssues, ResultSet rsIssueCommentsView, ResultSet rsProjectCustomFieldValues, ResultSet rsIssueHistory) throws Exception {


        //Populate the most of the fields , those fields use only one query as reference
        populateServiceReqBaseFileds(rsIssuesView);

        List<ResultSetRelatedIssues> listFromrsRelatedIssues = populateListFromRSRelatedIssues(rsRelatedIssues);

        //Populate CustParent
        if (isThisFirstIterationMapSecondary) {
            populateMapSecondaryIdToConcatenatePrimaryId(listFromrsRelatedIssues);
            isThisFirstIterationMapSecondary = false;
        }
        populateCustParent(srRecords);


        //Populate CustRelated
        if (isThisFirstIterationMapPrimary) {
            populateMapPrimaryIdToConcatenateSecondaryId(listFromrsRelatedIssues);
            isThisFirstIterationMapPrimary = false;
        }
        populateCustRelated(srRecords);

        //Populate Notes
        //List<ResultSetIssueCommentsView> listFromrsIssueCommentsView = populateListFromRSIssueCommentsView(rsIssueCommentsView);
        //populateMapIssueIdToConcatenateSortedComment(listFromrsIssueCommentsView);
        populateMapIssueIdToConcatenateSortedComment(rsIssueCommentsView);
        formatResultSetIssueCommentsView();
        populateNotes(srRecords);

        //Populate sr_cust_partner & sr_cust_oldcompany
        populateSrCustPartnerAndSrCustOldcompany(rsProjectCustomFieldValues);

        //Populate sr_cust_IssueHistory
        List<ResultSetIssueHistory> listFromRSIssueHistory = populateListFromRSIssueHistory(rsIssueHistory);
        populateMapIssueIdToConcatenateIssueHistory(listFromRSIssueHistory);
        formatResultSetIssueHistory();
        populateSrCustIssueHistory(srRecords);


    }


    public static void parseResultSetToServiceRequestFiles(ResultSet rsIssueAttachment) {
        try {
            while (rsIssueAttachment.next()) { //Go over all the records
                ServiceRequestFiles currentServiceRequestFiles = new ServiceRequestFiles();

                //Populate id
                int issueId = Integer.parseInt(rsIssueAttachment.getString(1));
                Integer id = srCustIssueIdToIdOfServiceRec.get(issueId);
                if (id == null) {
                    System.out.println("continuing == issueId = " + issueId);
                    continue;
                }
                currentServiceRequestFiles.setId(id);


                //Populate account_id
                currentServiceRequestFiles.setAccountId("its");

                //Populate file_id
                String fileName = rsIssueAttachment.getString(2);
                System.out.println("fileName = " + fileName);
                currentServiceRequestFiles.setFileId(generateFileId(fileName));

                //Populate file_Name
                currentServiceRequestFiles.setFileName(fileName);

                //Populate file_Content
                String fileContent = rsIssueAttachment.getString(3);
                currentServiceRequestFiles.setFileContent(fileContent);

                //Populate file_date
                String fileDate = rsIssueAttachment.getString(4);
                currentServiceRequestFiles.setFileDate(fileDate);


                srRecordsFiles.add(currentServiceRequestFiles);


            }//close While
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void populateServiceReqBaseFileds(ResultSet rsIssuesView) throws SQLException, ParseException {
        int counter = 0;

        while (rsIssuesView.next()) { //Go over all the records
            ServiceRequest serviceRequest = new ServiceRequest();
            int relationType = 999;
            for (int i = 1; i <= rsIssuesView.getMetaData().getColumnCount(); ++i) { //Go over all the fields
                if (rsIssuesView.getString(i) != null && !rsIssuesView.getString(i).isEmpty()) {
                    String recordVal = rsIssuesView.getString(i).trim();
                    switch (i) {
                        case 1://IV.IssueId
                            serviceRequest.setSr_cust_issueid(Integer.parseInt(recordVal));
                            break;
                        case 2://IssueTitle
                            serviceRequest.setTitle(recordVal);
                            break;
                        case 3://IssueDescription
                            serviceRequest.setDescription(Jsoup.parse(recordVal).text());
                            break;
                        case 4://ResolutionName
                            serviceRequest.setResolution(recordVal);
                            break;
                        case 5://CategoryName
                            serviceRequest.setProblem_type(recordVal);
                            break;
                        case 6://OwnerUserName
                            serviceRequest.setRequest_user(recordVal);
                            break;
                        case 7://CreatorUserName
                            serviceRequest.setSubmit_user(recordVal);
                            break;
                        case 8://AssignedUserName
                            serviceRequest.setResponsibility(recordVal);
                            break;
                        case 9://DateCreated
                            serviceRequest.setInsert_time(recordVal);
                            break;
                        case 10://LastUpdate
                            serviceRequest.setClose_time(DateUtils.convertStringToDate(DateUtils.DateFormatAmPmHMS, recordVal));
                            serviceRequest.setUpdate_time(DateUtils.convertStringToDate(DateUtils.DateFormatAmPmHMS, recordVal));
                            break;
                        case 11://IssueDueDate
                            serviceRequest.setDue_date(DateUtils.convertStringToDate(DateUtils.DateFormatAmPmHMS, recordVal));
                            break;
                        case 12://StatusName
                            if (recordVal != null) {
                                if (recordVal.equalsIgnoreCase("Closed - Not An Issue".trim())) {
                                    serviceRequest.setStatus(3);
                                    break;
                                } else if (recordVal != null && recordVal.equalsIgnoreCase("Waiting on client".trim())) {
                                    serviceRequest.setStatus(3);
                                    break;
                                } else if (recordVal != null && recordVal.equalsIgnoreCase("Closed - Cannot Replicate".trim())) {
                                    serviceRequest.setStatus(3);
                                    break;
                                }

                            }
                            serviceRequest.setStatus(MappingValues.status.get(recordVal) == null ? 0 : MappingValues.status.get(recordVal));
                            break;
                        case 13://PriorityName
                            serviceRequest.setPriority(MappingValues.priority.get(recordVal) == null ? 0 : MappingValues.priority.get(recordVal));
                            break;
                        case 14://MilestoneName
                            serviceRequest.setSr_cust_Milestone(recordVal);
                            break;
                        case 15://AffectedMilestoneName
                            serviceRequest.setSr_cust_affMilestone(recordVal);
                            break;
                        case 16://IssueTypeName
                            serviceRequest.setSr_cust_issuetype(recordVal);
                            break;
                        case 17://IssueEstimation
                            serviceRequest.setSr_cust_issest(Float.parseFloat(recordVal));
                            break;
                        case 18://TimeLogged
                            serviceRequest.setSr_cust_isstime(Float.parseFloat(recordVal));
                            break;

                        default:
                            System.err.println("Error: ");

                    }

                }
            } // Close for
            srRecords.add(serviceRequest);
            issueIdToServReq.put(serviceRequest.getSr_cust_issueid(), serviceRequest);

        } //Close While
    }

    private static void populateMapSecondaryIdToConcatenatePrimaryId(List<ResultSetRelatedIssues> rsRelatedIssuesList) {
        for (ResultSetRelatedIssues currentRS : rsRelatedIssuesList) {
            StringBuilder concatenatePrimaryId;
            StringBuilder value;
            if (Integer.parseInt(currentRS.getRelationType()) == 0) {
                Integer key = Integer.parseInt(currentRS.getSecondaryIssueId());
                value = secondaryIdToConcatenatePrimaryId.get(key);
                if (value != null) {
                    concatenatePrimaryId = (value).append(",").append(new StringBuilder(currentRS.getPrimaryIssueId()));
                    secondaryIdToConcatenatePrimaryId.put(key, concatenatePrimaryId);
                } else {
                    secondaryIdToConcatenatePrimaryId.put(key, new StringBuilder(currentRS.getPrimaryIssueId()));
                }
            }
        }


    }

    public static List<ServiceRequest> populateCustParent(List<ServiceRequest> srRecords) {
        for (ServiceRequest currentRecord : srRecords) {
            Integer key = currentRecord.getSr_cust_issueid();
            StringBuilder value = secondaryIdToConcatenatePrimaryId.get(key);
            if (value != null) {
                currentRecord.setSr_cust_parent(value.toString());
            }

        }

        return srRecords;
    }


    private static void populateMapPrimaryIdToConcatenateSecondaryId(List<ResultSetRelatedIssues> rsRelatedIssuesList) {
        for (ResultSetRelatedIssues currentRS : rsRelatedIssuesList) {
            StringBuilder concatenateSecondaryId;
            StringBuilder value;
            if (Integer.parseInt(currentRS.getRelationType()) == 1) {
                Integer key = Integer.parseInt(currentRS.getPrimaryIssueId());
                value = primaryIdToConcatenateSecondaryId.get(key);
                if (value != null) {
                    concatenateSecondaryId = value.append(",").append(new StringBuilder(currentRS.getSecondaryIssueId()));
                    primaryIdToConcatenateSecondaryId.put(key, concatenateSecondaryId);
                } else {
                    primaryIdToConcatenateSecondaryId.put(key, new StringBuilder(currentRS.getSecondaryIssueId()));
                }
            }
        }

    }


    private static void populateSrCustPartnerAndSrCustOldcompany(ResultSet rsProjectCustomFieldValues) {

        try {
            while (rsProjectCustomFieldValues.next()) { //Go over all the records
                int issueId = Integer.parseInt(rsProjectCustomFieldValues.getString(1));
                int customFieldId = Integer.parseInt(rsProjectCustomFieldValues.getString(2));
                String customFieldValue = rsProjectCustomFieldValues.getString(3);
                ServiceRequest currentServiceRequest = issueIdToServReq.get(issueId);

                if (currentServiceRequest != null) {
                    if (customFieldId == 4 || customFieldId == 12 || customFieldId == 16 || customFieldId == 24) {
                        currentServiceRequest.setSr_cust_partner(customFieldValue);
                    } else if (customFieldId == 28) {
                        currentServiceRequest.setSr_cust_oldcompany(customFieldValue);
                    }
                }
            }//close While
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static String generateFileId(String fileName) {
        Random rand = new Random();
        String ranndom = Integer.toString(rand.nextInt(1000) + 1);
        File file = new File(fileName);

        return (fileName+ranndom).hashCode() + "_" + file.hashCode();
    }


    public static List<ServiceRequest> populateCustRelated(List<ServiceRequest> srRecords) {
        for (ServiceRequest currentRecord : srRecords) {
            Integer key = currentRecord.getSr_cust_issueid();
            StringBuilder value = primaryIdToConcatenateSecondaryId.get(key);
            if (value != null) {
                currentRecord.setSr_cust_related(value.toString());
            }
        }
        return srRecords;
    }


    /*    private static void populateMapIssueIdToConcatenateSortedComment(List<ResultSetIssueCommentsView> rsIssueCommentsView) {
            int i = 0;
            for (ResultSetIssueCommentsView currentRS : rsIssueCommentsView) {
                List<ResultSetIssueCommentsView> aggrigateList = new ArrayList<>();
                List<ResultSetIssueCommentsView> value = new ArrayList<>();
                Integer key = Integer.parseInt(currentRS.getIssueId());
                value = issueIdToAgregateCommentViewPojo.get(key);
                if (value != null) {
                    aggrigateList.addAll(value);
                    aggrigateList.add(currentRS);
                    try {
                        issueIdToAgregateCommentViewPojo.put(key, sortedLisResultSetIssueCommentsView(aggrigateList));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    List<ResultSetIssueCommentsView> list = new ArrayList();
                    list.add(currentRS);
                    issueIdToAgregateCommentViewPojo.put(key, list);
                }
                if (i++ % 1000 == 0) {
                    System.out.println("finished-3 " + i);
                }
            }


        }*/

    private static void populateMapIssueIdToConcatenateSortedComment(ResultSet rsIssueCommentsView) {

        int i = 0;
        try {
            while (rsIssueCommentsView.next()) {
                ResultSetIssueCommentsView currentRS = new ResultSetIssueCommentsView();
                try {
                    currentRS.setIssueCommentId(rsIssueCommentsView.getString(1));
                    currentRS.setIssueId(rsIssueCommentsView.getString(2));
                    currentRS.setCreatorUserName(rsIssueCommentsView.getString(3));
                    currentRS.setDateCreated(rsIssueCommentsView.getString(4));
                    currentRS.setComment(rsIssueCommentsView.getString(5) == null ? null : Jsoup.parse(rsIssueCommentsView.getString(5)).text());

                    List<ResultSetIssueCommentsView> aggrigateList = new ArrayList<>();
                    List<ResultSetIssueCommentsView> value = new ArrayList<>();
                    Integer key = Integer.parseInt(currentRS.getIssueId());
                    if (!blackListIssuesWithInvalidComments.contains(key)) {
                        value = issueIdToAgregateCommentViewPojo.get(key);
                        if (value != null) {
                            aggrigateList.addAll(value);
                            aggrigateList.add(currentRS);
                            try {
                                issueIdToAgregateCommentViewPojo.put(key, sortedLisResultSetIssueCommentsView(aggrigateList));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            List<ResultSetIssueCommentsView> list = new ArrayList();
                            list.add(currentRS);
                            issueIdToAgregateCommentViewPojo.put(key, list);
                        }

                    }
                    if (i++ % 1000 == 0) {
                        System.out.println("finished-3 " + i);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


/*SOURCE
        int i = 0;
        for (ResultSetIssueCommentsView currentRS : rsIssueCommentsView) {
            List<ResultSetIssueCommentsView> aggrigateList = new ArrayList<>();
            List<ResultSetIssueCommentsView> value = new ArrayList<>();
            Integer key = Integer.parseInt(currentRS.getIssueId());
            value = issueIdToAgregateCommentViewPojo.get(key);
            if (value != null) {
                aggrigateList.addAll(value);
                aggrigateList.add(currentRS);
                try {
                    issueIdToAgregateCommentViewPojo.put(key, sortedLisResultSetIssueCommentsView(aggrigateList));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                List<ResultSetIssueCommentsView> list = new ArrayList();
                list.add(currentRS);
                issueIdToAgregateCommentViewPojo.put(key, list);
            }
            if (i++ % 1000 == 0) {
                System.out.println("finished-3 " + i);
            }
        }*/

/*
SWITCH TO LIKE THIS
        int i = 0;
        try {
            while (rsIssueCommentsView.next()) {
                ResultSetIssueCommentsView resultSetIssueCommentsView = new ResultSetIssueCommentsView();
                resultSetIssueCommentsView.setIssueCommentId(rsIssueCommentsView.getString(1));
                resultSetIssueCommentsView.setIssueId(rsIssueCommentsView.getString(2));
                resultSetIssueCommentsView.setCreatorUserName(rsIssueCommentsView.getString(3));
                resultSetIssueCommentsView.setDateCreated(rsIssueCommentsView.getString(4));
                resultSetIssueCommentsView.setComment(rsIssueCommentsView.getString(5) == null ? null : Jsoup.parse(rsIssueCommentsView.getString(5)).text());
                list.add(resultSetIssueCommentsView);
                i++;
                if (i % 1000 == 0) {
                    System.out.println("finished-2 " + i);
                }
                   */
/* if (i == 292010) {
                        break;
                    }*//*

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/

    }

    public static void populateMapSrCustIssueIdToId(ResultSet rsServiceReq) {
        int i = 0;
        try {
            while (rsServiceReq.next()) {
                srCustIssueIdToIdOfServiceRec.put(Integer.parseInt(rsServiceReq.getString(1)), Integer.parseInt(rsServiceReq.getString(2)));
                if (i++ % 1000 == 0) {
                    System.out.println("finished-4 " + i);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void populateMapIssueIdToConcatenateIssueHistory(List<ResultSetIssueHistory> rsIssueCommentsView) {
        for (ResultSetIssueHistory currentRS : rsIssueCommentsView) {
            List<ResultSetIssueHistory> aggrigateList = new ArrayList<>();
            List<ResultSetIssueHistory> value = new ArrayList<>();
            Integer key = currentRS.getIssueId();
            value = issueIdToAgregateIssueHistoryPojo.get(key);
            if (value != null) {
                aggrigateList.addAll(value);
                aggrigateList.add(currentRS);
                try {
                    issueIdToAgregateIssueHistoryPojo.put(key, sortedLisResultSetIssueHistory((aggrigateList)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                List<ResultSetIssueHistory> list = new ArrayList();
                list.add(currentRS);
                issueIdToAgregateIssueHistoryPojo.put(key, list);
            }
        }


    }


    private static List<ResultSetIssueHistory> populateListFromRSIssueHistory(ResultSet rsIssueHistory) {
        List<ResultSetIssueHistory> list = new ArrayList();
        try {
            int i = 0;
            while (rsIssueHistory.next()) { //Go over all the records
                ResultSetIssueHistory resultSetIssueHistory = new ResultSetIssueHistory();

                resultSetIssueHistory.setIssueHistoryId(Integer.parseInt(rsIssueHistory.getString(1)));
                resultSetIssueHistory.setIssueId(Integer.parseInt(rsIssueHistory.getString(2)));
                resultSetIssueHistory.setFieldChanged(rsIssueHistory.getString(3));
                resultSetIssueHistory.setOldValue(rsIssueHistory.getString(4));
                resultSetIssueHistory.setNewValue(rsIssueHistory.getString(5));
                resultSetIssueHistory.setDateCreated(rsIssueHistory.getString(6));
                resultSetIssueHistory.setUserId(rsIssueHistory.getString(7));
                resultSetIssueHistory.setUserName(rsIssueHistory.getString(8));
                list.add(resultSetIssueHistory);
                if (i++ % 1000 == 0) {
                    System.out.println("finished-1 " + i);
                }
             /*   if (i == 400000) {
                    break;
                }*/
            }//close While
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<ServiceRequest> populateNotes(List<ServiceRequest> srRecords) {
        for (ServiceRequest currentRecord : srRecords) {
            Integer key = currentRecord.getSr_cust_issueid();
            String value = issueIdToConcatenateComment.get(key);
            if (value != null) {
                currentRecord.setNotes(value);
            }
        }

        return srRecords;
    }

    public static List<ServiceRequest> populateSrCustIssueHistory(List<ServiceRequest> srRecords) {
        for (ServiceRequest currentRecord : srRecords) {
            Integer key = currentRecord.getSr_cust_issueid();
            String value = issueIdToConcatenateHistory.get(key);
            if (value != null) {
                currentRecord.setSr_cust_IssHistory(value.toString());
            }
        }

        return srRecords;
    }


    private static List<ResultSetRelatedIssues> populateListFromRSRelatedIssues(ResultSet rsRelatedIssues) {
        List<ResultSetRelatedIssues> list = new ArrayList();
        try {
            while (rsRelatedIssues.next()) {
                ResultSetRelatedIssues resultSetRelatedIssues = new ResultSetRelatedIssues();
                resultSetRelatedIssues.setRelationType(rsRelatedIssues.getString(1));
                resultSetRelatedIssues.setPrimaryIssueId(rsRelatedIssues.getString(2));
                resultSetRelatedIssues.setSecondaryIssueId(rsRelatedIssues.getString(3));
                list.add(resultSetRelatedIssues);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    private static List<ResultSetIssueCommentsView> populateListFromRSIssueCommentsView(ResultSet rsIssueCommentsView) {
        List<ResultSetIssueCommentsView> list = new ArrayList();
        try {
            int i = 0;
            while (rsIssueCommentsView.next()) {
                ResultSetIssueCommentsView resultSetIssueCommentsView = new ResultSetIssueCommentsView();
                resultSetIssueCommentsView.setIssueCommentId(rsIssueCommentsView.getString(1));
                resultSetIssueCommentsView.setIssueId(rsIssueCommentsView.getString(2));
                resultSetIssueCommentsView.setCreatorUserName(rsIssueCommentsView.getString(3));
                resultSetIssueCommentsView.setDateCreated(rsIssueCommentsView.getString(4));
                resultSetIssueCommentsView.setComment(rsIssueCommentsView.getString(5) == null ? null : Jsoup.parse(rsIssueCommentsView.getString(5)).text());
                list.add(resultSetIssueCommentsView);
                i++;
                if (i % 1000 == 0) {
                    System.out.println("finished-2 " + i);
                }
               /* if (i == 292010) {
                    break;
                }*/
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<ResultSetIssueCommentsView> formatResultSetIssueCommentsView() throws ParseException {

        //go over all sorted Lists
        for (Map.Entry<Integer, List<ResultSetIssueCommentsView>> entry : issueIdToAgregateCommentViewPojo.entrySet()) {
            List<ResultSetIssueCommentsView> listResultSetIssueCommentsViews = entry.getValue();

            int listSize = listResultSetIssueCommentsViews.size();
            ResultSetIssueCommentsView first = listResultSetIssueCommentsViews.get(listSize - 1);
            String commentFormat = first.getCreatorUserName() + " (" + first.getDateCreated() + "):\n" + first.getComment() + "\n************************************************\n";

            if (listSize == 1) {
                issueIdToConcatenateComment.put(entry.getKey(), commentFormat);
                continue;
            }
            //for each sorted List (that belong to same IssueId), union the comments
            for (int i = listSize - 2; i >= 0; i--) {
                ResultSetIssueCommentsView current = listResultSetIssueCommentsViews.get(i);
                commentFormat = commentFormat + current.getCreatorUserName() + " (" + current.getDateCreated() + "):\n" + current.getComment() + "\n************************************************\n";

                //
               /* if (i == (listSize - 1000)) {
                    issueIdToConcatenateComment.put(entry.getKey(), commentFormat);

                    commentFormat = null;
                }*/
            }
            issueIdToConcatenateComment.put(entry.getKey(), commentFormat);

        }

        return null;
    }

/*    public static List<ResultSetIssueCommentsView> formatResultSetIssueCommentsView() throws ParseException {

        //go over all sorted Lists
        for (Map.Entry<Integer, List<ResultSetIssueCommentsView>> entry : issueIdToAgregateCommentViewPojo.entrySet()) {
            List<ResultSetIssueCommentsView> listResultSetIssueCommentsViews = entry.getValue();

            int listSize = listResultSetIssueCommentsViews.size();
            ResultSetIssueCommentsView first = listResultSetIssueCommentsViews.get(listSize - 1);
            String commentFormat = first.getCreatorUserName() + " (" + first.getDateCreated() + "):\n" + first.getComment() + "\n************************************************\n";

            if (listSize == 1) {
                issueIdToConcatenateComment.put(entry.getKey(), commentFormat);
                continue;
            }
            //for each sorted List (that belong to same IssueId), union the comments
            for (int i = listSize - 1; i > 0; i--) {
                ResultSetIssueCommentsView current = listResultSetIssueCommentsViews.get(i);
                commentFormat = commentFormat + current.getCreatorUserName() + " (" + current.getDateCreated() + "):\n" + current.getComment() + "\n************************************************\n";
            }
            issueIdToConcatenateComment.put(entry.getKey(), commentFormat);

        }

        return null;
    }*/
/*    Timestamp: datecreated
    User: username
    Description:
    SR #IssueId FieldChanged has been changed to NewValue.
    ================================*/

    public static List<ResultSetIssueHistory> formatResultSetIssueHistory() throws ParseException {

        //go over all sorted Lists
        for (Map.Entry<Integer, List<ResultSetIssueHistory>> entry : issueIdToAgregateIssueHistoryPojo.entrySet()) {
            List<ResultSetIssueHistory> listResultSetIssueHistory = entry.getValue();

            int listSize = listResultSetIssueHistory.size();
            ResultSetIssueHistory first = listResultSetIssueHistory.get(listSize - 1);
            String historyFormat = "Timestamp : " + first.getDateCreated() + "\n User : " + first.getUserName() + "\n Description:\n"
                    + "SR# " + first.getIssueId() + " " + first.getFieldChanged() + " " + formatDescriptionIssueHistory(first.getFieldChanged(), first.getNewValue()) + ".\n************************************************\n";

            if (listSize == 1) {
                issueIdToConcatenateHistory.put(entry.getKey(), historyFormat);
                continue;
            }
            for (int i = listSize - 2; i >= 0; i--) {
                ResultSetIssueHistory current = listResultSetIssueHistory.get(i);
                historyFormat = historyFormat + "Timestamp : " + current.getDateCreated() + "\n User : " + current.getUserName() + "\n Description:\n"
                        + "SR# " + current.getIssueId() + " " + current.getFieldChanged() + " " + formatDescriptionIssueHistory(current.getFieldChanged(), current.getNewValue()) + ".\n************************************************\n";
            }
            issueIdToConcatenateHistory.put(entry.getKey(), historyFormat);

        }

        return null;
    }

    public static String formatDescriptionIssueHistory(String placeHolder, String newValue) {
        String resultFromMap = MappingValues.descritionIssueHistory.get(placeHolder);
        if (resultFromMap != null && newValue != null) {
            return resultFromMap.replace("_X_", newValue);
        }
        return placeHolder;
    }

    public static List<ResultSetIssueCommentsView> sortedLisResultSetIssueCommentsView(List resultSetIssueCommentsViewList) throws ParseException {
        Collections.sort(resultSetIssueCommentsViewList, new Comparator<ResultSetIssueCommentsView>() {
            public int compare(ResultSetIssueCommentsView v1, ResultSetIssueCommentsView v2) {
                return v1.getDateCreated().compareTo(v2.getDateCreated());
            }
        });

        return resultSetIssueCommentsViewList;
    }

    public static List<ResultSetIssueHistory> sortedLisResultSetIssueHistory(List resultSetIssueHistoryList) throws ParseException {
        Collections.sort(resultSetIssueHistoryList, new Comparator<ResultSetIssueHistory>() {
            public int compare(ResultSetIssueHistory v1, ResultSetIssueHistory v2) {
                return v1.getDateCreated().compareTo(v2.getDateCreated());
            }
        });

        return resultSetIssueHistoryList;
    }

}
