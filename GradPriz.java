import java.util.Arrays;

public class GradPriz {

    private GradMat[] values;

    /**
     * creates GradPriz object with specified data
     * @param data data to be stored in GradPriz
     */
    public GradPriz(double[][][] data){
        this.values = new GradMat[data.length];
        for (int i =0; i<data.length; i++){
            this.values[i] = new GradMat(data[i]);
        }
    }

    /**
     * creates GradPriz object with GradMats as matrices along axis 0
     * @param data matrices along axis 0
     */
    public GradPriz(GradMat[] data){
        this.values = data;
    }
    
    /**
     * creates GradVec object with random GradMat objects with data as random double in range(-1, 1)
     * @param shape shape of GradPriz
     */
    public GradPriz(int[] shape){
        this.values = new GradMat[shape[0]];
        for (int i =0 ; i<shape[0]; i++){
            values[i] = new GradMat(new int[]{shape[1], shape[2]});
        }
    }

    /**
     * 
     * @param axis to be taken sum along
     * @return resulting GradMat
     */
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

    /**
     * 
     * @param axis axis to be taken mean along
     * @return resulting GradMat
     */
    public GradMat mean(int axis){
        return sum(axis).div(new GradNode(shape()[axis]));
    }

    /**
     * Example:
     * mat is matrix of shape = [2,3,4]
     * mat.mats(axis=0) leads to shape [2,3,4]
     * mat.mats(axis=1) leads to shape [3,2,4]
     * mat.mats(axis=2) leads to shape [4,2,3]
     * The data of the object is preserved
     * @param axis axis to be pulled to 0-th dimension of resulting shape
     * @return resulting array of GradMat objects
     */
    public GradMat[] mats(int axis){
        if (axis==0){
            return values;
        }
        if (axis==1){
            GradMat[] res = new GradMat[shape()[1]];
            for (int i = 0; i<shape()[1]; i++){
                GradMat cur = new GradMat(new int[]{shape()[0], shape()[2]});
                for (int j = 0; j<shape()[0]; j++){
                    cur.setRow(values[j].row(i), j);
                }
                res[i] = cur;
            }
            return res;
        }
        GradMat[] res = new GradMat[shape()[2]];
        for (int i = 0; i<shape()[2]; i++){
            GradMat cur = new GradMat(new int[]{shape()[0], shape()[1]});
            for (int j = 0; j<shape()[0]; j++){
                cur.setRow(values[j].col(i), j);
            }
            res[i] = cur;
        }
        return res;
    }

    /**
     * matrix-multiplication of GradPriz objects
     * Example:
     * shapes (3,6,5) and (3,5,2) lead to shape (3,6,2), as all matrices along axis 0 of GradPriz are being matrix-multiplied
     * @param other GradPriz to be multiplied with
     * @return result of matrix multiplication with other
     */
    public GradPriz matmul(GradPriz other){
        if (shape()[0]!=other.shape()[0]||shape()[2]!=other.shape()[1]){
            throw new IllegalArgumentException("Cannot perform matrix multiplication on shapes "+Arrays.toString(shape())+" and "+Arrays.toString(other.shape()));
        }
        GradPriz resCube = GradPriz.zeros(new int[]{shape()[0], shape()[1], other.shape()[2]});
        for (int i = 0; i<shape()[0]; i++){
            resCube.setMat(values[i].matmul(other.getMat(i)), i);
        }
        return resCube;
    }

    /**
     * Rules for Broadcasting: other gets broadcasted along axis 0
     * @param other GradMat to be broadcasted and matrix-multiplied with
     * @return result of matix-multiplication with other
     */
    public GradPriz matmul(GradMat other){
        if (shape()[1]!=other.shape()[0]||shape()[2]!=other.shape()[1]){
            throw new IllegalArgumentException("Cannot perform matrix multiplication with GradPriz of shape "+Arrays.toString(shape())+" and GradMat of shape "+Arrays.toString(other.shape()));
        }
        GradPriz resCube = GradPriz.zeros(new int[]{shape()[0], shape()[1], other.shape()[1]});
        for (int i = 0; i<shape()[0]; i++){
            resCube.setMat(values[i].matmul(other), i);
        }
        return resCube;
    }
    
    /**
     * 
     * @param other GradPriz to be added
     * @return result of element-wise add with other
     */
    public GradPriz add(GradPriz other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(getMat(i).add(other.getMat(i)), i);
        }
        return res;
    }

    /**
     * 
     * @param other GradPrix to be subtracted
     * @return result of element-wise subtract with other
     */
    public GradPriz sub(GradPriz other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(getMat(i).sub(other.getMat(i)), i);
        }
        return res;
    }

    /**
     * NOTE: This is not matrix-multiplication. For matrix multiplication call GradPriz.matmul(GradPriz)
     * @param other GradPriz to be multiplied with
     * @return result of element-wise multiplication
     */
    public GradPriz mul(GradPriz other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(getMat(i).mul(other.getMat(i)), i);
        }
        return res;
    }

    /**
     * 
     * @param other GradPriz to serve as divisor
     * @return result of element-wise division with other
     */
    public GradPriz div(GradPriz other){
        if(!Arrays.equals(shape(), other.shape())){
            throw new IllegalArgumentException("Matrix Shapes must be identical for element-wise operations."+Arrays.toString(shape())+" and "+Arrays.toString(other.shape())+" not compatible.");
        }
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            res.setMat(getMat(i).div(other.getMat(i)), i);
        }
        return res;
    }
    
    /**
     * Rules for Broadcasting: If other is of shape (this.shape()[1], this.shape[2]), 
     * other gets broadcasted along axis 0. If other is of shape (this.shape()[0], this.shape()[2]), 
     * other gets broadcasted along axis 1. If other is of shape (this.shape()[0], this.shape()[2]),
     * other gets broadcasted along axis 2.
     * @param other GradMat to be broadcasted and added
     * @return result of element-wise addition with other
     */
    public GradPriz add(GradMat other){
        //Broadcasting
        if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].add(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].add(other), i);
            }
            return new GradPriz(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradPriz res = new GradPriz(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].add(other), i);
            }
            return new GradPriz(new GradPriz(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }

    /**
     * Rules for Broadcasting: If other is of shape (this.shape()[1], this.shape[2]), 
     * other gets broadcasted along axis 0. If other is of shape (this.shape()[0], this.shape()[2]), 
     * other gets broadcasted along axis 1. If other is of shape (this.shape()[0], this.shape()[2]),
     * other gets broadcasted along axis 2.
     * @param other GradMat to be broadcasted and subtracted
     * @return result of element-wise subtraction with other
     */
    public GradPriz sub(GradMat other){
         //Broadcasting
         if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].sub(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].sub(other), i);
            }
            return new GradPriz(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradPriz res = new GradPriz(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].sub(other), i);
            }
            return new GradPriz(new GradPriz(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }

    /**
     * NOTE: This is not matrix multiplication. For matrix-multiplication call GradPriz.matmul(GradMat)
     * Rules for Broadcasting: If other is of shape (this.shape()[1], this.shape[2]), 
     * other gets broadcasted along axis 0. If other is of shape (this.shape()[0], this.shape()[2]), 
     * other gets broadcasted along axis 1. If other is of shape (this.shape()[0], this.shape()[2]),
     * other gets broadcasted along axis 2.
     * @param other GradMat to be broadcasted and multiplied with
     * @return result of element-wise multiplication with other
     */
    public GradPriz mul(GradMat other){
         //Broadcasting
         if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].mul(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].mul(other), i);
            }
            return new GradPriz(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradPriz res = new GradPriz(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].mul(other), i);
            }
            return new GradPriz(new GradPriz(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }

    /**
     * Rules for Broadcasting: If other is of shape (this.shape()[1], this.shape[2]), 
     * other gets broadcasted along axis 0. If other is of shape (this.shape()[0], this.shape()[2]), 
     * other gets broadcasted along axis 1. If other is of shape (this.shape()[0], this.shape()[2]),
     * other gets broadcasted along axis 2.
     * @param other GradMat to be broadcasted and serve as divisor
     * @return result of element-wise division with other
     */
    public GradPriz div(GradMat other){
         //Broadcasting
         if (other.shape()[0]==shape()[1]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(shape());
            for (int i = 0; i<shape()[0]; i++){
                res.setMat(values[i].div(other), i);
            }
            return res;
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[2]){
            GradPriz res = new GradPriz(new int[]{shape()[1], shape()[0], shape()[2]});
            GradMat[] pulled = mats(1);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].div(other), i);
            }
            return new GradPriz(res.mats(1));
        }
        if (other.shape()[0]==shape()[0]&&other.shape()[1]==shape()[1]){
            GradPriz res = new GradPriz(new int[]{shape()[2], shape()[0], shape()[1]});
            GradMat[] pulled = mats(2);
            for (int i = 0; i<pulled.length; i++){
                res.setMat(pulled[i].div(other), i);
            }
            return new GradPriz(new GradPriz(res.mats(2)).mats(2));
        }
        throw new IllegalArgumentException("Cannot broadcast matrix of shape "+Arrays.toString(other.shape())+" onto shape"+Arrays.toString(shape()));
    }
    
    /**
     * Rules for Broadcasting: if other is of size shape[0], other gets broadcasted along axis 1, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[1], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[2], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and added
     * @return result of element-wise addition with other
     */
    public GradPriz add(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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

    /**
     * Rules for Broadcasting: if other is of size shape[0], other gets broadcasted along axis 1, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[1], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[2], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and subtracted
     * @return result of element-wise subtraction with other
     */
    public GradPriz sub(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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

    /**
     * Rules for Broadcasting: if other is of size shape[0], other gets broadcasted along axis 1, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[1], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[2], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and multiplied with
     * @return result of element-wise multiplication with other
     */
    public GradPriz mul(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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

    /**
     * Rules for Broadcasting: if other is of size shape[0], other gets broadcasted along axis 1, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[1], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 2
     * otherwise, if other is of size shape[2], other gets broadcasted along axis 0, and the resulting matrix gets broadcasted along axis 1
     * @param other GradVec to be broadcasted and serve as divisor
     * @return result of element-wise division with other
     */
    public GradPriz div(GradVec other){
        //Broadcasting
        if (other.getValues().length==shape()[0]){
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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
            GradPriz res = new GradPriz(shape());
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

    /**
     * adding GradPriz with scalar
     * @param other scalar to be added
     * @return GradPriz object with resulting values
     */
    public GradPriz add(GradNode other){
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).add(other), i, j, k);
                }
            }
        }
        return res;
    }

    /**
     * subtracing scalar from GradPriz
     * @param other scalar to be subtracted
     * @return GradPriz object with resulting values
     */
    public GradPriz sub(GradNode other){
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).sub(other), i, j, k);
                }
            }
        }
        return res;
    }

    /**
     * multiplying GradPriz with scalar
     * @param other scalar to be multiplied with
     * @return GradPriz object with resulting values
     */
    public GradPriz mul(GradNode other){
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).mul(other), i, j, k);
                }
            }
        }
        return res;
    }

    /**
     * dividing GradPriz with scalar
     * @param other scalar to serve as divisor
     * @return GradPriz object with resulting values
     */
    public GradPriz div(GradNode other){
        GradPriz res = new GradPriz(shape());
        for (int i = 0; i<shape()[0]; i++){
            for (int j = 0; j<shape()[1]; j++){
                for (int k = 0; k<shape()[2]; k++){
                    res.setValue(getValue(i, j, k).div(other), i, j, k);
                }
            }
        }
        return res;
    }

    /**
     * Flattens GradPrix in fashion of appending matrices to each other, and appending vectors of each matrix to each other
     * @return Array of GradNode objects of GradPriz in flattened shape
     */
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
    
    /**
     * 
     * @return array of matrices along axis 0 of GradPriz
     */
    public GradMat[] getMats(){
        return values;
    }

    /**
     * 
     * @param index index along axis 0 of desired matrix
     * @return resulting GradMat
     */
    public GradMat getMat(int index){
        return values[index];
    }

    /**
     * 
     * @param newMat GradMat to be inserted
     * @param index index along axis 0 whee GradMat is to be inserted
     */
    public void setMat(GradMat newMat, int index){
        values[index] = newMat;
    }

    /**
     * 
     * @param dim0 index along dimension 0 of desired value
     * @param dim1 index along dimension 1 of desired value
     * @param dim2 index along dimension 2 of desired value
     * @return resulting GradNode
     */
    public GradNode getValue(int dim0, int dim1, int dim2){
        return values[dim0].getValue(dim1, dim2);
    }

    /**
     * 
     * @param newNode GradNode to be inserted
     * @param dim0 index along dimension 0 where GradNode is to be inserted
     * @param dim1 index along dimension 1 where GradNode is to be inserted
     * @param dim2 index along dimension 2 where GradNode is to be inserted
     */
    public void setValue(GradNode newNode, int dim0, int dim1, int dim2){
        values[dim0].setValue(newNode, dim1, dim2);
    }

    /**
     * 
     * @return shape of GradPriz as Integer Array
     */
    public int[] shape(){
        return new int[]{values.length, values[0].getVectors().length, values[0].getVectors()[0].getValues().length};
    }

    @Override
    public String toString(){
        String res = "GradCube{";
        for (GradMat mat: values){
            res = res+mat+"\n";
        }
        return res+"}\n";
    }

    /**
     * 
     * @param shape desired shape
     * @return zero GradPriz of shape
     */
    public static GradPriz zeros(int[] shape){
        GradPriz res = new GradPriz(shape);
        for (int i = 0; i<shape[0]; i++){
            res.setMat(GradMat.zeros(new int[]{shape[1], shape[2]}), i);
        }
        return res;
    }

    public static void main(String[] args) {
        GradPriz myCube = new GradPriz(new double[][][]{
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
        GradPriz res = myCube.add(myMat);
        System.out.println(res);
    }

}
