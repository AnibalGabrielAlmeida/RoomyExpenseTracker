package com.RoomyExpense.tracker.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseSplitDTO {
    private Long id;
    private String userName;
    private String expenseName;
    private Double sharedAmount;
}

