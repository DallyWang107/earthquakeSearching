package pro_final;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class Read_Scraping {
	private static Connection  con = null;

    public void Write_DB()throws Exception {
    	int id=0;
		String date="";
		double la=0;
		double lo=0;
		int depth=0;
		double mag=0;
		String region="";
		
		MaxDate md=new MaxDate();
		String date_max_S=md.maxDate();
		long date_max=md.IntDate(date_max_S);
		long date_now=date_max+1;
		
		String path="earthquakes-1.sqlite";
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:" + path);
		con.setAutoCommit(true);
		PreparedStatement stmt = null ;
		ResultSet rs = null;
		
		String page="1";
		String web0="https://www.emsc-csem.org/Earthquake/?view=";
		outer:while(date_now>date_max){
			String web=web0+page;
			int p=1;
			//web scraping
			Document doc = Jsoup
			          .connect(web)
			          .get();
			Elements tables = doc.select("table");
			outFor:for (Element t: tables) {
				Elements rows = t.select("tbody tr");
				for (Element r: rows) {
					if(p==50){
						break outFor;
					}
					Elements cells_date = r.select("td.tabev6");
					Elements cells_lalo = r.select("td.tabev1");
					Elements cells_nsewmg = r.select("td.tabev2");
					Elements cells_depth = r.select("td.tabev3");
					Elements cells_region = r.select("td.tb_region");
					if (cells_date.size() > 0) {
						Element date_ = cells_date.get(0).select("a").get(0);
						Element la_=cells_lalo.get(0);
			            Element lo_=cells_lalo.get(1);
			            Element ns_=cells_nsewmg.get(0);
			            Element ew_=cells_nsewmg.get(1);
			            Element mag_=cells_nsewmg.get(2);
			            Element dep_=cells_depth.get(0);
			            Element reg_=cells_region.get(0);
			            
			            id=Integer.parseInt(r.id());
			            date=date_.text();
			            mag=Double.parseDouble(mag_.text());
			            depth=Integer.parseInt(dep_.text());
			            region=reg_.text();
			            if(ns_.text().equals("N")){
			            	la=Double.parseDouble(la_.text());
			            }else{
			            	la=-Double.parseDouble(la_.text());
			            }
			            if(ew_.text().equals("E")){
			            	lo=Double.parseDouble(lo_.text());
			            }else{
			            	lo=-Double.parseDouble(lo_.text());
			            }

			            System.out.println(date+"\t"+r.id()+"\t"+ew_.text() + "\t"+lo+"\t"+region);
			            
			            date_now=md.IntDate(date);
			            if(date_now<=date_max){
			            	break outer;
			            }
			              
			              //insert into DB
		      			stmt = con.prepareStatement("insert into quakes "
		      					+ "(id, UTC_date, latitude, longitude, depth, magnitude, region, area_id) "
		      					+ "values (?,?,?,?,?,?,?,?)");
		      			stmt.setInt(1, id);
		      			stmt.setString(2, date);
		      			stmt.setDouble(3, la);
		      			stmt.setDouble(4, lo);
		      			stmt.setInt(5, depth);
		      			stmt.setDouble(6, mag);
		      			stmt.setString(7, region);
		      			stmt.setInt(8, 0);
		      			stmt.executeUpdate();
					}
				}
	        }
			int page_i=Integer.parseInt(page)+1;
			page=String.valueOf(page_i);
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
    }

    public static void main(String[]arges) throws Exception{
    	Read_Scraping r=new Read_Scraping();
    	r.Write_DB();
    }
}
