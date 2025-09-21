package com.visaulredactor;

class NodeChoice {
    private String text;
    private String targetNodeId;

    public NodeChoice(String text, String targetNodeId) {
        this.text = text;
        this.targetNodeId = targetNodeId;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTargetNodeId() { return targetNodeId; }
    public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
}