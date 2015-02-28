package edu.utdallas.sai.model;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class is the model for the sprite
 * In our case, this class will hold the robot image and all 32 variations of the image
 * which will be helpful for rotating the image when user moves the robot around with mouse
 * NetID: dxp141030
 * Date: 19th February, 2015
 * @author Durga Sai Preetham Palagummi
 */
@SuppressWarnings({"deprecation", "unused"})
public class MiniRobot extends Sprite {

	/**
	 * 360 degree turn
	 */
	private final static int TWO_PI_DEGREES = 360;

	/**
	 * Number of frames and directions the robot is pointing nose
	 */
	private final static int NUM_DIRECTIONS = 60;

	/**
	 * The angle of one direction (adjacent directions) (11.25 degrees)
	 */
	private final static float UNIT_ANGLE_PER_FRAME = ((float) TWO_PI_DEGREES / NUM_DIRECTIONS);

	/**
	 * Amount of time it takes the robot to move 180 degrees in milliseconds.
	 */
	private final static int MILLIS_TURN_180_DEGREES = 300;

	/**
	 * When the robot turns on each direction one amount of time for one frame or turn of the robot. (18.75 milliseconds)
	 */
	private final static float MILLIS_PER_FRAME = (float) MILLIS_TURN_180_DEGREES / (NUM_DIRECTIONS / 2);

	/**
	 * All possible turn directions Clockwise, Counter Clockwise, or Neither when the user clicks mouse around robot
	 */
	private enum DIRECTION {
		CLOCKWISE, COUNTER_CLOCKWISE, NEITHER
	}

	/**
	 * Velocity amount used vector when robot moves forward. scale vector of robot. See flipBook translateX and Y.
	 */
	private  static float THRUST_AMOUNT_SLOW = 0.5f;
	private  static float THRUST_AMOUNT_MEDIUM = 2.5f;
	private  static float THRUST_AMOUNT_FAST = 4.0f;

	/**
	 * Current turning direction. default is NEITHER. Clockwise and Counter Clockwise.
	 */
	private DIRECTION turnDirection = DIRECTION.NEITHER;

	/**
	 * The current starting position of the vector or coordinate where the nose of the robot is pointing towards.
	 */
	private Vec u; // current or start vector

	/**
	 * All ImageViews of all the possible image frames for each direction the robot is pointing. ie: 32 directions.
	 */
	private final List<RotatedRobotImage> directionalRobots = new ArrayList<>();

	/**
	 * The Timeline instance to animate the robot rotating using images. This is an optical illusion similar to page
	 * flipping as each frame is displayed the previous visible attribute is set to false. No rotation is happening.
	 */
	private Timeline rotateRobotTimeline;

	/**
	 * The current index into the list of ImageViews representing each direction of the robot. Zero is the robot
	 * pointing to the right or zero degrees.
	 */
	private int uIndex = 0;

	/**
	 * The end index into the list of ImageViews representing each direction of the robot. Zero is the robot
	 * pointing to the right or zero degrees.
	 */
	private int vIndex = 0;

	/**
	 * The spot where the user has right clicked letting the engine check the robot's center is in this area.
	 */
	private Circle stopArea = new Circle();

	/**
	 * A group contain all of the robot image view nodes.
	 */
	private final Group flipBook = new Group();

	/**
	 * The collision bounding region for the robot
	 */
	private Circle hitBounds;

	public MiniRobot() {

		// Load one image.
		Image robotImage;
		robotImage = new Image(getClass().getClassLoader().getResource("resources/robot.png").toExternalForm(), true);
		stopArea.setRadius(40);
		stopArea.setStroke(Color.ORANGE);
		RotatedRobotImage prev = null;
		// create all the number of directions based on a unit angle. 360 divided by NUM_DIRECTIONS
		for (int i = 0; i < NUM_DIRECTIONS; i++) {
			RotatedRobotImage imageView = new RotatedRobotImage();
			imageView.setImage(robotImage);
			imageView.setRotate(-1 * i * UNIT_ANGLE_PER_FRAME);
			imageView.setCache(true);
			imageView.setCacheHint(CacheHint.SPEED);
			imageView.setManaged(false);

			imageView.prev = prev;
			imageView.setVisible(false);
			directionalRobots.add(imageView);
			if (prev != null) {
				prev.next = imageView;
			}
			prev = imageView;
			flipBook.getChildren().add(imageView);
		}

		RotatedRobotImage firstrobot = directionalRobots.get(0);
		firstrobot.prev = prev;
		prev.next = firstrobot;
		// set javafx node to an image
		firstrobot.setVisible(true);
		node = flipBook;
		flipBook.setTranslateX(300);
		flipBook.setTranslateY(300);
		flipBook.setCache(true);
		flipBook.setCacheHint(CacheHint.SPEED);
		flipBook.setManaged(true);
		flipBook.setAutoSizeChildren(true);
		initHitZone();

	}

	/**
	 * Initialize the collision region for the space robot.
	 * It's just a inscribed circle.
	 */
	public void initHitZone() {
		// build hit zone
		if (hitBounds == null) {
			double hZoneCenterX = 55;
			double hZoneCenterY = 34;
			hitBounds = CircleBuilder.create()
					.centerX(hZoneCenterX)
					.centerY(hZoneCenterY)
					.stroke(Color.PINK)
					.fill(Color.RED)
					.radius(15)
					.opacity(0)
					.build();
			flipBook.getChildren().add(hitBounds);
		}

	}

	/**
	 * Change the velocity of the atom particle.
	 */
	@Override
	public void update() {
		flipBook.setTranslateX(flipBook.getTranslateX() + vX);
		flipBook.setTranslateY(flipBook.getTranslateY() + vY);

		if (stopArea.contains(getCenterX(), getCenterY())) {
			vX = 0;
			vY = 0;
		}

	}


	private RotatedRobotImage getCurrentrobotImage() {
		return directionalRobots.get(uIndex);
	}

	/**
	 * The center X coordinate of the current visible image. See <code>getCurrentrobotImage()</code> method.
	 *
	 * @return The scene or screen X coordinate.
	 */
	public double getCenterX() {
		RotatedRobotImage robotImage = getCurrentrobotImage();
		return node.getTranslateX() + (robotImage.getBoundsInLocal().getWidth() / 2);
	}

	/**
	 * The center Y coordinate of the current visible image. See <code>getCurrentrobotImage()</code> method.
	 *
	 * @return The scene or screen Y coordinate.
	 */
	public double getCenterY() {
		RotatedRobotImage robotImage = getCurrentrobotImage();
		return node.getTranslateY() + (robotImage.getBoundsInLocal().getHeight() / 2);
	}

	/**
	 * Determines the angle between it's starting position and ending position (Similar to a clock's second hand).
	 * When the user is shooting the robot nose will point in the direction of the mouse press using the primary button.
	 * When the user is thrusting to a location on the screen the right click mouse will pass true to the thrust
	 * parameter.
	 *
	 * @param screenX The mouse press' screen x coordinate.
	 * @param screenY The mouse press' screen ycoordinate.
	 * @param thrust  Thrust robot forward or not. True move forward otherwise false.
	 */
	public void plotCourse(double screenX, double screenY, boolean thrust, String speed, boolean fromKeyBoard) {
		// get center of robot
		double sx = getCenterX();
		double sy = getCenterY();

		// get user's new turn position based on mouse click
		Vec v = new Vec(screenX, screenY, sx, sy);
		if (u == null) {
			u = new Vec(1, 0);
		}


		double atan2RadiansU = Math.atan2(u.y, u.x);
		double atan2DegreesU = Math.toDegrees(atan2RadiansU);

		double atan2RadiansV = Math.atan2(v.y, v.x);
		double atan2DegreesV = Math.toDegrees(atan2RadiansV);

		double angleBetweenUAndV = atan2DegreesV - atan2DegreesU;


		// if abs value is greater than 180 move counter clockwise
		//(or opposite of what is determined)
		double absAngleBetweenUAndV = Math.abs(angleBetweenUAndV);
		boolean goOtherWay = false;
		if (absAngleBetweenUAndV > 180) {
			if (angleBetweenUAndV < 0) {
				turnDirection = DIRECTION.COUNTER_CLOCKWISE;
				goOtherWay = true;
			} else if (angleBetweenUAndV > 0) {
				turnDirection = DIRECTION.CLOCKWISE;
				goOtherWay = true;
			} else {
				turnDirection = MiniRobot.DIRECTION.NEITHER;
				goOtherWay = true; ///
			}
		} else {
			if (angleBetweenUAndV < 0) {
				turnDirection = MiniRobot.DIRECTION.CLOCKWISE;
			} else if (angleBetweenUAndV > 0) {
				turnDirection = MiniRobot.DIRECTION.COUNTER_CLOCKWISE;
			} else {
				turnDirection = MiniRobot.DIRECTION.NEITHER;
				goOtherWay = true; ////
			}
		}

		double degreesToMove = absAngleBetweenUAndV;
		if (goOtherWay) {
			degreesToMove = TWO_PI_DEGREES - absAngleBetweenUAndV;
		}

		uIndex = Math.round((float) (atan2DegreesU / UNIT_ANGLE_PER_FRAME));
		if (uIndex < 0) {
			uIndex = NUM_DIRECTIONS + uIndex;
		}
		vIndex = Math.round((float) (atan2DegreesV / UNIT_ANGLE_PER_FRAME));
		if (vIndex < 0) {
			vIndex = NUM_DIRECTIONS + vIndex;
		}
		/***
		 * Use this message for any debugging purposes
		String debugMsg = turnDirection +
				" U [m(" + u.mx + ", " + u.my + ")  => c(" + u.x + ", " + u.y + ")] " +
				" V [m(" + v.mx + ", " + v.my + ")  => c(" + v.x + ", " + v.y + ")] " +
				" start angle: " + atan2DegreesU +
				" end angle:" + atan2DegreesV +
				" Angle between: " + degreesToMove +
				" Start index: " + uIndex +
				" End index: " + vIndex;

		System.out.println(debugMsg);
		 */
		if(thrust) {

			if(speed.equalsIgnoreCase("slow")) {
				vX = Math.cos(atan2RadiansV) * THRUST_AMOUNT_SLOW;
				vY = -Math.sin(atan2RadiansV) * THRUST_AMOUNT_SLOW;	
			}
			else if(speed.equalsIgnoreCase("medium")) {
				vX = Math.cos(atan2RadiansV) * THRUST_AMOUNT_MEDIUM;
				vY = -Math.sin(atan2RadiansV) * THRUST_AMOUNT_MEDIUM;	
			}
			else if(speed.equalsIgnoreCase("fast")) {
				vX = Math.cos(atan2RadiansV) * THRUST_AMOUNT_FAST;
				vY = -Math.sin(atan2RadiansV) * THRUST_AMOUNT_FAST;	
			}

		}
		if(!fromKeyBoard)
			turnrobot();
		update();

		u = v;
	}

	private void turnrobot() {

		final Duration oneFrameAmt = Duration.millis(MILLIS_PER_FRAME);
		RotatedRobotImage startImage = directionalRobots.get(uIndex);
		RotatedRobotImage endImage = directionalRobots.get(vIndex);
		List<KeyFrame> frames = new ArrayList<>();

		RotatedRobotImage currImage = startImage;

		int i = 1;
		while (true) {

			final Node displayNode = currImage;

			KeyFrame oneFrame = new KeyFrame(oneFrameAmt.multiply(i),
					new EventHandler<ActionEvent>() {

				@Override
				public void handle(javafx.event.ActionEvent event) {
					for (RotatedRobotImage robotImg : directionalRobots) {
						robotImg.setVisible(false);
					}
					displayNode.setVisible(true);
				}
			}); // oneFrame

			frames.add(oneFrame);

			if (currImage == endImage) {
				break;
			}
			if (turnDirection == DIRECTION.CLOCKWISE) {
				currImage = currImage.prev;
			}
			if (turnDirection == DIRECTION.COUNTER_CLOCKWISE) {
				currImage = currImage.next;
			}
			i++;
		}


		if (rotateRobotTimeline != null) {
			rotateRobotTimeline.stop();
			rotateRobotTimeline.getKeyFrames().clear();
			rotateRobotTimeline.getKeyFrames().addAll(frames);
		} else {
			// sets the game world's game loop (Timeline)
			rotateRobotTimeline = TimelineBuilder.create()
					.keyFrames(frames)
					.build();

		}

		rotateRobotTimeline.playFromStart();


	}

	/**
	 * Stops the robot from thrusting forward.
	 *
	 * @param screenX the screen's X coordinate to stop the robot.
	 * @param screenY the screen's Y coordinate to stop the robot.
	 */
	public void applyTheBrakes(double screenX, double screenY) {
		stopArea.setCenterX(screenX);
		stopArea.setCenterY(screenY);
	}

}
