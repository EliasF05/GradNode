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
 * custom rectified linear unit
 */
class CustomReLU implements Activation{

    private double upper;
    private double lower;

    /**
     * clipping values at -1 and 1
     */
    public CustomReLU(){
        this.upper = 1;
        this.lower = -1;
    }

    /**
     * clipping values at specified bounds
     * @param lower lowerbound
     * @param upper upperbound
     */
    public CustomReLU(double lower, double upper){
        this.lower = lower;
        this.upper = upper;
    }

    public GradVec forward(GradVec in){
        GradVec res = GradVec.zeros(in.getValues().length);
        for (int i = 0; i<in.getValues().length; i++){
            if (in.getValue(i).data()<lower){
                res.setValue(i, new GradNode(lower));
            }
            else if (in.getValue(i).data()>upper){
                res.setValue(i, new GradNode(upper));
            }
            else{
                res.setValue(i, in.getValue(i));
            }
        }
        return res;
    }
}

/**
 * sigmoid function: f(x) = 1/(1-e^(-x)). To prevent numerical overflow or underflow issues,
 * we clip the inputs using a rectified linear unit between -100, and 100
 */
class Sigmoid implements Activation{

    private static CustomReLU sigReLU = new CustomReLU(-10, 10);

    public GradVec forward(GradVec in){
        GradVec clipped = sigReLU.forward(in);
        GradVec res = GradVec.zeros(in.getValues().length);
        for (int i = 0; i<in.getValues().length; i++){
                res.setValue(i, new GradNode(1.0).div(new GradNode(1).add((new GradNode(0.0).sub(clipped.getValue(i))).exp())));
        }
        if (Double.isNaN(res.getValue(0).data())||res.getValue(0).abs().data()>1.1){
            throw new IllegalArgumentException("NaN Error present in sigmoid activation"+res+clipped);
        }
        return res;
    }
}

