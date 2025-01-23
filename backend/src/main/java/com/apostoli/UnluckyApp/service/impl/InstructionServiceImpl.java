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

    public void createInstruction(Instruction instruction){

        instruction.setTitle(instruction.getTitle());
        instruction.setDescription(instruction.getDescription());
        instruction.setDisasterType(instruction.getDisasterType());

        instructionRepository.save(instruction);

    }

    public void deleteInstruction(Long id,String username){

        Instruction instruction=instructionRepository.getById(id);

        AppUser user = appUserRepository.findByUsername(username).orElse(null);


        instructionRepository.delete(instruction);

    }

    public List<Instruction> fetchByDisasterType(DisasterType disasterType) { return instructionRepository.getByDisasterType(disasterType); }
}
