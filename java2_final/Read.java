package pro_final;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Read {
	private static List<earthQuack> eqs;
	
	public void info(){
		eqs=new ArrayList<earthQuack>();
        File csv = new File("earthquakes.csv");  // CSV
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        String line = "";
        try {
        	line = br.readLine();
//        	String[] s=line.split(",");
//        	for(int i=0;i<s.length;i++){
//
//            	System.out.println(s[i*]);
//        	}
            while ((line = br.readLine()) != null){  
            	String[] s=line.split(",");
            	int id=Integer.parseInt(s[0]);
            	String date=s[1];
            	double la=Double.parseDouble(s[2]);
            	double lo=Double.parseDouble(s[3]);
            	int depth=Integer.parseInt(s[4]);
            	double mag=Double.parseDouble(s[5]);
            	String region=s[6];
            	earthQuack eq=new earthQuack(id,date,la,lo,depth,mag,region);
            	this.eqs.add(eq);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	public List<earthQuack> getList(){
		info();
		return this.eqs;
	}
//	public static void main(String[]arges){
//		Read r=new Read();
//		r.info();
//		for(earthQuack e:eqs){
//			System.out.println(e.toString());
//		}
//	}

}
