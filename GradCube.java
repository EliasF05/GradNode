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

    public GradCube(GradMat[] Data){
        this.values = Data;
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
            res.setMat(getMat(i).add(other.getMat(i)), i);
        }
        return res;
    }

    public GradCube sub(GradCube other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(getMat(i).sub(other.getMat(i)), i);
        }
        return res;
    }

    public GradCube mul(GradCube other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(getMat(i).mul(other.getMat(i)), i);
        }
        return res;
    }

    public GradCube div(GradCube other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(getMat(i).div(other.getMat(i)), i);
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
            GradCube res = new GradCube(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].add(other), i);
            }
            return new GradCube(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradCube res = new GradCube(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].add(other), i);
            }
            return new GradCube(new GradCube(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }

    public GradCube sub(GradMat other){
         //Broadcasting
         if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].sub(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].sub(other), i);
            }
            return new GradCube(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradCube res = new GradCube(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].sub(other), i);
            }
            return new GradCube(new GradCube(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }

    public GradCube mul(GradMat other){
         //Broadcasting
         if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].mul(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].mul(other), i);
            }
            return new GradCube(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradCube res = new GradCube(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].mul(other), i);
            }
            return new GradCube(new GradCube(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }

    public GradCube div(GradMat other){
         //Broadcasting
         if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].div(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradCube res = new GradCube(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].div(other), i);
            }
            return new GradCube(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradCube res = new GradCube(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].div(other), i);
            }
            return new GradCube(new GradCube(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }
    
    public GradCube add(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                for (int j = 0; j<shape()[1]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(i, j, k).add(other.getValue(i)), i, j, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[1]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[1]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(j, i, k).add(other.getValue(i)), j, i, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[2]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[1]; k++){
                        res.setValue(getValue(j, k, i).add(other.getValue(i)), j, k, i);
                    }
                }
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto shape "+Arrays.toString(shape()));
    }

    public GradCube sub(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                for (int j = 0; j<shape()[1]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(i, j, k).sub(other.getValue(i)), i, j, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[1]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[1]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(j, i, k).sub(other.getValue(i)), j, i, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[2]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[1]; k++){
                        res.setValue(getValue(j, k, i).sub(other.getValue(i)), j, k, i);
                    }
                }
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto shape "+Arrays.toString(shape()));
    }

    public GradCube mul(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                for (int j = 0; j<shape()[1]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(i, j, k).mul(other.getValue(i)), i, j, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[1]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[1]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(j, i, k).mul(other.getValue(i)), j, i, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[2]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[1]; k++){
                        res.setValue(getValue(j, k, i).mul(other.getValue(i)), j, k, i);
                    }
                }
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto shape "+Arrays.toString(shape()));
    }

    public GradCube div(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[0]; i++){
                for (int j = 0; j<shape()[1]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(i, j, k).div(other.getValue(i)), i, j, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[1]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[1]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[2]; k++){
                        res.setValue(getValue(j, i, k).div(other.getValue(i)), j, i, k);
                    }
                }
            }
            return res;
        }
        if (other.getValues().length==shape()[2]){
            GradCube res = new GradCube(shape());
            for (int i = 0; i<shape()[2]; i++){
                for (int j = 0; j<shape()[0]; j++){
                    for (int k = 0; k<shape()[1]; k++){
                        res.setValue(getValue(j, k, i).div(other.getValue(i)), j, k, i);
                    }
                }
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto shape "+Arrays.toString(shape()));
    }

    public GradCube add(GradNode other){
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).add(other), i, j, k);
                }
            }
        }
        return res;
    }

    public GradCube sub(GradNode other){
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).sub(other), i, j, k);
                }
            }
        }
        return res;
    }

    public GradCube mul(GradNode other){
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).mul(other), i, j, k);
                }
            }
        }
        return res;
    }

    public GradCube div(GradNode other){
        GradCube res = new GradCube(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).div(other), i, j, k);
                }
            }
        }
        return res;
    }

    public GradNode[] flat(){
        GradNode[] res = new GradNode[shape()[0]*shape()[1]*shape()[2]];
        int idx = 0;
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res[idx] = getValue(i, j, k);
                    idx+=1;
                }
            }
        }
        return res;
    }
    
    public void setValue(GradNode newNode, int dim0, int dim1, int dim2){
        values[dim0].setValue(newNode, dim1, dim2);
    }

    public GradNode getValue(int dim0, int dim1, int dim2){
        return values[dim0].getValue(dim1, dim2);
    }

    public GradMat[] getValMats(){
        return values;
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
            {{1,2,3,4},
             {3,4,5,6},
             {7,8,9,0}},
            {{5,6,7,8},
             {7,8,9,0},
             {1,2,3,4}}
        });
        GradMat myMat = new GradMat(new double[][]{
            {1,1,1},
            {2,2,2}
        });
        GradCube res = myCube.add(myMat);
        System.out.println(res);
    }

}
