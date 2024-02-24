public class Playground {
    public static void main(String[] args) {
        
        GradMat trainX = new GradMat(new double[][]{
                                            {0.0,1.0},
                                            {1.0,0.0},
                                            {1.0,0.0},
                                            {0.0,1.0},
                                            {0.0,1.0}});
        GradVec trainY = new GradVec(new double[]{-1,1,1,-1,-1});
        GradVec transFo = new GradVec(2);

        // Forward Pass
        GradNode loss = new GradNode(0);
        for (int i = 0; i<trainX.shape()[0]; i++){
            GradNode yPred = trainX.getValues(i).dot(transFo);
            GradNode yLabel = trainY.getValues()[i];
            if (yLabel.getData()>yPred.getData()){
                loss = loss.add(yLabel.sub(yPred));
            }
            else{
                loss = loss.add(yPred.sub(yLabel));
            }
        }
        // Backpropagation
        loss.backward();
        System.out.println(transFo);
        System.out.println(loss);
        double lr = 0.01;
        for (GradNode param: transFo.getValues()){
            param.setData(param.getData()-param.getGrad()*lr);
        }
        

        // Recalculate Loss
        loss = new GradNode(0);
        for (int i = 0; i<trainX.shape()[0]; i++){
            GradNode yPred = trainX.getValues(i).dot(transFo);
            GradNode yLabel = trainY.getValues()[i];
            if (yLabel.getData()>yPred.getData()){
                loss = loss.add(yLabel.sub(yPred));
            }
            else{
                loss = loss.add(yPred.sub(yLabel));
            }
        }

        loss.zeroGrad(); // Reset Gradients
        System.out.println(transFo);
        loss.backward();
        System.out.println(transFo);
        System.out.println(loss);


    }
}
