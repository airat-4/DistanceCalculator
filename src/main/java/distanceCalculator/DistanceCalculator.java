package distanceCalculator;

import entity.City;

import java.io.File;
import java.util.List;

/**
 * Created by airat on 25.03.16.
 */
public interface DistanceCalculator {

    List getAllCities();

    List calculateDistance(CalculationType calculationType, List<City> fromCities, List<City> toCities);

    int uploadDataToDB(File xmlFile);

}
