package com.visaulredactor;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NodeEditDialog extends Dialog<SceneNode> {
    private SceneNode node;
    private List<SceneNode> allNodes; // добавим список всех нод
    private TextField idField;
    private TextField characterNameField;
    private TextArea dialogueArea;
    private ComboBox<String> backgroundCombo;
    private ComboBox<String> characterCombo;
    private CheckBox showCharacterCheck;
    private VBox choicesContainer;

    public NodeEditDialog(SceneNode node, List<SceneNode> allNodes) {
        this.node = node;
        this.allNodes = allNodes;
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

        // Поле текста варианта
        TextField textField = new TextField(choiceText);
        textField.setPromptText("Текст варіанту");
        textField.setPrefWidth(200);

        // ComboBox для выбора целевой ноды
        ComboBox<String> targetCombo = new ComboBox<>();
        targetCombo.setPrefWidth(150);
        targetCombo.setPromptText("ID цільової ноди");

        // Заполняем список доступных нод
        Set<String> usedTargets = allNodes.stream()
                .flatMap(n -> n.getChoices().stream())
                .map(NodeChoice::getTargetNodeId)
                .collect(Collectors.toSet());

        List<String> availableTargets = allNodes.stream()
                .map(SceneNode::getId)
                .filter(id -> !id.equals(node.getId())) // не ссылка на саму себя
                .filter(id -> !usedTargets.contains(id) || id.equals(targetNode)) // исключаем уже занятые
                .toList();

        targetCombo.getItems().addAll(availableTargets);
        if (targetNode != null && !targetNode.isEmpty()) {
            targetCombo.setValue(targetNode);
        }

        // Кнопка удаления строки
        Button removeBtn = new Button("×");
        removeBtn.setOnAction(e -> choicesContainer.getChildren().remove(choiceRow));

        // Добавляем всё в строку
        choiceRow.getChildren().addAll(new Label("→"), textField, new Label("до:"), targetCombo, removeBtn);

        // Добавляем строку в контейнер
        choicesContainer.getChildren().add(choiceRow);
    }

    private SceneNode saveChanges() {
        // основные поля
        node.setId(idField.getText() != null ? idField.getText().trim() : "");
        node.setCharacterName(characterNameField.getText());
        node.setDialogueText(dialogueArea.getText());
        node.setBackground(backgroundCombo.getValue());
        node.setCharacter(characterCombo.getValue());
        node.setShowCharacter(showCharacterCheck.isSelected());

        // очистить старые варинты
        node.getChoices().clear();

        // Проходим по всем HBox-строкам в контейнере и безопасно ищем TextField и ComboBox
        for (javafx.scene.Node child : choicesContainer.getChildren()) {
            if (!(child instanceof HBox)) continue;
            HBox choiceRow = (HBox) child;

            // Найдём первое TextField и первый ComboBox в этой строке (не используем "магические" индексы)
            TextField textField = null;
            @SuppressWarnings("unchecked")
            ComboBox<String> targetCombo = null;

            for (javafx.scene.Node n : choiceRow.getChildren()) {
                if (n instanceof TextField && textField == null) {
                    textField = (TextField) n;
                } else if (n instanceof ComboBox && targetCombo == null) {
                    targetCombo = (ComboBox<String>) n;
                }
            }

            if (textField == null || targetCombo == null) continue;

            String text = textField.getText() != null ? textField.getText().trim() : "";
            String target = targetCombo.getValue();

            if (!text.isEmpty() && target != null && !target.isEmpty()) {
                node.addChoice(new NodeChoice(text, target));
            }
        }

        return node;
    }


}