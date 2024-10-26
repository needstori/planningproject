package com.planningproject;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.border.EmptyBorder;

public class PlanningTaskListPanel extends PluginPanel {

    PlanningTaskListManager planningProjectManager;

    public PlanningTaskListPanel(PlanningTaskListManager planningProjectPlugin)
    {
        super(false);

        this.planningProjectManager = planningProjectPlugin;

        buildPanel();
    }

    void buildPanel()
    {
        removeAll();

        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new BorderLayout());

        // Create a container for an add task button
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BorderLayout());

        JButton newTaskButton = new JButton("Add Task");
        newTaskButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        newTaskButton.setLayout(new BorderLayout());
        newTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                planningProjectManager.addNewTask();
                SwingUtilities.invokeLater(PlanningTaskListPanel.this::buildPanel);
            }
        });

        headerContainer.add(newTaskButton, BorderLayout.SOUTH);

        add(headerContainer, BorderLayout.NORTH);

        // Create a scrolling panel to display tasks
        JPanel planningListPanel = new JPanel();
        planningListPanel.setLayout(new DynamicGridLayout(0, 1, 0, 2));

        for ( PlanningTask planningTask : planningProjectManager.getLoadedPlanningTasks())
        {
            PlanningTaskPanel newTaskPanel = new PlanningTaskPanel(planningTask, planningProjectManager);
            planningListPanel.add(newTaskPanel);
        }

        JScrollPane scrollPane = new JScrollPane(planningListPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(2, 2, 2, 2));
        scrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
    }
}
