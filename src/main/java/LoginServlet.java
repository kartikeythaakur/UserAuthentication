//import com.mysql.cj.Session;
import java.io.IOException;
import java.sql.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/engtech";
    private static final String USER = "root";
    private static final String PASSWORD = "kartikey1234";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String Email = req.getParameter("email");
        String Password = req.getParameter("password");

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);

            String query = "select * from user where email=? and password=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,Email);
            ps.setString(2,Password);

            ResultSet set = ps.executeQuery();

            if(set.next()){
                String id = set.getString("id");
                String name = set.getString("name");
                String phone = set.getString("phone");

                HttpSession session = req.getSession();
                session.setAttribute("userEmail",Email);
                session.setAttribute("userPassword",Password);
                session.setAttribute("name",name);
                session.setAttribute("phone",phone);

                res.sendRedirect("profile.jsp");
            }
            else{
                HttpSession session = req.getSession();
                session.setAttribute("errorMessage","Invalid email or password!, Try again");
                req.setAttribute("errorMessage","Invalid email or password , Try again");
                res.sendRedirect("login.jsp");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
