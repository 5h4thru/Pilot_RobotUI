package edu.utdallas.sai.model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/***
 * The abstract class for game loop
 * NetID: dxp141030
 * Date: 19th February, 2015
 * Modified: 26th February, 2015
 * @author Durga Sai Preetham Palagummi
 *
 */
@SuppressWarnings("deprecation")
public abstract class GameWorld {
	public GameWorld() {
		
	}
    private AnchorPane gameSurface;
    private static Timeline gameLoop;
    private  int framesPerSecond;
    private  String windowTitle;
    private  SpriteManager spriteManager = new SpriteManager();

    /**
     * Constructor that is called by the derived class. This will
     * set the frames per second, title, and setup the game loop.
     *
     * @param fps   - Frames per second.
     * @param title - Title of the application window.
     */
    public GameWorld(final int fps, final String title) {
        framesPerSecond = fps;
        windowTitle = title;
        // create and set timeline for the game loop
        buildAndSetGameLoop();
    }


    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {

        final Duration oneFrameAmt = Duration.millis(1000 / (float) getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                    	
                        // update actors
                        updateSprites();

                    }
                }); // oneFrame

        // sets the game world's game loop (Timeline)
        setGameLoop(TimelineBuilder.create()
                .cycleCount(Animation.INDEFINITE)
                .keyFrames(oneFrame)
                .build());
    }

    /**
     * Initialize the game world by update the JavaFX Stage.
     *
     * @param primaryStage The main window containing the JavaFX Scene.
     */
    public abstract void initialize(final Stage primaryStage);

    /**
     * Kicks off (plays) the Timeline objects containing one key frame
     * that simply runs indefinitely with each frame invoking a method
     * to update sprite objects, check for collisions, and cleanup sprite
     * objects.
     */
    public void beginGameLoop() {
        getGameLoop().play();
    }

    /**
     * Updates each game sprite in the game world. This method will
     * loop through each sprite and passing it to the handleUpdate()
     * method. The derived class should override handleUpdate() method.
     */
    protected void updateSprites() {
        for (Sprite sprite : spriteManager.getAllSprites()) {
            handleUpdate(sprite);
        }
    }

    /**
     * Updates the sprite object's information to position on the game surface.
     *
     * @param sprite - The sprite to update.
     */
    protected abstract void handleUpdate(Sprite sprite);

    /**
     * Returns the frames per second.
     *
     * @return int The frames per second.
     */
    protected int getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * Returns the game's window title.
     *
     * @return String The game's window title.
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * The game loop (Timeline) which is used to update, check collisions, and
     * cleanup sprite objects at every interval (fps).
     *
     * @return Timeline An animation running indefinitely representing the game
     *         loop.
     */
    protected static Timeline getGameLoop() {
        return gameLoop;
    }

    /**
     * The sets the current game loop for this game world.
     *
     * @param gameLoop Timeline object of an animation running indefinitely
     *                 representing the game loop.
     */
    protected static void setGameLoop(Timeline gameLoop) {
        GameWorld.gameLoop = gameLoop;
    }

    /**
     * Returns the sprite manager containing the sprite objects to
     * manipulate in the game.
     *
     * @return SpriteManager The sprite manager.
     */
    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    /**
     * Returns the JavaFX Scene. This is called the game surface to
     * allow the developer to add JavaFX Node objects onto the Scene.
     *
     * @return Scene The JavaFX scene graph.
     */
    public AnchorPane getGameSurface() {
        return gameSurface;
    }

    /**
     * Sets the JavaFX Scene. This is called the game surface to
     * allow the developer to add JavaFX Node objects onto the Scene.
     *
     * @param gameSurface The main game surface (JavaFX Scene).
     */
    protected void setGameSurface(AnchorPane gameSurface) {
        this.gameSurface = gameSurface;
    }

    /**
     * Stop threads.
     */
    public void shutdown() {
        getGameLoop().stop();
    }
}
