package com.example.quoteapi.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creates and stores a Bucket per IP using a ConcurrentHashMap.
 * rate.limit.capacity -> number of tokens (e.g. 5)
 * rate.limit.refillDurationSeconds -> refill period in seconds (e.g. 60)
 *
 * Note: keep property names simple so they are easy to parse.
 */
@Service
public class RateLimitService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final long capacity;
    private final Duration refillDuration;

    public RateLimitService(
            @Value("${rate.limit.capacity:5}") long capacity,
            @Value("${rate.limit.refillDurationSeconds:60}") long refillSeconds) {
        this.capacity = capacity;
        this.refillDuration = Duration.ofSeconds(refillSeconds);
    }

    public Bucket getBucket(String ipAddress) {
        return buckets.computeIfAbsent(ipAddress, k -> {
            // refill 'capacity' tokens every 'refillDuration'
            Refill refill = Refill.intervally(capacity, refillDuration);
            Bandwidth limit = Bandwidth.classic(capacity, refill);
            return Bucket.builder().addLimit(limit).build();
        });
    }
}
