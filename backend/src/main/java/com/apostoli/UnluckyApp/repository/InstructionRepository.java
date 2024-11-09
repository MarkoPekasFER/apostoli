package com.apostoli.UnluckyApp.repository;

import com.apostoli.UnluckyApp.model.entity.Instruction;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructionRepository extends JpaRepository<Instruction,Long> {

    Optional<Instruction> getByDisasterType(DisasterType disasterType);
}
