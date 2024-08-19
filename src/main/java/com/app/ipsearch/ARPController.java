package com.app.ipsearch;


import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ARPController {
    @FXML
    public Label typeOut;

    @FXML
    public Label ipAddressOut;

    @FXML
    public Label macAddressOut;

    @FXML
    private Button myButton;

    @FXML
    private TextArea arpOutputArea;

    @FXML
    private TextField macTextField;

    @FXML
    public void runArpCommand() {

        // Implementierung des ARP -a-Befehls
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("arp", "-a");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {

                output.append(line).append("\n");
            }

            arpOutputArea.setText(output.toString());
        } catch (Exception e) {
            arpOutputArea.setText("An error occurred: " + e.getMessage());
        }
    }


    @FXML
    public void findMac() {

        // Check that the MAC address is not empty
        String macAddress = macTextField.getText();
        if (macAddress == null || macAddress.isEmpty() || !isValidMacAddress(macAddress)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Invalid MAC-Address");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Mac address entered. Please check and try again.");
            alert.showAndWait();
            return;
        }

        myButton.setVisible(true);
        // Implementierung des ARP -a-Befehls
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("arp", "-a");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                //
                String macTextFieldreplaced = macTextField.getText().replaceAll(":", "-").replaceAll(" ", "");
                if (line.contains(macTextFieldreplaced.toLowerCase())) {
                    output.append(line).append("\n");
                    break;
                }
            }

            String[] parts = output.toString().split(" {3,}");
            // Receiving parts
            ipAddressOut.setText(parts[0]);
            macAddressOut.setText(parts[1]);
            typeOut.setText(parts[2]);


        } catch (Exception e) {
            arpOutputArea.setText("An error occurred: " + e.getMessage());
        }


    }

    // Method for Mac address validation (optional)
    private boolean isValidMacAddress(String mac) {
        String macPattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$|^([0-9A-Fa-f]{12})$";
        return mac.matches(macPattern);
    }

    @FXML
    public void cleanTextarea() {
        macTextField.setText("");
        arpOutputArea.setText("");
        ipAddressOut.setText("");
        macAddressOut.setText("");
        typeOut.setText("");
        myButton.setVisible(false);
    }

}

