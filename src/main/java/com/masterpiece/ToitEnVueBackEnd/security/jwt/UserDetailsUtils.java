package com.masterpiece.ToitEnVueBackEnd.security.jwt;

import com.masterpiece.ToitEnVueBackEnd.service.impl.authentication.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDetailsUtils {
    public static UserDetailsImpl getUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
