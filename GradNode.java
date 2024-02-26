import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class GradNode{

    private static Random rand = new Random();
    private double data;
    private ArrayList<GradNode> children;
    private ArrayList<Double> childGrads;
    private double grad;
    /** Creates GradNode Object with specified data
     * 
     * @param data the data attribute of the Node
     */
    public GradNode(double data){
        this.data = data;
        this.children = new ArrayList<GradNode>();
        this.childGrads = new ArrayList<Double>();
        this.grad = 0.0;
    }
    /** Creates GradNode Object with random double in range(-1, 1) as data
     * 
     */
    public GradNode(){
        this.data = rand.nextDouble(-1, 1);
        this.children = new ArrayList<GradNode>();
        this.childGrads = new ArrayList<Double>();
        this.grad = 0.0;
    }

    /**
     * Adds GradNode Objects' data attribute
     * @param other GradNode to be added to this node
     * @return GradNode object with resulting data value
     */
    public GradNode add(GradNode other){
        GradNode res = new GradNode(this.data+other.getData());
        res.addChild(this);
        res.addChild(other);
        res.addChildGrad(1.0);
        res.addChildGrad(1.0);
        return res;
    }
    
    /**
     * Subtracts GradNode Objects' data attribute
     * @param other GradNode to be subtracted from this node
     * @return GradNode object with resulting data value
     */
    public GradNode sub(GradNode other){
        GradNode res = new GradNode(this.data-other.getData());
        res.addChild(this);
        res.addChild(other);
        res.addChildGrad(1.0);
        res.addChildGrad(-1.0);
        return res;
    }
    
    /**
     * Multiplies GradNode Objects' data attribute
     * @param other GradNode to be multiplied with this node
     * @return GradNode object with resulting data value
     */
    public GradNode mul(GradNode other){
        GradNode res = new GradNode(this.data*other.getData());
        res.addChild(this);
        res.addChild(other);
        res.addChildGrad(other.getData());
        res.addChildGrad(this.data);
        return res;
    }
    
    /**
     * Divides GradNode Object's data attribute
     * @param other GradNode to serve as divisor of this node
     * @return GradNode object with resuling data value
     */
    public GradNode div(GradNode other){
        GradNode res = new GradNode(this.data/other.getData());
        res.addChild(this);
        res.addChild(other);
        res.addChildGrad(1/other.getData());
        res.addChildGrad(-(this.data/Math.pow(other.getData(),2)));
        return res;
    }

    /**
     * Exponentiates GradNode's data attribute
     * @return GradNode object with resulting data value
     */
    public GradNode exp(){
        GradNode res = new GradNode(Math.exp(data));
        res.addChild(this);
        res.addChildGrad(Math.exp(data));
        return res;
    }

    /**
     * Takes GradNode's data attribute to power 
     * @param power GradNode to serve as exponent
     * @return GradNode object with resulting data value
     */
    public GradNode pow(GradNode power){
        GradNode res = new GradNode(Math.pow(this.data, power.data));
        res.addChild(this);
        res.addChild(power);
        res.addChildGrad(Math.pow(power.getData()*data, power.getData()-1));
        res.addChildGrad(Math.pow(data, power.getData())*Math.log(data));
        return res;
    }

    /**
     * Computes sin of GradNode's data attribute
     * @return GradNode object with resulting data value
     */
    public GradNode sin(){
        GradNode res = new GradNode(Math.sin(data));
        res.addChild(this);
        res.addChildGrad(Math.cos(data));
        return res;
    }

    /**
     * Computes cosine of GradNode's data attribute
     * @return GradNode object with resulting data value
     */
    public GradNode cos(){
        GradNode res = new GradNode(Math.cos(data));
        res.addChild(this);
        res.addChildGrad(-Math.sin(data));
        return res;
    }
    
    /**
     * Computes absoulte value of GradNode's data attribute
     * @return GradNode object with resulting data value
     */
    public GradNode abs(){
        if (data>=0){
            GradNode res =  new GradNode(data);
            res.addChild(this);
            res.addChildGrad(1.0);
            return res;
        }
        GradNode res = new GradNode(-data);
        res.addChild(this);
        res.addChildGrad(-1.0);
        return res;
    }

    /**
     * 
     * @return Gradient attribute of node
     */
    public double getGrad(){
        return this.grad;
    }
    
    /**
     * 
     * @return Data attribute of node
     */
    public double getData(){
        return this.data;
    }
    
    /**
     * 
     * @return Children in computational graph of node
     */
    public ArrayList<GradNode> getChildren(){
        return this.children;
    }

    /**
     * 
     * @return Gradients of node with respect to all its children
     */
    public ArrayList<Double> getChildGrads(){
        return this.childGrads;
    }

    private void addChildGrad(double grad){
        childGrads.add(grad);
    }
    
    /**
     * sets gradient to specified value
     * @param newGrad new gradient
     */
    public void setGrad(double newGrad){
        this.grad = newGrad;
    }
    
    /**
     * sets data to specified value
     * @param newGrad new data
     */
    public void setData(double newData){
        this.data = newData;
    }
    
    /**
     * adds child in computational Graph
     * @param child child to be added
     */
    public void addChild(GradNode child){
        this.children.add(child);
    }
    
    /**
     * Computes derivative with respect to all children and stores them in their grad attribute
     */
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
    
    /**
     * Sets gradient of node and all of its childrren to 0
     */
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

    public Double data(){
        return data;
    }

    /**
     * 
     * @return GradNode Object with data=0.0
     */
    public static GradNode zero(){
        return new GradNode(0.0);
    }
}
