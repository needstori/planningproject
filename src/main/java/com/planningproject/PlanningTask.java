package com.planningproject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanningTask {
    private String taskName;
    private String taskDescription;
    private boolean taskDone;
}
