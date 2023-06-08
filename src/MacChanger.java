import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MacChanger extends JFrame implements ActionListener {
    private JLabel currentMacLabel, newMacLabel;
    private JTextField currentMacField, newMacField;
    private JButton changeButton;

    public MacChanger() {
        setTitle("MAC Changer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        currentMacLabel = new JLabel("Current MAC Address:");
        currentMacField = new JTextField(20);
        currentMacField.setEditable(false);

        newMacLabel = new JLabel("New MAC Address:");
        newMacField = new JTextField(20);

        changeButton = new JButton("Change MAC");
        changeButton.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(currentMacLabel);
        panel.add(currentMacField);
        panel.add(newMacLabel);
        panel.add(newMacField);
        panel.add(changeButton);

        add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeButton) {
            String newMac = newMacField.getText();
            if (changeMacAddress(newMac)) {
                JOptionPane.showMessageDialog(this, "MAC address changed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to change MAC address.");
            }
        }
    }

    private boolean changeMacAddress(String newMac) {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            Process process;
            if (os.contains("win")) {
                String[] command = {"cmd.exe", "/c", "ipconfig /release && ipconfig /renew"};
                process = Runtime.getRuntime().exec(command);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                String[] command = {"/bin/sh", "-c", "ifconfig eth0 down && ifconfig eth0 hw ether " + newMac + " && ifconfig eth0 up"};
                process = Runtime.getRuntime().exec(command);
            } else {
                throw new UnsupportedOperationException("Unsupported operating system.");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return true;
            }
        } catch (IOException | InterruptedException | UnsupportedOperationException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MacChanger macChanger = new MacChanger();
            macChanger.setVisible(true);
        });
    }
}
