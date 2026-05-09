package grupa1.jutjubic.auth;

import grupa1.jutjubic.model.User;
import grupa1.jutjubic.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserActivityFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, Instant> activityCache = new ConcurrentHashMap<>();

    public UserActivityFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            Instant now = Instant.now();
            Instant lastUpdate = activityCache.get(username);

            if (lastUpdate == null || lastUpdate.isBefore(now.minus(5, ChronoUnit.MINUTES))) {
                User user = userRepository.findByUsername(username);

                if (user != null) {
                    user.setLastActiveAt(now);
                    userRepository.save(user);
                    activityCache.put(username, now);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}