import java.util.Arrays;

public class GradMat{

    private GradVec[] values;
    
    /**
     * Creates GradMat object with specified data
     * @param data data to be stored GradMat
     */
    public GradMat(double[][] data){
        this.values = new GradVec[data.length];
        for (int i =0 ; i<data.length; i++){
            this.values[i] = new GradVec(data[i]);
        }
    }
    
    /**
     * Creates GradMat object with GradVecs as row-vectors
     * @param values row vectors
     */
    public GradMat(GradVec[] values){
        this.values = values;
    }
    /**
     * creates GradMat object with random GradVec Objects with data as random double in range(-1, 1)
     * @param shape shape of matrix
     */
    public GradMat(int[] shape){
        values = new GradVec[shape[0]];
        for (int i = 0; i<shape[0]; i++){
            values[i] = new GradVec(shape[1]);
        }
    }
    
    /**
     * 
     * @param axis axis to be taken sum along
     * @return resulting GradVec
     */
    public GradVec sum(int axis){
        if (axis==0){
            GradVec res = new GradVec(new double[shape()[1]]);
            for (int i = 0; i<shape()[1]; i++){
                res.setValue(i, col(i).sum());
            }
            return res;
        }
        GradVec res = new GradVec(new double[shape()[0]]);
        for (int i =0 ; i<shape()[0]; i++){
            res.setValue(i, row(i).sum());
        }
        return res;
        
    }

    /**
     * 
     * @param axis axis to be taken mean along
     * @return resulting GradVec
     */
    public GradVec mean(int axis){
            return sum(axis).div(new GradNode(shape()[axis]));
    }

    /**
     * 
     * @param other GradMat to be multiplied with
     * @return result of matrix multipliation with other
     */
    public GradMat matmul(GradMat other){
        if (this.shape()[1]!=other.shape()[0]){
            throw new IllegalArgumentException("matrix multiplication cannot be performed with shapes: "+Arrays.toString(this.shape())+" and "+Arrays.toString(other.shape()));
        }
        GradMat res = new GradMat(new double[this.shape()[0]][other.shape()[1]]);
        for (int i = 0; i<this.shape()[0]; i++){
            for (int j = 0; j<other.shape()[1]; j++){
                res.row(i).setValue(j, this.values[i].dot(other.col(j)));
            }
        }
        return res;
    }

    /**
     * matrix-vector product
     * @param other vector acting in matrix-vector product
     * @return result of matrix-vector product
     */
    public GradVec matmul(GradVec other){
        if (other.getValues().length!=shape()[1]){
            throw new IllegalArgumentException("matrix-vector product not defined for matrix with shape: "+Arrays.toString(shape())+" and vector with size: "+other.getValues().length);
        }
        GradVec res = new GradVec(shape()[0]);
        for (int i = 0; i<values.length; i++){
            res.setValue(i, row(i).dot(other));
        }
        return res;
    }

    /**
     * @param other GradMat to be added
     * @return result of element-wise add with other
     */
    public GradMat add(GradMat other){
        if (!(Arrays.equals(shape(), other.shape()))){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setRow(values[i].add(other.row(i)), i);
        }
        return res;
    }
    
    /**
     * 
     * @param other GradMat to be subtracted
     * @return result of element-wise subtract with other
     */
    public GradMat sub(GradMat other){
        if (!(Arrays.equals(shape(), other.shape()))){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setRow(values[i].sub(other.row(i)), i);
        }
        return res;
    }
    
    /**
     * NOTE: This is not matrix-multiplication. For matrix-multiplication call GradMat.matmul(GradMat)
     * @param other GradMat to be multiplied with
     * @return result of element-wise multiplication with other
     */
    public GradMat mul(GradMat other){
        if (!(Arrays.equals(shape(), other.shape()))){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setRow(values[i].mul(other.row(i)), i);
        }
        return res;
    }

    /**
     * 
     * @param other GradMat to serve as divisor
     * @return result of element-wise division with other
     */
    public GradMat div(GradMat other){
        if (!(Arrays.equals(shape(), other.shape()))){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setRow(values[i].div(other.row(i)), i);
        }
        return res;
    }

    /**
     * Rules for Broadcasting:
     * If other is of size shape[1](row Amount), other gets broadcasted along axis 0
     * Otherwise, if other is of size[0](column Amount), other gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and added
     * @return result of element-wise addition with other
     */
    public GradMat add(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setRow(row(i).add(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setColumn(col(i).add(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    /**
     * Rules for Broadcasting:
     * If other is of size shape[1](row Amount), other gets broadcasted along axis 0
     * Otherwise, if other is of size[0](column Amount), other gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and subtracted
     * @return result of element-wise subtraction with other
     */
    public GradMat sub(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setRow(row(i).sub(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setColumn(col(i).sub(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    /**
     * Rules for Broadcasting:
     * If other is of size shape[1](row Amount), other gets broadcasted along axis 0
     * Otherwise, if other is of size[0](column Amount), other gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and multiplied with
     * @return result of element-wise multiplied with other
     */
    public GradMat mul(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setRow(row(i).mul(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setColumn(col(i).mul(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    /**
     * Rules for Broadcasting:
     * If other is of size shape[1](row Amount), other gets broadcasted along axis 0
     * Otherwise, if other is of size[0](column Amount), other gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and serve as divisor
     * @return result of element-wise division with other
     */
    public GradMat div(GradVec other){
        // Broadcasting
        if (other.getValues().length==shape()[1]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values.length; i++){
                res.setRow(row(i).div(other), i);
            }
            return res;
        }
        if (other.getValues().length==shape()[0]){
            GradMat res = new GradMat(shape());
            for (int i = 0; i<values[0].getValues().length; i++){
                res.setColumn(col(i).div(other), i);
            }
            return res;
        }
        throw new IllegalArgumentException("Cannot broadcast GradVec of size "+other.getValues().length+" onto GradMat of shape "+Arrays.toString(shape()));
    }

    /**
     * adding matrix with scalar
     * @param other scalar to be added
     * @return GradMat object with resulting values
     */
    public GradMat add(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].add(other), i, j);
            }
        }
        return res;
    }

    /**
     * subtracting scalar from matrix
     * @param other scalar to be subtracted
     * @return GradMat object with resulting values
     */
    public GradMat sub(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].sub(other), i, j);
            }
        }
        return res;
    }

    /**
     * multiplying matrix with scalar
     * @param other scalar to be multiplied with
     * @return GradMat object with resulting values
     */
    public GradMat mul(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].mul(other), i, j);
            }
        }
        return res;
    }

    /**
     * dividing matrix with scalar
     * @param other scalar to serve as divisor
     * @return GradMat object with resulting values
     */
    public GradMat div(GradNode other){
        GradMat res = new GradMat(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; i++){
                res.setValue(row(i).getValues()[j].div(other), i, j);
            }
        }
        return res;
    }
    
    /**
     * Flattens matrix in fashion of appending vectors to each other
     * @return Array of GradNode objects of GradMat in flattened shape
     */
    public GradNode[] flat(){
        GradNode[] res = new GradNode[shape()[0]*shape()[1]];
        int idx = 0;
        for (GradVec row : values){
            for (GradNode node: row.getValues()){
                res[idx] = node;
                idx+=1;
            }
        }
        return res;
    }

    /**
     * 
     * @return array of row-vectors of matrix
     */
    public GradVec[] getVectors(){
        return values;
    }

    /**
     * 
     * @param rowIndex row-index of desired row-vector (0-indexed)
     * @return resulting GradVec
     */
    public GradVec row(int rowIndex){
        return values[rowIndex];
    }
    
    /**
     * 
     * @param colIndex column-index of desired column-vector (0-indexed)
     * @return resulting GradVec
     */
    public GradVec col(int colIndex){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i =0 ; i<res.getValues().length; i++){
            res.setValue(i, this.values[i].getValues()[colIndex]);
        }
        return res;
    }

    /**
     * 
     * @param newRow GradVec to be inserted
     * @param rowIndex row where GradVec is to be inserted
     */
    public void setRow(GradVec newRow, int rowIndex){
        values[rowIndex] = newRow;
    }

    /**
     * 
     * @param newCol GradVec to be inserted
     * @param colIndex column where GradVec is to be inserted
     */
    public void setColumn(GradVec newCol, int colIndex){
        for (int i = 0; i<values[0].getValues().length; i++){
            values[i].setValue(colIndex, newCol.getValues()[i]);
        }
    }
    
    /**
     * 
     * @param rowIndex row index of desired Value (0-indexed)
     * @param colIndex column index of desired Value (0-indexed)
     * @return resulting GradNode
     */
    public GradNode getValue(int rowIndex, int colIndex){
        return values[rowIndex].getValue(colIndex);
    }

    /**
     * 
     * @param newVal GradNode to be inserted
     * @param rowIndex row-index where GradNode is to be inserted (0-indexed)
     * @param colIndex col-index where GradNode is to be inserted (0-indexed)
     */
    public void setValue(GradNode newVal, int rowIndex, int colIndex){
        values[rowIndex].setValue(colIndex, newVal);
    }

    /**
     * 
     * @return shape of matrix as Integer Array: [rows,columns]
     */
    public int[] shape(){
        return new int[]{this.values.length, this.values[0].getValues().length};
    }

    /**
     * 
     * @return single element in matrix
     */
    public GradNode asNode(){
        if (values.length!=1||values[0].getValues().length!=1){
            throw new IllegalArgumentException("Cannot convert GradMat of shape "+Arrays.toString(shape())+" to GradNode. Only shape (1,1) allowed.");
        }
        return getValue(0, 0);
    }

    /**
     * 
     * @return single row or column vector of GradMat
     */
    public GradVec asVec(){
        if (values.length!=1&&values[0].getValues().length!=1){
            throw new IllegalArgumentException("Cannot convert GradMat of shape "+Arrays.toString(shape())+" to vector. Only shapes (n,1) or (1,n) allowed");
        }
        if (values.length==1){
            return values[0];
        }
        return col(0);
    }
    
    @Override
    public String toString(){
        String res = "GradMat{";
        for (GradVec row : values){
            res = res+row+"\n";
        }
        return res+"}\n";
    }
    
    /**
     * 
     * @param shape desired shape
     * @return zero matrix of shape 
     */
    public static GradMat zeros(int[] shape){
        GradMat res = new GradMat(shape);
        for (int i = 0; i<shape[0]; i++){
            res.setRow(GradVec.zeros(shape[1]), i);
        }
        return res;
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
