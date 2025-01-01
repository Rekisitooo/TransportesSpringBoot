package com.transports.spring.controller;

import com.transports.spring.model.AppUser;
import com.transports.spring.repository.IAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public final class AppUserController {

    @Autowired
    private IAppUserRepository appUserRepository;

    @GetMapping("/getById")
    public AppUser getById(@PathVariable (value = "id") final int appUserId) {
        return this.appUserRepository.findById(appUserId).orElseThrow();
    }

    @GetMapping("/getAll")
    public List<AppUser> getAll() {
        return this.appUserRepository.findAll();
    }

    @GetMapping("/create")
    public AppUser create(@RequestBody final AppUser appUser) {
        return this.appUserRepository.save(appUser);
    }

    @GetMapping("/update")
    public AppUser update(@RequestBody final AppUser appUserToUpdate, @PathVariable (value = "id") final int appUserId) {
        final AppUser appUser = this.appUserRepository.findById(appUserId).orElseThrow();
        appUser.setName(appUserToUpdate.getName());
        return this.appUserRepository.save(appUser);
    }

    @GetMapping("/delete")
    public ResponseEntity<AppUser> delete(@PathVariable (value = "id") final int appUserId) {
        final AppUser existingAppUser = this.appUserRepository.findById(appUserId).orElseThrow();
        this.appUserRepository.delete(existingAppUser);
        return ResponseEntity.ok().build();
    }
}