package ua.nure.kn.akhremenko.usermanagement.web;

import ua.nure.kn.akhremenko.usermanagement.User;
import ua.nure.kn.akhremenko.usermanagement.db.DaoFactory;
import ua.nure.kn.akhremenko.usermanagement.db.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class BrowseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("addButton") != null) {
            addUser(req, resp);
        } else if (req.getParameter("editButton") != null) {
            editUser(req, resp);
        } else if (req.getParameter("deleteButton") != null) {
            deleteUser(req, resp);
        } else if (req.getParameter("detailsButton") != null) {
            userDetails(req, resp);
        } else {
            browse(req, resp);
        }
    }

    private void browse(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        Collection<User> users;
        try {
            users = DaoFactory.getInstance().getUserDao().findAll();
            req.getSession().setAttribute("users", users);
            req.getRequestDispatcher("/browse.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addUser(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect("add");
    }

    private void editUser(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.trim().length() == 0) {
            req.setAttribute("error", "You must select a user.");
            req.getRequestDispatcher("/browse.jsp").forward(req, res);
            return;
        }
        try {
            User user = DaoFactory.getInstance().getUserDao().find(new Long(id));
            req.getSession().setAttribute("user", user);
        } catch (DatabaseException e) {
            req.setAttribute("error", "ERROR: " + e.getMessage());
            req.getRequestDispatcher("/browse.jsp").forward(req, res);
            return;
        }
        res.sendRedirect("edit");
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.trim().length() == 0) {
            req.setAttribute("error", "You must select a user.");
            req.getRequestDispatcher("/browse.jsp").forward(req, res);
            return;
        }
        try {
            DaoFactory.getInstance().getUserDao().delete(new Long(id));
        } catch (DatabaseException e) {
            req.setAttribute("error", "ERROR: " + e.getMessage());
            req.getRequestDispatcher("/browse.jsp").forward(req, res);
            return;
        }
        browse(req, res);
    }

    private void userDetails(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.trim().length() == 0) {
            req.setAttribute("error", "You must select a user.");
            req.getRequestDispatcher("/browse.jsp").forward(req, res);
            return;
        }
        try {
            User user = DaoFactory.getInstance().getUserDao().find(new Long(id));
            req.getSession().setAttribute("user", user);
        } catch (DatabaseException e) {
            req.setAttribute("error", "ERROR: " + e.getMessage());
            req.getRequestDispatcher("/browse.jsp").forward(req, res);
            return;
        }
        res.sendRedirect("details");
    }
}
