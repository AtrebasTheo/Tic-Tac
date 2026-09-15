package Tic_Tac_Atrebas.jav.Neural;

import java.io.*;
import java.util.*;

public class NeuralNetwork implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<NeuralNode> nodes;
    private List<Connection> connections;
    private int nextNodeId;
    private int nextInnovationId;

    int fitness=0;

    private int inputCount;
    private int outputCount;

    // Mutations-Parameter
    public static double addNodeRate = 0.03;
    public static double removeNodeRate = 0.01;
    public static double addConnectionRate = 0.05;
    public static double removeConnectionRate = 0.02;
    public static double mutateWeightRate = 0.8;
    public static double toggleConnectionRate = 0.01;
    public static double fitnessKeepRate = 0.2;

    public NeuralNetwork(int inputCount, int outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.nextNodeId = 0;
        this.nextInnovationId = 0;

        initializeNetwork();
    }


    private void initializeNetwork() {
        // Input-Nodes
        for (int i = 0; i < inputCount; i++) {
            nodes.add(new NeuralNode(nextNodeId++, NeuralNode.NodeType.INPUT,
                NeuralNode.ActivationFunction.LINEAR));
        }

        // Output-Nodes
        for (int i = 0; i < outputCount; i++) {
            nodes.add(new NeuralNode(nextNodeId++, NeuralNode.NodeType.OUTPUT,
                NeuralNode.ActivationFunction.SIGMOID));
        }

        // Initiale Vollverbindung Input -> Output
        for (int i = 0; i < inputCount; i++) {
            for (int j = inputCount; j < inputCount + outputCount; j++) {
                addConnection(nodes.get(i), nodes.get(j));
            }
        }
    }

    // ========== NETZWERK-AUSFÜHRUNG ==========
    public double[] forward(double[] inputs) {
        if (inputs.length != inputCount) {
            throw new IllegalArgumentException("Input size mismatch");
        }

        // Input-Nodes setzen
        for (int i = 0; i < inputCount; i++) {
            nodes.get(i).activation = inputs[i];
        }

        // Hidden und Output Nodes aktivieren (topologisch sortiert)
        List<NeuralNode> sortedNodes = topologicalSort();
        for (NeuralNode node : sortedNodes) {
            if (node.type != NeuralNode.NodeType.INPUT) {
                double sum = 0;
                // Eingehende Verbindungen sammeln
                for (Connection conn : connections) {
                    if (conn.toNode == node && conn.enabled) {
                        sum += conn.transmit(conn.fromNode.activation);
                    }
                }
                node.activate(sum);
            }
        }

        // Output-Werte extrahieren
        double[] outputs = new double[outputCount];
        for (int i = 0; i < outputCount; i++) {
            outputs[i] = nodes.get(inputCount + i).activation;
        }
        return outputs;
    }

    // ========== TOPOLOGISCHE SORTIERUNG ==========
    private List<NeuralNode> topologicalSort() {
        Set<NeuralNode> visited = new HashSet<>();
        List<NeuralNode> sorted = new ArrayList<>();

        // Erst Input-Nodes
        for (NeuralNode node : nodes) {
            if (node.type == NeuralNode.NodeType.INPUT) {
                sorted.add(node);
                visited.add(node);
            }
        }

        // Dann Hidden und Output (einfache DFS)
        for (NeuralNode node : nodes) {
            if (!visited.contains(node)) {
                dfs(node, visited, sorted);
            }
        }

        return sorted;
    }

    private void dfs(NeuralNode node, Set<NeuralNode> visited, List<NeuralNode> sorted) {
        visited.add(node);
        sorted.add(node);
    }


    public void mutate() {
        // 1. Gewichte und Biase mutieren
        for (NeuralNode node : nodes) {
            node.mutateBias(mutateWeightRate * 0.5, 0.1);
            //node.mutateActivationFunction(0.01);
        }

        for (Connection conn : connections) {
            conn.mutateWeight(mutateWeightRate, 0.2);
            conn.toggleEnabled(toggleConnectionRate);
        }

        // 2. Nodes hinzufügen
        if (Math.random() < addNodeRate && connections.size() > 0) {
            addNodeMutation();
        }

        // 3. Nodes entfernen
        if (Math.random() < removeNodeRate && nodes.size() > inputCount + outputCount) {
            removeNodeMutation();
        }

        // 4. Verbindungen hinzufügen
        if (Math.random() < addConnectionRate) {
            addConnectionMutation();
        }

        // 5. Verbindungen entfernen
        if (Math.random() < removeConnectionRate && connections.size() > inputCount * outputCount) {
            removeConnectionMutation();
        }
    }


    private void addNodeMutation() {
        // Wähle zufällige Verbindung
        Connection conn = connections.get((int) (Math.random() * connections.size()));

        // Neue Node erstellen
        NeuralNode newNode = new NeuralNode(nextNodeId++, NeuralNode.NodeType.HIDDEN,
            NeuralNode.ActivationFunction.RELU);
        nodes.add(newNode);

        // Alte Verbindung deaktivieren
        conn.enabled = false;

        // Neue Verbindungen: fromNode -> newNode -> toNode
        Connection conn1 = new Connection(nextInnovationId++, conn.fromNode, newNode, 1.0);
        Connection conn2 = new Connection(nextInnovationId++, newNode, conn.toNode, conn.weight);

        conn1.fromNode.addConnection(conn1);
        conn2.fromNode.addConnection(conn2);

        connections.add(conn1);
        connections.add(conn2);
    }

    private void removeNodeMutation() {
        // Wähle zufälligen Hidden-Node
        List<NeuralNode> hiddenNodes = new ArrayList<>();
        for (NeuralNode node : nodes) {
            if (node.type == NeuralNode.NodeType.HIDDEN) {
                hiddenNodes.add(node);
            }
        }

        if (hiddenNodes.isEmpty()) return;

        NeuralNode nodeToRemove = hiddenNodes.get((int) (Math.random() * hiddenNodes.size()));

        // Alle Verbindungen entfernen
        connections.removeIf(conn -> conn.fromNode == nodeToRemove || conn.toNode == nodeToRemove);
        nodes.remove(nodeToRemove);
    }

    // ========== VERBINDUNGS-MUTATIONEN ==========
    private void addConnectionMutation() {
        NeuralNode from = nodes.get((int) (Math.random() * nodes.size()));
        NeuralNode to = nodes.get((int) (Math.random() * nodes.size()));

        // Keine Self-Loops, keine Rückwärts-Verbindungen
        if (from == to || from.type == NeuralNode.NodeType.OUTPUT) return;
        if (to.type == NeuralNode.NodeType.INPUT) return;

        // Prüfe, ob Verbindung bereits existiert
        if (from.getConnection(to.id) != null) return;

        addConnection(from, to);
    }

    private void addConnection(NeuralNode from, NeuralNode to) {
        Connection conn = new Connection(nextInnovationId++, from, to,
            Math.random() * 2 - 1);
        from.addConnection(conn);
        connections.add(conn);
    }

    private void removeConnectionMutation() {
        if (connections.isEmpty()) return;
        Connection conn = connections.get((int) (Math.random() * connections.size()));
        conn.fromNode.removeConnection(conn);
        connections.remove(conn);
    }

    public List<NeuralNode> getNodes() {
        return nodes;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getConnectionCount() {
        return connections.size();
    }

    @Override
    public String toString() {
        return String.format("TopologyNetwork[nodes=%d, connections=%d, inputs=%d, outputs=%d]",
            nodes.size(), connections.size(), inputCount, outputCount);
    }
}

