package ui.listeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static ui.MainWindow.mpcInput;
import static ui.MainWindow.saveProp;

public class ChooseMpcPathListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser j = new JFileChooser();
        j.setFileSelectionMode(JFileChooser.FILES_ONLY);
        j.setCurrentDirectory(new File(mpcInput.getText()));
        Integer returnVal = j.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File choosedDir = j.getSelectedFile();
            mpcInput.setText(choosedDir.getAbsolutePath());
            saveProp("mpc_path", choosedDir.getAbsolutePath());
        }
    }
}
