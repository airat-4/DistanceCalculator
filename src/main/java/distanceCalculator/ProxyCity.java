package distanceCalculator;

import entity.City;

/**
 * Created by airat on 26.03.16.
 */
public class ProxyCity {
    private City city;

    public ProxyCity(City city) {
        this.city = city;
    }

    public long getID() {
        return city.getID();
    }

    public String getName() {
        return city.getName();
    }
}
