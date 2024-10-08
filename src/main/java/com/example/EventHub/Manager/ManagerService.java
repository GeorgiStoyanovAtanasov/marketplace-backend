package com.example.EventHub.Manager;

import com.example.EventHub.Organisation.Organisation;
import com.example.EventHub.Organisation.OrganisationDTO;
import com.example.EventHub.Organisation.OrganisationMapper;
import com.example.EventHub.User.User;
import com.example.EventHub.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    OrganisationMapper organisationMapper;
    public Integer getManagerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        Manager manager = managerRepository.findByUser(user);
        return manager.getId();
    }
    public OrganisationDTO getOrganisationForManager(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        Manager manager = managerRepository.findByUser(user);
        if(manager.getOrganisation() == null){
            return null;
        }
        return organisationMapper.toDTO(manager.getOrganisation());
    }
}
