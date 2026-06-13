package servlet.currency;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.currency.CurrencyDAO;
import dao.currency.CurrencyDAOImpl;

import java.io.IOException;
import java.io.PrintWriter;


import dto.currency.CurrencyResponseDTO;
import exception.DatabaseAccessException;
import mapper.CurrencyMapper;
import model.Currency;
import validator.CurrenciesValidator;

import java.util.List;
import java.util.Map;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        try {

            List<Currency> currencies = currencyDAO.getAll();

            List<CurrencyResponseDTO> dto = CurrencyMapper.toResponseDTOList(currencies);

            String json = objectMapper.writeValueAsString(dto);
            out.print(json);

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
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();


        try {
            String code = req.getParameter("code");
            String fullName = req.getParameter("name");
            String sign = req.getParameter("sign");

            if (code != null) code = code.trim().toUpperCase();
            if (fullName != null) fullName = fullName.trim();
            if (sign != null) sign = sign.trim();

            List<String> errors = CurrenciesValidator.validateCurrenciesCode(code, fullName, sign);
            if (!errors.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(objectMapper.writeValueAsString(Map.of("Неверный формат", errors)));
                return;
            }

            Currency currency = new Currency();
            currency.setCode(code);
            currency.setFullName(fullName);
            currency.setSign(sign);

            Currency saved = currencyDAO.save(currency);

            CurrencyResponseDTO responseDTO = CurrencyMapper.toResponseDTO(saved);
            String json = objectMapper.writeValueAsString(responseDTO);
            out.print(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DatabaseAccessException e) {

            if (e.getMessage().contains("уже существует")) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }


    }
}
