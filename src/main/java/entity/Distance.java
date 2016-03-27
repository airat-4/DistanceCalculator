package entity;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by airat on 25.03.16.
 */
@XmlType(propOrder = {"fromCity", "toCity", "distance"})
public class Distance {
    private City fromCity;
    private City toCity;
    private double distance;

    public Distance() {
    }

    public Distance(City fromCity, City toCity, double distance) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Distance distance1 = (Distance) o;

        if (Double.compare(distance1.distance, distance) != 0) return false;
        if (!fromCity.equals(distance1.fromCity)) return false;
        return toCity.equals(distance1.toCity);

    }

    @Override
    public int hashCode() {
        int result = fromCity.hashCode();
        result = 31 * result + toCity.hashCode();
        return result;
    }

    public City getFromCity() {
        return fromCity;
    }

    public void setFromCity(City fromCity) {
        this.fromCity = fromCity;
    }

    public City getToCity() {
        return toCity;
    }

    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
