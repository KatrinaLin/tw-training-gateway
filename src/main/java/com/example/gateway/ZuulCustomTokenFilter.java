package com.example.gateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_FORWARD_FILTER_ORDER;

@Component
public class ZuulCustomTokenFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_FORWARD_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.addZuulRequestHeader("x-username", authentication.getName());
        requestContext.addZuulRequestHeader("x-authorities", toStringForAuthorities(authentication.getAuthorities()));
        return null;
    }

    private String toStringForAuthorities(Collection<? extends GrantedAuthority> authorities) {
        StringBuilder stringBuilder = new StringBuilder();
        authorities.forEach(role -> {
            stringBuilder.append(role);
            stringBuilder.append(" ");
        });

        return stringBuilder.toString();
    }
}
