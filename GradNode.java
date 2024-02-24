import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
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
        res.addChildGrad(1.0);
        res.addChildGrad(1.0);
        return res;
    }
    
    public GradNode sub(GradNode other){
        GradNode res = new GradNode(this.data-other.getData());
        res.addChild(this);
        res.addChild(other);
        res.addChildGrad(1.0);
        res.addChildGrad(-1.0);
        return res;
    }
    
    public GradNode mul(GradNode other){
        GradNode res = new GradNode(this.data*other.getData());
        res.addChild(this);
        res.addChild(other);
        res.addChildGrad(other.getData());
        res.addChildGrad(this.data);
        return res;
    }
    
    public GradNode div(GradNode other){
        GradNode res = new GradNode(this.data/other.getData());
        res.addChild(this);
        res.addChild(other);
        res.addChildGrad(1/other.getData());
        res.addChildGrad(-(this.data/Math.pow(other.getData(),2)));
        return res;
    }

    public GradNode exp(){
        GradNode res = new GradNode(Math.exp(data));
        res.addChild(this);
        res.addChildGrad(Math.exp(data));
        return res;
    }

    public GradNode pow(GradNode power){
        GradNode res = new GradNode(Math.pow(this.data, power.data));
        res.addChild(this);
        res.addChild(power);
        res.addChildGrad(Math.pow(power.getData()*data, power.getData()-1));
        res.addChildGrad(Math.pow(data, power.getData())*Math.log(data));
        return res;
    }

    public GradNode sin(){
        GradNode res = new GradNode(Math.sin(data));
        res.addChild(this);
        res.addChildGrad(Math.cos(data));
        return res;
    }

    public GradNode cos(){
        GradNode res = new GradNode(Math.cos(data));
        res.addChild(this);
        res.addChildGrad(-Math.sin(data));
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

    public ArrayList<Double> getChildGrads(){
        return this.childGrads;
    }

    public void addChildGrad(double grad){
        childGrads.add(grad);
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
