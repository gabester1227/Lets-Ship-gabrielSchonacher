//Gabriel Schonacher
import java.util.ArrayList;

public class ShippingOrder {
    public static final int FASTEST_METHOD_PREFERRED = 0;
    public static final int CHEAPEST_METHOD_PREFERRED = 1;
    private final TransportationMap transportationMap;
    private final String source;
    private final String destination;
    private final int weight;
    private final int preferredMethod;
    private double shippingTime;
    private double shippingCost;
    private ArrayList<Edge> shippingPath;
    private boolean processed;

    public ShippingOrder(String source, String destination, int weight, int preferredMethod, TransportationMap transportationMap){
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.preferredMethod = preferredMethod;
        this.transportationMap = transportationMap;
    }

    public double getShippingCost() {
        if(!processed){
            processShippingOrder();
        }
        return shippingCost;
    }

    public double getShippingTime() {
        if(!processed){
            processShippingOrder();
        }
        return shippingTime;
    }

    public void processShippingOrder(){
        if(processed){
            return;
        }
        if(preferredMethod == CHEAPEST_METHOD_PREFERRED){
            shippingPath = transportationMap.getCheapestRout(source, destination, weight);
        }
        else {
            shippingPath = transportationMap.getFastestRout(source, destination, weight);
        }
        for(Edge edge : shippingPath){
            shippingCost += edge.getCost(weight);
            shippingTime += edge.getTime(weight);
        }
        shippingTime = shippingTime / 60.0;
        processed = true;
    }

    public ArrayList<String> getShippingPath(){
        ArrayList<String> shippingPathStr = new ArrayList<>();

        for(Edge edge : shippingPath) {
            shippingPathStr.add(edge.toString());
        }

        return shippingPathStr;
    }
}
