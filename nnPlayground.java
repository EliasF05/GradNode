public class nnPlayground {
    public static void main(String[] args) {
        GradVec[] trainX = new GradVec[]{
            new GradVec(new double[]{
                1,0
            }),
            new GradVec(new double[]{
                1,0
            }),
            new GradVec(new double[]{
                0,1
            })
        };
        GradVec[] trainY = {new GradVec(new double[]{1.0}), new GradVec(new double[]{1.0}), new GradVec(new double[]{0.0})};
        Linear myLayer = new Linear(2, 1);
        JNN myModel = new JNN(new Layer[]{
            myLayer,
        });
        GradNode loss = Loss.MSE(trainY, myModel, trainX);
        loss.backward();
        System.out.println(loss);
        System.out.println(myLayer);
        double lr = 0.01;
        for (GradNode param: myLayer.params()){
            param.setData(param.getData()-(lr*param.getGrad()));
        }
        loss.zeroGrad();
        loss = Loss.MSE(trainY, myModel, trainX);
        System.out.println(loss);
        System.out.println(myLayer);
        
    }
}
