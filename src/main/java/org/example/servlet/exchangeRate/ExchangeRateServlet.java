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


import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {


    private final ObjectMapper objectMapper = new ObjectMapper();
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


    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        if (method.equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else
            super.service(req, resp);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            String pair = pathInfo.substring(1);// удаляет слещ
            pair = pair.toUpperCase();

            if (pair.length() != 6) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Неверный формат.Ожидается 6 символов\"}");
                return;
            }
            String baseCode = pair.substring(0, 3);
            String targetCode = pair.substring(3, 6);

            String rateParam = req.getParameter("rate");
            if (rateParam == null || rateParam.isBlank()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Missing parameter: rate\"}");
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
                out.write("{\"message\": \"Неверный формат курса. Используйте точку (например, 0.11)\"}");
                return;
            }

            ExchangeRateResponseDTO dto = service.upDateExchangeRate(baseCode, targetCode, rate);
            String json = objectMapper.writeValueAsString(dto);
            out.print(json);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (NotFoundException e) {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("{\"message\": \"" + e.getMessage() + "\"}");

        } catch (ValidationException e) {

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"" + e.getMessage() + "\"}");

        } catch (Exception e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Ошибка сервера: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }


    }
}



