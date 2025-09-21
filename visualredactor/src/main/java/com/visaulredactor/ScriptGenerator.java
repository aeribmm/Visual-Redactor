package com.visaulredactor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class ScriptGenerator {
    private ObjectMapper mapper = new ObjectMapper();

    public String generateScript(List<SceneNode> nodes, SceneNode startNode) {
        ObjectNode root = mapper.createObjectNode();
        root.put("startNode", startNode != null ? startNode.getId() : "");

        ArrayNode scenesArray = mapper.createArrayNode();

        for (SceneNode node : nodes) {
            ObjectNode sceneNode = mapper.createObjectNode();
            sceneNode.put("id", node.getId());
            sceneNode.put("dialogueText", node.getDialogueText());
            sceneNode.put("characterName", node.getCharacterName());
            sceneNode.put("background", node.getBackground());
            sceneNode.put("character", node.getCharacter());
            sceneNode.put("showCharacter", node.isShowCharacter());

            ArrayNode choicesArray = mapper.createArrayNode();
            for (NodeChoice choice : node.getChoices()) {
                ObjectNode choiceNode = mapper.createObjectNode();
                choiceNode.put("text", choice.getText());
                choiceNode.put("targetNodeId", choice.getTargetNodeId());
                choicesArray.add(choiceNode);
            }
            sceneNode.set("choices", choicesArray);
            scenesArray.add(sceneNode);
        }

        root.set("scenes", scenesArray);

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (Exception e) {
            e.printStackTrace();
            return "Помилка генерації скрипта: " + e.getMessage();
        }
    }

    public String generateJavaCode(List<SceneNode> nodes, SceneNode startNode) {
        StringBuilder code = new StringBuilder();

        code.append("// Автоматично згенерований код для Visual Novel Engine\n");
        code.append("package com.engine.generated;\n\n");
        code.append("import com.engine.Game;\n");
        code.append("import com.engine.BGManager.BackgroundManager;\n");
        code.append("import com.engine.CharacterManager.CharacterManager;\n");
        code.append("import com.engine.Character.Character;\n\n");

        code.append("public class GeneratedScene {\n");
        code.append("    private Game game;\n");
        code.append("    private BackgroundManager backgroundManager;\n");
        code.append("    private CharacterManager characterManager;\n\n");

        code.append("    public GeneratedScene() {\n");
        code.append("        game = Game.getInstance();\n");
        code.append("        backgroundManager = new BackgroundManager();\n");
        code.append("        characterManager = new CharacterManager();\n");
        code.append("    }\n\n");

        code.append("    public void executeScene(String sceneId) {\n");
        code.append("        switch(sceneId) {\n");

        for (SceneNode node : nodes) {
            code.append("            case \"").append(node.getId()).append("\":\n");
            code.append("                execute_").append(sanitizeId(node.getId())).append("();\n");
            code.append("                break;\n");
        }

        code.append("            default:\n");
        code.append("                System.err.println(\"Scene not found: \" + sceneId);\n");
        code.append("        }\n");
        code.append("    }\n\n");

        // Генерація методів для кожної сцени
        for (SceneNode node : nodes) {
            generateSceneMethod(code, node);
        }

        code.append("}\n");

        return code.toString();
    }

    private void generateSceneMethod(StringBuilder code, SceneNode node) {
        String methodName = "execute_" + sanitizeId(node.getId());

        code.append("    private void ").append(methodName).append("() {\n");

        // Зміна фону
        if (node.getBackground() != null && !node.getBackground().equals("none")) {
            code.append("        backgroundManager.changeBackground(\"")
                    .append(node.getBackground()).append("\");\n");
        }

        // Управління персонажами
        if (node.getCharacter() != null && !node.getCharacter().equals("none")) {
            code.append("        Character character = characterManager.findById(\"")
                    .append(node.getCharacter()).append("\");\n");
            code.append("        if (character != null) {\n");

            if (node.isShowCharacter()) {
                code.append("            characterManager.show(character);\n");
            } else {
                code.append("            characterManager.hide(character);\n");
            }
            code.append("        }\n");
        }

        // Виведення тексту
        if (node.getDialogueText() != null && !node.getDialogueText().isEmpty()) {
            String characterName = node.getCharacterName() != null ? node.getCharacterName() : "";
            code.append("        System.out.println(\"");
            if (!characterName.isEmpty()) {
                code.append(escapeString(characterName)).append(": ");
            }
            code.append(escapeString(node.getDialogueText())).append("\");\n");
        }

        // Варіанти вибору
        if (!node.getChoices().isEmpty()) {
            code.append("        // Варіанти вибору:\n");
            for (int i = 0; i < node.getChoices().size(); i++) {
                NodeChoice choice = node.getChoices().get(i);
                code.append("        // ").append(i + 1).append(". ")
                        .append(choice.getText()).append(" -> ")
                        .append(choice.getTargetNodeId()).append("\n");
            }
        }

        code.append("    }\n\n");
    }

    private String sanitizeId(String id) {
        return id.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private String escapeString(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}