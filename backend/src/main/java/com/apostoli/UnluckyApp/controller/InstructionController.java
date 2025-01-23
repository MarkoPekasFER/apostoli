package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.Instruction;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.service.impl.InstructionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/instruction")
public class InstructionController {

    public final InstructionServiceImpl instructionService;

    @Autowired
    public InstructionController(InstructionServiceImpl instructionService){
        this.instructionService=instructionService;
    }

    @PostMapping("/create")
    public void createInstruction(@RequestBody Instruction instruction, Principal principal){
        instructionService.createInstruction(instruction);
    }

    @PostMapping("/delete/{instructionID}")
    public void deleteInstruction(Principal principal, @PathVariable Long id){
        instructionService.deleteInstruction(id,principal.getName());
    }

    @PostMapping("/{disasterType}")
    public List<Instruction> getInstructionByDisasterType(@PathVariable DisasterType disasterType){
        return instructionService.fetchByDisasterType(disasterType);
    }

    @PostMapping("/all")
    public List<Instruction> getAllInstructions(){ return instructionService.fetchAllInstructions(); }
}
