//Gabriel Schonacher
import java.util.*;

public class TransportationMap {
    private static final int FASTEST_PATH = 0;
    private static final int CHEAPEST_PATH = 1;
    private final Hashtable<String, CityNode> cityNodes;
    // Transportation hashtable key is companyName + transportationMethod (i.e. A0, A1, B0, B1 ...)
    private final Hashtable<String, Transportation> transportations;

    public TransportationMap() {
        cityNodes = new Hashtable<>();
        transportations = new Hashtable<>();
    }

    public void addCityNode(String name){
        if(cityNodes.containsKey(name)){
            throw new IllegalArgumentException("The city node is already in the map.");
        }
        cityNodes.put(name, new CityNode(name));
    }

    public void addTransportation(String company, int method, double cost, double speed, int maxWeight){
        if(transportations.containsKey(company + method)){
            throw new IllegalArgumentException("Transportation already in the system.");
        }
        transportations.put(company + method , new Transportation(company, method, cost, speed, maxWeight));
    }

    public boolean containsCityNode(String name) {
        return cityNodes.containsKey(name);
    }

    public boolean containsTransportation(String transportation){
        return transportations.containsKey(transportation);
    }

    public void makeConnection(String cityName1, String cityName2, double distance, String transportation){
        if(!cityNodes.containsKey(cityName1)){
            throw new IllegalArgumentException("City node not found.");
        }
        if(!cityNodes.containsKey(cityName2)){
            throw new IllegalArgumentException("City node not found.");
        }
        if(!transportations.containsKey(transportation)){
            throw new IllegalArgumentException("Transportation node not found.");
        }
        cityNodes.get(cityName1).addEdge(cityNodes.get(cityName2), transportations.get(transportation), distance);
    }

    public ArrayList<Edge> getFastestRout(String source, String destination, double weight){
        return runDijkstra(source, destination, weight, FASTEST_PATH);
    }

    public ArrayList<Edge> getCheapestRout(String source, String destination, double weight){
        return runDijkstra(source, destination, weight, CHEAPEST_PATH);
    }

    private ArrayList<Edge> runDijkstra(String source, String destination, double weight, int algorithm){
        ArrayList<Edge> path = null;

        DijkstraNode sourceDNode = null;
        DijkstraNode destinationDNode = null;

        PriorityQueue<DijkstraNode> queue = new PriorityQueue<>(Comparator.comparingDouble(DijkstraNode::getAccCost));
        ArrayList<DijkstraNode> dNodes = generateDijkstraGraph(source, weight);
        sourceDNode = dNodes.get(0);
        queue.add(sourceDNode);
        sourceDNode.setVisited(true);

        while(!queue.isEmpty()){
            DijkstraNode coveredNode = queue.poll();
            coveredNode.setCovered(true);
            if(coveredNode.getCityNode().getName().equals(destination)){
                destinationDNode = coveredNode;
                break;
            }
            for(DijkstraNode dNode : coveredNode.getNeighbors()){
                Edge minEdge = getMinEdgeToReach(coveredNode.getCityNode(), dNode.getCityNode(), algorithm, weight);
                double minCost = Double.MAX_VALUE;
                if(algorithm == FASTEST_PATH) {
                    minCost = minEdge.getTime(weight);
                }
                else if(algorithm == CHEAPEST_PATH) {
                    minCost = minEdge.getCost(weight);
                }
                double accCostFromCoveredNode = coveredNode.getAccCost() + minCost;
                if(!dNode.isVisited()) {
                    dNode.setAccCost(accCostFromCoveredNode);
                    dNode.setParentDijkstraNode(coveredNode);
                    dNode.setLeadingEdge(minEdge);
                    dNode.setVisited(true);
                    queue.add(dNode);
                }
                else if(!dNode.isCovered()){
                    if(accCostFromCoveredNode < dNode.getAccCost()){
                        queue.remove(dNode);
                        dNode.setAccCost(accCostFromCoveredNode);
                        dNode.setParentDijkstraNode(coveredNode);
                        dNode.setLeadingEdge(minEdge);
                        queue.add(dNode);
                    }
                }
            }
        }
        if(destinationDNode != null){
            path = generatePath(destinationDNode);
        }

        return path;
    }

    private ArrayList<DijkstraNode> generateDijkstraGraph(String source, double weight) {
        ArrayList<DijkstraNode> dNodes = new ArrayList<>();
        DijkstraNode sourceNode = null;
        for(CityNode node : cityNodes.values()){
            DijkstraNode dNode = new DijkstraNode(node);
            if(node.getName().equals(source)){
                dNode.setAccCost(0);
                sourceNode = dNode;
            }
            dNodes.add(dNode);
        }

        for(DijkstraNode dNode1 : dNodes){
            for(DijkstraNode dNode2 : dNodes){
                if(dNode1 != dNode2){
                    if(dNode1.getCityNode().canTransport(dNode2.getCityNode(), weight)){
                        dNode1.addNeighbor(dNode2);
                    }
                }
            }
        }
        dNodes.remove(sourceNode);
        dNodes.add(0,sourceNode);
        return dNodes;
    }

    private Edge getMinEdgeToReach(CityNode source, CityNode destination, int algorithm, double weight){
        Edge minEdge = null;
        double minCost = Double.MAX_VALUE;
        for(Edge edge : source.getEdges()){
            if(edge.getDestination().equals(destination) && edge.getTransportation().isValidWeight(weight)){
                double cost;
                if(algorithm == CHEAPEST_PATH){
                    cost = edge.getCost(weight);
                    if( cost < minCost ) {
                        minEdge = edge;
                        minCost = cost;
                    }
                }
                else if(algorithm == FASTEST_PATH){
                    cost = edge.getTime(weight);
                    if( cost < minCost ) {
                        minEdge = edge;
                        minCost = cost;
                    }
                }
            }
        }
        return minEdge;
    }

    private ArrayList<Edge> generatePath(DijkstraNode destinationDNode){
        ArrayList<Edge> path = new ArrayList<>();
        while(destinationDNode.getParentDijkstraNode() != null){
            path.add(destinationDNode.getLeadingEdge());
            destinationDNode = destinationDNode.getParentDijkstraNode();
        }

        Collections.reverse(path);

        return path;
    }

    private static class DijkstraNode {
        private final CityNode cityNode;
        private final ArrayList<DijkstraNode> neighbors;
        private DijkstraNode parentDijkstraNode;
        private Edge edgeOnShortestPathToCity;
        private double accCost;
        private boolean covered;
        private boolean visited;

        public DijkstraNode(CityNode cityNode){
            this.cityNode = cityNode;
            accCost = Double.MAX_VALUE;
            neighbors = new ArrayList<>();
        }

        public void addNeighbor(DijkstraNode dNode){
            neighbors.add(dNode);
        }

        public ArrayList<DijkstraNode> getNeighbors() {
            return neighbors;
        }

        public void setParentDijkstraNode(DijkstraNode parentDijkstraNode) {
            this.parentDijkstraNode = parentDijkstraNode;
        }

        public void setLeadingEdge(Edge edgeOnShortestPathToCity) {
            this.edgeOnShortestPathToCity = edgeOnShortestPathToCity;
        }

        public void setAccCost(double accCost) {
            this.accCost = accCost;
        }

        public void setCovered(boolean covered) {
            this.covered = covered;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public DijkstraNode getParentDijkstraNode() {
            return parentDijkstraNode;
        }

        public CityNode getCityNode() {
            return cityNode;
        }

        public Edge getLeadingEdge(){
            return edgeOnShortestPathToCity;
        }

        public double getAccCost() {
            return accCost;
        }

        public boolean isCovered() {
            return covered;
        }

        public boolean isVisited() {
            return visited;
        }
    }
}
