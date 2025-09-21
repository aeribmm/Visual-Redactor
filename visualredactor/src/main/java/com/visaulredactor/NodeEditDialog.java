package com.visaulredactor;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NodeEditDialog extends Dialog<SceneNode> {
    private SceneNode node;
    private TextField idField;
    private TextField characterNameField;
    private TextArea dialogueArea;
    private ComboBox<String> backgroundCombo;
    private ComboBox<String> characterCombo;
    private CheckBox showCharacterCheck;
    private VBox choicesContainer;

    public NodeEditDialog(SceneNode node) {
        this.node = node;
        setupDialog();
        fillFields();
    }

    private void setupDialog() {
        setTitle("Редагування сцени: " + node.getId());
        setHeaderText("Налаштуйте параметри сцени");

        // Створення полів
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        idField = new TextField();
        characterNameField = new TextField();
        dialogueArea = new TextArea();
        dialogueArea.setPrefRowCount(4);
        dialogueArea.setWrapText(true);

        backgroundCombo = new ComboBox<>();
        backgroundCombo.getItems().addAll("none", "airi", "pres");

        characterCombo = new ComboBox<>();
        characterCombo.getItems().addAll("none", "gg", "airi");

        showCharacterCheck = new CheckBox();

        // Секція варіантів вибору
        choicesContainer = new VBox(5);
        Button addChoiceBtn = new Button("Додати варіант");
        addChoiceBtn.setOnAction(e -> addChoiceField());

        ScrollPane choicesScroll = new ScrollPane(choicesContainer);
        choicesScroll.setPrefHeight(150);
        choicesScroll.setFitToWidth(true);

        // Розміщення елементів
        int row = 0;
        grid.add(new Label("ID ноди:"), 0, row);
        grid.add(idField, 1, row++);

        grid.add(new Label("Імʼя персонажа:"), 0, row);
        grid.add(characterNameField, 1, row++);

        grid.add(new Label("Текст діалогу:"), 0, row);
        grid.add(dialogueArea, 1, row++);

        grid.add(new Label("Фон:"), 0, row);
        grid.add(backgroundCombo, 1, row++);

        grid.add(new Label("Персонаж:"), 0, row);
        grid.add(characterCombo, 1, row++);

        grid.add(new Label("Показати персонажа:"), 0, row);
        grid.add(showCharacterCheck, 1, row++);

        grid.add(new Label("Варіанти вибору:"), 0, row);
        VBox choicesSection = new VBox(5);
        choicesSection.getChildren().addAll(choicesScroll, addChoiceBtn);
        grid.add(choicesSection, 1, row++);

        getDialogPane().setContent(grid);

        // Кнопки
        ButtonType saveButtonType = new ButtonType("Зберегти", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Обробка результату
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return saveChanges();
            }
            return null;
        });
    }

    private void fillFields() {
        idField.setText(node.getId());
        characterNameField.setText(node.getCharacterName() != null ? node.getCharacterName() : "");
        dialogueArea.setText(node.getDialogueText() != null ? node.getDialogueText() : "");
        backgroundCombo.setValue(node.getBackground() != null ? node.getBackground() : "none");
        characterCombo.setValue(node.getCharacter() != null ? node.getCharacter() : "none");
        showCharacterCheck.setSelected(node.isShowCharacter());

        // Заповнити існуючі варіанти
        for (NodeChoice choice : node.getChoices()) {
            addChoiceField(choice.getText(), choice.getTargetNodeId());
        }
    }

    private void addChoiceField() {
        addChoiceField("", "");
    }

    private void addChoiceField(String choiceText, String targetNode) {
        HBox choiceRow = new HBox(5);
        choiceRow.setPadding(new Insets(2));

        TextField textField = new TextField(choiceText);
        textField.setPromptText("Текст варіанту");
        textField.setPrefWidth(200);

        TextField targetField = new TextField(targetNode);
        targetField.setPromptText("ID цільової ноди");
        targetField.setPrefWidth(150);

        Button removeBtn = new Button("×");
        removeBtn.setOnAction(e -> choicesContainer.getChildren().remove(choiceRow));

        choiceRow.getChildren().addAll(
                new Label("→"), textField,
                new Label("до:"), targetField,
                removeBtn
        );

        choicesContainer.getChildren().add(choiceRow);
    }

    private SceneNode saveChanges() {
        node.setId(idField.getText());
        node.setCharacterName(characterNameField.getText());
        node.setDialogueText(dialogueArea.getText());
        node.setBackground(backgroundCombo.getValue());
        node.setCharacter(characterCombo.getValue());
        node.setShowCharacter(showCharacterCheck.isSelected());

        // Зберегти варіанти вибору
        node.getChoices().clear();
        for (javafx.scene.Node child : choicesContainer.getChildren()) {
            if (child instanceof HBox) {
                HBox choiceRow = (HBox) child;
                TextField textField = (TextField) choiceRow.getChildren().get(1);
                TextField targetField = (TextField) choiceRow.getChildren().get(3);

                String text = textField.getText().trim();
                String target = targetField.getText().trim();

                if (!text.isEmpty() && !target.isEmpty()) {
                    node.addChoice(new NodeChoice(text, target));
                }
            }
        }

        return node;
    }
}