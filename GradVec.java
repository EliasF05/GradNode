public class GradVec {

    private GradNode[] values;

    public GradVec(double[] data){
        this.values = new GradNode[data.length];
        for (int i =0 ; i<data.length; i++){
            values[i] = new GradNode(data[i]);
        }
    }
    
    public GradVec(int size){
        this.values = new GradNode[size];
        for (int i = 0; i<size; i++){
            values[i] = new GradNode();
        }
    }
    
    public GradNode sum(){
        GradNode res = new GradNode(0);
        for (GradNode value:values){
            res = value.add(res);
        }
        return res;
    }
    
    public GradNode mean(){
        return sum().div(new GradNode(values.length));
    }

    public GradNode dot(GradVec other){
        sameLength(this, other);
        GradNode res = new GradNode(0);
        for (int i = 0; i<this.values.length; i++){
            res = res.add(this.values[i].mul(other.getValues()[i]));
        }
        return res;

    }
    
    public GradVec add(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].add(other.getValues()[i]));
        }
        return res;
    }

    public GradVec sub(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].sub(other.getValues()[i]));
        }
        return res;
    }
    
    public GradVec mul(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].mul(other.getValues()[i]));
        }
        return res;
    }

    public GradVec div(GradVec other){
        sameLength(this, other);
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].div(other.getValues()[i]));
        }
        return res;
    }
    
    public GradVec add(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].add(other));
        }
        return res;
    }

    public GradVec sub(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].sub(other));
        }
        return res;
    }

    public GradVec mul(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].mul(other));
        }
        return res;
    }

    public GradVec div(GradNode other){
        GradVec res = new GradVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].div(other));
        }
        return res;
    }
    
    public static void sameLength(GradVec a, GradVec b){
        if (a.getValues().length!=b.getValues().length){
            throw new IllegalArgumentException("dot product cannot be performed for vectors of sizes: "+a.getValues().length+" and "+b.getValues().length);
        }
    }
    
    public void setValue(int index, GradNode val){
        this.values[index] = val;
    }
    
    public GradNode[] getValues(){
        return this.values;
    }
    
    @Override
    public String toString(){
        String res = "GradVec{";
        for (GradNode x: values){
            res = res+x+",";
        }
        return res+"}";
    }
    public static void main(String[] args) {
        GradVec myVec = new GradVec(new double[]{1,2,4});
        GradVec myVec2 = new GradVec(new double[]{3,4, 10});
        GradNode sum = myVec.dot(myVec2);
        sum.backward();
        System.out.println(sum.graphToString()+" "+ sum.getChildren());
    }
}
