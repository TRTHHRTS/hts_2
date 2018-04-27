package ui.listeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static ui.MainWindow.dirInput;
import static ui.MainWindow.saveProp;

public class ChooseDirListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser j = new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        j.setCurrentDirectory(new File(dirInput.getText()));
        j.setMultiSelectionEnabled(false);
        Integer returnVal = j.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File choosedDir = j.getSelectedFile();
            dirInput.setText(choosedDir.getAbsolutePath());
            saveProp("init_dir", choosedDir.getAbsolutePath());
        }
    }
}
