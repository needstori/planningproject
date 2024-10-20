package com.planningproject;
import com.google.inject.Provides;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;

import javax.inject.Inject;
@Slf4j
public class PlanningTaskListManager {

    private Gson gson;
    private ConfigManager configManager;

    private PlanningProjectPlugin planningProjectPlugin;

    @Getter
    private ArrayList<PlanningTask> loadedPlanningTasks;

    public PlanningTaskListManager(PlanningProjectPlugin planningProjectPlugin, Stream<PlanningTask> planningTasks){
        this.planningProjectPlugin = planningProjectPlugin;
        this.loadedPlanningTasks = planningTasks.collect(Collectors.toCollection(ArrayList::new));
        this.gson = planningProjectPlugin.getInjector().getInstance(Gson.class);
        this.configManager = planningProjectPlugin.getInjector().getInstance(ConfigManager.class);
    }

    public void saveConfig()
    {
        final String json = gson.toJson(loadedPlanningTasks);
        configManager.setConfiguration(planningProjectPlugin.CONFIG_GROUP, planningProjectPlugin.CONFIG_KEY, json);
    }

    public void addNewTask()
    {
        loadedPlanningTasks.add(new PlanningTask("Task Name", "Task Description", false));
    }
}
