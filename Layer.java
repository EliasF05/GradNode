public interface Layer {
    public GradPriz forward(GradPriz in);
    public GradMat forward(GradMat in);
    public GradVec forward(GradVec in);
    public GradNode forward(GradNode in);
}

