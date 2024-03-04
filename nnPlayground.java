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
        
        // Let's look at a training Loop:
        int epochs = 100;
        double learning_rate = 0.01;

        GradNode loss = new GradNode(0.0);

        for (int i = 1; i<=epochs; i++){

            // Reset Gradients
            loss.zeroGrad();

            // Calculate Loss
            loss = Loss.MSE(trainY, myModel, trainX);
            if (i%10==0){System.out.println("Epoch: "+i+ ", Loss: "+loss.getData());}
            
            // Backpropagate
            loss.backward();

            // Update Parameters
            myModel.step(learning_rate);
        }
        
    }
}
