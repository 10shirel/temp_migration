package com.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Created by shirel on 24/07/2016.
 */
public class Connections {
    @NotNull
    public static DriverManagerDataSource[] getDBConnections() {
        DriverManagerDataSource sourceTargetConnections [] = new DriverManagerDataSource[2];

        //Source DB
        DriverManagerDataSource sourceDs = new DriverManagerDataSource();
        sourceDs.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        sourceDs.setUrl("jdbc:sqlserver://10.14.1.19\\dbo:1433;databaseName=itstraining");
        sourceDs.setUsername("sa");
        sourceDs.setPassword("Password1");


        //Target DB
        DriverManagerDataSource targetDs = new DriverManagerDataSource();
        targetDs.setDriverClassName("com.mysql.jdbc.Driver");
        targetDs.setUrl("jdbc:mysql://10.14.1.167:3306/itssandbox");
        targetDs.setUsername("root");
        targetDs.setPassword("Password1");

        sourceTargetConnections[0] = sourceDs;
        sourceTargetConnections[1] = targetDs;
        return sourceTargetConnections;
    }
}
