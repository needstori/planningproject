package com.planningproject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanningTask {
    private String taskName;
    private String taskDescription;
    private boolean taskDone;
    private ArrayList<Integer> requiredItems;
}
