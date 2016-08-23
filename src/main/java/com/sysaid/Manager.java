package com.sysaid;

import com.database.migration.*;
import com.pojo.ResultSetIssueCommentsView;
import com.pojo.ResultSetIssueHistory;
import com.utils.Connections;
import com.utils.Queries;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.*;

import java.util.*;



/**
 * Created by Shirel Azulay on 24/07/2016.
 */

public class Manager implements CommandLineRunner {

    static public List<ServiceRequest> srRecords = new ArrayList<>();
    static public List<Integer> blackListIssuesWithInvalidComments = new ArrayList<>(Arrays.asList(1598, 25908 , 26148, 26440, 26492,26570, 26704, 27666, 26865));
    static public List<ServiceRequestFiles> srRecordsFiles = new ArrayList<>();
    static public Map<Integer, ServiceRequest> issueIdToServReq = new HashMap<>();
    static public Map<Integer, Integer> srCustIssueIdToIdOfServiceRec = new HashMap<>();
    static public Map<Integer, StringBuilder> secondaryIdToConcatenatePrimaryId = new HashMap<>();
    static public boolean isThisFirstIterationMapSecondary = true;
    static public Map<Integer, StringBuilder> primaryIdToConcatenateSecondaryId = new HashMap<>();
    static public boolean isThisFirstIterationMapPrimary = true;
    static public Map<Integer, String> issueIdToConcatenateComment = new HashMap<>();
    static public Map<Integer, List<ResultSetIssueCommentsView>> issueIdToAgregateCommentViewPojo = new HashMap<>();
    static public Map<Integer, String> issueIdToConcatenateHistory = new HashMap<>();
    static public Map<Integer, List<ResultSetIssueHistory>> issueIdToAgregateIssueHistoryPojo = new HashMap<>();


    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public static void main(String[] args) throws Exception {


        //Source DB
        DriverManagerDataSource sourceDs = Connections.getDBConnections()[0];

        //Target DB
        DriverManagerDataSource targetDs = Connections.getDBConnections()[1];

        //Statements
        Statement stMaxIssueIdNumber = sourceDs.getConnection().createStatement();
        Statement stIssuesView = sourceDs.getConnection().createStatement();
        Statement stProjectCustomFieldValues = sourceDs.getConnection().createStatement();
        Statement stRelatedIssues = sourceDs.getConnection().createStatement();
        Statement stIssueCommentsView = sourceDs.getConnection().createStatement();
        Statement stIssueHistory = sourceDs.getConnection().createStatement();
        Statement stIssueAttachment = sourceDs.getConnection().createStatement();


        //Build Queries
        String querystMaxIssueIdNumber = Queries.maxIssueIdNumber();
        String queryRelatedIssues = Queries.buildQueryRelatedIssues();
        String queryIdOfServiceRec = Queries.buildQueryIdOfServiceRec();

        //Execute Queries
        ResultSet rstMaxIssueIdNumber = stMaxIssueIdNumber.executeQuery(querystMaxIssueIdNumber);
        ResultSet rsRelatedIssues = stRelatedIssues.executeQuery(queryRelatedIssues);


        rstMaxIssueIdNumber.next();
        int limit = Integer.parseInt(rstMaxIssueIdNumber.getString(1)) +1 ;



        //Populate tables - service_req & service_req_history
        populateTablesServiceReqAndServiceReqHistory(0, 500, limit, targetDs, stIssuesView, stProjectCustomFieldValues, stIssueCommentsView, stIssueHistory, queryIdOfServiceRec, rsRelatedIssues);


        //Populate table - service_req_files
        populateTableServiceReqFiles(0, 500, limit, targetDs, stIssueAttachment);

        //Useing for for testing
        //populateTablesServiceReqAndServiceReqHistory(0, 250, 1000, targetDs, stIssuesView, stProjectCustomFieldValues, stIssueCommentsView, stIssueHistory, queryIdOfServiceRec, rsRelatedIssues);
        //populateTablesServiceReqAndServiceReqHistoryTest(0, 500, limit, targetDs, stIssuesView, stProjectCustomFieldValues, stIssueCommentsView, stIssueHistory, queryIdOfServiceRec, rsRelatedIssues);

    }





    /**
     * Populate tables - service_req & service_req_history with bulks
     *
     * @param targetDs
     * @param stIssuesView
     * @param stProjectCustomFieldValues
     * @param stIssueCommentsView
     * @param stIssueHistory
     * @param queryIdOfServiceRec
     * @param rsRelatedIssues
     * @throws Exception
     */
    public static void populateTablesServiceReqAndServiceReqHistory(int aMin, int incremental, int limit, DriverManagerDataSource targetDs, Statement stIssuesView, Statement stProjectCustomFieldValues, Statement stIssueCommentsView, Statement stIssueHistory, String queryIdOfServiceRec, ResultSet rsRelatedIssues) throws Exception {

        int min = aMin;
        int max = min+incremental;


        int iterationNumbers = ((limit-min)/incremental)+1;
        for (int i = 0; i < iterationNumbers ; i++) {
            System.out.println("i=" + i + "====");
            System.out.println("min=" + min + "====");
            System.out.println("max=" + max + "====");

            String queryIssuesView = Queries.buildQueryBugnetIssuesView("where IssueId>=" + min + " and IssueId<" + max);
            ResultSet rsIssuesView = stIssuesView.executeQuery(queryIssuesView);

            String queryIssueCommentsView = Queries.buildQueryIssueCommentsView("where IssueId in (select IssueId from BugNet_Issues where IssueId>=" + min + " and IssueId<" + max + ")");
            ResultSet rsIssueCommentsView = stIssueCommentsView.executeQuery(queryIssueCommentsView);

            String querysProjectCustomFieldValues = Queries.buildQueryProjectCustomFieldValues("where IssueId in (select IssueId from BugNet_Issues where IssueId>=" + min + " and IssueId<" + max + ")");
            ResultSet rsProjectCustomFieldValues = stProjectCustomFieldValues.executeQuery(querysProjectCustomFieldValues);

            String queryIssuehistory = Queries.buildQueryIssuehistory(" and IssueId in (select IssueId from BugNet_Issues where IssueId>=" + min + " and IssueId<" + max + ")");
            ResultSet rsIssuehistory = stIssueHistory.executeQuery(queryIssuehistory);

            //For next iteration
            min = max;
            max = max + incremental;

            ResultSetParser.parseResultSetToServiceRequest(rsIssuesView, rsRelatedIssues, rsIssueCommentsView, rsProjectCustomFieldValues, rsIssuehistory);

            //populate service_req
            SaverData.updateServiceReqTable(targetDs, Queries.INSERT_SERVICE_REQ_SQL, srRecords, false);

            //populate service_req_history
            SaverData.updateServiceReqHistoryTable(targetDs, Queries.INSERT_SERVICE_REQ_HISTORY_SQL, queryIdOfServiceRec, srRecords);

            //Clean data of last iteration
            cleanLastIterationSR();

        }
    }


    /**
     *  Use for testing - without bulks, can be run on specific issueId.
     * @param aMin
     * @param incremental
     * @param limit
     * @param targetDs
     * @param stIssuesView
     * @param stProjectCustomFieldValues
     * @param stIssueCommentsView
     * @param stIssueHistory
     * @param queryIdOfServiceRec
     * @param rsRelatedIssues
     * @throws Exception
     */
 public static void populateTablesServiceReqAndServiceReqHistoryTest(int aMin, int incremental, int limit, DriverManagerDataSource targetDs, Statement stIssuesView, Statement stProjectCustomFieldValues, Statement stIssueCommentsView, Statement stIssueHistory, String queryIdOfServiceRec, ResultSet rsRelatedIssues) throws Exception {

        int min = aMin;
        int max = min+incremental;


            String queryIssuesView = Queries.buildQueryBugnetIssuesView("where IssueId in (21376)");
            ResultSet rsIssuesView = stIssuesView.executeQuery(queryIssuesView);

            String queryIssueCommentsView = Queries.buildQueryIssueCommentsView("where IssueId in (21376)");
            ResultSet rsIssueCommentsView = stIssueCommentsView.executeQuery(queryIssueCommentsView);

            String querysProjectCustomFieldValues = Queries.buildQueryProjectCustomFieldValues("where IssueId in (select IssueId from BugNet_Issues where IssueId in (21376))");
            ResultSet rsProjectCustomFieldValues = stProjectCustomFieldValues.executeQuery(querysProjectCustomFieldValues);

            String queryIssuehistory = Queries.buildQueryIssuehistory(" and IssueId in (select IssueId from BugNet_Issues where IssueId in (21376))");
            ResultSet rsIssuehistory = stIssueHistory.executeQuery(queryIssuehistory);

            ResultSetParser.parseResultSetToServiceRequest(rsIssuesView, rsRelatedIssues, rsIssueCommentsView, rsProjectCustomFieldValues, rsIssuehistory);

            //populate service_req
            SaverData.updateServiceReqTable(targetDs, Queries.INSERT_SERVICE_REQ_SQL, srRecords, false);

            //populate service_req_history
            SaverData.updateServiceReqHistoryTable(targetDs, Queries.INSERT_SERVICE_REQ_HISTORY_SQL, queryIdOfServiceRec, srRecords);

    }

    /**
     * Populate table - service_req_files
     *
     * @param targetDs
     * @param stIssueAttachment
     * @throws SQLException
     */
    static void populateTableServiceReqFiles(int aMinSRF, int incremental, int limit, DriverManagerDataSource targetDs, Statement stIssueAttachment) throws SQLException {
        int minSRF = aMinSRF;
        int maxSRF = minSRF + incremental;
        int iterationNumbers = ((limit-minSRF)/incremental)+1;
        for (int i = 0; i < iterationNumbers; i++) {
            System.out.println("i=" + i + "====");
            System.out.println("minSRF=" + minSRF + "====");
            System.out.println("maxSRF=" + maxSRF + "====");

            String queryServiceReq = Queries.buildQueryServiceRecId("where sr_cust_issueid>=" + minSRF + " and sr_cust_issueid<" + maxSRF);
            Statement stServiceReq = targetDs.getConnection().createStatement();
            ResultSet rsServiceReq = stServiceReq.executeQuery(queryServiceReq);

            String queryIssueAttachment = Queries.buildQueryIssueAttachment(("where IssueId>=" + minSRF + " and IssueId<" + maxSRF));
            ResultSet rsIssueAttachment = stIssueAttachment.executeQuery(queryIssueAttachment);



            ResultSetParser.populateMapSrCustIssueIdToId(rsServiceReq);
            ResultSetParser.parseResultSetToServiceRequestFiles(rsIssueAttachment);

            //For next iteration
            minSRF = maxSRF;
            maxSRF = maxSRF + incremental;

            //populate service_req_files
            SaverData.updateServiceReqFilesTable(targetDs, Queries.INSERT_SERVICE_REQ_FILES_SQL, srRecordsFiles);

            //Clean data of last iteration
            cleanLastIterationSRF();

        }
    }

    private static void cleanLastIterationSR() {
        srRecords = new ArrayList<>();
        issueIdToServReq = new HashMap<>();
        issueIdToConcatenateComment = new HashMap<>();
        issueIdToAgregateCommentViewPojo = new HashMap<>();
        issueIdToConcatenateHistory = new HashMap<>();
        issueIdToAgregateIssueHistoryPojo = new HashMap<>();
    }

    private static void cleanLastIterationSRF() {
        srRecordsFiles = new ArrayList<>();
        srCustIssueIdToIdOfServiceRec = new HashMap<>();
    }


}
