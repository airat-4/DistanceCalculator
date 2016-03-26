package distanceCalculator;

import entity.City;
import entity.Distance;

import java.sql.*;
import java.util.*;

/**
 * Created by airat on 25.03.16.
 */
public class CachedConnection {
    private static final String url = "jdbc:mysql://localhost:3306/distance_calculator";
    private static final String user = "root";
    private static final String password = "root";
    private static CachedConnection cachedConnection;
    private static boolean loaded;
    private HashMap<Long, City> allCities = new HashMap<Long, City>();
    private HashMap<Long, List<Distance>> allDistance = new HashMap<Long, List<Distance>>();
    private long maxID;
    private CachedConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Драйвер БД загружен");
        } catch (ClassNotFoundException e) {
            System.err.println("Неудалось загрузить драйвер БД");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение с БД устоновлено");
        } catch (SQLException e) {
            System.err.println("Неудалось подключится к БД");
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();

                ResultSet cities = statement.executeQuery("select * from city");
                while (cities.next()) {
                    long id = cities.getLong("id");
                    String name = cities.getString("name");
                    double latitude = cities.getDouble("latitude");
                    double longitude = cities.getDouble("longitude");
                    City city = new City(id, name, latitude, longitude);
                    allCities.put(id, city);
                    if (maxID < id) {
                        maxID = id;
                    }
                }

                ResultSet distances = statement.executeQuery("select * from distance");
                while (distances.next()) {
                    double distance = distances.getDouble("distance");
                    long from_city = distances.getLong("from_city");
                    long to_city = distances.getLong("to_city");
                    Distance distance1 = new Distance(getCityByID(from_city), getCityByID(to_city), distance);
                    if (allDistance.get(from_city) == null) {
                        allDistance.put(from_city, new LinkedList<Distance>());
                    }
                    allDistance.get(from_city).add(distance1);
                }

                statement.close();
                connection.close();
                System.out.println("БД загружена");
                loaded = true;

            } catch (SQLException e) {
                System.err.println("Не удалось загрузить БД");
                e.printStackTrace();
            }
        }
    }


    public static synchronized CachedConnection getInstance() {
        if (!loaded) {
            cachedConnection = new CachedConnection();
        }
        return cachedConnection;
    }

    public City getCityByID(long id) {
        return allCities.get(id);
    }

    public List<Distance> getDistances(City fromCity) {
        return allDistance.get(fromCity.getID());
    }

    public ArrayList<ProxyCity> getAllCities() {
        Collection<City> values = allCities.values();
        ArrayList<ProxyCity> cities = new ArrayList<ProxyCity>(values.size());
        for (City city : values) {
            cities.add(new ProxyCity(city));
        }
        return cities;
    }
}
