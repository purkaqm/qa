package com.cinteractive.ps3.test.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Insert the type's description here.
 * @author: Yoritaka Sakakura
 */
public class TestDbServlet extends HttpServlet
{
	private static final String IP_INIT_PARAM          = "accept.ip";
	
	private static final String JDBC_DRIVER_INIT_PARAM = "jdbc.driver";
	private static final String JDBC_URL_INIT_PARAM    = "jdbc.url";
	private static final String DB_USER_INIT_PARAM     = "db.user";
	private static final String DB_PASSWORD_INIT_PARAM = "db.password";
	
	private static final String DB_NAME_INIT_PARAM     = "db.name";
	private static final String BACKUP_NAME_INIT_PARAM = "db.backup.name";

	private Set _ips;
	
	private String _url;
	private String _user;
	private String _password;

	private String _dbName;
	private String _backupName;
	private void backupDatabase( Connection conn )
	throws SQLException
	{
		final String sql =
			"BACKUP DATABASE " + _dbName + " " +
			"TO " + _backupName + " " +
			"WITH INIT, NAME = '" + _backupName + "'";
		
		final Statement stmt = conn.createStatement();
		stmt.executeUpdate( sql );
		stmt.close();
	}
	private void doBackup( PrintWriter out )
	throws ServletException, IOException
	{
		Connection conn = null;
		try
		{
			conn = getConnection();
			backupDatabase( conn );

			out.println( "Backed up '" + _dbName + "' to '" + _backupName + "'." );
			out.println();
		}
		catch( SQLException e )
		{
			throw new ServletException( e );
		}
		finally
		{
			try { if( conn != null ) conn.close(); }
			catch( SQLException ignored ) {}
		}
	}
	private void doRestore( PrintWriter out )
	throws ServletException, IOException
	{
		out.println( "Attempting to restore'" + _dbName + "' from '" + _backupName + "'." );
		Connection conn = null;
		try
		{
			conn = getConnection();

			final int myId = getProcessId( conn );
			out.println( "...Current process id: '" + myId + "'." );

			final List pids = listKillRecipients( myId, conn, out );
			killConnections( pids, conn, out );
			restoreDatabase( conn );
			
			out.println( "Restored '" + _dbName + "' from '" + _backupName + "'." );
			out.println();
		}
		catch( SQLException e )
		{
			throw new ServletException( e );
		}
		finally
		{
			try { if( conn != null ) conn.close(); }
			catch( SQLException ignored ) {}
		}
	}
	private Connection getConnection()
	throws SQLException
	{
		Connection conn = DriverManager.getConnection( _url, _user, _password );
		conn.setAutoCommit( true );
		return conn;
	}
	private String getInitParameter( String name, ServletConfig config )
	throws ServletException
	{
		final String value = config.getInitParameter( name );
		if( value == null || value.length() < 1 )
			throw new ServletException( "Init parameter not specified: '" + name + "'." );
		else
			return value;
	}
	private int getProcessId( Connection conn )
	throws SQLException
	{
		final int id;
		
		final Statement stmt = conn.createStatement();
		
		final ResultSet rset = stmt.executeQuery( "SELECT @@SPID" );
		rset.next();
		
		id = rset.getInt( 1 );
		
		rset.close();
		stmt.close();

		return id;
	}
	public void init( ServletConfig config )
	throws ServletException
	{
		final String driver = getInitParameter( JDBC_DRIVER_INIT_PARAM, config );
		try { Class.forName( driver ); }
		catch( ClassNotFoundException e )
		{
			throw new ServletException( "JDBC Driver not found: '" + driver + "'.", e );
		}
		
		_ips = parseIps( config );

		_url      = getInitParameter( JDBC_URL_INIT_PARAM, config );
		_user     = getInitParameter( DB_USER_INIT_PARAM, config );
		_password = getInitParameter( DB_PASSWORD_INIT_PARAM, config );

		_dbName     = getInitParameter( DB_NAME_INIT_PARAM, config );
		_backupName = getInitParameter( BACKUP_NAME_INIT_PARAM, config );
	}
	private void killConnections( List pids, Connection conn, PrintWriter out )
	throws SQLException, IOException
	{
		final Statement stmt = conn.createStatement();
		for( Iterator i = pids.iterator(); i.hasNext(); )
		{
			Integer pid = (Integer) i.next();
			stmt.executeUpdate( "KILL " + pid );
			out.println( "...Killed process '" + pid + "'." );
		}
		stmt.close();
	}
	private List listKillRecipients( int id, Connection conn, PrintWriter out )
	throws SQLException, IOException
	{
		final List pids = new java.util.LinkedList();
		
		final Statement stmt = conn.createStatement();
		final ResultSet rset = stmt.executeQuery( "EXEC sp_who" );
		while( rset.next() )
		{
			int    processId = rset.getInt( "spid" );
			String dbName    = rset.getString( "dbname" );
			if( dbName != null ) dbName.trim();
			if( id == processId || !_dbName.equals( dbName ) )
				continue;

			String cmd = rset.getString( "cmd" );
			if( cmd != null ) cmd.trim();
			if( "AWAITING COMMAND".equals( cmd ) )
				pids.add( new Integer( processId ) );
			else
				out.println( "...Skipped process '" + processId + "' exeucting command '" + cmd + "'." );
		}
		rset.close();
		stmt.close();

		return pids;
	}
	private Set parseIps( ServletConfig config )
	throws ServletException
	{
		final Set set = new java.util.TreeSet();
		for( StringTokenizer tokens = new StringTokenizer( getInitParameter( IP_INIT_PARAM, config ), ",", false ); tokens.hasMoreTokens(); )
			set.add( tokens.nextToken() );
		return set;
	}
	private void restoreDatabase( Connection conn )
	throws SQLException
	{
		final Statement stmt = conn.createStatement();
		stmt.executeUpdate( "RESTORE DATABASE " + _dbName + " FROM " + _backupName );
		stmt.close();
	}
	protected void service( HttpServletRequest req, HttpServletResponse res )
	throws ServletException, IOException
	{
		if( !_ips.contains( req.getRemoteAddr() ) )
		{
			res.setStatus( HttpServletResponse.SC_FORBIDDEN );
			return;
		}
		
		res.setContentType( "text/plain" );
		final PrintWriter out = res.getWriter();
		out.println();

		final String event = req.getParameter( "event" );
		if( "backup".equals( event ) )
			doBackup( out );
		else if( "restore".equals( event ) )
			doRestore( out );

		showConfig( out );
		out.close();
	}
	private void showConfig( PrintWriter out )
	throws ServletException, IOException
	{
		showParam( IP_INIT_PARAM, _ips, "Comma-separated list of accepted IP addresses", out );
		
		showParam( JDBC_URL_INIT_PARAM, _url, "JDBC URL to 'master' database", out );
		showParam( DB_USER_INIT_PARAM, _user, "Database user name", out );
		showParam( DB_PASSWORD_INIT_PARAM, _password, "Database user password", out );
		
		showParam( DB_NAME_INIT_PARAM, _dbName, "Name of an existing database to be backed up/restored", out );
		showParam( BACKUP_NAME_INIT_PARAM, _backupName, "Name of exisiting logical backup device for backup/restore", out );
	}
	private void showParam( String name, Object value, String description, PrintWriter out )
	throws IOException
	{
		out.print( name );
		out.print( " = " );
		out.println( value );

		if( description != null )
		{
			out.print( '\t' );
			out.println( description );
		}
	}
}