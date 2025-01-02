package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.ExpenseSplitCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitUpdateDTO;
import com.RoomyExpense.tracker.model.ExpenseSplit;
import com.RoomyExpense.tracker.service.ExpenseSplitService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expense-splits")
public class ExpenseSplitController {

    @Autowired
    private ExpenseSplitService expenseSplitService;

    @PostMapping("/createExpenseSplit")
    public ResponseEntity<?> createExpenseSplit(@RequestBody ExpenseSplitCreationDTO expenseSplitCreationDTO) {
        try {
            List<ExpenseSplit> expenseSplits = expenseSplitService.createExpenseSplit(expenseSplitCreationDTO);
            StringBuilder response = new StringBuilder();

            for (ExpenseSplit expenseSplit : expenseSplits) {
                response.append("Expense Split created. ")
                        .append("\nUser: ").append(expenseSplit.getUser().getName())
                        .append("\nExpense: ").append(expenseSplit.getExpense().getName())
                        .append("\nAmount split: ").append(expenseSplit.getAmount()).append("\n");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred while creating the Expense split");
        }
    }




    @GetMapping("/getAll")
    public ResponseEntity<?> getAllExpenseSplits() {
        List<ExpenseSplitDTO> expenseSplitDTOS = expenseSplitService.getAllExpenseSplits();
        if (expenseSplitDTOS.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("There are no Expense splits registered at the moment");
        }
        return ResponseEntity.ok(expenseSplitDTOS);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getExpenseSplitById(@PathVariable Long id) {
        Optional<ExpenseSplitDTO> expenseSplitDTO = expenseSplitService.getExpenseSplitById(id);
        if (expenseSplitDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("Expense Split not found");
        }
        return ResponseEntity.ok(expenseSplitDTO.get());
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteExpenseSplit(@PathVariable Long id){
        return expenseSplitService.getExpenseSplitById(id)
                .map(expenseSplitDTO -> {expenseSplitService.deleteExpenseSplit(id);
                return ResponseEntity.status(HttpStatus.OK).body("Expense Split with Id: " + id + " Succesfully eliminated.");
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense Split with Id: " + id + " not found"));
    }

    @PatchMapping("/updateExpenseSplit/{expenseSplitId}")
    public ResponseEntity<ExpenseSplit> updateExpenseSplit(
            @PathVariable Long expenseSplitId,
            @RequestBody ExpenseSplitUpdateDTO expenseSplitUpdateDTO) {
        try {
            ExpenseSplit updatedExpenseSplit = expenseSplitService.updateExpenseSplit(expenseSplitId, expenseSplitUpdateDTO);
            return ResponseEntity.ok(updatedExpenseSplit);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
