public class mnistPlayground {
    public static void main(String[] args) {

        // Prepare our data
        Datasets.readDigits();
        System.out.println("Read Successfull");

        // Let's build our model (784 pixels per image)
        Linear hidden1 = new Linear(784, 64);
        Linear hidden2 = new Linear(64, 28);
        Linear hidden3 = new Linear(28,10);

        // Let's add a non-linearity to the output to scale it between 0-1
        hidden1.activate(new Sigmoid());
        hidden2.activate(new Sigmoid());
        hidden3.activate(new Sigmoid());

        JNN digitModel = new JNN(new Layer[]{
            hidden1, 
            hidden2, 
            hidden3,
        });

        // Let's train our model
        int epochs = 200;
        double learning_rate = 0.01;
        int batch_size = 8;

        GradNode loss, testLoss;

        for (int i = 1; i<=epochs; i++){

            //Let's use batches
            GradVec[][] batch = Datasets.digits_train_batch(batch_size);
            GradVec[][] testBatch = Datasets.digits_test_batch(batch_size);
            System.out.println("Created batches in epoch "+i);

            //Calculate Loss (Forward Pass)
            loss = Loss.MSE(batch[1], digitModel, batch[0]);
            testLoss = Loss.MSE(testBatch[1], digitModel, testBatch[0]);
            System.out.println("Epoch: "+i+", Train Loss: "+loss.getData()+" Test Loss: "+testLoss.getData());

            // Backpropagate
            loss.backward();

            //Update Parameters
            digitModel.step(learning_rate);

            //Reset Gradients
            loss.zeroGrad();

            // Let's use learning rate decay
            if (i==100){
                learning_rate = 0.001;
            }
        }
    }
}
