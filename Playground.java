public class Playground {
    public static void main(String[] args) {
        GradNodeMat trainX = new GradNodeMat(new double[][]{
                                            {0,1},
                                            {0.6,0},
                                            {0.9,0},
                                            {0, 1},
                                            {0, 1}});
        GradNodeVec trainY = new GradNodeVec(new double[]{0,10,10,0,0});
        GradNodeVec transFo = new GradNodeVec(2);

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
        double lr = 0.1;
        System.out.println(transFo);
        for (GradNode param: transFo.getValues()){
            param.setData(param.getData()-param.getGrad()*lr);
        }
        System.out.println(transFo);
        System.out.println(loss);

    }
}
