package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Instruction;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.InstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructionServiceImpl {

    public InstructionRepository instructionRepository;

    public AppUserRepository appUserRepository;

    @Autowired
    public InstructionServiceImpl(InstructionRepository instructionRepository,AppUserRepository appUserRepository){
        this.instructionRepository=instructionRepository;
        this.appUserRepository=appUserRepository;
    }

    public List<Instruction> fetchAllInstructions(){ return instructionRepository.findAll(); }

    public void createInstruction(Instruction instruction,String username){

        AppUser user = appUserRepository.findByUsername(username).orElse(null);

        List<RoleType> allowedRoles = new ArrayList<>();

        allowedRoles.add(RoleType.SUPER_ADMIN);
        allowedRoles.add(RoleType.ADMIN);
        allowedRoles.add(RoleType.RESPONDER);
        allowedRoles.add(RoleType.ORGANISATION);


        boolean allowed=false;

        List<Role> roles = user.getRoles();

        for(int i=0;i<roles.size();i++){
            if(allowedRoles.contains(roles.get(i)))
                allowed=true;
        }

        if(!allowed){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
        }

        instruction.setTitle(instruction.getTitle());
        instruction.setDescription(instruction.getDescription());
        instruction.setDisasterType(instruction.getDisasterType());

        instructionRepository.save(instruction);

    }

    public void deleteInstruction(Long id,String username){

        Instruction instruction=instructionRepository.getById(id);

        AppUser user = appUserRepository.findByUsername(username).orElse(null);

        List<RoleType> allowedRoles = new ArrayList<>();

        allowedRoles.add(RoleType.SUPER_ADMIN);
        allowedRoles.add(RoleType.ADMIN);
        allowedRoles.add(RoleType.RESPONDER);
        allowedRoles.add(RoleType.ORGANISATION);


        boolean allowed=false;

        List<Role> roles = user.getRoles();

        for(int i=0;i<roles.size();i++){
            if(allowedRoles.contains(roles.get(i)))
                allowed=true;
        }

        if(!allowed){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this resource.");
        }

        instructionRepository.delete(instruction);

    }

    public List<Instruction> fetchByDisasterType(DisasterType disasterType) { return instructionRepository.getByDisasterType(disasterType); }
}
