package com;/*
Created By Eduardo Milan Martinez
Bachelor's project
April 2019
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.Decrypter;

public class MyFrame extends JFrame {
    private JButton startButton;
    private JButton fileButton;
    private JLabel headerLabel;
    private JPanel northPanel;
    private JPanel centerPanel;

    JFileChooser fileChooser;
    JFileChooser fileSaver;

    private JTextField input1;
    private JTextField input2;
    private JTextField input3;
    private GridBagConstraints c;

    public MyFrame(){
        prepareGUI();
    }

    private void prepareGUI(){
        setSize(700,300);//400 width and 500 height
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        headerLabel = new JLabel("",JLabel.CENTER );

        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2,1));

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        setTitle("Offline File Decrypter");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(northPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);
        setVisible(true);

        initHeader();
    }

    private void initHeader(){
        headerLabel.setText("Offline File Decrypter");

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.lightGray);
        controlPanel.setSize(600,600);
        GridLayout layout = new GridLayout(0,4,10,10);

        controlPanel.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        int top = 6;
        int left = 2;
        int bottom = 6;
        int right = 2;
        c.insets = new Insets(top, left, bottom, right);

        initControlPanel(c,controlPanel);

        startButton = new JButton("Decrypt");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        controlPanel.add(startButton,c);

        startButton.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    inputsProcessing();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        northPanel.add(headerLabel);
        northPanel.add(controlPanel);

        setVisible(true);
    }

    private void initControlPanel(GridBagConstraints c,JPanel panel){
        //Good date for testing
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateInString = "13-01-2019 11:20:56"; //"16-11-2018 11:20:56" for word example
        Date testDate = new Date();
        try {
            testDate = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.MONTH, -6);
        Date earliestDate = calendar.getTime();

        fileChooser = new JFileChooser(".");
        fileChooser.setControlButtonsAreShown(false);
        input1 = new JTextField(80);
        input2 =  new JTextField(200);
        input3 =  new JTextField(600);
        fileButton = new JButton("Choose File");
        fileButton.addActionListener(e -> {
            selectFile();
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("File"), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(new JLabel("Encrypted Secret Key"), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        panel.add(new JLabel("Private Key"), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(fileButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(input1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 1;
        panel.add(input2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(input3, c);

        setVisible(true);
    }

    private void inputsProcessing() throws Exception {
        String secretKey= input1.getText();
        String privateKey= input2.getText();
        String filePath = input3.getText();

        fileSaver = new JFileChooser();
        File file = Decrypter.decrypt(filePath,secretKey,privateKey);

        JOptionPane.showMessageDialog(null, "File was decrypted and replaced with the original version");
    }

    public void selectFile() {
        JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            String path = f.getAbsolutePath();
            input3.setText(path);
            System.out.println(path);
        } else {
            // user changed their mind
        }

    }
}
