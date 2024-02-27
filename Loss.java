public class Loss {

    public GradNode MSE(GradNode yPred, GradNode yLabels){
        return (yPred.sub(yLabels)).pow(new GradNode(2));
    }

    public GradNode MSE(GradNode)

}
