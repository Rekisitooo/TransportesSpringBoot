package com.transports.spring.service;

import com.transports.spring.model.AppUser;
import com.transports.spring.repository.IAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private IAppUserRepository appUserRepository;

    public Optional<AppUser> findUserById(final int userAppId){
        return appUserRepository.findById(userAppId);
    }
}
