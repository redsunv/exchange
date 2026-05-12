package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.CurrencyDAO;
import org.example.dao.CurrencyDAOImpl;

import java.io.IOException;
import java.io.PrintWriter;

import org.example.dto.CurrencyRequestDTO;
import org.example.dto.CurrencyResponseDTO;
import org.example.mapper.CurrencyMapper;
import org.example.model.Currency;
import java.util.List;


@WebServlet("/currencies")
//список валют
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        List<Currency> currencies = currencyDAO.getAllCurrencies();






    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
