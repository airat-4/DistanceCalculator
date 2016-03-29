package distanceCalculator;

import entity.City;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by airat on 25.03.16.
 */
public interface DistanceCalculator {

    List<ProxyCity> getAllCities();

    List<Map<String, Double>> calculateDistance(CalculationType calculationType, List<City> fromCities, List<City> toCities);

    int uploadDataToDB(InputStream xmlFile);

}
