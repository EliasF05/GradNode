import java.util.Arrays;

public class GradMat {

    private GradVec[] values;
    
    public GradMat(double[][] Data){
        this.values = new GradVec[Data.length];
        for (int i =0 ; i<Data.length; i++){
            this.values[i] = new GradVec(Data[i]);
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
            GradVec res = new GradVec(new double[shape()[1]]);
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

    public GradVec mean(int axis){
            return sum(axis).div(new GradNode(shape()[axis]));
    }

    public GradMat matmul(GradMat other){
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
    
    public GradMat add(GradMat other){
        if (shape()!=other.shape()){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations.");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setGradVec(other.getValVecs()[i].add(getValVecs()[i]), i);
        }
        return res;
    }
    
    public GradMat sub(GradMat other){
        if (shape()!=other.shape()){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations.");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setGradVec(other.getValVecs()[i].sub(getValVecs()[i]), i);
        }
        return res;
    }
    
    public GradMat mul(GradMat other){
        if (shape()!=other.shape()){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations. If you would like Matrix Multiplication, call GradMat.matmul(other)");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setGradVec(other.getValVecs()[i].mul(getValVecs()[i]), i);
        }
        return res;
    }

    public GradMat div(GradMat other){
        if (shape()!=other.shape()){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations.");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setGradVec(other.getValVecs()[i].div(getValVecs()[i]), i);
        }
        return res;
    }

    public GradMat add(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setGradVec(getValues(i).add(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setGradCol(col(i).add(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    public GradMat sub(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setGradVec(getValues(i).sub(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setGradCol(col(i).sub(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    public GradMat mul(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setGradVec(getValues(i).mul(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setGradCol(col(i).mul(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    public GradMat div(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setGradVec(getValues(i).div(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setGradCol(col(i).div(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    public GradMat add(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].add(other), i, j);
            }
        }
        return res;
    }

    public GradMat sub(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].sub(other), i, j);
            }
        }
        return res;
    }

    public GradMat mul(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].mul(other), i, j);
            }
        }
        return res;
    }

    public GradMat div(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].div(other), i, j);
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

    public GradVec row(int rowIndex){
        return values[rowIndex];
    }
    
    public GradVec col(int colIndex){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i =0 ; i<res.getValues().length; i++){
            res.setValue(i, this.values[i].getValues()[colIndex]);
        }
        return res;
    }

    public void setGradVec(GradVec newRow, int rowIndex){
        values[rowIndex] = newRow;
    }

    public void setGradCol(GradVec newCol, int colIndex){
        for (int i = 0; i<values[0].getValues().length; i++){
            values[i].setValue(colIndex, newCol.getValues()[i]);
        }
    }
    
    public void setValue(GradNode newVal, int rowIndex, int colIndex){
        values[rowIndex].setValue(colIndex, newVal);
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
        GradMat res = mat.matmul(otherMat);
        GradVec resRow = res.sum(1);
        GradNode resVal = resRow.sum();
        resVal.backward();
        System.out.println(mat);
        System.out.println(otherMat);
        System.out.println(res);
        System.out.println(resRow);
        System.out.println(resVal);

    }
}
