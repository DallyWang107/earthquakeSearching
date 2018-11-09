package pro_final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MaxDate {
	private Connection  con = null;
	private String date="";
	public String maxDate(){
		// get the maximum date from DB
		String date_max="";
		String path="earthquakes-1.sqlite";
        try {
		    Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
		    System.err.println("Cannot find the driver.");
		    System.exit(1);
		}
		try {
			con = DriverManager.getConnection("jdbc:sqlite:" + path);
			con.setAutoCommit(false);
			PreparedStatement stmt;
			ResultSet         rs;
			stmt = con.prepareStatement("select max(UTC_date) from quakes");
			rs = stmt.executeQuery();
			while (rs.next()) {
				this.date=rs.getString(1);
			}
			if (rs != null) {
	            rs.close();
	        }
	        if (stmt != null) {
	            stmt.close();
	        }
	        if (con != null) {
	            con.close();
	        }
		} catch (Exception e) {
		    System.err.println(e.getMessage());
		    System.exit(1);
		}
		return this.date;
	}
	public long IntDate(String date){
		long d=0;
		String d_s="";
		char[] D=date.toCharArray();
		for(int i=0;i<D.length;i++){
			if(Character.isDigit(D[i])){
				d_s=d_s+D[i];
			}
		}
		d=Long.parseLong(d_s);
		return d;
	}
	
	public static void main(String[]arges){
		MaxDate m=new MaxDate();
		String d=m.maxDate();
		System.out.print(d);
	}

}
