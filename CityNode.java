//Gabriel Schonacher
import java.util.ArrayList;

public class CityNode {
    private final String name;
    private final ArrayList<Edge> edges;

    public CityNode(String name) {
        this.name = name;
        edges = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public void addEdge(CityNode destination, Transportation transpotation, double distance){
        Edge edge = new Edge(this, destination, transpotation, distance);
        if(edges.contains(edge)){
            throw new IllegalArgumentException("The edge is already in the city node");
        }
        edges.add(edge);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public boolean canTransport(CityNode destination, double weight){
        for(Edge edge : edges){
            if(edge.getDestination().equals(destination) && edge.canTransport(weight)){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(!o.getClass().equals(getClass())){
            return false;
        }

        CityNode c = (CityNode) o;

        return c.name.equals(name);
    }
}
