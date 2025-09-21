package com.engine.manager.bgmanager;

import com.engine.background.Background;
import com.engine.Game;

import java.util.HashMap;

public class BackgroundManager {
    private Game game;
    private final HashMap<String, Background> backHash = new HashMap<>();

    public BackgroundManager() {
        game = Game.getInstance();
        createBg();
    }


    public void changeBackground(String id){
        System.out.println("try to chang bg to : " + id);
        if(backHash.containsKey(id)){
            game.setCurrentBackground(backHash.get(id));
        }else{
            System.out.println("file not found");
        }
    }

    public void createBg(){
        Background bg1 = new Background("airi");
        Background bg2 = new Background("pres");
        backHash.put(bg1.getImage(),bg1);
        backHash.put(bg2.getImage(),bg2);
    }
}