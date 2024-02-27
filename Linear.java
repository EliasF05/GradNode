public class Linear implements Layer{

    private GradMat weights;
    private GradVec biases;

    public Linear(int in_features, int out_features){
        this.weights = new GradMat(new int[]{in_features, out_features});
        this.biases = new GradVec(out_features);
    }
    public Linear(int in_features, int out_features, boolean bias){
        this.weights = new GradMat(new int[]{in_features, out_features});
        if (bias) {this.biases = new GradVec(out_features);}
        else {this.biases = GradVec.zeros(out_features);}
    }
    public GradPriz forward(GradPriz in){
        return in.matmul(weights).add(biases);
    }
    public GradMat forward(GradMat in){
        return in.matmul(weights).add(biases);
    }
    
    public GradVec forward(GradVec in){
        return in.mul(weights.col(0)).add(biases);
    }
    public GradNode forward(GradNode in){
        return in.mul(weights.getValue(0, 0)).add(biases.getValue(0));
    }

    public GradNode[] params(){
        GradNode[] res = new GradNode[weights.shape()[0]*weights.shape()[1]+biases.getValues().length];
        int idx = 0;
        for (GradNode node: weights.flat()){
            res[idx] = node;
            idx+=1;
        }
        for (GradNode node: biases.getValues()){
            res[idx] = node;
            idx+=1;
        }
        return res;
    }

    @Override
    public String toString(){
        String res = "Linear{Weights{";
        for (GradNode node: weights.flat()){
            res = res+node;
        }
        res = res+"}, Biases{";
        for (GradNode node: biases.getValues()){
            res = res+node;
        }
        return res+"}}";
    }

}
