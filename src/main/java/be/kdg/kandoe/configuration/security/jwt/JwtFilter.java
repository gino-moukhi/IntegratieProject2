package be.kdg.kandoe.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

public class JwtFilter extends GenericFilterBean{

    private String SECRET;

    private long EXPIRATION_TIME;

    public JwtFilter(String SECRET, long EXPIRATION_TIME) {
        this.SECRET = SECRET;
        this.EXPIRATION_TIME = EXPIRATION_TIME;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final String authHeader = req.getHeader("authorization");

        if("OPTIONS".equals(req.getMethod())){
            res.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        }else{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                throw new ServletException("Missing or invalid Authorization header");
            }

            //Substring 7 because we want to remove the Bearer from our token so that we can check it
            final String token = authHeader.substring(7);
            try{

                Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
                chain.doFilter(request, response);
            }catch (Exception e){
                String t = "";
            }
        }


    }
}
