package org.zuyi.flashreserve.common.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ResponseUtils {

    private final ObjectMapper objectMapper;

    public void unauthorized(
            HttpServletResponse response,
            String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json");

        Result<Void> result = Result.fail(401, message);

        objectMapper.writeValue(
                response.getWriter(),
                result
        );
    }
}