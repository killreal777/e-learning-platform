package itmo.blps.courseservice.config;

import io.jsonwebtoken.JwtException;
import itmo.blps.courseservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private static final Object CREDENTIALS = null;

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        doFilterInternal(request);
        filterChain.doFilter(request, response);
    }

    private void doFilterInternal(HttpServletRequest request) {
        final String jwt = getJwtFromRequest(request);
        if (jwt == null) {
            return;
        }

        UserDetails userDetails;
        try {
            userDetails = jwtService.parseUser(jwt);
            if (!userDetails.isEnabled()) {
                return;
            }
        } catch (JwtException e) {
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                CREDENTIALS,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);

        if (authHeader != null && authHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            return authHeader.substring(AUTHORIZATION_HEADER_PREFIX.length());
        }
        return null;
    }
}