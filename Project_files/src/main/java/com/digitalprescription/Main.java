package com.digitalprescription;
import javax.swing.SwingUtilities;

import com.digitalprescription.presentation.LoginFrame;
public class Main{
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            new LoginFrame().setVisible(true);
        });
    }
}
