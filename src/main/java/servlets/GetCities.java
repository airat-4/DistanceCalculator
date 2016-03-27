package servlets;

import distanceCalculator.DistanceCalculatorImplementation;
import distanceCalculator.ProxyCity;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by airat on 27.03.16.
 */
public class GetCities extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        List<ProxyCity> allCities = DistanceCalculatorImplementation.getInstance().getAllCities();
        json.put("allCities", allCities);
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.close();
    }
}
