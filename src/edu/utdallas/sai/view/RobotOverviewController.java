package edu.utdallas.sai.view;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	Label temperatureStatus;
	@FXML
	Label batteryLabel;
	@FXML
	Label cameraLabel;
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

	//Simple variable for generating a random number from 50-70
	Random numbers = new Random();
	float minX = 50.0f;
	float maxX = 70.0f;
	//Set zoom for the image
	DoubleProperty zoomProperty = new SimpleDoubleProperty(50);

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

		cameraLabel.setText("Press button!!!");
		clearButton.setVisible(false);
		cameraButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				anchorPaneForFPSPic.getChildren().add(fpsPicture);
				fpsPicture.setId("myPic"); //Set the ID for this pic which will be used later
				if(cameraButton.isVisible()) {
					cameraButton.setVisible(false);
					cameraLabel.setText("");
					clearButton.setVisible(true);
				}
			}
		});

		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				anchorPaneForFPSPic.getChildren().remove(fpsPicture);
				if(!cameraButton.isVisible()) {
					cameraButton.setVisible(true);
					cameraLabel.setText("Press button!!!");
					clearButton.setVisible(false);
				}
			}
		});

		temperatureButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				String value = String.valueOf(numbers.nextFloat() * (maxX - minX) + minX);
				temperatureStatus.setText(String.format("%.2f", Float.parseFloat(value))+"\u00b0 F");
			}
		});

		wifiButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(wifiButton.getGraphic() == wifiOn) {
					wifiButton.setGraphic(wifiOff);
				}
				else {wifiButton.setGraphic(wifiOn);}
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
		((BorderPane)primaryStage.getScene().getRoot()).getChildren().get(0).setStyle("-fx-background-color: red;");
		((BorderPane)primaryStage.getScene().getRoot()).getChildren().get(1).setStyle("-fx-background-color: blue;");
		getSpriteManager().addSprites(miniRobot); //Add the robot to our Sprite for animations
		miniRobot.node.setTranslateX(10.0);
		miniRobot.node.setTranslateY(10.0);
		anchorPaneForRobot.getChildren().add(miniRobot.node);
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

		EventHandler<MouseEvent> fireOrMove = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				miniRobot.applyTheBrakes(event.getX(), event.getY());
				// move forward and rotate robot
				miniRobot.plotCourse(event.getX(), event.getY(), true);
			}
		};

		//Used for zooming the picture that we display in FPSPane
		anchorPaneForFPSPic.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0) {	                	
					zoomProperty.set(zoomProperty.get() * 1.1);
				} else if (event.getDeltaY() < 0) {
					zoomProperty.set(zoomProperty.get() / 1.1);
				}
				if (zoomProperty.get()>=80) {
					zoomProperty.set(zoomProperty.get() / 1.1);
				}        
			}
		});

		//Listener class for zooming
		zoomProperty.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				fpsPicture = (ImageView) anchorPaneForFPSPic.lookup("#myPic");
				if(fpsPicture!=null) {
					fpsPicture.setFitWidth(zoomProperty.get() * 4);
					fpsPicture.setFitHeight(zoomProperty.get() * 3);
				}
			}
		});
		/*
		anchorPaneForRobot.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				System.out.println(event.getCode());
				switch(event.getCode()) {
				case UP:
					System.out.println("You pressed "+event.getSource());
					TranslateTransition translateTransitionRight = new TranslateTransition();
					translateTransitionRight.setToX(translateTransitionRight.getNode().getTranslateX()+30);
					System.out.println();
					translateTransitionRight.play();
				default:
					System.out.println("ERROR!!!");
					break;
				}
			}
		});
		 */
		anchorPaneForRobot.setOnMousePressed(fireOrMove);
	}
}

