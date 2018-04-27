package ui;

import server.Server;
import ui.listeners.AutostartListener;
import ui.listeners.ChooseDirListener;
import ui.listeners.RunServerBtnListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

public class MainWindow extends JFrame {
    public static Properties PROPS = new Properties();

    static {
        try {
            PROPS.load(new FileReader("config.properties"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public static JButton runBtn = new JButton("RUN SERVER");
    public static JTextField dirInput = new JTextField("", 5);
    public static JButton chooseBtn = new JButton("...");
    public static JLabel status = new JLabel("Server status: NOT running");
    public static JLabel settings = new JLabel("Settings..");
    public static JCheckBox autostart = new JCheckBox("Запускать сервер при запуске приложения");

    public static Thread serverThread = null;

    private MainWindow() {
        super("HOME THEATER SERVER 2.0");
        this.setBounds(100,100,450,300);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipady = 50;
        c.gridx = 0;
        c.gridy = 0;
        this.add(settings, c);
        settings.setVerticalAlignment(SwingConstants.TOP);
        settings.setText(
                "<html>" +
                        "<p>Settings:" +
                        "<p>IP = " + PROPS.getProperty("ip") + ":" + PROPS.getProperty("port") +
                        "</html>"
        );

        c.weightx = 0.8;
        c.ipady = 8;
        c.gridx = 0;
        c.gridy = 1;
        this.add(dirInput, c);
        dirInput.setText(PROPS.getProperty("init_dir"));

        c.weightx = 0.2;
        c.gridx = 1;
        c.gridy = 1;
        this.add(chooseBtn, c);
        chooseBtn.addActionListener(new ChooseDirListener());

        c.weightx = 1;
        c.gridwidth = 2;
        c.ipady = 40;
        c.gridx = 0;
        c.gridy = 2;
        this.add(runBtn, c);
        runBtn.addActionListener(new RunServerBtnListener());

        c.ipady = 50;
        c.gridy = 3;
        this.add(autostart, c);
        autostart.setSelected(Boolean.valueOf(PROPS.getProperty("autostart")));
        autostart.addActionListener(new AutostartListener());

        c.ipady = 60;
        c.gridx = 0;
        c.gridy = 4;
        this.add(status, c);

        this.addWindowStateListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (serverThread != null) {
                    serverThread.interrupt();
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        MainWindow app = new MainWindow();
        app.setVisible(true);
        if (autostart.isSelected()) {
            runBtn.doClick();
        }
    }

    /**
     * Сохранить свойство в файл
     * @param key   ключ
     * @param value значение
     */
    public static void saveProp(String key, String value) {
        try (OutputStream outputStream = new FileOutputStream(new File("config.properties"))) {
            PROPS.setProperty(key, value);
            PROPS.store(outputStream, "");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
