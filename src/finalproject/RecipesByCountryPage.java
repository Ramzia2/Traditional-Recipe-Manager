/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RecipesByCountryPage extends JFrame {

    private String country;
    private DefaultListModel<String> recipeModel;
    private JList<String> recipeList;

    public RecipesByCountryPage(String country) {
        this.country = country;

        setTitle(country + " Recipes");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        recipeModel = new DefaultListModel<>();
        recipeList = new JList<>(recipeModel);
        recipeList.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {

        if (evt.getClickCount() == 2) {

            String selectedName = recipeList.getSelectedValue();
            if (selectedName == null) return;

            try {
                Connection conn = DBConnection.getConnection();

                String sql = "SELECT ingredients, steps FROM recipes WHERE name = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, selectedName);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String ingredients = rs.getString("ingredients");
                    String steps = rs.getString("steps");

               
                    new RecipeDetailDialog(selectedName, ingredients, steps).setVisible(true);
                }

                rs.close();
                pst.close();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to load recipe details.");
            }
        }
    }
});


        loadRecipes();

        setLayout(new BorderLayout());
        add(new JScrollPane(recipeList), BorderLayout.CENTER);
    }

   private void loadRecipes() {
    System.out.println("Country received in RecipesByCountryPage: [" + country + "]");

    try {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT name FROM recipes WHERE cuisine LIKE ?";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, "%" + country + "%"); 

        ResultSet rs = pst.executeQuery();

        recipeModel.clear();
        boolean found = false;

        while (rs.next()) {
            recipeModel.addElement(rs.getString("name"));
            found = true;
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "No recipes found for: " + country);
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading recipes");
    }
}}