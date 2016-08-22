package com.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


/**
 * Created by Shirel Azulay on 24/07/2016.
 */
public class Connections {

    //Source
    public static final String SOURCE_DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String SOURCE_URL = "jdbc:sqlserver://10.14.1.19\\dbo:1433;databaseName=itstraining";
    public static final String SOURCE_USER_NAME = "sa";
    public static final String SOURCE_PASSWORD = "Password1";

    //Target
    public static final String TARGET_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    public static final String TARGET_URL = "jdbc:mysql://10.14.1.167:3306/itssandbox";
    public static final String TARGET_USER_NAME = "root";
    public static final String TARGET_PASSWORD = "Password1";


    /**
     * Return source / target connections DB
     * @return
     */
    @NotNull
    public static DriverManagerDataSource[] getDBConnections() {
        DriverManagerDataSource sourceTargetConnections [] = new DriverManagerDataSource[2];

        //Source DB
        DriverManagerDataSource sourceDs = new DriverManagerDataSource();
        sourceDs.setDriverClassName(SOURCE_DRIVER_CLASS_NAME);
        sourceDs.setUrl(SOURCE_URL);
        sourceDs.setUsername(SOURCE_USER_NAME);
        sourceDs.setPassword(SOURCE_PASSWORD);


        //Target DB
        DriverManagerDataSource targetDs = new DriverManagerDataSource();
        targetDs.setDriverClassName(TARGET_DRIVER_CLASS_NAME);
        targetDs.setUrl(TARGET_URL);
        targetDs.setUsername(TARGET_USER_NAME);
        targetDs.setPassword(TARGET_PASSWORD);

        sourceTargetConnections[0] = sourceDs;
        sourceTargetConnections[1] = targetDs;

        return sourceTargetConnections;
    }
}
