package edu.utdallas.sai.view;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import edu.utdallas.sai.MainApp;
import edu.utdallas.sai.model.GameWorld;
import edu.utdallas.sai.model.MiniRobot;
import edu.utdallas.sai.model.Sprite;

/***
 * Controller class for Robot
 * @author Shathru
 *
 */
public class RobotOverviewController extends GameWorld{
	@FXML
	Button up;
	@FXML
	Button down;
	@FXML
	Button left;
	@FXML
	Button right;
	@FXML
	Button temperatureButton;
	@FXML
	Button wifiButton;
	@FXML
	Button cameraButton;
	@FXML
	Button clearButton;
	@FXML
	RadioButton slowRB;
	@FXML
	RadioButton mediumRB;
	@FXML
	RadioButton fastRB;
	@FXML
	Label temperatureStatus;
	@FXML
	Label batteryLabel;
	@FXML
	Label cameraLabel;
	@FXML
	Label wifiLabel;
	@FXML
	Label overallStatusLabel;
	@FXML
	Label sliderLabel;
	@FXML
	Label armImageLabel;
	@FXML
	ToggleButton armToggle;
	@FXML
	Slider sliderArm;
	@FXML
	AnchorPane anchorPaneForRobot;
	@FXML
	AnchorPane anchorPaneForFPSPic;
	@FXML
	SplitPane splitPane;
	MiniRobot miniRobot;

	private MainApp mainApp;
	private Scene scene;
	private Stage primaryStage;
	private ToggleGroup group = new ToggleGroup();
	private ToggleGroup groupArm = new ToggleGroup();
	private String speed = "slow"; //set default speed as slow

	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}


	//Simple variable for generating a random number from 50-70
	Random numbers = new Random();
	float minX = 50.0f;
	float maxX = 70.0f;
	//Set zoom for the image
	DoubleProperty zoomProperty = new SimpleDoubleProperty(1.1);
	Double SCALE_DELTA = 1.1;

	//Setting images for buttons
	private ImageView upImage = new ImageView(
			new Image("file:resources/images/arrows/ar_up.png")
			);
	private ImageView downImage = new ImageView(
			new Image("file:resources/images/arrows/ar_down.png")
			);
	private ImageView leftImage = new ImageView(
			new Image("file:resources/images/arrows/ar_left.png")
			);
	private ImageView rightImage = new ImageView(
			new Image("file:resources/images/arrows/ar_right.png")
			);
	private ImageView temperatureImage = new ImageView(
			new Image("file:resources/images/arrows/temp.png")
			);
	private ImageView batteryImage0 = new ImageView(
			new Image("file:resources/images/battery/bat_0.png")
			);
	private ImageView batteryImage20 = new ImageView(
			new Image("file:resources/images/battery/bat_20.png")
			);
	private ImageView batteryImage50 = new ImageView(
			new Image("file:resources/images/battery/bat_50.png")
			);
	private ImageView batteryImage100 = new ImageView(
			new Image("file:resources/images/battery/bat_100.png")
			);
	private ImageView fpsPicture = new ImageView(
			new Image("file:resources/images/stars/stars.jpg"));
	private ImageView wifiOff = new ImageView(
			new Image("file:resources/images/wifi/wifi_off.png"));
	private ImageView wifiOn = new ImageView(
			new Image("file:resources/images/wifi/wifi_on.png"));
	private ImageView cameraImg = new ImageView(
			new Image("file:resources/images/camera/camera.png"));
	private ImageView slowImg = new ImageView(
			new Image("file:resources/images/speed/crawl.png"));
	private ImageView mediumImg = new ImageView(
			new Image("file:resources/images/speed/walk.png"));
	private ImageView fastImg = new ImageView(
			new Image("file:resources/images/speed/run.png"));
	private ImageView armImgOpen = new ImageView(
			new Image("file:resources/images/arm/arm_open.png"));
	private ImageView armImgClose = new ImageView(
			new Image("file:resources/images/arm/arm_close.png"));
	//Ends here - images for buttons

	public RobotOverviewController() {
		//Intentionally left blank
		//Will be called before any other method. Use it when necessary
	}


	public RobotOverviewController(int fps, String title) {
		super(fps, title);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public MainApp getMainApp() {
		return mainApp;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void setStage(Stage prim) {
		this.primaryStage = prim;
	}

	/**
	 * Will be bound to FXML for manipulating the FXML file
	 */
	@FXML
	void initialize() {
		//Set the images initialized for each button
		up.setGraphic(upImage);
		down.setGraphic(downImage);
		left.setGraphic(leftImage);
		right.setGraphic(rightImage);
		temperatureButton.setGraphic(temperatureImage);
		batteryLabel.setGraphic(batteryImage0);
		wifiButton.setGraphic(wifiOn);
		cameraButton.setGraphic(cameraImg);
		slowRB.setGraphic(slowImg);
		mediumRB.setGraphic(mediumImg);
		fastRB.setGraphic(fastImg);
		armImageLabel.setGraphic(armImgOpen);
		anchorPaneForRobot.setStyle("-fx-background-image: url('file:resources/images/stars/stars.jpg')");

		//Add images of battery in array
		//Battery icon will change every 2 second causing impression that the robot is charging
		ImageView[] imgArr = new ImageView[10];
		imgArr[0] = batteryImage0;
		imgArr[1] = batteryImage20;
		imgArr[2] = batteryImage50;
		imgArr[3] = batteryImage100;
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(0),
						new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent actionEvent) {
						batteryLabel.setGraphic(imgArr[numbers.nextInt((3 - 0) + 1) + 0]);
					}
				}
						), new KeyFrame(Duration.seconds(1)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		firstMethod();
	}

	/**
	 * Will be called soon after the initialize method
	 */
	void firstMethod() {

		cameraButton.setFocusTraversable(false);
		cameraLabel.setText("Press camera button for Robot's POV");
		clearButton.setVisible(false);
		cameraButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				anchorPaneForFPSPic.getChildren().add(fpsPicture); 
				fpsPicture.setId("myPic"); //Set the ID for this pic which will be used later
				fpsPicture.fitWidthProperty().bind(anchorPaneForFPSPic.widthProperty()); //centres the image
				fpsPicture.fitHeightProperty().bind(anchorPaneForFPSPic.heightProperty()); //centers the image
				anchorPaneForFPSPic.getChildren().remove(clearButton);
				anchorPaneForFPSPic.setStyle("-fx-background-color: #D4DCFF;");
				if(cameraButton.isVisible()) {
					cameraButton.setVisible(false);
					cameraLabel.setText("");
					anchorPaneForFPSPic.getChildren().add(clearButton);
					clearButton.setVisible(true);
				}
			}
		});

		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				anchorPaneForFPSPic.getChildren().remove(fpsPicture);
				anchorPaneForFPSPic.setStyle("-fx-background-image: url('file:resources/images/camera/cam_bck.png'); -fx-background-size: contain;  -fx-background-repeat: no-repeat;");
				if(!cameraButton.isVisible()) {
					cameraButton.setVisible(true);
					cameraLabel.setText("Press camera button for Robot's POV");
					clearButton.setVisible(false);
				}
			}
		});

		temperatureButton.setFocusTraversable(false);
		temperatureButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				String value = String.valueOf(numbers.nextFloat() * (maxX - minX) + minX);
				temperatureStatus.setText("Temperature: "+String.format("%.2f", Float.parseFloat(value))+" \u00b0F");
			}
		});

		wifiButton.setFocusTraversable(false);
		wifiButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(wifiButton.getGraphic() == wifiOn) {
					wifiLabel.setText("WiFi - OFF");
					wifiButton.setGraphic(wifiOff);
				}
				else {wifiLabel.setText("WiFi - ON"); wifiButton.setGraphic(wifiOn);}
			}
		});

	}


	/**
	 * Method that is called from the MainApp
	 */
	@Override
	public void initialize(Stage primStage) {
		this.primaryStage = primStage;
		miniRobot = new MiniRobot();
		anchorPaneForRobot = (AnchorPane) (((BorderPane)primaryStage.getScene().getRoot()).lookup("#anchorPaneForRobot"));
		anchorPaneForFPSPic =  (AnchorPane) (((BorderPane)primaryStage.getScene().getRoot()).lookup("#anchorPaneForFPSPic"));
		setGameSurface(anchorPaneForRobot); //Sets the game surface for the Robot to move
		((BorderPane)primaryStage.getScene().getRoot()).getChildren().get(0).setStyle("-fx-background-color: #D4DCFF;");
		((BorderPane)primaryStage.getScene().getRoot()).getChildren().get(1).setStyle("-fx-background-color: #D4DCFF;");
		anchorPaneForFPSPic.setStyle("-fx-background-image: url('file:resources/images/camera/cam_bck.png'); -fx-background-size: contain;  -fx-background-repeat: no-repeat;");
		getSpriteManager().addSprites(miniRobot); //Add the robot to our Sprite for animations
		miniRobot.node.setTranslateX(10.0);
		miniRobot.node.setTranslateY(10.0);
		anchorPaneForRobot.getChildren().add(miniRobot.node);

		//Set the arrow keys
		up = (Button) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#up");
		down = (Button) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#down");
		left = (Button) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#left");
		right = (Button) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#right");
		up.setFocusTraversable(false); down.setFocusTraversable(false); left.setFocusTraversable(false); right.setFocusTraversable(false);

		//Set the button values
		slowRB = (RadioButton) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#slowRB");
		mediumRB = (RadioButton) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#mediumRB");
		fastRB = (RadioButton) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#fastRB");

		//Labels
		wifiLabel = (Label) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#wifiLabel");
		overallStatusLabel = (Label) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#overallStatusLabel");
		sliderArm = (Slider) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#sliderArm");
		sliderLabel = (Label) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#sliderLabel");
		armToggle = (ToggleButton) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#armToggle");
		armImageLabel = (Label) ((BorderPane)primaryStage.getScene().getRoot()).lookup("#armImageLabel");
		sliderArm.setMax(90.0); sliderArm.setFocusTraversable(false);
		sliderLabel.setText("Arm is at 0\u00b0");
		armToggle.setToggleGroup(groupArm); armToggle.setFocusTraversable(false);

		//Ensure this method is called last
		setupInput(primaryStage); //setUpInput method is responsible for causing any game animations
	}


	/**
	 * Each sprite will update it's velocity and bounce off wall borders.
	 *
	 * @param sprite - An atomic particle (a sphere).
	 */
	@Override
	protected void handleUpdate(Sprite sprite) {
		sprite.update();
		bounceOffWalls(sprite);
	}

	/**
	 * Change the direction of the moving object when it encounters the walls.
	 *
	 * @param sprite The sprite to update based on the wall boundaries.
	 */
	private void bounceOffWalls(Sprite sprite) {
		// bounce off the walls when outside of boundaries
		Node displayNode;
		if (sprite instanceof MiniRobot) {
			displayNode = sprite.node;
		} else {
			displayNode = sprite.node;
		}
		if (sprite.node.getTranslateX() > (getGameSurface().getWidth()) ||
				displayNode.getTranslateX() < 0) {
			sprite.vX = sprite.vX * -1;

		}
		if (sprite.node.getTranslateY() > getGameSurface().getHeight() ||
				sprite.node.getTranslateY() < 0) {
			sprite.vY = sprite.vY * -1;
		}
	}


	/**
	 * Sets up the mouse input.
	 *
	 * @param primaryStage The primary stage (app window).
	 */
	private void setupInput(Stage primaryStage) {

		//Toggle arm close-open
		groupArm.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable,
					Toggle oldValue, Toggle newValue) {
				if(newValue==null) {
					armImageLabel.setGraphic(armImgOpen);
					armToggle.setText("Arm is OPENED");
				}
				else {
					armImageLabel.setGraphic(armImgClose);
					armToggle.setText("Arm is CLOSED");
				}

			}
		});
		//////////////////////////////////
		//Slider logic
		sliderArm.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				sliderLabel.setText("Arm is at "+String.format("%.0f", new_val)+"\u00b0");
				anchorPaneForRobot.requestFocus();
			}
		});
		//////////////////////////////////

		//Keyboard buttons logic
		primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				//				System.out.println("CENTRE X: "+miniRobot.getCenterX()+"\tCENTRE Y: "+miniRobot.getCenterY());
				if(event.getCode()==KeyCode.UP){
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());
					if (speed.equalsIgnoreCase("slow")) {
						overallStatusLabel.setText("The robot is trudging at 10 mph");
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()-10.0, true, "slow", true);
					}
					else if (speed.equalsIgnoreCase("medium")) {
						overallStatusLabel.setText("The robot is walking at 25 mph");
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()-20.0, true, "medium", true);
					}
					else if (speed.equalsIgnoreCase("fast")) {
						overallStatusLabel.setText("The robot is cruising at 50 mph");
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()-40.0, true, "fast", true);
					}
				}

				else if(event.getCode()==KeyCode.DOWN){
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());
					if (speed.equalsIgnoreCase("slow")) {
						overallStatusLabel.setText("The robot is trudging at 10 mph");
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()+10.0, true, "slow", true);
					}
					else if (speed.equalsIgnoreCase("medium")) {
						overallStatusLabel.setText("The robot is walking at 25 mph");	
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()+20.0, true, "medium", true);
					}
					else if (speed.equalsIgnoreCase("fast")) {
						overallStatusLabel.setText("The robot is cruising at 50 mph");
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()+40.0, true, "fast", true);
					}
				}


				else if(event.getCode()==KeyCode.RIGHT){
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());
					if (speed.equalsIgnoreCase("slow")) {
						overallStatusLabel.setText("The robot is trudging at 10 mph");
						miniRobot.plotCourse(miniRobot.getCenterX()+10.0, miniRobot.getCenterY(), true, "slow", true);
					}
					else if (speed.equalsIgnoreCase("medium")) {
						overallStatusLabel.setText("The robot is walking at 25 mph");
						miniRobot.plotCourse(miniRobot.getCenterX()+20.0, miniRobot.getCenterY(), true, "medium", true);
					}
					else if (speed.equalsIgnoreCase("fast")) {
						overallStatusLabel.setText("The robot is cruising at 50 mph");
						miniRobot.plotCourse(miniRobot.getCenterX()+30.0, miniRobot.getCenterY(), true, "fast", true);
					}
				}

				else if(event.getCode()==KeyCode.LEFT){
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());
					if (speed.equalsIgnoreCase("slow")) {
						overallStatusLabel.setText("The robot is trudging at 10 mph");
						miniRobot.plotCourse(miniRobot.getCenterX()-10.0, miniRobot.getCenterY(), true, "slow", true);
					}
					else if (speed.equalsIgnoreCase("medium")) {
						overallStatusLabel.setText("The robot is walking at 25 mph");
						miniRobot.plotCourse(miniRobot.getCenterX()-20.0, miniRobot.getCenterY(), true, "medium", true);
					}
					else if (speed.equalsIgnoreCase("fast")) {
						overallStatusLabel.setText("The robot is cruising at 50 mph");
						miniRobot.plotCourse(miniRobot.getCenterX()-40.0, miniRobot.getCenterY(), true, "fast", true);
					}
				}
			}
		});
		//////////////////////////////////

		//Move the robot with mouse click
		EventHandler<MouseEvent> moveWithMouse = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				miniRobot.applyTheBrakes(event.getX(), event.getY());
				// move forward and rotate robot
				if (speed.equalsIgnoreCase("slow")) {
					overallStatusLabel.setText("The robot is trudging at 10 mph");
					miniRobot.plotCourse(event.getX(), event.getY(), true, "slow", false);
				}
				else if (speed.equalsIgnoreCase("medium")) {
					overallStatusLabel.setText("The robot is walking at 25 mph");
					miniRobot.plotCourse(event.getX(), event.getY(), true, "medium", false);
				}
				else if (speed.equalsIgnoreCase("fast")) {
					overallStatusLabel.setText("The robot is cruising at 50 mph");
					miniRobot.plotCourse(event.getX(), event.getY(), true, "fast", false);
				}
			}
		};
		//////////////////////////////////

		//Move with the arrow buttons in the UI
		EventHandler<MouseEvent> move = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getSource().toString().contains("up")) {
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());

					if (speed.equalsIgnoreCase("slow"))
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()-10.0, true, "slow", false);
					else if (speed.equalsIgnoreCase("medium"))
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()-20.0, true, "medium", false);
					else if (speed.equalsIgnoreCase("fast"))
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()-40.0, true, "fast", false);
				}
				if(event.getSource().toString().contains("down")) {
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());
					if (speed.equalsIgnoreCase("slow"))
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()+10.0, true, "slow", false);
					else if (speed.equalsIgnoreCase("medium"))
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()+20.0, true, "medium", false);
					else if (speed.equalsIgnoreCase("fast"))
						miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()+40.0, true, "fast", false);
				}
				if(event.getSource().toString().contains("right")) {
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());
					if (speed.equalsIgnoreCase("slow"))
						miniRobot.plotCourse(miniRobot.getCenterX()+10.0, miniRobot.getCenterY(), true, "slow", false);
					else if (speed.equalsIgnoreCase("medium"))
						miniRobot.plotCourse(miniRobot.getCenterX()+20.0, miniRobot.getCenterY(), true, "medium", false);
					else if (speed.equalsIgnoreCase("fast"))
						miniRobot.plotCourse(miniRobot.getCenterX()+30.0, miniRobot.getCenterY(), true, "fast", false);
				}
				if(event.getSource().toString().contains("left")) {
					miniRobot.applyTheBrakes(miniRobot.getCenterX(), miniRobot.getCenterY());
					if (speed.equalsIgnoreCase("slow"))
						miniRobot.plotCourse(miniRobot.getCenterX()-10.0, miniRobot.getCenterY(), true, "slow", false);
					else if (speed.equalsIgnoreCase("medium"))
						miniRobot.plotCourse(miniRobot.getCenterX()-20.0, miniRobot.getCenterY(), true, "medium", false);
					else if (speed.equalsIgnoreCase("fast"))
						miniRobot.plotCourse(miniRobot.getCenterX()-40.0, miniRobot.getCenterY(), true, "fast", false);
				}
			}
		};
		//////////////////////////////////


		//Zooming the FPS picture
		anchorPaneForFPSPic.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override public void handle(ScrollEvent event) {
				event.consume();

				if (event.getDeltaY() == 0) {
					return;
				}
				if(event.getDeltaY() > 0)
					zoomProperty.set(zoomProperty.get() * 1.1);
				else zoomProperty.set(zoomProperty.get() / 1.1);

				fpsPicture = (ImageView) anchorPaneForFPSPic.lookup("#myPic");
				if(fpsPicture!=null) {
					fpsPicture.setScaleX(zoomProperty.get());
					fpsPicture.setScaleY(zoomProperty.get());
				}
			}
		});
		//////////////////////////////////

		//Radio Buttons
		slowRB.setToggleGroup(group); slowRB.setUserData("slow");
		mediumRB.setToggleGroup(group); mediumRB.setUserData("medium");
		fastRB.setToggleGroup(group); fastRB.setUserData("fast");
		slowRB.setSelected(true); //Set default speed to be slow
		slowRB.setFocusTraversable(false);
		mediumRB.setFocusTraversable(false);
		fastRB.setFocusTraversable(false);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle old_toggle, Toggle new_toggle) {
				if (group.getSelectedToggle() != null) {
					setSpeed(group.getSelectedToggle().getUserData().toString());
				}
			}
		});

		anchorPaneForRobot.setOnMousePressed(moveWithMouse);

		//up.setOnMouseDragged(move);
		down.setOnMousePressed(move);
		left.setOnMousePressed(move);
		right.setOnMousePressed(move);

		/////////////////////////////////////////
		EventHandler<MouseEvent> circleOnMousePressedEventHandler = 
				new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {
				System.out.println(t.getSource().toString());
			}
		};

		EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = 
				new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {
				miniRobot.plotCourse(miniRobot.getCenterX(), miniRobot.getCenterY()-0.000001, true, "fast", true);
				System.out.println(t.getSource().toString()+"INSISDE DRAGS");
			}
		};
		/////////////////////////////////////////////////////
		up.setOnMousePressed(circleOnMousePressedEventHandler);
		up.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		
	}
}

