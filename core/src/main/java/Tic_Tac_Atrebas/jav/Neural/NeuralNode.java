package Tic_Tac_Atrebas.jav.Neural;

import java.io.*;

public class NeuralNode implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;                          // Eindeutige Node-ID
    public double bias;                     // Bias-Wert
    public double activation;               // Aktuelle Aktivierung
    public NodeType type;                   // INPUT, HIDDEN, OUTPUT
    public ActivationFunction activationFn; // Sigmoid, ReLU, etc.

    // Verbindungen (nur ausgehende)
    public transient java.util.List<Connection> outgoingConnections;
    public transient int fitness;
    public enum NodeType {
        INPUT, HIDDEN, OUTPUT
    }

    public enum ActivationFunction {
        SIGMOID, RELU, TANH, LINEAR
    }

    public NeuralNode(int id, NodeType type, ActivationFunction activationFn) {
        this.id = id;
        this.type = type;
        this.activationFn = activationFn;
        this.bias = Math.random() * 2 - 1;  // Random bias [-1, 1]
        this.activation = 0.0;
        this.outgoingConnections = new java.util.ArrayList<>();
    }

    public double activate(double input) {
        double sum = input + bias;
        this.activation = applyActivation(sum);
        return this.activation;
    }

    private double applyActivation(double x) {
        switch (activationFn) {
            case SIGMOID:
                return 1.0 / (1.0 + Math.exp(-x));
            case RELU:
                return Math.max(0, x);
            case TANH:
                return Math.tanh(x);
            case LINEAR:
                return x;
            default:
                return x;
        }
    }

    public void addConnection(Connection conn) {
        outgoingConnections.add(conn);
    }

    public void removeConnection(Connection conn) {
        outgoingConnections.remove(conn);
    }

    public Connection getConnection(int targetNodeId) {
        for (Connection conn : outgoingConnections) {
            if (conn.toNode.id == targetNodeId) {
                return conn;
            }
        }
        return null;
    }


    public void mutateBias(double mutationRate, double mutationStrength) {
        if (Math.random() < mutationRate) {
            bias += (Math.random() * 2 - 1) * mutationStrength;
            bias = Math.max(-5, Math.min(5, bias)); // Clamp
        }
    }

    public void mutateActivationFunction(double mutationRate) {
        if (Math.random() < mutationRate && type == NodeType.HIDDEN) {
            ActivationFunction[] functions = ActivationFunction.values();
            activationFn = functions[(int) (Math.random() * functions.length)];
        }
    }

    // ========== SERIALISIERUNG ==========
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        // outgoingConnections werden separat durch das Netz gespeichert
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.outgoingConnections = new java.util.ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("Node[id=%d, type=%s, bias=%.2f, activation=%.2f, connections=%d]",
            id, type, bias, activation, outgoingConnections.size());
    }
}
