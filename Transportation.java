//Gabriel Schonacher
public class Transportation {
    public static final int AIR = 0;
    public static final int GROUND = 1;
    private final String company;         // A B C D ...
    private final int method;             // AIR or GROUND
    private final double cost;
    private final double speed;
    private final int maxWeight;

    public Transportation( String company, int method, double cost, double speed, int weight){
        if(method != Transportation.AIR && method != Transportation.GROUND){
            throw new IllegalArgumentException("Invalid transportaion method.");
        }
        if( cost <= 0 ) {
            throw new IllegalArgumentException("Invalid transportaion cost.");
        }
        if( speed <= 0 ) {
            throw new IllegalArgumentException("Invalid transportaion speed.");
        }
        if( weight <= 0 ) {
            throw new IllegalArgumentException("Invalid transportaion weight.");
        }
        this.company = company;
        this.method = method;
        this.cost = cost;
        this.speed = speed;
        this.maxWeight = weight;
    }

    public String getCompany() {
        return company;
    }

    public int getMethod() {
        return method;
    }

    public double getBaseCost() {
        return cost;
    }

    public double getBaseSpeed() {
        return speed;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public double getTransportationCost(double distance, double weight) {
        if(distance <= 0) {
            throw new IllegalArgumentException("Transportation distance needs to be positive.");
        }
        if(weight > this.maxWeight){
            throw new IllegalArgumentException("Transportation weight exeeds maximum allowed weight.");
        }
        return cost * distance * weight;
    }

    public double getTransportationTimeInMinutes(double distance, double weight) {
        if(distance <= 0) {
            throw new IllegalArgumentException("Transportation distance needs to be positive.");
        }
        if(weight > this.maxWeight){
            throw new IllegalArgumentException("Transportation weight exeeds maximum allowed weight.");
        }
        return speed * distance;
    }

    public boolean isValidWeight(double weight) {
        return weight <= this.maxWeight;
    }

    @Override
    public String toString(){
        return company + method;
    }

    @Override
    public boolean equals(Object o){
        if(!o.getClass().equals(getClass())){
            return false;
        }

        Transportation t = (Transportation) o;

        return t.company.equals(company) && t.method == method;
    }
}
