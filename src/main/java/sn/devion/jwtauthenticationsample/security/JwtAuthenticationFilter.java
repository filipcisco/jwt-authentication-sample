package sn.devion.jwtauthenticationsample.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sn.devion.jwtauthenticationsample.repositories.TokenRepository;
import sn.devion.jwtauthenticationsample.services.JwtService;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Used to filter requests and check if they have a valid JWT token
 * @Component Indicates that an annotated class is a "component".
 * Such classes are considered as candidates for auto-detection when using annotation-based configuration and classpath scanning.
 * @RequiredArgsConstructor Lombok annotation to create a constructor with required arguments
 * @see OncePerRequestFilter - Abstract base class for <code>Filter</code>s that perform once-per-request
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;


  /**
   * Filter requests - check if they have a valid JWT token
   * @param request             request
   * @param response            response
   * @param filterChain         filter chain
   * @throws ServletException   servlet exception
   * @throws IOException        io exception
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    //  Allow authentication requests to pass through filter chain
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Get authorization header and validate
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    // Check if the header contains the Bearer token
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    //
    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt);

    /*
     * If the token is valid, set the authentication in context to specify that the current user is authenticated.
     * Spring Security uses this class to perform authorization checks.
     */
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      var isTokenValid = tokenRepository.findByToken(jwt)
          .map(t -> !t.isExpired() && !t.isRevoked())
          .orElse(false);
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    // Continue filter execution
    filterChain.doFilter(request, response);
  }
}
