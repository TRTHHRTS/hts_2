package ui.listeners;

import server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;
import static ui.MainWindow.runBtn;
import static ui.MainWindow.serverThread;
import static ui.MainWindow.status;

public class RunServerBtnListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser j = new JFileChooser();
        Runnable serverRunnable = () -> {
            Server server = new Server(4444);
            try {
                server.start();
            } catch (Exception e1) {
                showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
            }
        };
        serverThread = new Thread(serverRunnable);
        serverThread.start();
        runBtn.setEnabled(false);
        status.setText("Server status: RUNNING");
    }
}
