public class Linear implements Layer{

    private GradMat weights;
    private GradVec biases;

    /**
     * 
     * @param in_features amount of attributes of input
     * @param out_features desired size of output
     */
    public Linear(int in_features, int out_features){
        this.weights = new GradMat(new int[]{out_features, in_features});
        this.biases = new GradVec(out_features);
    }

    /**
     * 
     * @param in_features amount of attributes of input
     * @param out_features desired size of output
     * @param bias determine wether an additive bias will be added to learning process on top of weights
     */
    public Linear(int in_features, int out_features, boolean bias){
        this.weights = new GradMat(new int[]{in_features, out_features});
        if (bias) {this.biases = new GradVec(out_features);}
        else {this.biases = GradVec.zeros(out_features);}
    }

    /**
     * @param in input feeding into layer
     * @return result of linear transformation: weights*(in) + biases (if bias=True) 
     */
    public GradVec forward(GradVec in){
        return (weights.matmul(in)).add(biases);
    }

    /**
     * applies the following change to all parameters of layer: data = data-learning_rate*gradient
     * @param learning_rate factor to be applied to gradient before subtracting from value
     */
    public void step(double learning_rate){
        for (GradNode weight: weights.flat()){
            weight.setData(weight.getData()-weight.getGrad()*learning_rate);
        }
    }
    
    /**
     * 
     * @return all parameters of layer as array of GradNode objects
     */
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

    /**
     * @return GradMat of weights
     */
    public GradMat getWeights(){
        return weights;
    }

    /**
     * @return GradVec of biases
     */
    public GradVec getBiases(){
        return biases;
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

    public String shape(){
        return "in_features="+weights.shape()[0]+", out_features="+weights.shape()[1];
    }

}
