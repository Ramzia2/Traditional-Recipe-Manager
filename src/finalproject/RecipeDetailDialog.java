/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import javax.swing.*;
import java.awt.*;

public class RecipeDetailDialog extends JDialog {

    public RecipeDetailDialog(String name, String ingredients, String steps) {
        setTitle("Recipe Details - " + name);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea txtIngredients = new JTextArea(ingredients);
        JTextArea txtSteps = new JTextArea(steps);

        txtIngredients.setLineWrap(true);
        txtIngredients.setWrapStyleWord(true);
        txtSteps.setLineWrap(true);
        txtSteps.setWrapStyleWord(true);

        txtIngredients.setEditable(false);
        txtSteps.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(new JScrollPane(txtIngredients));
        panel.add(new JScrollPane(txtSteps));

        add(panel, BorderLayout.CENTER);
    }
}