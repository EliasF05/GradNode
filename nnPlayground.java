public class nnPlayground {
    public static void main(String[] args) {
        GradMat[] trainX = new GradMat[]{
            new GradMat(new double[][]{
                {0,0},
                {1,0}
            }),
            new GradMat(new double[][]{
                {1,0},
                {0,0}
            }),
            new GradMat(new double[][]{
                {0,1},
                {0,1}
            })
        };
        GradVec[] trainY = {new GradVec(new double[]{1.0, 0.0}), new GradVec(new double[]{1.0, 0.0}), new GradVec(new double[]{0.0, 1.0})};
        int in_features = 2;
        int out_features = 1;
        Linear myLayer = new Linear(in_features, out_features);

        // Forward Pass
        GradNode loss = new GradNode(0.0);
        for (int i = 0; i<trainX.length; i++){
            GradMat yPred = myLayer.forward(trainX[i]);
            GradVec yLabel = trainY[i];
            loss = loss.add(Loss.SE(yPred, yLabel));
        }
        loss.backward();
        System.out.println(myLayer);
        System.out.println(loss);
        // Backpropagation
        double lr = 0.005;
        for (GradNode param : myLayer.params()){
            param.setData(param.getData()-param.getGrad()*lr);;
        }

        // Re-evaluate loss
        loss.zeroGrad();
        loss = new GradNode(0.0);
        for (int i = 0; i<trainX.length; i++){
            GradMat yPred = myLayer.forward(trainX[i]);
            GradVec yLabel = trainY[i];
            loss = loss.add(Loss.SE(yPred, yLabel));
        }
        loss.backward();
        System.out.println(myLayer);
        System.out.println(loss);


        
    }
}
