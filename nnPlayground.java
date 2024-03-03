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
        Sequence model = new Sequence(new Layer[]{
            new Layer()
        })


        
    }
}
