package com.huwcbjones.CircleDetector;

import javax.swing.*;
import javax.swing.tree.ExpandVetoException;

/**
 * Circle Detector GUI
 *
 * @author Huw Jones
 * @since 20/11/2015
 */
public class CircleDetector extends JFrame {



    public CircleDetector() {
        try {
             SwingUtilities.invokeAndWait(() -> {
                 _buildGUI();
             });
        } catch (Exception ex){
            System.err.println("Failed to start program. " + ex.getMessage());
        }
    }

    private void _buildGUI(){

    }
}
