package edu.utdallas.sai.model;

import javafx.scene.image.ImageView;

/**
 * Represents a double link list to assist in the rotation of the robot.
 * This helps to move clockwise and counter clockwise.
 * NetID: dxp141030
 * Date: 19th February, 2015
 * @author Durga Sai Preetham Palagummi
 */
public class RotatedRobotImage extends ImageView {
    public RotatedRobotImage next;
    public RotatedRobotImage prev;
}
