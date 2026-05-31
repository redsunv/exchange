package org.example.servlet.exchangeRate;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.exchange.ExchangeRateResponseDTO;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;
import org.example.service.ExchangeRateService;
import org.example.validator.ExchangeRateValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<ExchangeRateResponseDTO> rates = exchangeRateService.getAllExchangeRates();
            String json = objectMapper.writeValueAsString(rates);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();


        try {

            String baseCode = req.getParameter("baseCurrencyCode");
            String targetCode = req.getParameter("targetCurrencyCode");
            String rateParam = req.getParameter("rate");
            if (rateParam == null || rateParam.isBlank()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Missing parameter: rate\"}");
                return;
            }

            List<String> formatErrors = ExchangeRateValidator.validateExchangeRateCode(baseCode, targetCode);
            if (!formatErrors.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"" + String.join(", ", formatErrors) + "\"}");
                return;
            }

            rateParam = rateParam.trim().replace(',', '.').replaceAll("\\s", "");

            BigDecimal rate;
            try {
                rate = new BigDecimal(rateParam);
                if (rate.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Неверный формат курса.\"}");
                return;
            }
            ExchangeRateResponseDTO response = exchangeRateService.createNewExchangeRate(baseCode, targetCode, rate);

            String json = objectMapper.writeValueAsString(response);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.write(json);


        } catch (ValidationException e) {
            if (e.getMessage().contains("уже существует")) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT); // 409
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            }
            out.write("{\"message\": \"" + e.getMessage() + "\"}");

        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            out.write("{\"message\": \"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            out.write("{\"message\": \"Ошибка сервера: " + e.getMessage() + "\"}");
        }
    }
}
