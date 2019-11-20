package ua.nure.kn.akhremenko.usermanagement.web;

import ua.nure.kn.akhremenko.usermanagement.User;
import ua.nure.kn.akhremenko.usermanagement.db.DaoFactory;
import ua.nure.kn.akhremenko.usermanagement.db.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;

public class EditUserServlet extends UserServlet {

    public EditUserServlet() {
        super();
    }

    @Override
    String getJspTemplate() {
        return "/edit.jsp";
    }

    @Override
    void doOk(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            User user = getUser(req);
            DaoFactory.getInstance().getUserDao().update(user);
            res.sendRedirect("browse");
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            showPage(req, res);
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected User getUser(HttpServletRequest req) throws ValidationException {
        User user = super.getUser(req);
        String id = req.getParameter("id");
        if (id == null) {
            throw new ValidationException("Invalid ID.");
        }
        try {
            user.setId(Long.valueOf(id));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid ID.");
        }
        return user;
    }
}
