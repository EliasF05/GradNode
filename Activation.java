/**
 * interface for activation functions to be added to Layer and JNN objects
 */
public interface Activation {
    public GradVec forward(GradVec in);
}

/**
 * identity function (default): f(x) = x
 */
class Identity implements Activation{
    public GradVec forward(GradVec in){
        return in;
    }
}

/**
 * rectified linear unit: f(x) = max(x,0)
 */
class ReLU implements Activation{
    public GradVec forward(GradVec in){
        GradVec res = GradVec.zeros(in.getValues().length);
        for (int i = 0; i<in.getValues().length; i++){
            res.setValue(i,GradNode.max(in.getValue(i), new GradNode(0)));
        }
        return res;
    }
}

/**
 * sigmoid function: f(x) = 1/(1-e^(-x))
 */
class Sigmoid implements Activation{
    public GradVec forward(GradVec in){
        GradVec res = GradVec.zeros(in.getValues().length);
        for (int i = 0; i<in.getValues().length; i++){
            res.setValue(i, new GradNode(1.0).div(new GradNode(1).sub((new GradNode(0.0).sub(in.getValue(i))).exp())));
        }
        return res;
    }
}

