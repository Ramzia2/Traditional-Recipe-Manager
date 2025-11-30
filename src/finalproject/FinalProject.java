/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */package finalproject;

import jiconfont.swing.IconFontSwing;  
import icon.FontAwesome;
import icon.GoogleMaterialDesignIcons;

public class FinalProject {

    public static void main(String[] args) {
        
        IconFontSwing.register(FontAwesome.getIconFont());
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());

      
        java.awt.EventQueue.invokeLater(() -> {
            DBConnection.getConnection();
            new Menu().setVisible(true); 
        });
    }
}