package com.visaulredactor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class GameTestWindow {
    private Stage stage;
    private SceneNode currentNode;
    private List<SceneNode> allNodes;

    private Label backgroundLabel;
    private Label characterLabel;
    private Label dialogueLabel;
    private VBox choicesBox;

    public GameTestWindow(SceneNode startNode, List<SceneNode> nodes) {
        this.currentNode = startNode;
        this.allNodes = nodes;
        setupWindow();
    }

    private void setupWindow() {
        stage = new Stage();
        stage.setTitle("Тест сцени - " + currentNode.getId());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Верхня панель - інформація про сцену
        VBox topPanel = createTopPanel();
        root.setTop(topPanel);

        // Центральна панель - діалог
        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);

        // Нижня панель - варіанти вибору
        choicesBox = new VBox(5);
        choicesBox.setAlignment(Pos.CENTER);
        root.setBottom(choicesBox);

        // Кнопки управління
        HBox controlButtons = new HBox(10);
        controlButtons.setAlignment(Pos.CENTER);
        controlButtons.setPadding(new Insets(10, 0, 0, 0));

        Button restartBtn = new Button("Перезапустити");
        Button closeBtn = new Button("Закрити");

        restartBtn.setOnAction(e -> restartTest());
        closeBtn.setOnAction(e -> stage.close());

        controlButtons.getChildren().addAll(restartBtn, closeBtn);

        VBox bottomContainer = new VBox(10);
        bottomContainer.getChildren().addAll(choicesBox, controlButtons);
        root.setBottom(bottomContainer);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);

        // Відобразити початкову сцену
        displayCurrentScene();
    }

    private VBox createTopPanel() {
        VBox panel = new VBox(5);
        panel.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #ccc;");

        backgroundLabel = new Label();
        characterLabel = new Label();

        panel.getChildren().addAll(
                new Label("Поточна сцена: " + currentNode.getId()),
                backgroundLabel,
                characterLabel
        );

        return panel;
    }

    private VBox createCenterPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd;");

        dialogueLabel = new Label();
        dialogueLabel.setWrapText(true);
        dialogueLabel.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");

        panel.getChildren().add(dialogueLabel);
        return panel;
    }

    private void displayCurrentScene() {
        stage.setTitle("Тест сцени - " + currentNode.getId());

        // Оновити інформацію про фон
        String bgText = currentNode.getBackground() != null ?
                "Фон: " + currentNode.getBackground() : "Фон: не встановлено";
        backgroundLabel.setText(bgText);

        // Оновити інформацію про персонажа
        String charText = currentNode.getCharacter() != null ?
                "Персонаж: " + currentNode.getCharacter() +
                        (currentNode.isShowCharacter() ? " (показано)" : " (приховано)") :
                "Персонаж: не встановлено";
        characterLabel.setText(charText);

        // Оновити діалог
        String dialogueText = currentNode.getDialogueText();
        if (currentNode.getCharacterName() != null && !currentNode.getCharacterName().isEmpty()) {
            dialogueText = currentNode.getCharacterName() + ": " + dialogueText;
        }
        dialogueLabel.setText(dialogueText);

        // Оновити варіанти вибору
        updateChoices();
    }

    private void updateChoices() {
        choicesBox.getChildren().clear();

        if (currentNode.getChoices().isEmpty()) {
            Label noChoicesLabel = new Label("Кінець сцени");
            noChoicesLabel.setStyle("-fx-text-fill: #888; -fx-font-style: italic;");
            choicesBox.getChildren().add(noChoicesLabel);
        } else {
            for (NodeChoice choice : currentNode.getChoices()) {
                Button choiceBtn = new Button(choice.getText());
                choiceBtn.setPrefWidth(300);
                choiceBtn.setOnAction(e -> selectChoice(choice));
                choicesBox.getChildren().add(choiceBtn);
            }
        }
    }

    private void selectChoice(NodeChoice choice) {
        SceneNode targetNode = findNodeById(choice.getTargetNodeId());
        if (targetNode != null) {
            currentNode = targetNode;
            displayCurrentScene();
        } else {
            Label errorLabel = new Label("Помилка: сцену '" + choice.getTargetNodeId() + "' не знайдено");
            errorLabel.setStyle("-fx-text-fill: red;");
            choicesBox.getChildren().add(errorLabel);
        }
    }

    private SceneNode findNodeById(String id) {
        return allNodes.stream()
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void restartTest() {
        // Знайти стартову ноду або використати поточну як початкову
        SceneNode startNode = allNodes.stream()
                .filter(SceneNode::isStartNode)
                .findFirst()
                .orElse(currentNode);
        currentNode = startNode;
        displayCurrentScene();
    }

    public void show() {
        stage.show();
    }
}
