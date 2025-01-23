package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Instruction;
import com.apostoli.UnluckyApp.model.enums.DisasterType;

import java.util.List;

public interface InstructionService {
    List<Instruction> fetchAllInstructions();

    void createInstruction(Instruction instruction);

    void deleteInstruction(Long id, String username);

    List<Instruction> fetchByDisasterType(DisasterType disasterType);
}
