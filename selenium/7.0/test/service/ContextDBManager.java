package test.service;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

public class ContextDBManager {
	private Connection con;
	private Logger logger;

	private static ContextDBManager manager;

	private ContextDBManager(Logger logger){
		this.logger = logger;
	}

	public static synchronized ContextDBManager getConnection(String connectionString, Logger logger) throws SQLException, ClassNotFoundException{
		if (null == manager) manager = new ContextDBManager(logger);

		logger.info("Connecting to the SQL server");
		if (connectionString.indexOf("jtds") > -1) {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} else if (connectionString.indexOf("inetdae7") > -1) {
			Class.forName("com.inet.tds.TdsDriver");
		} else {
			throw new IllegalArgumentException("Unknown JDBC connection string type: driver not recognized");
		}

		manager.con = DriverManager.getConnection(connectionString, "psdev", "psdev");
		return manager;
	}

	public void killActiveConnectionAndRestoreDB(String dbName, String backUpPath) throws SQLException{
			logger.info("Killing the active connections to the context database");
			Statement stmt;
			stmt = con.createStatement();

			stmt.execute("USE master");

			ResultSet resultSet = stmt.executeQuery("SELECT * FROM sys.sysprocesses WHERE dbid = db_id('"
					+ dbName + "') AND spid <> @@SPID");
			LinkedList<String> spidList = new LinkedList<String>();
			while (resultSet.next()) {
				spidList.add(resultSet.getString("spid"));
			}
			for ( Iterator<String> i = spidList.iterator(); i.hasNext(); ) {
				stmt.execute("KILL " + i.next());
			}

			logger.info("Restoring the context database");
			stmt.execute("RESTORE DATABASE [" + dbName + "] FROM  DISK = N'" + backUpPath +
					"' WITH  FILE = 1,  NOUNLOAD,  REPLACE,  STATS = 10");
			stmt.close();
	}

}
