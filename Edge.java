//Gabriel Schonacher
public class Edge{
    private final CityNode source;
    private final CityNode destination;
    private final Transportation transportation;
    private final double distance;

    public Edge(CityNode source, CityNode destination, Transportation transportation, double distance){
        if( distance <= 0 ) {
            throw new IllegalArgumentException("Invalid distance between cities.");
        }
        this.source = source;
        this.destination = destination;
        this.transportation = transportation;
        this.distance = distance;
    }

    public CityNode getSource() {
        return source;
    }

    public CityNode getDestination() {
        return destination;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public double getDistance() {
        return distance;
    }

    public double getCost(double weight) {
        return transportation.getTransportationCost(distance, weight);
    }

    public double getTime(double weight) {
        return transportation.getTransportationTimeInMinutes(distance, weight);
    }

    public boolean canTransport(double weight){
        return transportation.isValidWeight(weight);
    }

    @Override
    public String toString(){
        String result;
        if(transportation.getMethod() == Transportation.GROUND) {
            result = source.getName() + " to " + destination.getName() + " by ground with Company " + transportation.getCompany();
        }
        else {
            result = source.getName() + " to " + destination.getName() + " by air with Company " + transportation.getCompany();
        }
        return result;
    }

    @Override
    public boolean equals(Object o){
        if(!o.getClass().equals(getClass())){
            return false;
        }

        Edge r = (Edge) o;

        return r.destination.equals(destination) && r.transportation.equals(transportation);
    }
}
