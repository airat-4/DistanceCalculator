package distanceCalculator;

import entity.City;
import entity.Distance;

import java.util.HashMap;
import java.util.List;

/**
 * Created by airat on 25.03.16.
 */
public class CachedConnection {
    private static CachedConnection connection = new CachedConnection();
    private HashMap<Long, City> AllCities;

    private CachedConnection() {
    }

    public static CachedConnection getInstance() {
        return connection;
    }

    public List<Distance> getDistances(City city) {

    }
}
