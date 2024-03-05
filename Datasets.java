import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

// To not exceed GitHub's file size limit of 100 MB, i had to cutoff some of the last rows of the previously 104.5 MB mnist_train.csv file

public class Datasets {

    private static Random myRand = new Random();
    private static String digitsTrain = "Data/mnist_train.csv";
    private static String digitsTest = "Data/mnist_test.csv";

    public static GradVec[] digits_yTrain_one_hot = null;
    public static GradVec[] digits_xTrain = null;

    public static GradVec[] digits_yTest_one_hot = null;
    public static GradVec[] digits_xTest = null;
    
    /**
     * read mnist data into public fields, labels get one_hot encoded. Had to do some compromise in not reading in all the data,
     * as i'm running this on a somewhat slow laptop, and after 42000 rows, it starts struggling.
     */
    public static void readDigits(){
        // Start with trainset
        GradNode[] firstColumn = new GradNode[40000];
        GradVec[] otherColumns = new GradVec[40000];
        int rowIndex = 0;
        String csvSplitBy = ",";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(digitsTrain))){
            while ((line = br.readLine()) !=null&&rowIndex<40000){
                String[] columns = line.split(csvSplitBy);
                firstColumn[rowIndex] = new GradNode(Integer.parseInt(columns[0]));
                otherColumns[rowIndex] = new GradVec(784);
                for (int i = 1; i < columns.length; i++) {
                    otherColumns[rowIndex].setValue(i-1, new GradNode(Integer.parseInt(columns[i])));
                }
                rowIndex++;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        GradVec[] res = new GradVec[40000];
        
        int i = 0;
        for (GradNode node: firstColumn){
            res[i] = GradVec.one_hot(node, 10);
            i+=1;
        }
        digits_yTrain_one_hot = res;
        digits_xTrain = otherColumns;

        // testset
        firstColumn = new GradNode[2000];
        otherColumns = new GradVec[2000];
        rowIndex = 0;
        String line2;

        try (BufferedReader br = new BufferedReader(new FileReader(digitsTrain))){
            while ((line2 = br.readLine()) !=null&&rowIndex<2000){
                String[] columns = line2.split(csvSplitBy);
                firstColumn[rowIndex] = new GradNode(Integer.parseInt(columns[0]));
                otherColumns[rowIndex] = new GradVec(784);
                for (i = 1; i < columns.length; i++) {
                    otherColumns[rowIndex].setValue(i-1, new GradNode(Integer.parseInt(columns[i])));
                }
                rowIndex++;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        res = new GradVec[2000];
        i = 0;
        for (GradNode node: firstColumn){
            res[i] = GradVec.one_hot(node, 10);
            i+=1;
        }
        digits_yTest_one_hot = res;
        digits_xTest = otherColumns;
    }

    /**
     * 
     * @param batch_size size of batch
     * @param max maximum index available
     * @return random indices to create batch
     */
    public static int[] batch_indices(int batch_size, int max){
        int[] res = new int[batch_size];
        for (int i = 0; i<res.length; i++){
            res[i] = myRand.nextInt(0,max);
        }
        return res;
    }

    /**
     * 
     * @param batch_size desired batch size
     * @return array of x values at index 0, y values at index 1
     */
    public static GradVec[][] digits_train_batch(int batch_size){
        GradVec[] resX = new GradVec[batch_size];
        GradVec[] resY = new GradVec[batch_size];
        int[] indices = batch_indices(batch_size, 40000);
        for (int i = 0; i<resX.length; i++){
            resX[i] = digits_xTrain[indices[i]];
            resY[i] = digits_yTrain_one_hot[indices[i]];
        }
        return new GradVec[][]{resX, resY};
    }

    /**
     * 
     * @param batch_size desired batch size
     * @return array of x vales at index 0, y values at index 1
     */
    public static GradVec[][] digits_test_batch(int batch_size){
        GradVec[] resX = new GradVec[batch_size];
        GradVec[] resY = new GradVec[batch_size];
        int[] indices = batch_indices(batch_size, 2000);
        for (int i = 0; i<resX.length; i++){
            resX[i] = digits_xTrain[indices[i]];
            resY[i] = digits_yTrain_one_hot[indices[i]];
        }
        return new GradVec[][]{resX, resY};
    }
    public static void main(String[] args) {
        readDigits();
        System.out.println(digits_xTest.length+" "+digits_yTest_one_hot.length);
        System.out.println(digits_xTrain.length+" "+digits_yTrain_one_hot.length);
    }
}
