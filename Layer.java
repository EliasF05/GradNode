/**
 * Interface for Layer types to implement to be used in JNN Models
 */
public interface Layer {
    public GradVec forward(GradVec in);
    public GradMat getWeights();
    public GradVec getBiases();
    public String shape();
    public void step(double learning_rate);
}

