public class GradNodeVec {

    private GradNode[] values;

    public GradNodeVec(double[] data){
        this.values = new GradNode[data.length];
        for (int i =0 ; i<data.length; i++){
            values[i] = new GradNode(data[i]);
        }
    }
    public GradNodeVec(int size){
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
    public GradNode dot(GradNodeVec other){
        sameLength(this, other);
        GradNode res = new GradNode(0);
        for (int i = 0; i<this.values.length; i++){
            res = res.add(this.values[i].mul(other.getValues()[i]));
        }
        return res;

    }
    public GradNodeVec add(GradNodeVec other){
        sameLength(this, other);
        GradNodeVec res = new GradNodeVec(new double[this.values.length]);
        for (int i = 0; i<this.values.length; i++){
            res.setValue(i, this.values[i].add(other.getValues()[i]));
        }
        return res;

    }
    public static void sameLength(GradNodeVec a, GradNodeVec b){
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
        String res = "GradeNodeVec{";
        for (GradNode x: values){
            res = res+x+",";
        }
        return res+"}";
    }
    public static void main(String[] args) {
        GradNodeVec myVec = new GradNodeVec(new double[]{1,2,4});
        GradNodeVec myVec2 = new GradNodeVec(new double[]{3,4, 10});
        GradNode sum = myVec.dot(myVec2);
        sum.backward();
        System.out.println(sum.graphToString()+" "+ sum.getChildren());
    }
}
