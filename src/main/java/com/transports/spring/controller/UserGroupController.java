package com.transports.spring.controller;

import com.transports.spring.model.UserGroup;
import com.transports.spring.repository.IUserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_group")
public final class UserGroupController {

    @Autowired
    private IUserGroupRepository userGroupRepository;

    @GetMapping("/getById")
    public UserGroup getById(@PathVariable (value = "id") final int userGroupId) {
        return this.userGroupRepository.findById(userGroupId).orElseThrow();
    }

    @GetMapping("/getAll")
    public List<UserGroup> getAll() {
        return this.userGroupRepository.findAll();
    }

    @GetMapping("/create")
    public UserGroup create(@RequestBody final UserGroup userGroup) {
        return this.userGroupRepository.save(userGroup);
    }

    @GetMapping("/update")
    public UserGroup update(@RequestBody final UserGroup userGroupToUpdate, @PathVariable (value = "id") final int userGroupId) {
        final UserGroup userGroup = this.userGroupRepository.findById(userGroupId).orElseThrow();
        userGroup.setName(userGroupToUpdate.getName());
        return this.userGroupRepository.save(userGroup);
    }

    @GetMapping("/delete")
    public ResponseEntity<UserGroup> delete(@PathVariable (value = "id") final int userGroupId) {
        final UserGroup existingUserGroup = this.userGroupRepository.findById(userGroupId).orElseThrow();
        this.userGroupRepository.delete(existingUserGroup);
        return ResponseEntity.ok().build();
    }
}