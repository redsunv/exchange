package org.example.servlet.exchangeRate;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.dao.exchange.JdbcExchangeRateDaoImpl;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {

    ExchangeRateDAO exchangeRateDAO = new JdbcExchangeRateDaoImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();


        try {
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            String amount = req.getParameter("amount");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
