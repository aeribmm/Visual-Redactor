package com.visaulredactor;

class NodeConnection {
    private SceneNode fromNode;
    private SceneNode toNode;
    private NodeChoice choice;

    public NodeConnection(SceneNode fromNode, SceneNode toNode, NodeChoice choice) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.choice = choice;
    }

    public SceneNode getFromNode() { return fromNode; }
    public SceneNode getToNode() { return toNode; }
    public NodeChoice getChoice() { return choice; }
}