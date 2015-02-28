package edu.utdallas.sai.model;

import java.util.*;

/**
 * Sprite manager is responsible for holding all sprite objects.
 * NetID: dxp141030
 * Date: 19th February, 2015
 * @author Durga Sai Preetham Palagummi
 */
public class SpriteManager {
    /** All the sprite objects currently in play */
    private final static List<Sprite> GAME_ACTORS = new ArrayList<>();

    /** */
    public List<Sprite> getAllSprites() {
        return GAME_ACTORS;
    }
    
    /**
     * VarArgs of sprite objects to be added to the game.
     * @param sprites 
     */
    public void addSprites(Sprite... sprites) {       
        GAME_ACTORS.addAll(Arrays.asList(sprites));
    }
    
}
