public class JNN{

    private Layer[] layers;

    public JNN(Layer[] layers){
        this.layers = layers;
    }

    public GradVec forward(GradVec in){
        GradVec res = in;
        for (Layer layer: layers){
            res = layer.forward(res);
        }
        return res;
    }

    public Layer[] getLayers(){
        return layers;
    }

    @Override
    public String toString(){
        String res = "";
        for (Layer layer: layers){
            res = res+"Layer: "+layer.shape();
        }
        return res;
    }
}