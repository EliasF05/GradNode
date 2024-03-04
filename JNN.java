public class JNN{

    private Layer[] layers;

    /**
     * Creates Neural Network with specified hidden layers
     * @param layers hidden layers 
     */
    public JNN(Layer[] layers){
        this.layers = layers;
    }

    /**
     * 
     * @param in input feeding into model 
     * @return predicted values
     */
    public GradVec forward(GradVec in){
        GradVec res = in;
        for (Layer layer: layers){
            res = layer.forward(res);
        }
        return res;
    }

    /**
     * applies the following change to all parameters of model:  data = data-learning_rate*gradient
     * @param learning_rate factor to be applied to gradient before subtracting from value
     */
    public void step(double learning_rate){
        for (Layer layer: layers){
            layer.step(learning_rate);
        }
    }

    /**
     * put output of specified layer through an activation function
     * @param layerIndex index of layer desired in array of hidden layers (0-indexed)
     * @param function desired activation function
     */
    public void activate(int layerIndex, Activation function){
        layers[layerIndex].activate(function);
    }

    /**
     * 
     * @return all layers of model ar array of Layer objects 
     */
    public Layer[] getLayers(){
        return layers;
    }

    @Override
    public String toString(){
        String res = "";
        for (Layer layer: layers){
            res = res+"Layer: "+layer.shape();
        }
        return res;
    }
}