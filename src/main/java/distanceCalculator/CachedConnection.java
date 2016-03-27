package distanceCalculator;

import entity.City;
import entity.Distance;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
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

    public boolean loadWithXMLFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(City.class, Distance.class, Container.class);
            Unmarshaller unmarshaller =
                    context.createUnmarshaller();
            Container container = (Container) unmarshaller.unmarshal(file);
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            for (City city : container.cities) {
                addCity(statement, city);
            }
            for (Distance distance : container.distances) {
                addDistance(statement, distance);
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void addDistance(Statement statement, Distance distance) throws SQLException {
        List<Distance> distanceList = allDistance.get(distance.getFromCity().getID());
        Distance oldDistance = null;
        if (distanceList != null) {
            for (Distance distance1 : distanceList) {
                if (distance1.getToCity().getID() == distance.getToCity().getID())
                    oldDistance = distance1;
            }
        }
        addCity(statement, distance.getToCity());
        addCity(statement, distance.getFromCity());
        if (oldDistance == null) {
            Distance newDistance = new Distance(getCityByID(distance.getFromCity().getID()),
                    getCityByID(distance.getToCity().getID()), distance.getDistance());
            if (allDistance.get(distance.getToCity().getID()) == null) {
                allDistance.put(distance.getToCity().getID(), new LinkedList<Distance>());
            }
            allDistance.get(distance.getToCity().getID()).add(newDistance);
            statement.execute("insert into distance (from_city, to_city, distance) values (" +
                    distance.getFromCity().getID() + ", " + distance.getToCity().getID() + "," + distance.getDistance() + ")");
        } else {
            oldDistance.setDistance(distance.getDistance());
            statement.execute("update distance " +
                    "set distance = " + distance.getDistance() +
                    " where from_city = " + distance.getFromCity().getID() +
                    " and to_city = " + distance.getToCity().getID());
        }
    }

    private void addCity(Statement statement, City city) throws SQLException {
        City oldCity = getCityByID(city.getID());
        if (oldCity == null) {
            allCities.put(city.getID(), city);
            statement.execute("insert into city (id, name, latitude, longitude) " +
                    "values(" + city.getID() + ",\"" + city.getName() + "\", " + city.getLatitude() + "," + city.getLongitude() + ")");
        } else {
            if (!city.equals(oldCity)) {
                oldCity.setName(city.getName());
                oldCity.setLatitude(city.getLatitude());
                oldCity.setLongitude(city.getLongitude());
                statement.execute("update city " +
                        "set name = \"" + city.getName() + "\", latitude = " + city.getLatitude() + ", longitude = " + city.getLongitude() +
                        " where id = " + city.getID());
            }
        }
    }

    @XmlType(propOrder = {"cities", "distances"})
    @XmlRootElement
    private static class Container {
        private List<City> cities = new ArrayList<>();
        private List<Distance> distances = new ArrayList<>();

        public List<Distance> getDistances() {
            return distances;
        }

        public void setDistances(List<Distance> distances) {
            this.distances = distances;
        }

        public List<City> getCities() {
            return cities;
        }

        public void setCities(List<City> cities) {
            this.cities = cities;
        }
    }
}
