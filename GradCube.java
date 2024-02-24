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

    public GradMat sum(int axis){
        if (axis==0){
            GradMat res = new GradMat(new double[shape()[1]][shape()[2]]);
            for (int i = )
        }
    }

    public int[] shape(){
        return new int[]{values.length, values[0].getValVecs().length, values[0].getValVecs()[0].getValues().length};
    }

}
