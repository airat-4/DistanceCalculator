package entity;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by airat on 25.03.16.
 */

@XmlType(propOrder = {"ID", "name", "latitude", "longitude"})
public class City {
    private long ID;
    private String name;
    private double latitude;
    private double longitude;

    public City() {
    }

    public City(long ID, String name, double latitude, double longitude) {
        this.ID = ID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return ID == city.ID
                && name.equals(city.name)
                && latitude == city.latitude
                && longitude == city.longitude;

    }

    @Override
    public int hashCode() {
        return (int) (ID ^ (ID >>> 32));
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
