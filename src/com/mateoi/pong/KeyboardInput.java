package com.mateoi.pong;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class KeyboardInput {
    private ArrayList<KeyCode> input = new ArrayList<>();

    public KeyboardInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (!input.contains(e.getCode())) {
                input.add(e.getCode());
            }
        });
        scene.setOnKeyReleased(e -> input.remove(e.getCode()));
    }

    public boolean contains(KeyCode code) {
        return input.contains(code);
    }

}
