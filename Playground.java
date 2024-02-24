public class Playground {
    public static void main(String[] args) {
        
        GradMat[] trainX = new GradMat[]{
            new GradMat(new double[][]{
                {0,0,1},
                {1,0,0}
            }),
            new GradMat(new double[][]{
                {1,0,0},
                {0,0,1}
            }),
            new GradMat(new double[][]{
                {0,1,0},
                {0,1,0}
            })
        };
        GradMat[] trainY = new GradMat[]{
            new GradMat(new double[][]{
                {1,-1},
                {-1,1}
            }),
            new GradMat(new double[][]{
                {-1,1},
                {1,-1}
            }),
            new GradMat(new double[][]{
                {0,0},
                {0,0}
            })
        };

        GradMat weights = new GradMat(new int[]{3,2});
        GradMat biases = new GradMat(new int[]{2,2});

        // Forward Pass
        GradNode loss = new GradNode(0);
        
        for (int i = 0; i<trainX.length; i++){
            GradMat yPred = (trainX[i].matmul(weights)).add(biases);
            GradMat yLabel = trainY[i];
            GradMat diff = yPred.sub(yLabel);
            for (GradNode node: diff.flat()){
                loss = loss.add(node.abs());
            }
        }

        // Backpropagation
        loss.backward();
        System.out.println("Weights Pre Training: "+weights);
        System.out.println("Biases Pre Training: "+biases);
        System.out.println("Loss Pre Training"+loss);

        // Parameter Update
        double lr = 0.01;
        for (GradNode param: weights.flat()){
            param.setData(param.getData()-param.getGrad()*lr);
        }
        for (GradNode param: biases.flat()){
            param.setData(param.getData()-param.getGrad()*lr);
        }
        
        // Recalculate Loss
        loss = new GradNode(0);

        for (int i = 0; i<trainX.length; i++){
            GradMat yPred = (trainX[i].matmul(weights)).add(biases);
            GradMat yLabel = trainY[i];
            GradMat diff = yPred.sub(yLabel);
            for (GradNode node: diff.flat()){
                loss = loss.add(node.abs());
            }
        }

        loss.zeroGrad(); // Reset Gradients
        loss.backward();
        System.out.println("Weights Post Training: "+weights);
        System.out.println("Biases Post Training: "+biases);
        System.out.println("Loss Post Training: "+loss);


    }
}
