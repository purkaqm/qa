package test.service;

import java.sql.*;
import java.util.*;

public class ContextDBManager {
	private Connection con;

	public ContextDBManager() throws Exception{
		if (Config.getInstance().getDbConnectionString().indexOf("jtds") > -1) {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} else if (Config.getInstance().getDbConnectionString().indexOf("inetdae7") > -1) {
			Class.forName("com.inet.tds.TdsDriver");
		} else {
			throw new IllegalArgumentException("Unknown JDBC connection string type: driver not recognized");
		}

		con = DriverManager.getConnection(Config.getInstance().getDbConnectionString(), "psdev", "psdev");
	}

	public void killActiveConnectionAndRestoreDB() throws Exception {
			Statement stmt;
			stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM sys.sysprocesses WHERE dbid = db_id('"
					+ Config.getInstance().getDbName() + "')");
			LinkedList<String> spidList = new LinkedList<String>();
			while (resultSet.next()) {
				spidList.add(resultSet.getString("spid"));
			}
			for ( Iterator<String> i = spidList.iterator(); i.hasNext(); ) {
				stmt.execute("KILL " + i.next());
			}
			stmt.execute("RESTORE DATABASE [" + Config.getInstance().getDbName()
					+ "] FROM  DISK = N'" + Config.getInstance().getDbBackupPath()
					+ "' WITH  FILE = 1,  NOUNLOAD,  REPLACE,  STATS = 10");
			stmt.close();
	}

}
