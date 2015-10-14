package edu.ohiou.mfgresearch.labimp.spacesearch;
import java.sql.Statement;
import java.io.BufferedReader;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
 *
 *  Title:MakeDB
 *  Description:This class creates a databse of two tables namely Books table and
 *		   Authors table. It reads the properties file from hard disk, sets
 *		   the drivers and URL for the database.
 *  Copyright:  Copyright (c) 2002
 *  Company:
 *  @author
 *  @version 1.0
 * 
 */

public class MakeDB {

	public MakeDB() {
	}
	public static void main(String[] args) {
		try {
			Connection con = getConnection();
			Statement smt = con.createStatement();
			//smt.executeUpdate("DROP TABLE "+args[0]);
			String tableName = "";
			if (args.length > 0) {
				tableName = args[0];

				BufferedReader in = new BufferedReader(new FileReader(tableName
					+ ".dat"));
				//	createTable(tableName, in , smt);
				showTable(tableName, smt);
			} else {
				System.out.println("Usage: MakeDB TableName");
				System.exit(0);
			}
			//in.close();
			smt.close();
			con.close();
		} catch (SQLException ex) {
			System.out.println("SQLException:");
			while (ex != null) {
				System.out.println("SQL State : " + ex.getSQLState());
				System.out.println("Message: " + ex.getMessage());
				System.out.println("Vendor :" + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}//catch(SQLException)

		catch (IOException ioex) {
			System.out.println("IOException : " + ioex);
			ioex.printStackTrace();
		}//catch(IOException ioex)

		catch (Exception ex) {
			ex.printStackTrace();
		}
		//MakeDB makeDB1 = new MakeDB();
	}//main

	public static Connection getConnection()
		throws SQLException,
		IOException,
		ClassNotFoundException {

		Properties prop = new Properties();
		FileInputStream in = new FileInputStream("MakeDB.properties");
		prop.load(in);
		String driver = prop.getProperty("jdbc.drivers");
		if (driver != null)
			System.setProperty("jdbc.drivers", driver);
		//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		String url = prop.getProperty("jdbc.url");
		String uname = prop.getProperty("jdbc.username");
		String passwd = prop.getProperty("jdbc.password");
		return DriverManager.getConnection(url, uname, passwd);
	}

	public static void createTable(
		String tabName,
		BufferedReader in,
		Statement smt) {
		try {
			String line = in.readLine();
			smt.executeUpdate("CREATE TABLE " + tabName + "(" + line + ")");

			while ((line = in.readLine()) != null) {
				System.out.println("" + line);
				//          smt.executeUpdate("INSERT INTO " +tabName + " VALUES " + "("+line+")" );
				//		    smt.addBatch("INSERT INTO "+tabName+" VALUES " + "( "+line+" )");
			}
			smt.executeBatch();
			smt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showTable(String name, Statement smt)
		throws SQLException {

		String query = "SELECT * FROM " + name;
		ResultSet rs = smt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= columns; i++) {
				if (i > 1)
					System.out.print(",");
				// if(rs.getString(i).trim().equals(""))
				//System.out.print("NOT AVAILABLE");
				System.out.print(rs.getString(i).trim());
			}
			System.out.println();
		}
		rs.close();
	}
}