//Gabriel Schonacher
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        runTests("transportations.txt", "tests.txt");
    }

    private static void runTests(String transportationsFile, String testFile) throws FileNotFoundException {
        ArrayList<Transportation> transportations = loadTransportations(transportationsFile);

        Scanner scanner = new Scanner(new File(testFile));
        int numTests = scanner.nextInt();

        for(int i = 0; i < numTests; i++){
            int numRouts = scanner.nextInt();
            int numOrders = scanner.nextInt();
            TransportationMap transportationMap = new TransportationMap();
            for(Transportation transportation : transportations){
                transportationMap.addTransportation(transportation.getCompany(), transportation.getMethod(),
                        transportation.getBaseCost(), transportation.getBaseSpeed(), transportation.getMaxWeight());
            }
            ShippingOrder[] shippingOrders = new ShippingOrder[numOrders];
            for(int j = 0; j < numRouts; j++) {
                String company = scanner.next();
                String source = scanner.next();
                String destination = scanner.next();
                String methodStr = scanner.next();
                double distance = scanner.nextDouble();
                if(!transportationMap.containsCityNode(source)){
                    transportationMap.addCityNode(source);
                }
                if(!transportationMap.containsCityNode(destination)){
                    transportationMap.addCityNode(destination);
                }
                if(methodStr.equals("B")) {
                    transportationMap.makeConnection(source, destination, distance, company + Transportation.AIR);
                    transportationMap.makeConnection(source, destination, distance, company + Transportation.GROUND);
                    // BOTH DIRECTIONAL TRANSPORTATIONS
                    transportationMap.makeConnection(destination, source, distance, company + Transportation.AIR);
                    transportationMap.makeConnection(destination, source, distance, company + Transportation.GROUND);
                }
                else{
                    int method = methodStr.equals("A") ? Transportation.AIR : Transportation.GROUND;
                    transportationMap.makeConnection(source, destination, distance, company + method);
                    // BOTH DIRECTIONAL TRANSPORTATIONS
                    transportationMap.makeConnection(destination, source, distance, company + method);
                }
            }
            for(int j = 0; j < numOrders; j++) {
                String orderSource = scanner.next();
                String orderDestination = scanner.next();
                int weight = scanner.nextInt();
                int preferredMethod = scanner.next().equals("C") ? ShippingOrder.CHEAPEST_METHOD_PREFERRED : ShippingOrder.FASTEST_METHOD_PREFERRED;
                shippingOrders[j] = new ShippingOrder(orderSource, orderDestination, weight, preferredMethod, transportationMap);
            }
            processShippingOrdersAndDisplayResult(shippingOrders);
        }
    }

    private static ArrayList<Transportation> loadTransportations(String transportationsFile) throws FileNotFoundException {
        ArrayList<Transportation> transportations = new ArrayList<>();
        Scanner scanner = new Scanner(new File(transportationsFile));
        while(scanner.hasNext()){
            String company = scanner.next();
            double cost = scanner.nextDouble();
            double speed = scanner.nextDouble();
            int weight = scanner.nextInt();
            if(weight == -1){
                weight = Integer.MAX_VALUE;
            }
            // If the air transportation is not supported, cost is -1.0
            if(cost != -1.0) {
                transportations.add(new Transportation(company, Transportation.AIR, cost, speed, weight));
            }
            cost = scanner.nextDouble();
            speed = scanner.nextDouble();
            weight = scanner.nextInt();
            if(weight == -1){
                weight = Integer.MAX_VALUE;
            }
            if(cost != -1.0) {
                transportations.add(new Transportation(company, Transportation.GROUND, cost, speed, weight));
            }
        }
        return transportations;
    }

    private static void processShippingOrdersAndDisplayResult(ShippingOrder[] shippingOrders) {
        for(ShippingOrder order : shippingOrders){
            order.processShippingOrder();
            ArrayList<String> shippingPath = order.getShippingPath();
            for(String path : shippingPath){
                System.out.println(path);
            }
            double time = order.getShippingTime() * 100.0;
            double cost = order.getShippingCost() * 100.0;
            time = Math.round(time) / 100.0;
            cost = Math.round(cost) / 100.0;
            System.out.printf("Total Hours: %.2f%n", time);
            System.out.printf("Total Cost: $%.2f%n", cost);
        }
    }

}
