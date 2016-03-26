package distanceCalculator;

import entity.City;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by airat on 25.03.16.
 */
public interface DistanceCalculator {

    List getAllCities();

    List<Map<String, Double>> calculateDistance(CalculationType calculationType, List<City> fromCities, List<City> toCities)
            throws DistanceCannotBeCalculatedException;

    int uploadDataToDB(File xmlFile);

}
