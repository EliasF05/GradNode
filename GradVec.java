public class GradVec{

    private GradNode[] values;


    /** Creates GradVec object with specified data
     * 
     * @param data data to be stored in GradNodes of GradVec
     */
    public GradVec(double[] data){
        this.values = new GradNode[data.length];
        for (int i =0 ; i<data.length; i++){
            values[i] = new GradNode(data[i]);
        }
    }
    
    /**
     * creates GradVec object with GradNode Objects with data as random double in range-1, 1)
     * @param size size of vector
     */
    public GradVec(int size){
        this.values = new GradNode[size];
        for (int i = 0; i<size; i++){
            values[i] = new GradNode();
        }
    }
    
    /**
     * 
     * @return sum of all GradNode objects' data attributes in GradVec
     */
    public GradNode sum(){
        GradNode res = new GradNode(0);
        for (GradNode value:values){
            res = value.add(res);
        }
        return res;
    }
    
    /**
     * 
     * @return mean of all GradNode objects' data attributes in GradVEc
     */
    public GradNode mean(){
        return sum().div(new GradNode(values.length));
    }

    /**
     * dot product of all GradNode object's data attributes in GradVec
     * @param other serving as other factor in dot product
     * @return dot product of all GradNode object's data attributes in GradVec, and GradVec other
     */
    public GradNode dot(GradVec other){
        sameLength(this, other);
        GradNode res = new GradNode(0);
        for (int i = 0; i<this.values.length; i++){
            res = res.add(this.values[i].mul(other.getValues()[i]));
        }
        return res;
    }
    
    /**
     * element-wise adding of vectors
     * @param other GradVec to be added
     * @return GradVec object with resulting values
     */
    public GradVec add(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].add(other.getValues()[i]));
        }
        return res;
    }

    /**
     * element-wise subtraction of vectors
     * @param other GradVec to be subtracted
     * @return GradVec object with resulting Values
     */
    public GradVec sub(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].sub(other.getValues()[i]));
        }
        return res;
    }
    
    /**
     * element-wise multiplication of vectors
     * @param other GradVec to be multiplied with
     * @return GradVec object with resulting values
     */
    public GradVec mul(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].mul(other.getValues()[i]));
        }
        return res;
    }

    /**
     * element-wise division of vectors
     * @param other GradVec to serve as divisor
     * @return GradVec object with resulting values
     */
    public GradVec div(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].div(other.getValues()[i]));
        }
        return res;
    }
    
    /**
     * adding vector with scalar
     * @param other scalar to be added
     * @return GradVec object with resulting values
     */
    public GradVec add(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].add(other));
        }
        return res;
    }

    /**
     * subtracting scalar from vector
     * @param other scalar to be subtracted
     * @return GradVec object with resulting values
     */
    public GradVec sub(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].sub(other));
        }
        return res;
    }

    /**
     * multiplying vector with scalar
     * @param other scalar to be multiplied with
     * @return GradVec object with resulting values
     */
    public GradVec mul(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].mul(other));
        }
        return res;
    }

    /**
     * dividing vector with scalar
     * @param other scalar to serve as divisor
     * @return GradVec object with resulting values
     */
    public GradVec div(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, values[i].div(other));
        }
        return res;
    }

    /**
     * taking every GradNode of GradVec to power
     * @param power power to be raised to
     * @return GradVec object with resulting values
     */
    public GradVec pow(GradNode power){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, values[i].pow(power));
        }
        return res;
    }
    
    /**
     * Check if GradVec's are of the same Size, else throw Exception
     * @param a GradVec one
     * @param b GradVec two
     */
    public static void sameLength(GradVec a, GradVec b){
        if (a.getValues().length!=b.getValues().length){
            throw new IllegalArgumentException("dot product cannot be performed for vectors of sizes: "+a.getValues().length+" and "+b.getValues().length);
        }
    }
    
    /**
     * set GradNode object in GradVec to specified GradNode
     * @param index index in vector (0-indexed) where insertion is to be performed
     * @param val GradNode to be inserted
     */
    public void setValue(int index, GradNode val){
        this.values[index] = val;
    }
    
    /**
     * 
     * @return Array of GradNode objects of GradVec
     */
    public GradNode[] getValues(){
        return this.values;
    }
    
    /**
     * 
     * @param index index of desired GradNode
     * @return GradNode at specified index (0-indexed)
     */
    public GradNode getValue(int index){
        return values[index];
    }

    /**
     * 
     * @return Single GradNode contained in GradVec
     */
    public GradNode asNode(){
        if (values.length!=1){
            throw new IllegalArgumentException("Cannot convert GradVec of size "+values.length+" to GradNode. Only size 1 allowed.");
        }
        return values[0];
    }

    @Override
    public String toString(){
        String res = "GradVec{";
        for (GradNode x: values){
            res = res+x+",";
        }
        return res+"}";
    }
    
    /**
     * 
     * @param size desired size
     * @return zero vector in R^size
     */
    public static GradVec zeros(int size){
        GradVec res = new GradVec(size);
        for (int i= 0; i<size; i++){
            res.setValue(i, GradNode.zero());
        }
        return res;
    }
    public static void main(String[] args) {
        GradVec myVec = new GradVec(new double[]{1,2,4});
        GradVec myVec2 = new GradVec(new double[]{3,4, 10});
        GradNode sum = myVec.dot(myVec2);
        sum.backward();
        System.out.println(sum.graphToString()+" "+ sum.getChildren());
    }
}
