package Tic_Tac_Atrebas.jav.Neural;

import java.io.*;

public class Connection implements Serializable {
    private static final long serialVersionUID = 1L;

    public int innovationId;  // Globale eindeutige ID (für NEAT)
    public NeuralNode fromNode;
    public NeuralNode toNode;
    public double weight;
    public boolean enabled;

    public Connection(int innovationId, NeuralNode fromNode, NeuralNode toNode, double weight) {
        this.innovationId = innovationId;
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
        this.enabled = true;
    }

    public double transmit(double signal) {
        if (!enabled) return 0.0;
        return signal * weight;
    }

    public void mutateWeight(double mutationRate, double mutationStrength) {
        if (Math.random() < mutationRate) {
            weight += (Math.random() * 2 - 1) * mutationStrength;
            weight = Math.max(-5, Math.min(5, weight)); // Clamp
        }
    }

    public void toggleEnabled(double toggleRate) {
        if (Math.random() < toggleRate) {
            enabled = !enabled;
        }
    }

    @Override
    public String toString() {
        return String.format("Connection[%d -> %d, w=%.2f, enabled=%s]",
            fromNode.id, toNode.id, weight, enabled);
    }
}

