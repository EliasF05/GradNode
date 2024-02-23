import java.util.ArrayList;
import java.util.Random;
public class GradNode{
    private static Random rand = new Random();
    private double data;
    private ArrayList<GradNode> children;
    private ArrayList<Double> childGrads;
    private double grad;

    public GradNode(double data){
        this.data = data;
        this.children = new ArrayList<GradNode>();
        this.childGrads = new ArrayList<Double>();
        this.grad = 0.0;
    }
    public GradNode(){
        this.data = rand.nextDouble(-1, 1);
        this.children = new ArrayList<GradNode>();
        this.childGrads = new ArrayList<Double>();
        this.grad = 0.0;
    }
    public GradNode add(GradNode other){
        GradNode res = new GradNode(this.data+other.getData());
        res.addChild(this);
        res.addChild(other);
        res.childGrads.add(1+this.grad);
        res.childGrads.add(1+other.getGrad());
        return res;
    }
    public GradNode sub(GradNode other){
        GradNode res = new GradNode(this.data-other.getData());
        res.addChild(this);
        res.addChild(other);
        res.childGrads.add(1+this.grad);
        res.childGrads.add(other.getGrad()-1);
        return res;
    }
    public GradNode mul(GradNode other){
        GradNode res = new GradNode(this.data*other.getData());
        res.addChild(this);
        res.addChild(other);
        res.childGrads.add(other.getData()+this.grad);
        res.childGrads.add(this.data+other.getGrad());
        return res;
    }
    public GradNode div(GradNode other){
        GradNode res = new GradNode(this.data/other.getData());
        res.addChild(this);
        res.addChild(other);
        res.childGrads.add(1/other.getData()+this.grad);
        res.childGrads.add(-(this.data/Math.pow(other.getData(),2))+other.getGrad());
        return res;
    }
    public double getGrad(){
        return this.grad;
    }
    public double getData(){
        return this.data;
    }
    public ArrayList<GradNode> getChildren(){
        return this.children;
    }
    public void setGrad(double newGrad){
        this.grad = newGrad;
    }
    public void setData(double newData){
        this.data = newData;
    }
    public void addChild(GradNode child){
        this.children.add(child);
    }
    public void backward(){
        this.grad = 1;
        for (int i = 0; i<childGrads.size(); i++){
            children.get(i).setGrad(children.get(i).getGrad()+childGrads.get(i)*this.grad);
            children.get(i).backwardN();
        }
    }
    private void backwardN(){
        for (int i = 0; i<childGrads.size(); i++){
            children.get(i).setGrad(children.get(i).getGrad()+childGrads.get(i)*this.grad);
            children.get(i).backwardN();
        }
    }
    public void zeroGrad(){
        this.grad = 0;
        for (int i =0; i<children.size(); i++){
            children.get(i).zeroGrad();
        }
    }
    @Override
    public String toString(){
        return "GradNode{Data="+this.data+"; Grad="+this.grad+"}";
    }
    public String graphToString(){
        String res =  this.toString();
        for (GradNode child:children){
            if (child!=this){
                res = res+"->"+child.graphToString();
            }
        }
        return res;
    }
}
