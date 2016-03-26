package distanceCalculator;

import entity.City;
import entity.Distance;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by airat on 25.03.16.
 */
public class distanceCalculatorImplementation implements DistanceCalculator {

    public List getAllCities() {
        return null;
    }

    public List<Map<String, Double>> calculateDistance(CalculationType calculationType, List<City> fromCities, List<City> toCities)
            throws DistanceCannotBeCalculatedException {
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
                throw new DistanceCannotBeCalculatedException("Invalid calculation type");
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
        distanceMap.put("Crowflight", distance);
    }

    private void calculateDistanceMatrix(List<Map<String, Double>> distanceList, List<City> fromCities, List<City> toCities)
            throws DistanceCannotBeCalculatedException {
        int amountDistances = Math.min(fromCities.size(), toCities.size());
        for (int i = 0; i < amountDistances; ++i) {
            calculateDistanceMatrix(distanceList.get(i), fromCities.get(i), toCities.get(i));
        }
    }

    private void calculateDistanceMatrix(Map<String, Double> distanceMap, City fromCity, City toCity)
            throws DistanceCannotBeCalculatedException {
        double distance;
        DoubleKeeper minDistance = new DoubleKeeper();
        calculateDistanceMatrixRec(fromCity, 0, toCity, minDistance);
        if (minDistance.aDouble == Double.POSITIVE_INFINITY) {
            throw new DistanceCannotBeCalculatedException("Way from " + fromCity.getName() + " to " + toCity.getName() + " not found");
        }
        distanceMap.put("DistanceMatrix", minDistance.aDouble);
    }

    private void calculateDistanceMatrixRec(City currentCity, double currentDistance, City toCity, DoubleKeeper minDistance) {
        for (Distance distance : CachedConnection.getInstance().getDistances(currentCity)) {
            if (distance.getFromCity().equals(currentCity)) {
                if (distance.getToCity().equals(toCity)) {
                    minDistance.aDouble = (currentDistance + distance.getDistance()) < minDistance.aDouble ?
                            (currentDistance + distance.getDistance())
                            : minDistance.aDouble;
                } else {
                    if (currentDistance + distance.getDistance() < minDistance.aDouble) {
                        calculateDistanceMatrixRec(distance.getToCity(), currentDistance + distance.getDistance(),
                                toCity, minDistance);
                    }
                }
            } else {
                if (distance.getFromCity().equals(toCity)) {
                    minDistance.aDouble = (currentDistance + distance.getDistance()) < minDistance.aDouble ?
                            (currentDistance + distance.getDistance())
                            : minDistance.aDouble;
                } else {
                    if (currentDistance + distance.getDistance() < minDistance.aDouble) {
                        calculateDistanceMatrixRec(distance.getFromCity(), currentDistance + distance.getDistance(),
                                toCity, minDistance);
                    }
                }
            }
        }
    }

    public int uploadDataToDB(File xmlFile) {
        return 0;
    }

    class DoubleKeeper {
        double aDouble = Double.POSITIVE_INFINITY;
    }
}
