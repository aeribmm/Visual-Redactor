package com.visaulredactor;

import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class SceneNode {
    private String id;
    private String dialogueText;
    private String characterName;
    private String background;
    private String character;
    private boolean showCharacter;
    private boolean isStartNode;
    private double x, y;
    private VBox nodeView;
    private List<NodeChoice> choices;

    public SceneNode(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dialogueText = "";
        this.choices = new ArrayList<>();
        this.showCharacter = false;
        this.isStartNode = false;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDialogueText() {
        return dialogueText;
    }

    public void setDialogueText(String dialogueText) {
        this.dialogueText = dialogueText;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public boolean isShowCharacter() {
        return showCharacter;
    }

    public void setShowCharacter(boolean showCharacter) {
        this.showCharacter = showCharacter;
    }

    public boolean isStartNode() {
        return isStartNode;
    }

    public void setStartNode(boolean startNode) {
        isStartNode = startNode;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public VBox getNodeView() {
        return nodeView;
    }

    public void setNodeView(VBox nodeView) {
        this.nodeView = nodeView;
    }

    public List<NodeChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<NodeChoice> choices) {
        this.choices = choices;
    }

    public void addChoice(NodeChoice choice) {
        this.choices.add(choice);
    }

    public void removeChoice(NodeChoice choice) {
        this.choices.remove(choice);
    }
}