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
import org.example.exception.DatabaseAccessException;
import org.example.mapper.CurrencyMapper;
import org.example.model.Currency;

import java.sql.SQLException;
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
        try {
            // Получаем Entity из БД
            List<Currency> currencies = currencyDAO.getAllCurrencies();

            // Преобразуем Entity → Response DTO
            List<CurrencyResponseDTO> dtos = CurrencyMapper.toResponseDTOList(currencies);

            //  DTO → JSON и отправка
            String json = objectMapper.writeValueAsString(dtos);
            out.print(json);

            //  Устанавливаем статус
            resp.setStatus(HttpServletResponse.SC_OK);


        } catch (DatabaseAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
            e.printStackTrace();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Internal server error\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

    }
}
