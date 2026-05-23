package org.example.servlet.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.currency.CurrencyDAO;
import org.example.dao.currency.CurrencyDAOImpl;
import org.example.dto.currency.CurrencyResponseDTO;
import org.example.exception.DatabaseAccessException;
import org.example.exception.NotFoundException;
import org.example.mapper.CurrencyMapper;
import org.example.model.Currency;
import org.example.validator.CurrencyValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String pathInfo = req.getPathInfo();

            String code = pathInfo.substring(1);
            code = code.toUpperCase();


            List<String> errors = CurrencyValidator.validateCurrencyCode(code);
            if (!errors.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(objectMapper.writeValueAsString(Map.of("errors", errors)));
                return;
            }


            Currency currency = currencyDAO.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Валюта не найдена"));

            CurrencyResponseDTO dto = CurrencyMapper.toResponseDTO(currency);


            String json = objectMapper.writeValueAsString(dto);


            out.print(json);
            resp.setStatus(HttpServletResponse.SC_OK);  // 200

        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);  // 404
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (DatabaseAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500
            out.print("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");

        }


    }
}