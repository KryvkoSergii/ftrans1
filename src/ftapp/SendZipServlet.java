package ftapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;


@WebServlet("/sendzip")
@MultipartConfig
public class SendZipServlet extends HttpServlet {
    public static String header = "<html><head><title>nikolll77.com welcome</title></head><body><html>";
    public static String footer = "</body></html>";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getParameter("pathval");
        if (s.isEmpty())  {
            response.getOutputStream().println("Folder must be!");
            return;
        }
        File folder = new File(s);
        //File folder = new File("d://forzip//");
        String tmpFileName;
        Collection<Part> parts = request.getParts();


        for (Part filePart:parts) {
            tmpFileName = getFileName(filePart);
            if ((tmpFileName!=null) && (!tmpFileName.isEmpty())){
                File file = new File(folder, tmpFileName);

                //------save file

                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
