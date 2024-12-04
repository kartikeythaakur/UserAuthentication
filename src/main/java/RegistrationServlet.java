
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/engtech";
    private static final String USER = "root";
    private static final String PASSWORD = "kartikey1234";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);

            //To check that Email already exists or not
            String query0 = "select * from user where email=?";
            PreparedStatement p = conn.prepareStatement(query0);
            p.setString(1,email);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                HttpSession s = req.getSession();
                s.setAttribute("emailExist","Email already exists!, Try login");
                resp.sendRedirect("registration.jsp");
            }
            else{
            String query = "insert into user (name, email, phone, password) values (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.executeUpdate();
            resp.sendRedirect("login.jsp");
         }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


