package servlets;

import distanceCalculator.DistanceCalculatorImplementation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by airat on 28.03.16.
 */
public class LoadXML extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        ServletFileUpload uploader;
        DiskFileItemFactory fileFactory = new DiskFileItemFactory();
        File filesDir = null;
        try {
            filesDir = new File(this.getClass().getResource("").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        fileFactory.setRepository(filesDir);
        uploader = new ServletFileUpload(fileFactory);
        try {
            List<FileItem> fileItems = uploader.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                InputStream inputStream = fileItem.getInputStream();
                int code = DistanceCalculatorImplementation.getInstance().uploadDataToDB(inputStream);
                response.setStatus(code);
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }
}
