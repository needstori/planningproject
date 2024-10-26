package com.planningproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import joptsimple.internal.Strings;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.http.api.item.ItemPrice;
import java.util.List;

@Slf4j
public class PlanningTaskPanel extends JPanel {

    private static final String COMMIT_ACTION = "commit";

    private static final Color INCOMPLETE = Color.RED;
    private static final Color COMPLETE = Color.GREEN;

    @Getter
    private final PlanningTask task;

    private JTextArea nameLabel;
    private JTextArea descriptionLabel;
    private JButton deleteButton;
    private JButton completeButton;
    private JTextField itemSearch;

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
        if( task.isTaskDone() )
            nameLabel.setForeground(COMPLETE);
        else
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

        deleteButton = new JButton();
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                planningProjectManager.removeTask(task);
            }
        });

        add(deleteButton);

        completeButton = new JButton();
        completeButton.setText("Complete");
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                task.setTaskDone(true);
                planningProjectManager.rebuildPanel();
                planningProjectManager.saveConfig();
            }
        });

        add(completeButton);

        itemSearch = new JTextField();
        ItemSearchAutocomplete autocomplete = new ItemSearchAutocomplete(itemSearch, planningProjectManager.getItemManager());
        itemSearch.getDocument().addDocumentListener(autocomplete);
        itemSearch.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        itemSearch.getActionMap().put(COMMIT_ACTION, autocomplete.new CommitAction());
        add(itemSearch);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}

