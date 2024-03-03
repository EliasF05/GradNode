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
        GradNode loss = new GradNode(0.0);
        for (int i = 0; i<trainX.length; i++){
            loss = loss.add(Loss.SE(myLayer.forward(trainX[i]), trainY[i]));
        }
        loss.backward();
        System.out.println(loss);
        System.out.println(myLayer);
        double lr = 0.01;
        for (GradNode param: myLayer.params()){
            param.setData(param.getData()-(lr*param.getGrad()));
        }
        loss = new GradNode(0.0);
        for (int i = 0; i<trainX.length; i++){
            loss = loss.add(Loss.SE(myLayer.forward(trainX[i]), trainY[i]));
        }
        System.out.println(loss);
        System.out.println(myLayer);
        
    }
}
