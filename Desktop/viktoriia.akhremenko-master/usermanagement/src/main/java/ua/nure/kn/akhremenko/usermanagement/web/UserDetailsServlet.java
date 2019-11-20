package ua.nure.kn.akhremenko.usermanagement.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserDetailsServlet extends UserServlet {

    public UserDetailsServlet() {
        super();
    }

    @Override
    String getJspTemplate() {
        return "/details.jsp";
    }

    @Override
    void doOk(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect("browse");
    }
}
