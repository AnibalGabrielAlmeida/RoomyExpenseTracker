package com.RoomyExpense.tracker.mapper;
import com.RoomyExpense.tracker.DTO.ExpenseSplitCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitDTO;
import com.RoomyExpense.tracker.model.ExpenseSplit;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpenseSplitMapper {
    public ExpenseSplit toEntity(ExpenseSplitCreationDTO expenseSplitCreationDTO) {
        ExpenseSplit expenseSplit = new ExpenseSplit();

        Double calculatedAmount = (expenseSplitCreationDTO.getDivisionPercentage() / 100) * expenseSplitCreationDTO.getTotalAmount();
        expenseSplit.setAmount(calculatedAmount);

        //Relationships managed in service layer.
        return expenseSplit;
    }

    public ExpenseSplitDTO toDTO(ExpenseSplit expenseSplit) {
        return new ExpenseSplitDTO(
                expenseSplit.getId(),
                expenseSplit.getUser().getName(),
                expenseSplit.getExpense().getName(),
                expenseSplit.getAmount()
        );
    }
}
