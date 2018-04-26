import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class MainWindow extends JFrame {
    private static Properties props = new Properties();

    static {
        try {
            props.load(new FileReader("config.properties"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private JButton runBtn = new JButton("RUN SERVER");
    private JTextField dirInput = new JTextField("", 5);
    private JButton chooseBtn = new JButton("...");
    private JLabel status = new JLabel("Server status:");
    private JLabel settings = new JLabel("settingsg");

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
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.8;
        c.ipady = 8;
        c.gridx = 0;
        c.gridy = 1;
        this.add(dirInput, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.2;
        c.gridx = 1;
        c.gridy = 1;
        this.add(chooseBtn, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.ipady = 40;
        c.gridx = 0;
        c.gridy = 2;
        this.add(runBtn, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 50;
        c.gridx = 0;
        c.gridy = 3;
        this.add(status, c);

        chooseBtn.addActionListener(e -> {
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
        });
        runBtn.addActionListener(e -> {
            String message = "Need run the server";
            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
        });

        settings.setVerticalAlignment(SwingConstants.TOP);
        settings.setText(
                "<html>" +
                "<p>Settings:" +
                "<p>IP = " + props.getProperty("ip") + ":" + props.getProperty("port") +
                "</html>"
        );

        dirInput.setText(props.getProperty("init_dir"));

    }

    public static void main(String[] args) throws IOException {
        MainWindow app = new MainWindow();
        app.setVisible(true);
    }

    /**
     * Сохранить свойство в файл
     * @param key   ключ
     * @param value значение
     */
    private static void saveProp(String key, String value) {
        try (OutputStream outputStream = new FileOutputStream(new File("config.properties"))) {
            props.setProperty(key, value);
            props.store(outputStream, "");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
