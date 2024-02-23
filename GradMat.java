import java.util.Arrays;

public class GradMat {

    private GradVec[] values;
    
    public GradMat(double[][] Data){
        values = new GradVec[Data.length];
        for (int i =0 ; i<Data.length; i++){
            values[i] = new GradVec(Data[i]);
        }
    }
    
    public GradMat(int[] shape){
        values = new GradVec[shape[0]];
        for (int i = 0; i<shape[0]; i++){
            values[i] = new GradVec(shape[1]);
        }
    }
    
    public GradVec sum(int axis){
        if (axis==0){
            GradVec res = new GradVec(new double[this.values[0].getValues().length]);
            for (int i = 0; i<this.values[0].getValues().length; i++){
                GradNode colISum = new GradNode(0);
                for (int j = 0; j<this.values.length; j++){
                    colISum = colISum.add(this.values[i].getValues()[j]);
                }
                res.setValue(i, colISum);
            }
            return res;
        }
        else{
            GradVec res = new GradVec(new double[this.values.length]);
            for (int i =0 ; i<this.values.length; i++){
                GradNode rowISum = new GradNode(0);
                for (int j = 0; j<this.values[0].getValues().length; j++){
                    rowISum = rowISum.add(this.values[i].getValues()[j]);
                }
                res.setValue(i, rowISum);
            }
            return res;
        }
    }

    public GradMat mul(GradMat other){
        if (this.shape()[1]!=other.shape()[0]){
            throw new IllegalArgumentException("matrix multiplication cannot be performed with shapes: "+Arrays.toString(this.shape())+" and "+Arrays.toString(other.shape()));
        }
        GradMat res = new GradMat(new double[this.shape()[0]][other.shape()[1]]);
        for (int i = 0; i<this.shape()[0]; i++){
            for (int j = 0; j<other.shape()[1]; j++){
                res.getValues(i).setValue(j, this.values[i].dot(other.col(j)));
            }
        }
        return res;

    }
    
    public GradVec getValues(int index){
        return this.values[index];
    }
    
    public GradVec[] getValVecs(){
        return this.values;
    }
    
    public GradVec col(int colIndex){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i =0 ; i<res.getValues().length; i++){
            res.setValue(i, this.values[i].getValues()[colIndex]);
        }
        return res;
    }

    public int[] shape(){
        return new int[]{this.values.length, this.values[0].getValues().length};
    }
    
    public String toString(){
        String res = "GradMat{";
        for (GradVec row : values){
            res = res+row+"\n";
        }
        return res+"}\n";
    }
    public static void main(String[] args) {
        GradMat mat = new GradMat(new double[][]{{1,2,3},
                                                         {4,5,6}});
        GradMat otherMat = new GradMat(new double[][]{{1,2,3,4},
                                                              {5,6,7,8},
                                                              {9,10,11,12}});
        GradMat res = mat.mul(otherMat);
        GradVec resRow = res.sum(1);
        GradNode resVal = resRow.sum();
        GradNode res2 = new GradNode(100);
        GradNode res3 = resVal.add(res2);
        resVal.backward();
        System.out.println(mat);
        System.out.println(otherMat);
        System.out.println(res);
        System.out.println(resRow);
        System.out.println(resVal);
        System.out.println(res2);
        System.out.println(res3);
    }
}
