package com.visaulredactor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VisualNovelEditor extends Application {

    private ZoomablePane canvas;
    private VBox nodePanel;
    private VBox previewPanel;
    private TreeView<String> pathTreeView;

    private List<SceneNode> nodes = new ArrayList<>();
    private List<NodeConnection> connections = new ArrayList<>();
    private SceneNode selectedNode = null;
    private SceneNode startNode = null;

    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        setupMainLayout(primaryStage);
        primaryStage.setTitle("Visual Novel Editor");
        primaryStage.show();
    }

    private void setupMainLayout(Stage stage) {
        BorderPane root = new BorderPane();

        // Центральна панель - полотно для нодів
        setupCanvas();
        root.setCenter(createCanvasWithToolbar());

        // Ліва панель - інструменти та налаштування
        setupNodePanel();
        root.setLeft(nodePanel);

        // Права панель - превʼю та дерево шляхів
        setupPreviewPanel();
        root.setRight(previewPanel);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/editor-styles.css").toExternalForm());
        stage.setScene(scene);
    }

    private VBox createCanvasWithToolbar() {
        VBox container = new VBox();

        // Панель інструментів
        ToolBar toolbar = new ToolBar();
        Button newNodeBtn = new Button("New scene");
        Button saveBtn = new Button("Save");
        Button loadBtn = new Button("Import");
        Button exportBtn = new Button("Export");
        Button playBtn = new Button("▶ Test");

        // ⬇️ НОВІ КНОПКИ для zoom
        Button zoomInBtn = new Button("🔍+");
        Button zoomOutBtn = new Button("🔍−");
        Button zoomResetBtn = new Button("↺");
        Button fitBtn = new Button("⊡");

        newNodeBtn.setOnAction(e -> createNewNode());
        saveBtn.setOnAction(e -> saveProject());
        loadBtn.setOnAction(e -> loadProject());
        exportBtn.setOnAction(e -> exportToCode());
        playBtn.setOnAction(e -> testFromCurrentNode());

        // ⬇️ Прив'язка zoom controls
        zoomInBtn.setOnAction(e -> canvas.zoomIn());
        zoomOutBtn.setOnAction(e -> canvas.zoomOut());
        zoomResetBtn.setOnAction(e -> canvas.resetZoom());
        fitBtn.setOnAction(e -> canvas.fitToContent());

        zoomInBtn.setTooltip(new Tooltip("Zoom in (Scroll up)"));
        zoomOutBtn.setTooltip(new Tooltip("Zoom out (Scroll down)"));
        zoomResetBtn.setTooltip(new Tooltip("Reset"));
        fitBtn.setTooltip(new Tooltip("Fit"));

        toolbar.getItems().addAll(
                newNodeBtn, new Separator(),
                saveBtn, loadBtn, new Separator(),
                zoomInBtn, zoomOutBtn, zoomResetBtn, fitBtn, new Separator(),
                exportBtn, new Separator(), playBtn
        );

        container.getChildren().addAll(toolbar, canvas);
        VBox.setVgrow(canvas, Priority.ALWAYS);
        return container;
    }

    // Опціонально: додати label з поточним масштабом
    private Label createZoomLabel() {
        Label zoomLabel = new Label("100%");
        zoomLabel.setStyle("-fx-text-fill: white; -fx-padding: 5;");

        canvas.scaleProperty().addListener((obs, oldVal, newVal) -> {
            int percentage = (int) (newVal.doubleValue() * 100);
            zoomLabel.setText(percentage + "%");
        });

        return zoomLabel;
    }
    private void setupCanvas() {
        canvas = new ZoomablePane();
        canvas.setStyle("-fx-background-color: #2b2b2b;");
        canvas.setPrefSize(3000, 3000);

        // Обробка кліків для створення нових нодів
        canvas.setOnMouseClicked(this::handleCanvasClick);
    }

    private void setupNodePanel() {
        nodePanel = new VBox(10);
        nodePanel.setPadding(new Insets(10));
        nodePanel.setPrefWidth(250);
        nodePanel.setStyle("-fx-background-color: #3c3c3c;");

        Label title = new Label("Node properties");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Поля для редагування ноди
        TextField nodeIdField = new TextField();
        nodeIdField.setPromptText("Node id");

        TextArea textArea = new TextArea();
        textArea.setPromptText("Dialogue Text...");
        textArea.setPrefRowCount(5);

        ComboBox<String> backgroundCombo = new ComboBox<>();
        backgroundCombo.getItems().addAll("airi", "pres", "none");
        backgroundCombo.setPromptText("Фон");

        ComboBox<String> characterCombo = new ComboBox<>();
        characterCombo.getItems().addAll("gg", "airi", "none");
        characterCombo.setPromptText("Персонаж");

        CheckBox showCharacterCheck = new CheckBox("Show Character");

        Button addChoiceBtn = new Button("Add choice");
        VBox choicesBox = new VBox(5);

        Button applyBtn = new Button("Apply changes");
        applyBtn.setOnAction(e -> applyNodeChanges(nodeIdField, textArea,
                backgroundCombo, characterCombo, showCharacterCheck, choicesBox));

        nodePanel.getChildren().addAll(
                title, new Separator(),
                new Label("ID:"), nodeIdField,
                new Label("Text:"), textArea,
                new Label("Bg:"), backgroundCombo,
                new Label("Character:"), characterCombo, showCharacterCheck,
                new Separator(),
                new Label("Choices:"), choicesBox, addChoiceBtn,
                new Separator(), applyBtn
        );

        addChoiceBtn.setOnAction(e -> addChoiceField(choicesBox));
    }

    private void setupPreviewPanel() {
        previewPanel = new VBox(10);
        previewPanel.setPadding(new Insets(10));
        previewPanel.setPrefWidth(300);
        previewPanel.setStyle("-fx-background-color: #3c3c3c;");

        Label previewTitle = new Label("Scene preview");
        previewTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Мініатюрне вікно превʼю
        VBox previewWindow = new VBox();
        previewWindow.setPrefHeight(200);
        previewWindow.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #555;");

        Label pathTitle = new Label("Path tree");
        pathTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Дерево шляхів до поточної ноди
        pathTreeView = new TreeView<>();
        pathTreeView.setStyle("-fx-background-color: #2a2a2a;");
        pathTreeView.setPrefHeight(300);

        previewPanel.getChildren().addAll(
                previewTitle, previewWindow,
                new Separator(), pathTitle, pathTreeView
        );
    }

    private void handleCanvasClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Конвертуємо screen coordinates в canvas coordinates з урахуванням масштабу
            Point2D canvasPoint = canvas.sceneToLocal(event.getSceneX(), event.getSceneY());
            createNodeAt(canvasPoint.getX(), canvasPoint.getY());
        }
    }

    private void createNewNode() {
        createNodeAt(canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    private void createNodeAt(double x, double y) {
        SceneNode node = new SceneNode("scene_" + (nodes.size() + 1), x, y);
        nodes.add(node);

        // Створення візуального представлення ноди
        VBox nodeView = createNodeView(node);
        nodeView.setLayoutX(x - 75);
        nodeView.setLayoutY(y - 50);

        canvas.getChildren().add(nodeView);

        // Якщо це перша нода, зробити її стартовою
        if (startNode == null) {
            startNode = node;
            node.setStartNode(true);
            nodeView.setStyle("-fx-background-color: #4a9eff; -fx-border-color: #ffffff;");
        }
    }

    private VBox createNodeView(SceneNode node) {
        VBox nodeView = new VBox(5);
        nodeView.setPrefSize(150, 100);
        nodeView.setStyle("-fx-background-color: #4a4a4a; -fx-border-color: #666; " +
                "-fx-border-width: 2; -fx-padding: 10; -fx-background-radius: 5;");

        Label idLabel = new Label(node.getId());
        idLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label textLabel = new Label(truncateText(node.getDialogueText()));
        textLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 10;");
        textLabel.setWrapText(true);

        nodeView.getChildren().addAll(idLabel, textLabel);

        // Обробка кліків на ноді
        nodeView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                selectNode(node, nodeView);
                e.consume(); // <-- чтобы не всплыло на canvas
            } else if (e.getClickCount() == 2) {
                editNode(node);
                e.consume(); // <-- обязательно останавливаем
            }
        });

        // Додавання можливості перетягування
        setupNodeDragging(nodeView, node);

        node.setNodeView(nodeView);
        return nodeView;
    }


    private void setupNodeDragging(VBox nodeView, SceneNode node) {
        final Delta dragDelta = new Delta();

        nodeView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                // Зберігаємо стартові координати
                dragDelta.x = e.getSceneX();
                dragDelta.y = e.getSceneY();
                dragDelta.initialNodeX = nodeView.getLayoutX();
                dragDelta.initialNodeY = nodeView.getLayoutY();
                e.consume();
            }
        });

        nodeView.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                // Різниця переміщення курсору
                double deltaX = e.getSceneX() - dragDelta.x;
                double deltaY = e.getSceneY() - dragDelta.y;

                // Враховуємо масштаб canvas
                double scale = canvas.getScaleX();
                nodeView.setLayoutX(dragDelta.initialNodeX + deltaX / scale);
                nodeView.setLayoutY(dragDelta.initialNodeY + deltaY / scale);

                // Оновлюємо координати ноди
                node.setX(nodeView.getLayoutX() + 75);
                node.setY(nodeView.getLayoutY() + 50);

                updateConnections();
                e.consume();
            }
        });
    }

    private void selectNode(SceneNode node, VBox nodeView) {
        // Зняти виділення з попередньої ноди
        if (selectedNode != null && selectedNode.getNodeView() != null) {
            selectedNode.getNodeView().setStyle("-fx-background-color: #4a4a4a; -fx-border-color: #666;");
        }

        selectedNode = node;
        nodeView.setStyle("-fx-background-color: #5a5a5a; -fx-border-color: #ffaa00;");

        // Оновити дерево шляхів
        updatePathTree(node);

        // Заповнити поля редагування
        fillNodeEditFields(node);
    }

    private void editNode(SceneNode node) {
        NodeEditDialog dialog = new NodeEditDialog(node, nodes);
        dialog.showAndWait().ifPresent(result -> {
            // result — объект, возвращённый диалогом (у нас это тот же объект node, но на всякий случай проверим)
            SceneNode updated = result;

            // Если диалог по каким-то причинам вернул новый экземпляр (маловероятно),
            // то заменим ссылку в списке nodes и в стартовой ноде/выделении:
            if (updated != node) {
                int idx = nodes.indexOf(node);
                if (idx >= 0) nodes.set(idx, updated);
                if (startNode == node) startNode = updated;
                selectedNode = updated;
            }

            // Обновляем визуальное представление ноды
            updateNodeView(updated);

            // Перерисовываем соединения (чтобы линии шли к новым id/координатам)
            updateConnections();

            // Обновляем дерево путей и выделение
            if (selectedNode != null) {
                updatePathTree(selectedNode);
            }
        });
    }


    private void updateNodeView(SceneNode node) {
        VBox nodeView = node.getNodeView();
        if (nodeView != null) {
            Label idLabel = (Label) nodeView.getChildren().get(0);
            Label textLabel = (Label) nodeView.getChildren().get(1);

            idLabel.setText(node.getId());
            textLabel.setText(truncateText(node.getDialogueText()));
        }
    }

    private void updatePathTree(SceneNode node) {
        TreeItem<String> rootItem = new TreeItem<>("Path to: " + node.getId());
        rootItem.setExpanded(true);

        // Рекурсивно побудувати шлях від стартової ноди
        if (startNode != null) {
            buildPathTree(startNode, node, rootItem, new ArrayList<>());
        }

        pathTreeView.setRoot(rootItem);
    }

    private boolean buildPathTree(SceneNode current, SceneNode target,
                                  TreeItem<String> parent, List<SceneNode> visited) {
        if (visited.contains(current)) return false;
        visited.add(current);

        TreeItem<String> currentItem = new TreeItem<>(current.getId());
        parent.getChildren().add(currentItem);

        if (current == target) {
            currentItem.setExpanded(true);
            return true;
        }

        for (NodeChoice choice : current.getChoices()) {
            SceneNode nextNode = findNodeById(choice.getTargetNodeId());
            if (nextNode != null) {
                TreeItem<String> choiceItem = new TreeItem<>("→ " + choice.getText());
                currentItem.getChildren().add(choiceItem);

                if (buildPathTree(nextNode, target, choiceItem, new ArrayList<>(visited))) {
                    currentItem.setExpanded(true);
                    return true;
                }
            }
        }

        return false;
    }

    private SceneNode findNodeById(String id) {
        return nodes.stream()
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private String truncateText(String text) {
        if (text == null || text.isEmpty()) return "Empty node";
        return text.length() > 50 ? text.substring(0, 47) + "..." : text;
    }

    private void fillNodeEditFields(SceneNode node) {
        // Заповнити поля в панелі редагування
        // Реалізація залежить від конкретних полів
    }

    private void applyNodeChanges(TextField nodeIdField, TextArea textArea,
                                  ComboBox<String> backgroundCombo,
                                  ComboBox<String> characterCombo,
                                  CheckBox showCharacterCheck, VBox choicesBox) {
        if (selectedNode == null) return;

        selectedNode.setId(nodeIdField.getText());
        selectedNode.setDialogueText(textArea.getText());
        selectedNode.setBackground(backgroundCombo.getValue());
        selectedNode.setCharacter(characterCombo.getValue());
        selectedNode.setShowCharacter(showCharacterCheck.isSelected());

        updateNodeView(selectedNode);
    }

    private void addChoiceField(VBox choicesBox) {
        HBox choiceRow = new HBox(5);
        TextField choiceText = new TextField();
        choiceText.setPromptText("Текст варіанту");
        ComboBox<String> targetNode = new ComboBox<>();
        // Заповнити доступними нодами
        nodes.forEach(node -> targetNode.getItems().add(node.getId()));

        Button removeBtn = new Button("×");
        removeBtn.setOnAction(e -> choicesBox.getChildren().remove(choiceRow));

        choiceRow.getChildren().addAll(choiceText, targetNode, removeBtn);
        choicesBox.getChildren().add(choiceRow);
    }

    private void updateConnections() {
        // Оновити візуальні зʼєднання між нодами
        canvas.getChildren().removeIf(node -> node instanceof Line);

        for (SceneNode node : nodes) {
            for (NodeChoice choice : node.getChoices()) {
                SceneNode targetNode = findNodeById(choice.getTargetNodeId());
                if (targetNode != null) {
                    Line connection = new Line();
                    connection.setStartX(node.getX());
                    connection.setStartY(node.getY());
                    connection.setEndX(targetNode.getX());
                    connection.setEndY(targetNode.getY());
                    connection.setStroke(Color.LIGHTBLUE);
                    connection.setStrokeWidth(2);
                    canvas.getChildren().add(0, connection);
                }
            }
        }
    }

    private void saveProject() {
        // Зберегти проект в JSON
        System.out.println("Saiving project...");
    }

    private void loadProject() {
        // Завантажити проект з JSON
        System.out.println("Loading project...");
    }

    private void exportToCode() {
        // Згенерувати код для движка
        ScriptGenerator generator = new ScriptGenerator();
        String code = generator.generateScript(nodes, startNode);
        System.out.println("Generated code:\n" + code);
    }

    private void testFromCurrentNode() {
        if (selectedNode != null) {
            // Запустити тест з поточної ноди
            GameTestWindow testWindow = new GameTestWindow(selectedNode, nodes);
            testWindow.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Допоміжний клас для перетягування
    class Delta {
        double x, y;
        double initialNodeX, initialNodeY;
    }
}
