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
    private HashMap<Long, City> allCities = new HashMap<Long, City>();
    private HashMap<Long, List<Distance>> allDistance = new HashMap<Long, List<Distance>>();
    private CachedConnection() {

    }
//        allCities.put(1L,new City(1,"City1", 0,0));
//        allCities.put(2L,new City(2,"City2", 5,5));
//        allCities.put(3L,new City(3,"City3", 5,10));
//        allCities.put(4L,new City(4,"City4", 25,40));
//        allDistance.put(1L, new LinkedList<Distance>());
//        allDistance.put(2L, new LinkedList<Distance>());
//        allDistance.put(3L, new LinkedList<Distance>());
//        allDistance.put(4L, new LinkedList<Distance>());
//        Distance distance;
//        distance = new Distance(allCities.get(1L),allCities.get(2l),1);
//
//        allDistance.get(1L).add(distance);
//
//        distance = new Distance(allCities.get(1l),allCities.get(3l),2);
//        allDistance.get(1L).add(distance);
//
//        distance = new Distance(allCities.get(2l),allCities.get(4l),3);
//        allDistance.get(2L).add(distance);
//
//        distance = new Distance(allCities.get(3l),allCities.get(4l),1);
//        allDistance.get(3L).add(distance);


    public static CachedConnection getInstance() {
        return connection;
    }

    public City getCityByID(long id) {
        return allCities.get(id);
    }

    public List<Distance> getDistances(City fromCity) {

        return allDistance.get(fromCity.getID());
    }
}
