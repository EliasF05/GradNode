import java.util.Arrays;

public class GradNodeMat {

    private GradNodeVec[] values;
    public GradNodeMat(double[][] Data){
        values = new GradNodeVec[Data.length];
        for (int i =0 ; i<Data.length; i++){
            values[i] = new GradNodeVec(Data[i]);
        }
    }
    public GradNodeMat(int[] shape){
        values = new GradNodeVec[shape[0]];
        for (int i = 0; i<shape[0]; i++){
            values[i] = new GradNodeVec(shape[1]);
        }
    }
    public GradNodeVec sum(int axis){
        if (axis==0){
            GradNodeVec res = new GradNodeVec(new double[this.values[0].getValues().length]);
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
            GradNodeVec res = new GradNodeVec(new double[this.values.length]);
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

    public GradNodeMat mul(GradNodeMat other){
        if (this.shape()[1]!=other.shape()[0]){
            throw new IllegalArgumentException("matrix multiplication cannot be performed with shapes: "+Arrays.toString(this.shape())+" and "+Arrays.toString(other.shape()));
        }
        GradNodeMat res = new GradNodeMat(new double[this.shape()[0]][other.shape()[1]]);
        for (int i = 0; i<this.shape()[0]; i++){
            for (int j = 0; j<other.shape()[1]; j++){
                res.getValues(i).setValue(j, this.values[i].dot(other.col(j)));
            }
        }
        return res;

    }
    public GradNodeVec getValues(int index){
        return this.values[index];
    }
    public GradNodeVec[] getValVecs(){
        return this.values;
    }
    public GradNodeVec col(int colIndex){
        GradNodeVec res = new GradNodeVec(new double[this.values.length]);
        for (int i =0 ; i<res.getValues().length; i++){
            res.setValue(i, this.values[i].getValues()[colIndex]);
        }
        return res;
    }

    public int[] shape(){
        return new int[]{this.values.length, this.values[0].getValues().length};
    }
    public String toString(){
        String res = "GradeNodeMat{";
        for (GradNodeVec row : values){
            res = res+row+"\n";
        }
        return res+"}\n";
    }
    public static void main(String[] args) {
        GradNodeMat mat = new GradNodeMat(new double[][]{{1,2,3},
                                                         {4,5,6}});
        GradNodeMat otherMat = new GradNodeMat(new double[][]{{1,2,3,4},
                                                              {5,6,7,8},
                                                              {9,10,11,12}});
        GradNodeMat res = mat.mul(otherMat);
        GradNodeVec resRow = res.sum(1);
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
