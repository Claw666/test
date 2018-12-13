package application;

import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application; 
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle; 
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.util.HashSet;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.Random;

class ColCircle // will contain the x,y and rad values for the circles
			{
			int x;
			int y;
			int rad = 20;
			}

         
public class test10 extends Application { 
   private int[] colorSol;
   private int width;
   private int height;
   private int[][] adjacency;
   private int edges;
   private static int Nvertices;
   private static ColCircle[] vertices;
   private dragNode[] arr;
   private Line[] lines; 
   private Text[] Text;
   private ToggleGroup tg;
   private int CN;
   private boolean finished = false;
   private ToggleButton hint;
   private ToggleButton highlight;
   private  Color [] color;
   private int pressed;
   private ToggleButton[] togle;
   private int Mode;
   private int[] NodeOrder;
   private int currValue = 0;
   
   
   //Timer variables
   private static final Integer STARTTIME = 15;
   private Timeline timeline;
   private Label timerLabel = new Label();
   // Make timeSeconds a Property
   private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

   //constructor for the random generator
   public test10(int[][] matrix,int vertices,int Edges,int mode) {
	   adjacency = matrix;
	   edges= Edges;
	   Nvertices = vertices;
	   Mode = mode;
   }
   
   public void start(Stage stage) { 

	   //Creating a root group
	   Group root = new Group();
	      
	   VBox menuBar = new VBox();
	   BorderPane menuBarAlignment = new BorderPane();
	   menuBar.getChildren().addAll(menuBarAlignment);
	   menuBar.setAlignment(Pos.BOTTOM_CENTER);
	   menuBar.setStyle("-fx-background-color: #e5e5e5;");
	   menuBar.setMinWidth(300);
	   
	   VBox colorleft = new VBox(8);
	   VBox colorright = new VBox(8);
	   
	   HBox colorCont = new HBox(8);
	   colorCont.setAlignment(Pos.TOP_CENTER);
	   menuBarAlignment.setCenter(colorCont);
	   colorCont.setPadding(new Insets(50,50,50,50));
	   VBox subMenus = new VBox();
	   menuBarAlignment.setBottom(subMenus);
	   BorderPane layoutAlignment = new BorderPane();
	   
	   
	   color=new Color[12];
	   Random RND = new Random();
	   for(int i=0; i<12; i++) {
		   int red = RND.nextInt(13)*20;
		   int green = RND.nextInt(13)*20;
		   int blue = RND.nextInt(13)*20;
		   Color colo = Color.rgb(red, green, blue);
		   color[i] = colo;
	   }
	   
	   tg = new ToggleGroup();
	   togle=new ToggleButton[color.length];
	   //Creatubg toggle buttons for the collers based on the chromatic number
	   for(int i=0; i<12; i++){
		   
		   togle[i]=new ToggleButton();
		   if (i==0) togle[0].setSelected(true);
		   
		   //Give the togle userData
		   togle[i].setUserData(color[i]);
		   //add the togle to the group
		   togle[i].setToggleGroup(tg);
		   //togle.setStyle("-fx-base: "+name[i]);
		   
		   String colo2 = color[i].toString().substring(2);
		   togle[i].setStyle("-fx-background-color: #"+colo2);
		   
		   togle[i].setMinSize(90,90);
		   if(i<6) {
			   colorright.getChildren().add(togle[i]);
			   }
		   else {
			   colorleft.getChildren().add(togle[i]);
			   }
		   }
	   colorCont.getChildren().addAll(colorleft,colorright);
	   
	   //boolean matrix to check which edges have been used or not
   	   boolean[][] edgesstate = new boolean[Nvertices][Nvertices];
   	   
   	   //make the class where the starting coordinates will be stored in
   	   ColCircle vertices[] = null;
   	   
   	   //class where the dragable nodes will be in
   	   arr = null;
   	   
   	   //class where the numbers of the vertices for make 3 will be in
   	   Text = null;
   	   
   	   //declare that vertices is  new array of the class ColCircle with length of the amount of vertices
   	   vertices = new ColCircle[Nvertices];
   	   
   	   //declare that arr is a new array of the class dragNode with length of the amount of vertices
   	   arr = new dragNode[Nvertices];
   	   
   	   //declare that Text is a new array of the class fixedText with length of the amount of vertices
   	   Text = new fixedText[Nvertices];
   	   
   	   //get the width and height of the stage
       width = (int) stage.getWidth();
       height = (int) stage.getHeight();
   	   
       //calculate the angle that has to be between every vertices in the center
   	   double angle = 2*Math.PI/Nvertices;
   	   // giving x and y values to the circles
   	   for(int i=0; i<Nvertices;i++){
   	   	   vertices[i] = new ColCircle();
   	   	   vertices[i].x = (int)(height/3.0*Math.cos(i*angle)+width/3.0);
   	   	   vertices[i].y = (int)(height/3.0*Math.sin(i*angle)+height/2.0);
   	   }   	     	   
      
      //make arrays to store the DoubleProperty values      
      DoubleProperty[] startX = new SimpleDoubleProperty[Nvertices];
      DoubleProperty[] startY = new SimpleDoubleProperty[Nvertices];
      DoubleProperty[] endX = new SimpleDoubleProperty[Nvertices];
      DoubleProperty[] endY = new SimpleDoubleProperty[Nvertices];
      
      //get the x,y values of the vertices and put them in the arrays
      for(int i = 0; i<Nvertices;i++){
      	startX[i] = new SimpleDoubleProperty(vertices[i].x);
      	startY[i] = new SimpleDoubleProperty(vertices[i].y);
      	endX[i] = new SimpleDoubleProperty(vertices[i].x);
      	endY[i] = new SimpleDoubleProperty(vertices[i].y);
      }
      
      //make the dragNodes
      for(int i = 0; i<Nvertices;i++){
    	  arr[i] = new dragNode(Color.WHITE,startX[i],startY[i],20);
    	  if(Mode==3) {
    		 Text[i] = new fixedText(startX[i],startY[i],Integer.toString(NodeOrder[i]));
    		 arr[i].value = NodeOrder[i];
    	  }
      }
      
      //mane an array to put the Boundes Lines in
      lines = new fixedLine[edges];
     
      //place counter
      int p = 0;
      
      //make all the lines
      for(int i=0;i<Nvertices;i++){
      	for(int j =0; j<Nvertices;j++){
      		if(adjacency[i][j]==1 && edgesstate[i][j]==false){
      			lines[p] = new fixedLine(startX[i], startY[i], startX[j], startY[j]);
      			p++;
      			edgesstate[i][j] = true; //we also make the place in the edgesstate true, this means we have used this edge
      			edgesstate[j][i] = true;
      		}
      	}
      }
      
      Button btnNext = new Button("NEXT");
      btnNext.setOnAction(
              new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(final ActionEvent e) {
      					RandomGraph rnd= new RandomGraph();
      					adjacency = rnd.createMatrix();
      					int numberofVertices = rnd.getVertices();
      					int numberofEdges = rnd.getEdges();
      					if(Mode==1) {
      						Mode1 test = new Mode1(adjacency,numberofVertices,numberofEdges);
      						test.start(stage);
      					}
      					else if(Mode==2) {
      						Mode2 test = new Mode2(adjacency,numberofVertices,numberofEdges);
      						test.start(stage);
      					}
                  }
				});
      
      hint = new ToggleButton("HINT");
      hint.setStyle("-fx-background-color: #D3D3D3");
      hint.setSelected(false);
      hint.setOnAction(
              new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(final ActionEvent e) {
                	  if(hint.isSelected()) {
                		  hint.setStyle("-fx-background-color: #90EE90");
                		  checkedges();
                	  }
                	  else {
                		  hint.setStyle("-fx-background-color: #D3D3D3");
                		  for(int p =0; p<edges;p++) {
                			  lines[p].setStroke(Color.GRAY);  
                		  }
                	  }
                  }
              });
      
      highlight = new ToggleButton("FOCUS");
      highlight.setStyle("-fx-background-color: #D3D3D3");
      highlight.setSelected(false);
      highlight.setOnAction(
              new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(final ActionEvent e) {
                	  if(highlight.isSelected()) {
                		  highlight.setStyle("-fx-background-color: #90EE90");
                	  }
                	  else {
                		  highlight.setStyle("-fx-background-color: #D3D3D3");
                	  }
                  }
              });
      
      TextField Input = new TextField();
      Input.setPromptText("amount of solving colors");
	  
      Button BackTrack = new Button("Solve it");
	  BackTrack.setOnAction(
			  new EventHandler<ActionEvent>() {
			    @Override 
			    public void handle(ActionEvent e) {
			    	String amount = Input.getText();
			    	if(!amount.equals("")) {
				    	pressed = (int) Double.parseDouble(amount)-1;
				    	backtracking();
				        checkcolors();
			    	}
			    }
			});
 
      Button btnBack = new Button("BACK");
      btnBack.setOnAction(
              new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(final ActionEvent e) {
                	  Main homePage = new Main();
                	  try {
						homePage.start(stage);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
                  }
				});
      
      Button colourDelete = new Button("MINIMIZE COLOURS");
      colourDelete.setStyle("-fx-background-color: #D3D3D3");
      colourDelete.setOnAction(
              new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(final ActionEvent e) {
      					for(int i = color.length-1;i>=CN;i--)
      						togle[i].setVisible(false);
                  }
				});
      
      //Timer
      if(Mode == 2) {
    	  HBox timerCont = new HBox();
   	   	  menuBarAlignment.setTop(timerCont);
	      timerLabel.setStyle("-fx-font-size: 2em;");
	      
	      timerCont.getChildren().add(timerLabel);
	      timerCont.setPrefHeight(50);
	      timerCont.setAlignment(Pos.CENTER);
	      
	      if (timeline != null) {
	          timeline.stop();
	      }
	      timeSeconds.set(STARTTIME);
	      timeline = new Timeline(new KeyFrame(Duration.seconds(STARTTIME+1), e -> lostPopup()));
	      timeline.getKeyFrames().add(
	              new KeyFrame(Duration.seconds(STARTTIME+1),
	              new KeyValue(timeSeconds, 0)));
	      timeline.playFromStart();
	      
	   // Bind the timerLabel text property to the timeSeconds property
	      timerLabel.textProperty().bind(timeSeconds.asString());
	      
      }
      
      //add the arrays to the group
      root.getChildren().addAll(lines);
      root.getChildren().addAll(arr);
      if(Mode==3)
    	  root.getChildren().addAll(Text);
      layoutAlignment.setRight(menuBar);
      layoutAlignment.setCenter(root);
      
      Text currentColorText = new Text("CURRENT COLOUR");
      
      VBox curColCont = new VBox();
      curColCont.setAlignment(Pos.CENTER);
      curColCont.setPadding(new Insets(10,66,10,66));
      curColCont.setPrefHeight(70);
      
      HBox curCol = new HBox();
      curCol.setPrefSize(100, 50);
      curColCont.getChildren().addAll(currentColorText, curCol);
      curCol.setStyle("-fx-background-color: #" + tg.getSelectedToggle().getUserData().toString().substring(2,8));
      tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
          public void changed(ObservableValue<? extends Toggle> ov,
              Toggle toggle, Toggle new_toggle) {
        	  curCol.setStyle("-fx-background-color: #" + tg.getSelectedToggle().getUserData().toString().substring(2,8));
          }
      });
      
      HBox nextBtnCont = new HBox();
      nextBtnCont.getChildren().addAll(btnNext,btnBack);
      nextBtnCont.setPrefHeight(70);
      nextBtnCont.setAlignment(Pos.CENTER_LEFT);
      nextBtnCont.setPadding(new Insets(0,50,0,50));
      nextBtnCont.setSpacing(50);
      nextBtnCont.setStyle("-fx-background-color: #D3D3D3;");
      
      HBox hintCont = new HBox();
      hintCont.getChildren().addAll(hint,colourDelete,highlight);
      hintCont.setPrefHeight(70);
      hintCont.setAlignment(Pos.CENTER_LEFT);
      hintCont.setSpacing(50);
      hintCont.setPadding(new Insets(10,50,10,50));
      
      HBox solveCont = new HBox();
      solveCont.getChildren().addAll(BackTrack,Input);
      solveCont.setPrefHeight(70);
      solveCont.setAlignment(Pos.CENTER_LEFT);
      solveCont.setPadding(new Insets(10,50,10,50));
      solveCont.setStyle("-fx-background-color: #D3D3D3;");
      
      subMenus.getChildren().addAll(curColCont,solveCont, hintCont,nextBtnCont);
      
      //Creating a scene object 
      Scene scene = new Scene(layoutAlignment);  
      scene.getStylesheets().add("application/Main.css");
      //Setting title to the Stage )
      stage.setTitle("graph from matrix");
      
      //Adding scene to the stage 
      stage.setScene(scene);
      
      Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
      //set Stage boundaries to visible bounds of the main screen
      stage.setX(primaryScreenBounds.getMinX());
      stage.setY(primaryScreenBounds.getMinY());
      stage.setWidth(primaryScreenBounds.getWidth());
      stage.setHeight(primaryScreenBounds.getHeight());
      
      //Displaying the contents of the stage 
      stage.show();         
   } 
   public static ColCircle[] getColCircle() {
	   return vertices;
   }
   public dragNode[] getdragNode() {
	   return arr;
   }
   public void setCN(int Chron) {
	   CN = Chron;
   }
   
   //the class fixed line will make an edge be connected to the vertices when they get dragged
   class fixedLine extends Line {
	      fixedLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
	      //the input of the constructor contains the coordinates of the vertices this edge connects
	      startXProperty().bind(startX);
	      startYProperty().bind(startY);
	      endXProperty().bind(endX);
	      endYProperty().bind(endY);
	      setStrokeWidth(2);
	      setStroke(Color.GRAY);
	      setStrokeLineCap(StrokeLineCap.ROUND);
	      getStrokeDashArray().setAll(10.0, 5.0);
	      setMouseTransparent(true);
	    }
	  }
   //the class fixedText will be used for mode 3, the numbers will be fixed to the center of the vertices
   class fixedText extends Text {
	      fixedText(DoubleProperty x, DoubleProperty y, String text) {
	      xProperty().bind(x);
	      yProperty().bind(y);
	      setText(text);
	      setMouseTransparent(true);
	      
	    }
	  }

	  // a draggable dragNode displayed around a point.
	  class dragNode extends Circle {
		  int value;
	      dragNode(Color color, DoubleProperty x, DoubleProperty y, double rad) {
	      super(x.get(), y.get(), rad);
	      setFill(color);
	      setStroke(Color.BLACK);
	      setStrokeWidth(2);
	      setStrokeType(StrokeType.OUTSIDE);
	      x.bind(centerXProperty());
	      y.bind(centerYProperty());
	      
	      // goes into the void that makes it possible we can drag the nodes around
	      enableDrag();
	    }

	    // make a node movable by dragging it around with the mouse.
	    private void enableDrag() {
	      final Delta dragDelta = new Delta();
	      //action to perform if mouse gets pressed
	      setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	        	MouseButton button = mouseEvent.getButton();
	        	if (button == MouseButton.SECONDARY) {
		          // record a delta distance for the drag and drop operation.
		          dragDelta.x = getCenterX() - mouseEvent.getX();
		          dragDelta.y = getCenterY() - mouseEvent.getY();
		          getScene().setCursor(Cursor.MOVE);
	        	}
	        	if (button == MouseButton.PRIMARY) {
	        		if(Mode<3) {
		        		setFill((Color)tg.getSelectedToggle().getUserData());
		        		checkcolors();
		        		if(hint.isSelected()) checkedges();
	        		}else {
	        			if(value == currValue) {
	        				setFill((Color)tg.getSelectedToggle().getUserData());
			        		checkcolors();
			        		if(hint.isSelected()) checkedges();
			        		currValue++;
	        			}else {
	        				System.out.println("NOOOO");
	        			}
	        		}
	        	}
	        }
	      });
	      setOnMouseReleased(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          getScene().setCursor(Cursor.HAND);
	        }
	      });
	      setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	        	MouseButton button = mouseEvent.getButton();
	        	if (button == MouseButton.SECONDARY) {
		          double newX = mouseEvent.getX() + dragDelta.x;
		          if (newX > 0 && newX < getScene().getWidth()) {
		            setCenterX(newX);
		          }  
		          double newY = mouseEvent.getY() + dragDelta.y;
		          if (newY > 0 && newY < getScene().getHeight()) {
		            setCenterY(newY);
		          }
	        	}
	        }
	      });
	      //action if mouse hovers over a node
	      setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          if (!mouseEvent.isPrimaryButtonDown()) {
	            getScene().setCursor(Cursor.HAND);
	          }
	          if(highlight.isSelected()) {
	          for(int p =0; p<edges;p++) {
					double xe = lines[p].getEndX();
					double xb = lines[p].getStartX();
					
					double ye = lines[p].getEndY();
					double yb = lines[p].getStartY();
					
						if((xe==getCenterX() && ye == getCenterY())||(xb==getCenterX() && yb == getCenterY()))
									lines[p].setStroke(Color.BLUE);
				}
	          }
	        }
	      });
	      
	      //action if mouse doesn't hover over a node
	      setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          if (!mouseEvent.isPrimaryButtonDown()) {
	            getScene().setCursor(Cursor.DEFAULT);
	          }
	          for(int p =0; p<edges;p++) {
					double xe = lines[p].getEndX();
					double xb = lines[p].getStartX();					
					double ye = lines[p].getEndY();
					double yb = lines[p].getStartY();
					
						if((xe==getCenterX() && ye == getCenterY())||(xb==getCenterX() && yb == getCenterY())) {
						lines[p].setStroke(Color.GRAY);
						lines[p].setStrokeLineCap(StrokeLineCap.ROUND);
					    lines[p].getStrokeDashArray().setAll(10.0, 5.0);
						}
						if(hint.isSelected())
							checkedges();
	          }
	        }
	      });
	    }
	    private class Delta { double x, y; }
	  }
	  
	  
	  private void checkcolors() {
		boolean check=true;
		Set<String> set = new HashSet<String>(); 
		for(int i = 0; i<Nvertices; i++) {
			for(int j=0; j<Nvertices; j++){
				if(adjacency[i][j]==1) {
					if(arr[i].getFill().equals(arr[j].getFill())||arr[i].getFill().equals(Color.WHITE)) {
						check = false;
					}else {
						set.add(arr[i].getFill().toString());
					}
				}
			}
		}
		int size = set.size();
		if(check && size==CN) finished = true;
		if(finished) {
			winPopup();
		}

	 }
	  
	  private void checkedges(){
		  for(int p =0; p<edges;p++) {
			  lines[p].setStroke(Color.LIGHTGREEN);  
		  }
		  for(int i = 0; i<Nvertices; i++) {
				for(int j=0; j<Nvertices; j++){
					if(adjacency[i][j]==1) {
						for(int p =0; p<edges;p++) {
							double xe = lines[p].getEndX();
							double xb = lines[p].getStartX();
							
							double ye = lines[p].getEndY();
							double yb = lines[p].getStartY();
							if(arr[i].getFill().equals(Color.WHITE)) {
								if((xe==arr[i].getCenterX() && ye == arr[i].getCenterY())||(xb==arr[i].getCenterX() && yb == arr[i].getCenterY()))
									if((xe==arr[j].getCenterX() && ye == arr[j].getCenterY())||(xb==arr[j].getCenterX() && yb == arr[j].getCenterY())) {
											lines[p].setStroke(Color.GRAY);
											lines[p].setStrokeLineCap(StrokeLineCap.ROUND);
										    lines[p].getStrokeDashArray().setAll(10.0, 5.0);
									}
							}else if(arr[i].getFill().equals(arr[j].getFill())){
								if((xe==arr[i].getCenterX() && ye == arr[i].getCenterY())||(xb==arr[i].getCenterX() && yb == arr[i].getCenterY()))
									if((xe==arr[j].getCenterX() && ye == arr[j].getCenterY())||(xb==arr[j].getCenterX() && yb == arr[j].getCenterY())){
											lines[p].setStroke(Color.RED);
									}
							}
						}
					}
				}
		  }
	  }
	   private void backtracking() {
		  for(int i = 0; i<Nvertices;i++){
			  if(colorSol[i]-1<=pressed) {
			  arr[i].setFill(color[colorSol[i]-1]); 
			  }
			  else {arr[i].setFill(Color.WHITE);}
		  }
	  }
	  public void setSol(int[] sol) {
		 this.colorSol = sol;
	  }
	  public void setNodeOrder(int[] Order) {
		  NodeOrder = Order;
	  }
	// Show a Information Alert without Header Text
	    public void winPopup() {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Congratulations!");
	 
	        // Header Text: null
	        alert.setHeaderText(null);
	        alert.setContentText("Great job! Now try a harder graph!");
	 
	        alert.showAndWait();
	    }
		 // Show a Information Alert without Header Text
	    public void lostPopup() {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Losing is not cool!");
	 
	        // Header Text: null
	        alert.setHeaderText(null);
	        alert.setContentText("You lost! Better luck next time!");
	 
	        //alert.setOnHidden(evt -> homePage.start(stage));

	        alert.show(); 
	    }
}