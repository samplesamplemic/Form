package com.example.registration.service.securitySession;

import com.example.registration.entity.CustomUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service
@Log4j2
public class SessionUserServiceImpl implements SessionUserService {

    @Autowired
    SessionRegistry sessionRegistry;

    @Override
    public void getUserLoggedInfo() {
        List<Object> allPrincipal = sessionRegistry.getAllPrincipals();
        for(Object principal : allPrincipal){
            if(principal instanceof CustomUserDetails){
                CustomUserDetails user = (CustomUserDetails) principal;
                SimpleDateFormat timeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                log.info("User "+user.getUsername()+" logged in - "+ timeDate.format(new Date()));
            }
        }
    }
}
