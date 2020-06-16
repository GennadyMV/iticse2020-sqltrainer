package rage.sqltrainer.security;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthenticationSuccessInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDetailsRetrievingService userDetailsRetrievingService;

    @Override
    public boolean preHandle(HttpServletRequest hsr, HttpServletResponse hsr1, Object o) throws Exception {
        if(hsr.getMethod().toLowerCase().contains("options")) {
            return true;
        }
        
        if(hsr.getRequestURI().toLowerCase().contains("/api")) {
            return true;
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return true;
        }

        String username = auth.getPrincipal().toString();
        if (username == null) {
            return true;
        } 

        if (!auth.getAuthorities().isEmpty()) {
            boolean checked = false;
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_CHECKED")) {
                    checked = true;
                }
            }

            if (checked) {
                return true;
            }
        } 

        boolean isAdmin = userDetailsRetrievingService.getUserDetails().isAdministrator();

        if (isAdmin) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }

            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_CHECKED"));
            UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), grantedAuths);

            SecurityContextHolder.getContext().setAuthentication(upat);

        } else {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_CHECKED"));
            UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), grantedAuths);

            SecurityContextHolder.getContext().setAuthentication(upat);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, ModelAndView mav) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, Exception excptn) throws Exception {
    }

}
