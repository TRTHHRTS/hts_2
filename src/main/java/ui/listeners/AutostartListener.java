package ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.MainWindow.autostart;
import static ui.MainWindow.saveProp;

public class AutostartListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        saveProp("autostart", String.valueOf(autostart.isSelected()));
    }
}
