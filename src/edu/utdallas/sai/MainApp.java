package edu.utdallas.sai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import edu.utdallas.sai.controller.RobotOverviewController;
import edu.utdallas.sai.model.GameWorld;
import edu.utdallas.sai.util.MyLogger;

/***
 * This is the main application class.
 * Please note that the instance of this class has to be set only once and then pass the instance to 
 * every other class when required
 * NetID: dxp141030
 * Date: 19th February, 2015
 * @author Durga Sai Preetham Palagummi
 *
 */
public class MainApp extends Application {
	private Stage primaryStage;
	GameWorld gameWorld = new RobotOverviewController(60, "Robot UI");

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		showRobotOverview();
        
		 // setup title, scene, stats, controls, and actors.
        gameWorld.initialize(primaryStage);

        // kick off the game loop
        gameWorld.beginGameLoop();  
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showRobotOverview() {
		try {
			// Load Robot overview.
			FXMLLoader newLoader = new FXMLLoader();
			newLoader.setLocation(MainApp.class.getResource("view/RobotOverview.fxml"));
			BorderPane personOverview = (BorderPane) newLoader.load();
			Scene scene = new Scene(personOverview);
			RobotOverviewController controller = (RobotOverviewController) newLoader.getController();
			controller.setMainApp(this);
			controller.setScene(scene);
			controller.setStage(primaryStage);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Robot UI");
			primaryStage.setMinHeight(600.0);
			primaryStage.setMinWidth(1000.0);
			//Add an application icon
			this.primaryStage.getIcons().add(new Image("file:resources/images/robot_app.png"));
			primaryStage.show();
			
		} catch (Exception e) {
			MyLogger.writeToLog(this.getClass().getCanonicalName(), e.getMessage());
		}
	}
	
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}