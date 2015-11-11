package com.huwcbjones.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.URI;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 09/11/2015
 */
public class GUI extends JFrame {

    private ChatClient client;

    private JPanel window;
    private JPanel pane_input;
    private JTextField text_msg;
    private JButton btn_send;
    private JPanel pane_output;
    private JScrollPane pane_chat;
    private JTextArea text_chat;
    private JScrollPane pane_log;
    private JTextArea text_log;


    public GUI() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    _buildGUI(); // initialize the GUI
                }
            });
        } catch (Exception exc) {
            System.out.println("Can't create because of " + exc);
            return;
        }
        _redirectPrintStreams();
    }

    private void _buildGUI() {

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.window = new JPanel(new BorderLayout());

        // Input Panel
        this.pane_input = new JPanel(new BorderLayout());

        this.text_msg = new JTextField();
        this.text_msg.addActionListener(new text_msg_event());
        this.pane_input.add(this.text_msg, BorderLayout.CENTER);

        this.btn_send = new JButton();
        this.btn_send.setText("Send");
        this.btn_send.addActionListener(new btn_send_Event());
        this.pane_input.add(this.btn_send, BorderLayout.EAST);

        this.window.add(this.pane_input, BorderLayout.PAGE_END);

        // Output Panel
        this.pane_output = new JPanel(new BorderLayout());

        this.text_chat = new JTextArea("MESSAGES\n");
        this.text_chat.setEnabled(false);
        this.pane_chat = new JScrollPane(this.text_chat);
        this.pane_output.add(this.pane_chat, BorderLayout.CENTER);

        this.text_log = new JTextArea("CHAT LOG\n");
        this.text_log.setEnabled(false);
        this.text_log.setRows(5);
        this.pane_log = new JScrollPane(this.text_log);
        this.pane_output.add(this.pane_log, BorderLayout.SOUTH);

        this.window.add(this.pane_output, BorderLayout.CENTER);

        this.setTitle("Chat Client");
        this.add(this.window);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    private void _redirectPrintStreams() {
        PrintStream log = new PrintStream(new JTextAreaOutputStream(this.text_log));
        System.setErr(log);
        PrintStream out = new PrintStream(new JTextAreaOutputStream(this.text_chat));
        System.setOut(out);
    }

    public void run(int port, URI server) {
        this.client = new ChatClient(port, server);
        this.getClientDetails();
        try {
            if (!this.client.runGUI()) {

            }
            this.setTitle("Chat Client: " + this.client.getName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Exception occurred whilst connecting to server.\n" + ex.toString(),
                    "Exception on Connection", JOptionPane.OK_OPTION);
        }
    }

    public void getClientDetails() {
        String username = null;
        while (username == null) {
            String inputUsername = (String) JOptionPane.showInputDialog(this, "Please enter a username:", "Username", JOptionPane.OK_OPTION);
            if ((inputUsername != null) && (inputUsername.length() > 0 && (inputUsername.matches("^[a-zA-Z_]*$")))) {
                username = inputUsername;
                this.client.setUsername(username);
            }
        }

        String name = null;
        while (name == null) {
            String inputName = (String) JOptionPane.showInputDialog(this, "Please enter your name:", "Name", JOptionPane.OK_OPTION);
            if ((inputName != null) && (inputName.length() > 0 && (inputName.matches("^[a-zA-Z_]*$")))) {
                name = inputName;
                this.client.setName(name);
            }
        }
    }

    private class btn_send_Event implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (text_msg.getText().length() > 0) {
                client.message(text_msg.getText());
                text_msg.setText("");
            }
        }
    }

    private class text_msg_event implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            btn_send.doClick();
        }
    }

}
