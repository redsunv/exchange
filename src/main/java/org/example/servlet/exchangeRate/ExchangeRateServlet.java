package org.example.servlet.exchangeRate;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.currency.CurrencyDAO;
import org.example.dao.currency.CurrencyDAOImpl;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.dao.exchange.JdbcExchangeRateDaoImpl;

import org.example.dto.exchange.ExchangeRateResponseDTO;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;
import org.example.model.Currency;
import org.example.service.ExchangeRateService;


import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    // пара кодов
    // редактирование по паре кодов
    //doPuch перегруз базового метода сервис и проверка http метода запроса если он размен пач, то вызываем совй ду пач
    private final ObjectMapper objectMapper = new ObjectMapper();
    ExchangeRateDAO exchangeRateDAO = new JdbcExchangeRateDaoImpl();
    CurrencyDAO currencyDAO = new CurrencyDAOImpl();
    ExchangeRateService service = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();


        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Коды валют пары отсутствуют в адресе\"}");
                return;
            }
            String pair = pathInfo.substring(1);
            pair = pair.toUpperCase();

            if (pair.length() != 6) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Неверный формат.\"}");
                return;
            }
            String baseCode = pair.substring(0, 3);
            String targetCode = pair.substring(3, 6);


            ExchangeRateResponseDTO rate = service.getExchangeRate(baseCode, targetCode);


            String json = objectMapper.writeValueAsString(rate);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.write(json);

        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("{\"message\": \"" + e.getMessage() + "\"}");

        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Ошибка сервера: " + e.getMessage() + "\"}");
        }
    }
}


