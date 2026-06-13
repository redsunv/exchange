package servlet.exchangeRate;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dto.exchange.ExchangeRateConversionResponseDTO;

import dto.exchange.ExchangeRateRequestDTO;
import exception.NotFoundException;
import exception.ValidationException;
import service.ExchangeService;

import validator.ExchangeRateValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {

    ExchangeService service = new ExchangeService();
    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();


        try {
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            String amountParam = req.getParameter("amount");


            List<String> errors = ExchangeRateValidator.validateRequestParameters(from, to, amountParam);
            if (!errors.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"" + String.join(", ", errors) + "\"}");
                return;
            }

            BigDecimal amount = new BigDecimal(amountParam);

            ExchangeRateRequestDTO requestDTO = new ExchangeRateRequestDTO();
            requestDTO.setBaseCurrency(from);
            requestDTO.setTargetCurrency(to);
            requestDTO.setAmount(amount);

            ExchangeRateConversionResponseDTO responseDTO = service.exchange(requestDTO);


            String json = objectMapper.writeValueAsString(responseDTO);
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
            e.printStackTrace();
        }
    }
}
