public class Loss {

    public static GradNode SE(GradNode yPred, GradNode yLabels){
        return (yPred.sub(yLabels)).pow(new GradNode(2));
    }

    public static GradNode SE(GradVec yPred, GradVec yLabels){
        return (yPred.sub(yLabels).pow(new GradNode(2))).sum();
    }

    public static GradNode SE(GradVec yPred, GradNode yLabels){
        return SE(yPred.asNode(), yLabels);
    }

    public static GradNode SE(GradMat yPred, GradVec yLabels){
        return SE(yPred.asVec(), yLabels);
    }

    public static GradNode MSE(GradNode[] yPred, GradNode[] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    public static GradNode MSE(GradVec [] yPred, GradVec [] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    public static GradNode MSE(GradVec [] yPred, GradNode [] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    public static GradNode MSE(GradMat [] yPred, GradVec [] yLabels){
        GradNode total = new GradNode(0.0);
        for (int i = 0; i<yPred.length; i++){
            total = total.add(SE(yPred[i], yLabels[i]));
        }
        return total.div(new GradNode(yPred.length));
    }

    

}
