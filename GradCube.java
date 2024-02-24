// Under Construction

import java.util.Arrays;

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
            GradMat res = GradMat.zeros(new int[]{shape()[1], shape()[2]});
            for (int i = 0; i<shape()[0]; i++){
                res = res.add(values[i]);
            }
            return res;
        }
        if (axis==1){
            GradMat res = GradMat.zeros(new int[]{shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res = res.add(pulled[i]);
            }
            return res;
        }
        GradMat res = GradMat.zeros(new int[]{shape()[0], shape()[1]});
        GradMat[] pulled = mats(2);
        for (int i = 0; i<pulled.length; i++){
            res = res.add(pulled[i]);
        }
        return res;
    }

    public GradMat mean(int axis){
        return sum(axis).div(new GradNode(shape()[axis]));
    }

    public GradMat[] mats(int axis){
        if (axis==0){
            return values;
        }
        if (axis==1){
            GradMat[] res = new GradMat[shape()[1]];
            for (int i = 0; i<shape()[1]; i++){
                GradMat cur = new GradMat(new int[]{shape()[0], shape()[2]});
                for (int j = 0; j<shape()[0]; j++){
                    cur.setGradVec(values[j].getValues(i), j);
                }
                res[i] = cur;
            }
            return res;
        }
        GradMat[] res = new GradMat[shape()[2]];
        for (int i = 0; i<shape()[2]; i++){
            GradMat cur = new GradMat(new int[]{shape()[0], shape()[1]});
            for (int j = 0; j<shape()[0]; j++){
                cur.setGradVec(values[j].col(i), j);
            }
            res[i] = cur;
        }
        return res;
    }

    public GradCube matmul(GradCube other){
        if (shape()[0]!=other.shape()[0]||shape()[2]!=other.shape()[1]){
            throw new IllegalArgumentException("Cannot perform matrix multiplication on shapes "+Arrays.toString(shape())+" and "+Arrays.toString(other.shape()));
        }
        GradCube resCube = GradCube.zeros(new int[]{shape()[0], shape()[1], other.shape()[2]});
        for (int i = 0; i<shape()[0]; i++){
            resCube.setMat(values[i].matmul(other.getMat(i)), i);
        }
        return resCube;
    }
    
    public GradCube add(GradCube other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(other.getMat(i).add(getMat(i)), i);
        }
        return res;
    }

    public GradCube sub(GradCube other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(other.getMat(i).sub(getMat(i)), i);
        }
        return res;
    }

    public GradCube mul(GradCube other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible. If you would like matrix multiplication call GradCube.matmul(GradCube).");
        }
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(other.getMat(i).mul(getMat(i)), i);
        }
        return res;
    }

    public GradCube div(GradCube other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(other.getMat(i).div(getMat(i)), i);
        }
        return res;
    }
    
    public GradCube add(GradMat other){
        //Broadcasting
        if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].add(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(shape());
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].add(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(shape());
        }
    }
    
    public void setMat(GradMat newMat, int index){
        values[index] = newMat;
    }

    public GradMat getMat(int index){
        return values[index];
    }

    public int[] shape(){
        return new int[]{values.length, values[0].getValVecs().length, values[0].getValVecs()[0].getValues().length};
    }

    public static GradCube zeros(int[] shape){
        GradCube res = new GradCube(shape);
        for (int i = 0; i<shape[0]; i++){
            res.setMat(GradMat.zeros(new int[]{shape[1], shape[2]}), i);
        }
        return res;
    }

    @Override
    public String toString(){
        String res = "GradCube{";
        for (GradMat mat: values){
            res = res+mat+"\n";
        }
        return res+"}\n";
    }
    public static void main(String[] args) {
        GradCube myCube = new GradCube(new double[][][]{
            {{1,2},
             {3,4}},
            {{5,6},
             {7,8}}
        });
        System.out.println(Arrays.toString(myCube.mats(0)));
        System.out.println(Arrays.toString(myCube.mats(1)));
        System.out.println(Arrays.toString(myCube.mats(2)));
    }

}
