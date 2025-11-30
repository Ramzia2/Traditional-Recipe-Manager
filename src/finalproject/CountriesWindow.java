/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CountriesWindow extends JFrame {

    private DefaultListModel<String> listModel;
    private JList<String> countryList;
    private JButton viewBtn;

    public CountriesWindow() {
        setTitle("Countries");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        countryList = new JList<>(listModel);
        viewBtn = new JButton("View Recipes");

        loadCountries();
        viewBtn.addActionListener(e -> openRecipes());

        setLayout(new BorderLayout());
        add(new JScrollPane(countryList), BorderLayout.CENTER);
        add(viewBtn, BorderLayout.SOUTH);
    }

    private void loadCountries() {

    try {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT DISTINCT Cuisine FROM recipes";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        listModel.clear();   // 清空 JList

        while (rs.next()) {
            listModel.addElement(rs.getString("Cuisine"));
        }

        rs.close();
        ps.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
            this,
            "Load country failed: " + e.getMessage()
        );
    }
}

    private void openRecipes() {
        String selected = countryList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a country.");
            return;
        }

        new RecipesByCountryPage(selected).setVisible(true);
    }
}
