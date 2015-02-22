package edu.utdallas.sai.model;

import javafx.scene.Node;

/**
 * The Sprite class represents a image or node to be displayed.
 *
 * @author Durga Sai Preetham Palagummi
 */
public abstract class Sprite {

    public Node node;

    /**
     * velocity vector x direction
     */
    public double vX = 0;

    /**
     * velocity vector y direction
     */
    public double vY = 0;

    /**
     * Updates this sprite object's velocity, or animations.
     */
    public abstract void update();
}
