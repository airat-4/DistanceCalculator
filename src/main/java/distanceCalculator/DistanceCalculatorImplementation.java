package distanceCalculator;

import entity.City;
import entity.Distance;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by airat on 25.03.16.
 */
public class DistanceCalculatorImplementation implements DistanceCalculator {
    private static DistanceCalculatorImplementation distanceCalculator = new DistanceCalculatorImplementation();

    private DistanceCalculatorImplementation() {
    }

    public static DistanceCalculatorImplementation getInstance() {
        return distanceCalculator;
    }

    public List<ProxyCity> getAllCities() {
        return CachedConnection.getInstance().getAllCities();
    }

    public List<Map<String, Double>> calculateDistance(CalculationType calculationType, List<City> fromCities, List<City> toCities) {
        List<Map<String, Double>> distanceList = createEmptyList(fromCities, toCities);
        switch (calculationType) {
            case CROWFLIGHT:
                calculateCrowflight(distanceList, fromCities, toCities);
                break;
            case DISTANCE_MATRIX:
                calculateDistanceMatrix(distanceList, fromCities, toCities);
                break;
            case ALL:
                calculateCrowflight(distanceList, fromCities, toCities);
                calculateDistanceMatrix(distanceList, fromCities, toCities);
                break;
            default:
                throw new RuntimeException("Invalid calculation type");
        }
        return distanceList;
    }

    private List<Map<String, Double>> createEmptyList(List<City> fromCities, List<City> toCities) {
        int amountDistances = Math.min(fromCities.size(), toCities.size());
        ArrayList<Map<String, Double>> list = new ArrayList<Map<String, Double>>(amountDistances);
        for (int i = 0; i < amountDistances; ++i) {
            list.add(new HashMap<String, Double>());
        }
        return list;
    }

    private void calculateCrowflight(List<Map<String, Double>> distanceList, List<City> fromCities, List<City> toCities) {
        int amountDistances = Math.min(fromCities.size(), toCities.size());
        for (int i = 0; i < amountDistances; ++i) {
            calculateCrowflight(distanceList.get(i), fromCities.get(i), toCities.get(i));
        }
    }

    private void calculateCrowflight(Map<String, Double> distanceMap, City fromCity, City toCity) {
        double distance = Math.sqrt(
                (toCity.getLatitude() - fromCity.getLatitude()) * (toCity.getLatitude() - fromCity.getLatitude())
                        + (toCity.getLongitude() - fromCity.getLongitude()) * (toCity.getLongitude() - fromCity.getLongitude()));
        distanceMap.put("crowflight", distance);
    }

    private void calculateDistanceMatrix(List<Map<String, Double>> distanceList, List<City> fromCities, List<City> toCities) {
        int amountDistances = Math.min(fromCities.size(), toCities.size());
        for (int i = 0; i < amountDistances; ++i) {

            try {
                double distance = calculateDistanceMatrix(fromCities.get(i), toCities.get(i));
                distanceList.get(i).put("distanceMatrix", distance);
            } catch (DistanceCannotBeCalculatedException e) {
                distanceList.get(i).put("distanceMatrix", null);
            }

        }
    }

    private double calculateDistanceMatrix(City fromCity, City toCity)
            throws DistanceCannotBeCalculatedException {
        if (fromCity.equals(toCity)) {
            return 0;
        }
        for (Distance distance : CachedConnection.getInstance().getDistances(fromCity)) {
            if (distance.getToCity().equals(toCity)) {
                return distance.getDistance();
            }
        }
        throw new DistanceCannotBeCalculatedException("Distance from " + fromCity.getName() + " to " + toCity.getName() + "  not in the distance table");


    }

    public int uploadDataToDB(InputStream xmlFile) {
        boolean success = CachedConnection.getInstance().loadWithXMLFile(xmlFile);
        return success ? 200 : 204;
    }
}
