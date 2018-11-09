package pro_final;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;

public  class mainUI extends Application {
	int flag=-1;
	TableView<earthQuack> Table;
	GraphicsContext gc;  
	ArrayList<earthQuack> resource;
	Calculate2 c;
	ImageView image;
	Canvas canvas;
	StackedBarChart<String, Number> sbc;
	Label response; 
	LineChart<String,Number> lineChart;
	CategoryAxis xAxis1;
	CategoryAxis xAxis;
	NumberAxis yAxis1;
	
	public static void main(String[] args) {
		launch(args);
	}
	public void start(Stage arg0) throws Exception {
		//閫夋嫨椤?
		Stage primaryStage =new Stage();
		primaryStage.setTitle("EarthQuake Searching");
        Group root = new Group();
        Scene scene = new Scene(root, 900, 500);
        //绗竴琛?
        double x = 40;
        double y = 40;
        Text text = new Text(x, y, "From");
        text.setFont(Font.font(14));
        root.getChildren().add(text);
        
        Text text2 = new Text(x+scene.getWidth()/2,y,"To");
        text2.setFont(Font.font(14));
        root.getChildren().add(text2);
        
        DatePicker startDate = new DatePicker();
        startDate.setLayoutX(x+50);
        startDate.setLayoutY(y-15);
        root.getChildren().add(startDate);
        
        DatePicker endDate = new DatePicker();
        endDate.setLayoutX(x+30+scene.getWidth()/2);
        endDate.setLayoutY(y-15);
        root.getChildren().add(endDate);
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        
        //绗簩琛?
        y=y+50;
        Text magnitude = new Text(x-30, y, "Magnitude>=");
        magnitude.setFont(Font.font(14));
        root.getChildren().add(magnitude);
		
        Text region = new Text(x-30+scene.getWidth()/2,y,"Region");
        region.setFont(Font.font(14));
        root.getChildren().add(region);
        
        Slider slider=new Slider(0,10,0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(2);//闅斿嚑涓�煎垎涓诲埢搴?
        slider.setMinorTickCount(1);//涓诲埢搴︿箣闂村垎鍑犱釜
        slider.setLayoutX(startDate.getLayoutX()+20);
        slider.setLayoutY(y-10);
        root.getChildren().add(slider);
        
        ObservableList<String> options = FXCollections.observableArrayList();
        //灏嗛�夐」鍒楄〃鍖呰鍒癘bservableList涓紝鐒跺悗浣跨敤observable鍒楄〃瀹炰緥鍖朇omboBox绫汇�?
        ComboBox<String> myComboBox = new ComboBox<String>(options);
//        Calculate c1=new Calculate();
        Calculate2 c1=new Calculate2();
        ArrayList<String> b= c1.getRegion();
        for (String b1 :b) {
        myComboBox.getItems().add(b1);//娣诲姞鏂板�?
        }
        myComboBox.getItems().add(null);
        myComboBox.setEditable(true);
        myComboBox.setPromptText("--------");
        myComboBox.setLayoutX(endDate.getLayoutX());
        myComboBox.setLayoutY(y-10);
        root.getChildren().add(myComboBox);
        
      //Button
        Button search = new Button();
        search.setText(" Search ");
        search.setLayoutY(y+60);
        search.setLayoutX(scene.getWidth()/2-40);
        search.setStyle("-fx-font: 16 arial;");
        root.getChildren().add(search);
        
        y=y+120;
        Button data = new Button("Data");
        Button Map1 =new Button("Map");
        Button mag = new Button("M D");
        Button date = new Button("Time scale");
        data.setLayoutY(y+20);
        data.setLayoutX(20);
        data.setStyle("-fx-font: 14 arial;");
        Map1.setLayoutY(y+55);
        Map1.setLayoutX(20);
        Map1.setStyle("-fx-font: 14 arial;");
        mag.setLayoutY(y+90);
        mag.setLayoutX(20);
        mag.setStyle("-fx-font: 14 arial;");
        date.setLayoutY(y+125);
        date.setLayoutX(20);
        date.setStyle("-fx-font: 14 arial;");  
        
        //鎺т欢
        response =new Label("");
        response.setLayoutX(scene.getWidth()-160);
        response.setLayoutY(y-50);
        root.getChildren().add(response);
        y=y+90;
        
        
        Table = new TableView<earthQuack>();
        Table.setPrefHeight(260);
        Table.setPrefWidth(600);
        Table.setLayoutX(200);
        Table.setLayoutY(190);
        Table.setVisible(false);
        root.getChildren().add(Table);
        
        Image myImage = new Image("file:///C:/Users/Dreamy/Desktop/Mercator.jpg");
		 image=new ImageView(myImage);
		 image.setLayoutX(Table.getLayoutX());
		 image.setLayoutY(Table.getLayoutY());
		 image.setFitHeight(Table.getPrefHeight());
		 image.setFitWidth(Table.getPrefWidth());
		 image.setVisible(false);
		 root.getChildren().add(image);
		
//		 canvas=new Canvas();
//		 gc=canvas.getGraphicsContext2D();
//		 gc.setStroke(Color.RED);
//	     canvas.setHeight(image.getFitHeight());
//	     canvas.setWidth(image.getFitWidth());
//	     canvas.setLayoutX(image.getLayoutX());
//		 canvas.setLayoutY(image.getLayoutY());
//		 canvas.setVisible(false);
//		 root.getChildren().add(canvas);
		 
		 	xAxis1 = new CategoryAxis();
	        yAxis1 = new NumberAxis();
	        sbc =new StackedBarChart<>(xAxis1, yAxis1);
	        sbc.setTitle("Magnitude Distributation:");
	         sbc.setLayoutX(image.getLayoutX());
	         sbc.setLayoutY(image.getLayoutY());
	         sbc.setPrefWidth(image.getFitWidth());
	         sbc.setPrefHeight(image.getFitHeight());
	         sbc.setVisible(false);
	         root.getChildren().add(sbc);
	         
	         	xAxis = new CategoryAxis();
	         	NumberAxis yAxis = new NumberAxis();
		        lineChart = new LineChart<String,Number>(xAxis,yAxis);
		         lineChart.setTitle("Time Scaling"); 
    	         lineChart.setLayoutX(sbc.getLayoutX());
    	         lineChart.setLayoutY(sbc.getLayoutY());
    	         lineChart.setPrefHeight(sbc.getPrefHeight());
    	         lineChart.setPrefWidth(sbc.getPrefWidth());
	         lineChart.setVisible(false);
	         root.getChildren().add(lineChart);

	         //鎸夐挳鍙戠敓浜嬩欢
	         search.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						//娴嬭瘯
			            System.out.println(startDate.getValue().toString());
			            System.out.println(endDate.getValue());
			            System.out.println(slider.getValue());
			            System.out.println(myComboBox.getValue());
			            
			           String a="";
			           String b="";
			           for (int i=0;i<4;i++) {
			        	   a=a+startDate.getValue().toString().charAt(i);
			        	   b=b+endDate.getValue().toString().charAt(i);
			           }
			           int start= Integer.parseInt(a);
			           int end = Integer.parseInt(b);
			           if (end - start==0) {//涓�骞撮噷
			        	   flag=0;
			           }else {
			        	   flag=1;
			           }
			       //search   
			          Search(startDate.getValue().toString(),endDate.getValue().toString(),slider.getValue(),myComboBox.getValue());
			          
			         root.getChildren().remove(canvas);
			         canvas=new Canvas();
			         GraphicsContext  gc=canvas.getGraphicsContext2D();
			 		 gc.setStroke(Color.RED);
			 	     canvas.setHeight(image.getFitHeight());
			 	     canvas.setWidth(image.getFitWidth());
			 	     canvas.setLayoutX(image.getLayoutX());
			 		 canvas.setLayoutY(image.getLayoutY());
					 ArrayList<Coor> clist=c.CoorData(canvas.getWidth(),canvas.getHeight());
					 for(Coor x : clist) {
						 gc.strokeOval(x.getX(), x.getY(), 15, 10);
				        }
					 root.getChildren().add(canvas);
					 
			           Table.setVisible(true);
			           image.setVisible(false);
			           canvas.setVisible(false);
			           sbc.setVisible(false);
			           lineChart.setVisible(false);
					}
	        });
	         data.setOnAction(new EventHandler<ActionEvent>() {
	 			@Override
	 			public void handle(ActionEvent event) {
	 			Table.setVisible(true);
	 		       image.setVisible(false);
	 		       canvas.setVisible(false);
	 		       sbc.setVisible(false);
	 				lineChart.setVisible(false);
	 			}
	         });
	         
	         Map1.setOnAction(new EventHandler<ActionEvent>() {
	 			@Override
	 			public void handle(ActionEvent event) {
	 			canvas.setVisible(true); 
	 			sbc.setVisible(false);
	 			 Table.setVisible(false);
	 	        image.setVisible(true);
	 			lineChart.setVisible(false);
	 	        
	 			}
	         });	         
	         mag.setOnAction(new EventHandler<ActionEvent>() {
	 			@Override
	 			public void handle(ActionEvent event) {
	 				canvas.setVisible(false);
	 				image.setVisible(false);
	 				Table.setVisible(false);
	 				lineChart.setVisible(false);
	 				sbc.setVisible(true);
	 			}});	         

	         date.setOnAction(new EventHandler<ActionEvent>() {
	         	public void handle(ActionEvent event) {
	         		canvas.setVisible(false);
	 				image.setVisible(false);
	 				Table.setVisible(false);
	 				sbc.setVisible(false);
	 				lineChart.setVisible(true);
	 				System.out.println(flag);
	         }
	         });	         
	         
	         
	         root.getChildren().addAll(data,Map1,mag,date);
	         primaryStage.setScene(scene);
	         primaryStage.show();
        
		
	}
	
@SuppressWarnings({ "rawtypes", "unchecked" })
private void Search(String a, String b, double c, String d) {
		this.c=new Calculate2(a,b,c,d);
		resource=null;
		resource=this.c.dataSource();
		
		
        TableColumn idCol = new TableColumn("Id");
        TableColumn dateCol = new TableColumn("UTC_data");
        TableColumn latitudeCol = new TableColumn("Latitude");
        TableColumn longtitudeCol = new TableColumn("Longitude");
        TableColumn magnitudeCol = new TableColumn("magnitude");
        TableColumn depthCol = new TableColumn("Depth");
        TableColumn regionCol = new TableColumn("Region");
        idCol.setCellValueFactory(
        	    new PropertyValueFactory<>("id")
        	);
        dateCol.setCellValueFactory(
        	    new PropertyValueFactory<>("date")
        	);
       latitudeCol.setCellValueFactory(
    		   new PropertyValueFactory<>("latitude")
        	);
       
       longtitudeCol.setCellValueFactory(
        	    new PropertyValueFactory<>("longitude")
        	);
       magnitudeCol.setCellValueFactory(
        	    new PropertyValueFactory<>("magnitude")
        	);
       depthCol.setCellValueFactory(
        	    new PropertyValueFactory<>("depth")
        	);
       regionCol.setCellValueFactory(
        	    new PropertyValueFactory<>("region")
        	);

       ObservableList<earthQuack> oListStavaka = FXCollections.observableArrayList();
       for (earthQuack stavka : resource) {
           oListStavaka.add(stavka);
       }
        Table.getItems().clear();
        Table.setItems(oListStavaka);
        Table.getColumns().clear();
        Table.getColumns().addAll(idCol, dateCol, latitudeCol,longtitudeCol,depthCol,magnitudeCol, regionCol);

	        xAxis1.setLabel("Magnitude");
	        yAxis1.setLabel("Quantity");
	         XYChart.Series<String, Number> series1 =new XYChart.Series<>();
	        series1.setName("number of Quacks");
	        int[] listn=this.c.magData();
	        
	        sbc.setTitle("Magnitude Distributation:");
	        	
		//鏁版嵁
	    
        series1.getData().add(new XYChart.Data<>("0-1", listn[0]));
        series1.getData().add(new XYChart.Data<>("1-2", listn[1]));
        series1.getData().add(new XYChart.Data<>("2-3", listn[2]));
        series1.getData().add(new XYChart.Data<>("3-4", listn[3]));
        series1.getData().add(new XYChart.Data<>("4-5", listn[4]));
        series1.getData().add(new XYChart.Data<>("5-6", listn[5]));
        series1.getData().add(new XYChart.Data<>("6-7", listn[6]));
        series1.getData().add(new XYChart.Data<>("7-8", listn[7]));
        series1.getData().add(new XYChart.Data<>(">8", listn[8]));
        sbc.getData().clear();
        sbc.getData().add(series1);
	        
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
        	lineChart.getData().clear();
	         if(flag==0) {//涓�骞村唴锛岀敤鏈堜唤浣滀负scale
	        	 XYChart.Series series = new XYChart.Series(); 
    			 xAxis.setLabel("Month");
    	         int[] a1 = this.c.datePre();
    	         series.setName("number of EarthQuacks");
    	         series.getData().add(new XYChart.Data("Jan", a1[0]));
    	         series.getData().add(new XYChart.Data("Feb", a1[1]));
    	         series.getData().add(new XYChart.Data("Mar", a1[2]));
    	         series.getData().add(new XYChart.Data("Apr", a1[3]));
    	         series.getData().add(new XYChart.Data("May", a1[4]));
    	         series.getData().add(new XYChart.Data("Jun", a1[5]));
    	         series.getData().add(new XYChart.Data("Jul", a1[6]));
    	         series.getData().add(new XYChart.Data("Aug", a1[7]));
    	         series.getData().add(new XYChart.Data("Sep", a1[8]));
    	         series.getData().add(new XYChart.Data("Oct", a1[9]));
    	         series.getData().add(new XYChart.Data("Nov", a1[10]));
    	         series.getData().add(new XYChart.Data("Dec", a1[11]));
    	         lineChart.getData().add(series);
    	         
    		}
    		else if(flag==1) {//瓒呰繃涓�骞达紝鐢ㄥ勾浠戒綔涓簒杞?
    			XYChart.Series series2 = new XYChart.Series<Integer,Integer>(); 
    			 xAxis.setLabel("Year");
    			 ArrayList<Coor> a2 =this.c.datePre2();
    			 for (Coor ax:a2) {
    				 System.out.println(ax.getX()+"\t"+ax.getY());
    				 series2.getData().add(new XYChart.Data(Integer.toString((int)ax.getX()),ax.getY()));
    			 }
    			 lineChart.getData().clear();
    	         lineChart.getData().add(series2); 
    		}
	         
	}

@Override
public void stop() {
	System.out.println("Application stoped.");
}

	
	
}
