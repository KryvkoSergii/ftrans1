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
    public static String header = "<html><head><title>Загрузка файлов об авариях и списков на обзвон</title></head><body><html>";
    public static String footer = "</body></html>";

    private static String[] getFileName(Part part) {
        String[] content = new String[2];
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                content[0] = fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
            if (cd.trim().startsWith("name")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                content[1] = fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return content;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s1 = request.getParameter("pathval1");
        response.setCharacterEncoding("utf-8");
        if (s1.isEmpty()) {
            response.getOutputStream().println("Путь к \"Сообщнение об аварии\" не должен быть пустым");
            return;
        }
        String s2 = request.getParameter("pathval2");
        if (s1.isEmpty()) {
            response.getOutputStream().println("Путь к \"Список на обзвон (Outbound)\" не должен быть пустым");
            return;
        }
        File folder1 = new File(s1);
        File folder2 = new File(s2);
        //File folder = new File("d://forzip//");
        String tmpFileName;
        Collection<Part> parts = request.getParts();

        for (Part filePart : parts) {
            String[] headers = getFileName(filePart);
            if ((headers[0] != null) && (!headers[0].isEmpty())) {
                File file;
                //------save first file
                if (headers[1].equals("file1")) {
                    file = new File(folder1, headers[0]);
                    try (InputStream input = filePart.getInputStream()) {
                        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                //------save first file
                else if (headers[1].equals("file2")) {
                    file = new File(folder2, headers[0]);
                    try (InputStream input = filePart.getInputStream()) {
                        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
        response.sendRedirect("/ftrans");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
