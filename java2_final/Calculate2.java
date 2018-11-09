package pro_final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Calculate2 {
	private String startDate;
	private String endDate;
	private double minMag;
	private String region;
	private ArrayList<earthQuack> Data;
	private Connection  con = null;
	
	public Calculate2() {
	}
	
	public Calculate2(String startDate,String endDate,double minMag,String region) {
		this.startDate=startDate;
		this.endDate=endDate;
		this.minMag=minMag;
		this.region=region;
		Data=new  ArrayList<earthQuack>();
	}
	

	//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	public ArrayList<earthQuack> dataSource(){	
		int id=0;
		String date="";
		double la=0;
		double lo=0;
		int depth=0;
		double mag=0;
		String reg="";
		
		MaxDate m=new MaxDate();
		long start=m.IntDate(startDate);
		long end=m.IntDate(endDate);
		
		ArrayList<earthQuack> list2=new ArrayList<earthQuack>();	
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
			//Should look for min/max dates
			PreparedStatement stmt = null;
			ResultSet         rs = null;
			
			long now=start;
			while(now<=end){
				String date_now=String.valueOf(now);
				date_now=date_now.substring(0, 4)+"-"+date_now.substring(4,6)+"-"+date_now.substring(6,8);
				String sql="select id, UTC_date, latitude, longitude, depth, magnitude, region from quakes"
						+ " where substr(UTC_date,1,10)=? and magnitude>=?";
				
				if(region!=null){
					sql=sql+" and region=?";
					stmt = con.prepareStatement(sql);
					stmt.setString(1, date_now);
					stmt.setDouble(2,minMag);
					stmt.setString(3, region);
				}else{
					stmt = con.prepareStatement(sql);
					stmt.setString(1, date_now);
					stmt.setDouble(2,minMag);
				}
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					id=rs.getInt(1);
					date=rs.getString(2);
					la=rs.getDouble(3);
					lo=rs.getDouble(4);
					depth=rs.getInt(5);
					mag=rs.getDouble(6);
					reg=rs.getString(7);
					earthQuack e=new earthQuack(id,date,la,lo,depth,mag,reg);
					list2.add(e);
				}
				
				if(date_now.substring(6,8)=="31"){
					if(date_now.substring(4,6)=="12"){
						now=now+10000-1130;
					}else{
						now=now+100-30;
					}
				}else{
					now=now+1;
				}
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
		
		this.Data=list2;
		return list2;
	}

//	public static void main(String[]arges){
//		Calculate2 c=new Calculate2("2017-12-26","2017-12-27",0,null);
//		ArrayList<earthQuack> a=c.dataSource();
//		for(earthQuack e:a){
//			System.out.println(e.getDate());
//		}
//	}


	//璁＄畻map闇�瑕佺殑鏁版嵁
	public ArrayList<Coor> CoorData(double width,double hight){
		ArrayList<Coor> clist1=new ArrayList<Coor>();
		
		for(earthQuack a : Data) {
			clist1.add(MillierConvertion(a.getLatitude(),a.getLongitude()));
		}
		double maxY=MillierConvertion(-90,0).getY()-MillierConvertion(90,0).getY();
		double maxX=MillierConvertion(0,180).getX()-MillierConvertion(0,-180).getX();

		ArrayList<Coor> clist=new ArrayList<Coor>();
		for (Coor x : clist1) {
			clist.add(new Coor((x.getX()/maxX)*width,(x.getY()/maxY)*hight));
		}
		
		return clist;
		
	}
	
	//璁＄畻mag闇�瑕佺殑鏁版嵁
	public int[] magData(){
		int[] a=new int[9];
		for(earthQuack x : Data) {
			if(x.getMagnitude()<=1) {
				a[0]++;
			}
			else if(x.getMagnitude()<=2) {
				a[1]++;
			}
			else if(x.getMagnitude()<=3) {
				a[2]++;
			}
			else if(x.getMagnitude()<=4) {
				a[3]++;
			}
			else if(x.getMagnitude()<=5) {
				a[4]++;
			}
			else if(x.getMagnitude()<=6) {
				a[5]++;
			}
			else if(x.getMagnitude()<=7) {
				a[6]++;
			}
			else if(x.getMagnitude()<=8) {
				a[7]++;
			}
			else {
				a[8]++;
			}
			
		}
		
		return a;
	}
	//璁＄畻TS闇�瑕佺殑鏁版嵁,鏍规嵁鏈堜唤
	public int[] datePre() {
		int[] res=new int[12];
		
		for (earthQuack x: Data) {
			String a=x.getDate();
			String num="";
			num=num+a.charAt(5)+a.charAt(6);
			int num2=Integer.parseInt(num);
			res[num2-1]++;
		}
		return res;
	}
	//璁＄畻TS闇�瑕佺殑鏁版嵁,鏍规嵁骞翠唤
	public ArrayList<Coor> datePre2() {
		ArrayList<Coor> list=new ArrayList<Coor>();
		ArrayList<Integer> yL=new ArrayList<Integer>();
		ArrayList<Integer> nL=new ArrayList<Integer>();
		
		for(earthQuack x : Data) {
			int year=Integer.parseInt(x.getDate().substring(1,5));
			
			if(!yL.contains(year)) {
				yL.add(year);
				nL.add(0);
			}else {
				int des=yL.indexOf(year);
				int sum=nL.get(des)+1;
				nL.set(des,sum);
			}
		}
		for (int y:yL) {
			list.add(new Coor(y,nL.get(yL.indexOf(y))));
		}
		
		return list;
	}
	
	//绫冲嫆鍧愭爣绯绘姇褰�
	public Coor MillierConvertion(double lat, double lon){
		     double L = 6381372 * Math.PI * 2;//鍦扮悆鍛ㄩ暱
		     double W=L;// 骞抽潰灞曞紑鍚庯紝x杞寸瓑浜庡懆闀�
		     double H=L/2;// y杞寸害绛変簬鍛ㄩ暱涓�鍗�
		     double mill=2.3;// 绫冲嫆鎶曞奖涓殑涓�涓父鏁帮紝鑼冨洿澶х害鍦ㄦ璐�2.3涔嬮棿
		     double x = lon * Math.PI / 180;// 灏嗙粡搴︿粠搴︽暟杞崲涓哄姬搴�
		     double y = lat * Math.PI / 180;// 灏嗙含搴︿粠搴︽暟杞崲涓哄姬搴�
		     y=1.25 * Math.log( Math.tan( 0.25 * Math.PI + 0.4 * y ) );// 绫冲嫆鎶曞奖鐨勮浆鎹�
		     // 寮у害杞负瀹為檯璺濈
	         x = ( W / 2 ) + ( W / (2 * Math.PI) ) * x;
	         y = ( H / 2 ) - ( H / ( 2 * mill ) ) * y;
	         Coor result=new Coor(x,y);
	         return result;
		}

	//region selection calculation 
	public ArrayList<String> getRegion() {
		int id=0;
		String date="";
		double la=0;
		double lo=0;
		int depth=0;
		double mag=0;
		String reg="";
		
		ArrayList<earthQuack>list2=new ArrayList<earthQuack>();
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
			//Should look for min/max dates
			PreparedStatement stmt = null;
			ResultSet         rs = null;
			
			String sql="select id, UTC_date, latitude, longitude, depth, magnitude, region from quakes";
			
			stmt = con.prepareStatement(sql);
			
			rs = stmt.executeQuery();
				
			while (rs.next()) {
				id=rs.getInt(1);
				date=rs.getString(2);
				la=rs.getDouble(3);
				lo=rs.getDouble(4);
				depth=rs.getInt(5);
				mag=rs.getDouble(6);
				reg=rs.getString(7);
				earthQuack e=new earthQuack(id,date,la,lo,depth,mag,reg);
				list2.add(e);
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
	
		ArrayList<String> b=new ArrayList<String>();
		for(earthQuack x :list2) {
			String s=x.getRegion();
			if(!b.contains(s)) {
				b.add(s);
			}
		}
		return b;
	}
	

}
