package com.engine.Character;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Character {
    private String id;
    private String name;
    private String currentSprite;
    private final Map<String,String> sprites = new HashMap<>();
    private boolean isShowed;
    private String position;

    public Character(String id, String name, String currentSprite) {
        this.id = id;
        this.name = name;
        this.currentSprite = currentSprite;
        this.isShowed = false;
        initSprites();
    }
    private void initSprites() {
        if (id.equals("gg")) {
            sprites.put("default", "gg-default.png");
            sprites.put("happy", "gg-happy.png");
            sprites.put("sad", "gg-sad.png");
        } else if (id.equals("airi")) {
            sprites.put("smiling", "airi-smiling.png");
            sprites.put("angry", "airi-angry.png");
            sprites.put("surprised", "airi-surprised.png");
        }
    }

    // 1. Метод setPosition - ДОБАВИТЬ
    public void setPosition(String position) {
        if (position.equals("left") || position.equals("center") || position.equals("right")) {
            this.position = position;
            System.out.println(name + " positioned at: " + position);
        } else {
            System.out.println("Invalid position! Use: left, center, right");
        }
    }

    // 2. Метод getPosition - ДОБАВИТЬ
    public String getPosition() {
        return position;
    }

    // 3. Метод changeSprite - ДОБАВИТЬ
    public boolean changeSprite(String spriteName) {
        if (sprites.containsKey(spriteName)) {
            this.currentSprite = sprites.get(spriteName);
            System.out.println(name + " sprite changed to: " + spriteName);
            return true;
        } else {
            System.out.println("Sprite '" + spriteName + "' not found for " + name);
            return false;
        }
    }

    public Map<String, String> getSprites() {
        return Collections.unmodifiableMap(sprites);
    }

    public String getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(String currentSprite) {
        this.currentSprite = currentSprite;
    }

    public boolean isShowed() {
        return isShowed;
    }

    public void setShowed(boolean showed) {
        isShowed = showed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSprite() {
        return currentSprite;
    }

    public void setSprite(String sprite) {
        this.currentSprite = sprite;
    }



}