package servlets;

import distanceCalculator.CachedConnection;
import distanceCalculator.CalculationType;
import distanceCalculator.DistanceCalculatorImplementation;
import entity.City;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by airat on 28.03.16.
 */
public class Calculate extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        String type = request.getParameter("type");
        Enumeration<String> parameterNames = request.getParameterNames();
        String[] from = request.getParameterValues("from[]");
        String[] to = request.getParameterValues("to[]");
        CalculationType calculationType = CalculationType.valueOf(type);
        ArrayList<City> fromCities = new ArrayList<>(from.length);
        ArrayList<City> toCities = new ArrayList<>(to.length);
        for (int i = 0; i < from.length; i++) {
            fromCities.add(CachedConnection.getInstance().getCityByID(Long.valueOf(from[i])));
            toCities.add(CachedConnection.getInstance().getCityByID(Long.valueOf(to[i])));
        }
        List<Map<String, Double>> result = DistanceCalculatorImplementation.getInstance().calculateDistance(calculationType, fromCities, toCities);
        json.put("result", result);
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.close();
    }
}
