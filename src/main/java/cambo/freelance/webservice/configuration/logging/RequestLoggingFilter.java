package cambo.freelance.webservice.configuration.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        CustomResponseWrapper wrappedResponse = new CustomResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        // Extract standard request info
        String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
        String ipAddress = extractClientIp(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");

        // ✅ Extract request parameters
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder paramString = new StringBuilder();
        if (!parameterMap.isEmpty()) {
            parameterMap.forEach((key, values) -> {
                paramString.append(key).append("=");
                paramString.append(Arrays.toString(values));
                paramString.append("  ");
            });
        }
        // Get and log response
        byte[] content = wrappedResponse.getBody();
        String responseBody = uri.contains("/download") ? "": new String(content, response.getCharacterEncoding());

        log.info("""
    
    ====== Incoming Request ===================================================
      Method      : {}
      URI         : {}
      IP Address  : {}
      User-Agent  : {}
      Parameters  : {}
      Request Body: {}
      Response    : {}
    ===========================================================================
    """, method, uri, ipAddress, userAgent, paramString.toString().trim(), requestBody,responseBody);


        // Write response back to client
        response.getOutputStream().write(content);
    }





    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0].trim(); // first IP is real client
        }
        return request.getRemoteAddr();
    }
}