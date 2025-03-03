//package sdk.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class AuthFilter extends OncePerRequestFilter {
//
//    @Value("${auth.header}")
//    private String TOKEN_HEADER;
//
//    @Autowired(required = false)
//    private UserService userService;
//
//    @Autowired(required = false)
//    private TokenUtil tokenUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String header = request.getHeader(TOKEN_HEADER);
//        final SecurityContext securityContext = SecurityContextHolder.getContext();
//
//        if (header != null && securityContext.getAuthentication() == null) {
//            String token = header.substring("Bearer ".length());
//            String username = tokenUtil.getUserNameFromToken(token);
//            if(username != null) {
//                UserDetails userDetails = userService.loadUserByUsername(username);
//                if (tokenUtil.isTokenValid(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        }
//
//
//        try {
//            filterChain.doFilter(request,response);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ServletException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
