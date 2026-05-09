package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.service.ILoginAttemptService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService implements ILoginAttemptService {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, this::createNewBucket);
    }

    private Bucket createNewBucket(String key) {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    public void loginSucceeded(String key) {
        cache.remove(key);
    }

    public boolean tryAttempt(String key) {
        Bucket bucket = resolveBucket(key);
        return bucket.tryConsume(1);
    }
}