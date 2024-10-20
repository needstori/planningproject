package com.planningproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;

@Slf4j
public class PlanningTaskPanel extends JPanel {

    private static final Color INCOMPLETE = Color.RED;
    private static final Color COMPLETE = Color.GREEN;

    @Getter
    private final PlanningTask task;

    private JTextArea nameLabel;
    private JTextArea descriptionLabel;

    private PlanningTaskListManager planningProjectManager;

    PlanningTaskPanel(@NonNull PlanningTask task, PlanningTaskListManager planningProjectManager)
    {
        this.task = task;
        this.planningProjectManager = planningProjectManager;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        nameLabel = new JTextArea();
        nameLabel.setWrapStyleWord(true);
        nameLabel.setLineWrap(true);
        nameLabel.setEditable(true);
        nameLabel.setOpaque(false);
        nameLabel.setFocusable(true);
        nameLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                task.setTaskName(nameLabel.getText());
                planningProjectManager.saveConfig();
            }
        });

        nameLabel.setText(task.getTaskName());
        nameLabel.setForeground(INCOMPLETE);
        add(nameLabel);

        descriptionLabel = new JTextArea();
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setEditable(true);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setFocusable(true);
        descriptionLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                task.setTaskDescription(descriptionLabel.getText());
                planningProjectManager.saveConfig();
            }
        });

        descriptionLabel.setText(task.getTaskDescription());
        add(descriptionLabel);

    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}
