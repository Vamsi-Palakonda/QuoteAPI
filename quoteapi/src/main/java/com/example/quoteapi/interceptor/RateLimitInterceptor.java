package com.example.quoteapi.interceptor;

import com.example.quoteapi.service.RateLimitService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * Interceptor that enforces per-IP rate limiting using Bucket4j.
 * Returns HTTP 429 with a JSON body when limit exceeded.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private final RateLimitService rateLimitService;

    public RateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = extractClientIp(request);
        Bucket bucket = rateLimitService.getBucket(ipAddress);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        int status = probe.isConsumed() ? HttpStatus.OK.value() : HttpStatus.TOO_MANY_REQUESTS.value();
        logger.info("Request from IP: {}, Path: {}, Status: {}", ipAddress, request.getRequestURI(), status);

        if (probe.isConsumed()) {
            // allowed
            response.setHeader("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));
            return true;
        } else {
            long nanosToWait = probe.getNanosToWaitForRefill();
            long secondsToWait = (nanosToWait + 999_999_999L) / 1_000_000_000L; // ceil to seconds
            sendRateLimitExceeded(response, secondsToWait);
            return false;
        }
    }

    private void sendRateLimitExceeded(HttpServletResponse response, long secondsToWait) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String json = String.format("{\"error\": \"Rate limit exceeded. Try again in %d seconds.\"}", secondsToWait);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    /**
     * Extract client IP - respects X-Forwarded-For (common when behind proxies).
     */
    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            // X-Forwarded-For may contain a list of IPs; the first is the original client
            return forwarded.split(",")[0].trim();
        }
        String remoteAddr = request.getRemoteAddr();
        return Objects.requireNonNullElse(remoteAddr, "unknown");
    }
}
