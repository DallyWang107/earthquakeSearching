package pro_final;

public class earthQuack {
	private int id;
	private String date;
	private double latitude;
	private double longitude;
	private double magnitude;
	private int depth;
	private String region;
	
	public earthQuack(int id,String date,double latitude,double longitude,int depth,double magnitude,String region) {
		this.id=id;
		this.date=date;
		this.latitude=latitude;
		this.magnitude=magnitude;
		this.longitude=longitude;
		this.region=region;
		this.depth=depth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	public String toString() {
		return (this.date+" "+ this.id+""+this.latitude+" "+this.longitude+" "+this.magnitude+" "+this.depth+" "+this.region);
	}
	
}
