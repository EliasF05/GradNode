// Umder Construction

public class GradCube {

    private GradMat[] values;

    public GradCube(double[][][] Data){
        this.values = new GradMat[Data.length];
        for (int i =0; i<Data.length; i++){
            this.values[i] = new GradMat(Data[i]);
        }
    }

    public GradCube(int[] shape){
        this.values = new GradMat[shape[0]];
        for (int i =0 ; i<shape[0]; i++){
            values[i] = new GradMat(new int[]{shape[1], shape[2]});
        }
    }

    public int[] shape(){
        return new int[]{values.length, values[0].getValVecs().length, values[0].getValVecs()[0].getValues().length};
    }

}
