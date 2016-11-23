package com.mateoi.pong;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Collects input for a Scene and presents it as a single method that checks if
 * a key is pressed or not.
 *
 * @author mateo
 */
public class KeyboardInput {
    /** List to keep the currently pressed keys in */
    private ArrayList<KeyCode> input = new ArrayList<>();

    /**
     * Attaches a keyboard listener to the given Scene. Key presses can then be
     * accessed through the {@link #contains(KeyCode)} method.
     *
     * @param node
     */
    public KeyboardInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (!input.contains(e.getCode())) {
                input.add(e.getCode());
            }
        });
        scene.setOnKeyReleased(e -> input.remove(e.getCode()));
    }

    /**
     * Checks if the given key is currently pressed or not.
     *
     * @param code
     * @return
     */
    public boolean contains(KeyCode code) {
        return input.contains(code);
    }

}
