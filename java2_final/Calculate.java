package pro_final;

import java.util.ArrayList;

public class Calculate {
	private String startDate;
	private String endDate;
	private double minMag;
	private String region;
	ArrayList<earthQuack> list;
	ArrayList<earthQuack> Data;
	
	public Calculate() {
		Read r=new Read();
		list=(ArrayList<earthQuack>) r.getList();
		Data=new ArrayList<earthQuack>();
		this.startDate=null;
		this.endDate=null;
		this.minMag=0;
		this.region=null;
	}

	public Calculate(String startDate,String endDate,double minMag,String region) {
		Read r=new Read();
		list=(ArrayList<earthQuack>) r.getList();
		Data=new ArrayList<earthQuack>();
		this.startDate=startDate;
		this.endDate=endDate;
		this.minMag=minMag;
		this.region=region;
		
	}
	
	//璁＄畻闇�瑕佸睍绀虹殑鍏ㄩ儴鏁版嵁
	public ArrayList<earthQuack> dataSource(){		
		
		Read r = new Read();
		ArrayList<earthQuack>list= (ArrayList<earthQuack>) r.getList();
		ArrayList<earthQuack> list2=new ArrayList<earthQuack>();	
		
		int startYear=Integer.parseInt(startDate.substring(0,4));
		int endYear=Integer.parseInt(endDate.substring(0,4));
		int startMonth=Integer.parseInt(startDate.substring(5,7));
		int endMonth=Integer.parseInt(endDate.substring(5,7));
		int startDay=Integer.parseInt(startDate.substring(8,10));
		int endDay=Integer.parseInt(endDate.substring(8,10));
		
		for(earthQuack q : list) {
			int year=Integer.parseInt(q.getDate().substring(1,5));
			int month=Integer.parseInt(q.getDate().substring(6,8));
			int day=Integer.parseInt(q.getDate().substring(9,11));
			
			if(year>startYear && year<endYear) {
				if(q.getMagnitude()>=minMag) {
					if( region==null || (region!=null&&q.getRegion().equals(region))) {
						list2.add(q);
					}
				}
			}
			else if(year==startYear) {
				if(month>startMonth) {
					if(q.getMagnitude()>=minMag) {
						if( region==null || (region!=null&&q.getRegion().equals(region))) {
							list2.add(q);
						}
					}
				
				}else if(month==startMonth) {
					if(day>=startDay) {
						if(q.getMagnitude()>=minMag) {
							if( region==null || (region!=null&&q.getRegion().equals(region))) {
								list2.add(q);
							}
						}
					}
				}
			}else if (year==endYear) {
				if(month<endMonth) {
					if(q.getMagnitude()>=minMag) {
						
						if( region==null || ((region!=null)&&(q.getRegion().equals(region)))) {
							list2.add(q);
						}
					}
				}
				}else if(month==endMonth) {
					if(day<=endDay) {
						if(q.getMagnitude()>=minMag) {
							if( region==null || (region!=null&&q.getRegion()==region)) {
								list2.add(q);
							}
						}
				}
		   }
		}		
		Data=list2;
		return list2;
	}
	
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
			num=num+a.charAt(6)+a.charAt(7);
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
		
		ArrayList<String> b=new ArrayList<String>();
		for(earthQuack x :list) {
			String s=x.getRegion();
			if(!b.contains(s)) {
				b.add(s);
			}
		}
		return b;
	}
	

}
