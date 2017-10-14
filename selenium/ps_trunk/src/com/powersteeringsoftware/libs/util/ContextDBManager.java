package com.powersteeringsoftware.libs.util;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import java.sql.*;
import java.util.LinkedList;
import java.util.Map;

public class ContextDBManager {
    private Connection con;
    private static ContextDBManager manager;

    private ContextDBManager() {
    }

    public static synchronized ContextDBManager getConnection(String connectionString) throws SQLException, ClassNotFoundException {
        if (null == manager) manager = new ContextDBManager();

        PSLogger.info("Connecting to the SQL server (" + connectionString + ")");
        if (connectionString.indexOf("jtds") > -1) {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } else if (connectionString.indexOf("inetdae7") > -1) {
            Class.forName("com.inet.tds.TdsDriver");
        } else {
            throw new IllegalArgumentException("Unknown JDBC connection string type: driver not recognized");
        }

        manager.con = DriverManager.getConnection(connectionString,
                CoreProperties.getContextDBUser(),
                CoreProperties.getContextDBPass());

        return manager;
    }

    public void killActiveConnectionAndRestoreDB(String dbName, String backUpPath, Map<String, String> files) throws SQLException {
        PSLogger.info("Killing the active connections to the context database");
        Statement stmt;
        String sqlQuery;
        stmt = con.createStatement();

        stmt.execute("USE master");
        /*
        LinkedList<String> spidList = getAllConnections(stmt, dbName);
        for (Iterator<String> i = spidList.iterator(); i.hasNext(); ) {
            sqlQuery = "KILL " + i.next();
            PSLogger.debug(sqlQuery);
            stmt.execute(sqlQuery);
        }*/
        PSLogger.debug("Killing connections");
        stmt.execute("ALTER DATABASE " + dbName + " SET SINGLE_USER WITH ROLLBACK IMMEDIATE");
        stmt.execute("ALTER DATABASE " + dbName + " SET MULTI_USER");
        PSLogger.debug("After killing..." + getAllConnections(stmt, dbName));

        sqlQuery = "RESTORE DATABASE [" + dbName + "] FROM  DISK = N'" + backUpPath +
                "' WITH FILE = 1, NOUNLOAD, REPLACE, STATS = 10";
        for (String name : files.keySet()) {
            sqlQuery += ", MOVE N'" + name + "' TO N'" + files.get(name) + "'";
        }
        PSLogger.debug("Restoring the context database; query={" + sqlQuery + "}");
        stmt.execute(sqlQuery);
        stmt.close();
    }


    private LinkedList<String> getAllConnections(Statement stmt, String dbName) throws SQLException {
        String sqlQuery = "SELECT * FROM sys.sysprocesses WHERE dbid = db_id('"
                + dbName + "') AND spid <> @@SPID";
        PSLogger.debug(sqlQuery);
        ResultSet resultSet = stmt.executeQuery(sqlQuery);
        LinkedList<String> spidList = new LinkedList<String>();
        while (resultSet.next()) {
            spidList.add(resultSet.getString("spid"));
        }
        return spidList;

    }

}
