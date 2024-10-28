package com.planningproject;
import com.google.inject.Provides;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import okhttp3.HttpUrl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;

@Slf4j
public class PlanningTaskListManager {

    private Gson gson;
    private ConfigManager configManager;

    private PlanningProjectPlugin planningProjectPlugin;

    @Getter
    private ArrayList<PlanningTask> loadedPlanningTasks;

    @Getter
    private PlanningTaskListPanel panel;

    @Getter
    private ItemManager itemManager;

    @Getter
    private ClientThread clientThread;

    @Getter
    private ScheduledExecutorService executor;

    public PlanningTaskListManager(PlanningProjectPlugin planningProjectPlugin, Stream<PlanningTask> planningTasks){
        this.planningProjectPlugin = planningProjectPlugin;
        this.loadedPlanningTasks = planningTasks.collect(Collectors.toCollection(ArrayList::new));
        this.gson = planningProjectPlugin.getInjector().getInstance(Gson.class);
        this.configManager = planningProjectPlugin.getInjector().getInstance(ConfigManager.class);
        this.itemManager = planningProjectPlugin.getInjector().getInstance(ItemManager.class);
        this.executor = planningProjectPlugin.getInjector().getInstance(ScheduledExecutorService.class);
        this.clientThread = planningProjectPlugin.getInjector().getInstance(ClientThread.class);

        this.panel = new PlanningTaskListPanel(this);
    }

    public void saveConfig()
    {
        final String json = gson.toJson(loadedPlanningTasks);
        configManager.setConfiguration(planningProjectPlugin.CONFIG_GROUP, planningProjectPlugin.CONFIG_KEY, json);
    }

    public void addNewTask()
    {
        loadedPlanningTasks.add(new PlanningTask("Task Name", "Task Description", false, new ArrayList<Integer>()));
    }

    public void removeTask(PlanningTask toRemove)
    {
        loadedPlanningTasks.remove(toRemove);
        SwingUtilities.invokeLater(panel::buildPanel);
    }

    public void rebuildPanel()
    {
        SwingUtilities.invokeLater(panel::buildPanel);
    }
}
