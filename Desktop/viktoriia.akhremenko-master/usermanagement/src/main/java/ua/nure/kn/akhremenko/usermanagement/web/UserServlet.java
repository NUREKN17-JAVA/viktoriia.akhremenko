package ua.nure.kn.akhremenko.usermanagement.web;

import ua.nure.kn.akhremenko.usermanagement.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

abstract class UserServlet extends HttpServlet {

    private String jspTemplate;

    UserServlet() {
        super();
        jspTemplate = getJspTemplate();
    }

    abstract String getJspTemplate();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getParameter("okButton") != null) {
            doOk(req, res);
        } else if (req.getParameter("cancelButton") != null) {
            doCancel(req, res);
        } else {
            showPage(req, res);
        }
    }

    protected void showPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher(jspTemplate).forward(req, res);
    }

    abstract void doOk(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;

    protected void doCancel(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect("browse");
    }

    protected User getUser(HttpServletRequest req) throws ValidationException {

        String firstName = req.getParameter("firstName");
        if (firstName == null) {
            throw new ValidationException("Invalid First name.");
        }
        String lastName = req.getParameter("lastName");
        if (lastName == null) {
            throw new ValidationException("Invalid Last name.");
        }

        String dateOfBirthString = req.getParameter("dateOfBirth");
        if (dateOfBirthString == null) {
            throw new ValidationException("Invalid Date of birth.");
        }

        try {
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString);

            return new User(firstName, lastName, dateOfBirth);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid Date of birth.");
        }
    }

}
