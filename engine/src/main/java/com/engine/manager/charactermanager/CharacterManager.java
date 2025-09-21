package com.engine.manager.charactermanager;

import com.engine.Character.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterManager {
    private List<Character> listOfCreatedCharacters;

    public CharacterManager() {
        createCharacters();
    }

    public void createCharacters(){
        listOfCreatedCharacters = new ArrayList<>();
        Character gg = new Character("gg","Acysi","gg-default");
        Character airi = new Character("airi","Airi","airi-smiling");
        listOfCreatedCharacters.add(gg);
        listOfCreatedCharacters.add(airi);
    }

    public void show(Character character){
        System.out.println(character.isShowed());
        if(!listOfCreatedCharacters.contains(character)){
            System.err.println("character not found");
            return;
        }
        if(!character.isShowed()){
            System.out.println("character " + character.getName() + " was showed");
            character.setShowed(true);
        }else {
            System.out.println("chracter already showed");
        }

    }
    public void hide(Character character){
        if(!listOfCreatedCharacters.contains(character)){
            System.err.println("character not found");
            return;
        }
        if(character.isShowed()){
            System.out.println("character " + character.getName() + " was hiden");
            character.setShowed(false);
        }else {
            System.err.println("character already hiden " + character.getName());
        }
    }

    public List<Character> getListOfCreatedCharacters() {
        return Collections.unmodifiableList(listOfCreatedCharacters);
    }
    public Character findById(String id){
        for(Character ch : listOfCreatedCharacters){
            if(ch.getId().equals(id)){
                return ch;
            }
        }
        return null;
    }
}