package rs.ac.uns.ftn.springauthcommon.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import rs.ac.uns.ftn.springcommonrestexceptions.dto.ApiError;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostConstruct
  public void init() {
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    ApiError apiError = new ApiError("Unauthorized - " + authException.getMessage());

    String json = objectMapper.writeValueAsString(apiError);
    response.getWriter().write(json);
  }
}
