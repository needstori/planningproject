package com.planningproject;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.inventorygrid.InventoryGridConfig;
import net.runelite.http.api.item.ItemPrice;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ItemSearchAutocomplete implements DocumentListener {

    private static enum Mode {
        INSERT,
        COMPLETION
    };

    private JTextField textField;
    private PlanningTaskListManager planningTaskListManager;
    private PlanningTask task;
    private ItemPrice currentCompletion;
    private int currentCompletionId;
    private Mode mode = Mode.INSERT;

    public ItemSearchAutocomplete(JTextField textField, PlanningTaskListManager planningTaskListManager, PlanningTask task)
    {
        this.textField = textField;
        this.planningTaskListManager = planningTaskListManager;
        this.task = task;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        int pos = e.getOffset();
        String content = null;
        try {
            content = textField.getText(0, pos + 1);
        } catch (BadLocationException exception) {
            exception.printStackTrace();
        }

        if (content.length() < 2)
            return;

        List<ItemPrice> results = planningTaskListManager.getItemManager().search(content);
        Stream<ItemPrice> resultsStream = results.stream();
        String finalContent = content;
        List<ItemPrice> filteredResults =  resultsStream.filter(r -> r.getName().startsWith(finalContent)).collect(Collectors.toList());
        if ( filteredResults.isEmpty() )
            return;

        currentCompletion = filteredResults.get(0);
        String result = currentCompletion.getName();
        currentCompletionId = currentCompletion.getId();
        String completion = result.substring(pos + 1);
        SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));


    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    public class CommitAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if ( mode == Mode.COMPLETION ){
                int pos = textField.getSelectionEnd();
                StringBuffer sb = new StringBuffer(textField.getText());
                textField.setText(sb.toString());
                textField.setCaretPosition(pos);
                ArrayList<Integer> newReqs = task.getRequiredItems();
                if ( newReqs == null )
                    newReqs = new ArrayList<Integer>();
                newReqs.add(currentCompletionId);
                task.setRequiredItems(newReqs);
                mode = Mode.INSERT;
                textField.setFocusTraversalKeysEnabled(true);
                planningTaskListManager.saveConfig();
                planningTaskListManager.rebuildPanel();
            } else {
                textField.replaceSelection("\t");
            }
        }
    }

    private class CompletionTask implements Runnable {
        private String completion;
        private int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            StringBuffer sb = new StringBuffer(textField.getText());
            sb.insert(position, completion);
            textField.setText(sb.toString());
            textField.setCaretPosition(position + completion.length());
            textField.moveCaretPosition(position);
            mode = Mode.COMPLETION;
            textField.setFocusTraversalKeysEnabled(false);
        }
    }
}
