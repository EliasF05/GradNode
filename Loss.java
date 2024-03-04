public class Loss {

    /**
     * 
     * @param yPred predicted outcome
     * @param yLabels actual outcome
     * @return squared error: (predicted-actual)**2
     */
    public static GradNode SE(GradNode yPred, GradNode yLabels){
        return (yPred.sub(yLabels)).pow(new GradNode(2));
    }

    /**
     * 
     * @param yPred predicted outcome
     * @param yLabels actual outcome
     * @return squared error: sum((predicted-actual)**2)
     */
    public static GradNode SE(GradVec yPred, GradVec yLabels){
        return (yPred.sub(yLabels).pow(new GradNode(2))).sum();
    }

    /**
     * 
     * @param yPred predicted outcome (GradVec of size 1)
     * @param yLabels actual outcome 
     * @return squared error: (predicted-actual)**2
     */
    public static GradNode SE(GradVec yPred, GradNode yLabels){
        return SE(yPred.asNode(), yLabels);
    }

    /**
     * 
     * @param yPred predicted outcome (GradMat of shape 1,n or n,1)
     * @param yLabels actual outcome
     * @return squared error: sum((predicted-actual)**2)
     */
    public static GradNode SE(GradMat yPred, GradVec yLabels){
        return SE(yPred.asVec(), yLabels);
    }

    /**
     * 
     * @param yPred predicted outcomes
     * @param yLabels actual outcomes
     * @return mean squared error: mean((predicted-actual)**2)
     */
    public static GradNode MSE(GradNode[] yPred, GradNode[] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    /**
     * performs forward pass and returns loss
     * @param trainY actual outcomes
     * @param Model model to fit
     * @param in input values
     * @return mean squared error resulting of forward pass
     */
    public static GradNode MSE(GradVec[] trainY, JNN Model, GradVec[] in){
        // Forward Pass
        GradNode loss = new GradNode(0.0);
        for (int i = 0; i<in.length; i++){
            loss = loss.add(SE(Model.forward(in[i]), trainY[i]));
        }
        return loss.div(new GradNode(in.length));
    }

    /**
     * 
     * @param yPred predicted outcomes
     * @param yLabels actual outcomes
     * @return mean squared error: mean((predicted-actual)**2)
     */
    public static GradNode MSE(GradVec [] yPred, GradVec [] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    /**
     * 
     * @param yPred predicted outcomes (GradVecs of size 1)
     * @param yLabels actual outcomes
     * @return mean squared error: mean((predicted-actual)**2)
     */
    public static GradNode MSE(GradVec [] yPred, GradNode [] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    /**
     * 
     * @param yPred predicted outcomes (GradMats of shape 1,n or n,1)
     * @param yLabels actual outcomes
     * @return mean squared error: mean((predicted-actual)**2)
     */
    public static GradNode MSE(GradMat [] yPred, GradVec [] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    

}
