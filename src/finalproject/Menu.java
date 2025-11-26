/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package finalproject;
import icon.FontAwesome;
import icon.GoogleMaterialDesignIcons;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jiconfont.swing.IconFontSwing;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import javax.swing.JTable;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.List;
        
/**
 *
 * @author thoan
 */
public class Menu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Menu.class.getName());
    /**
     * Creates new form Menu
     */
    

 // === PURE JAVA DATA STORAGE ===
    private List<User> users = new ArrayList<>();
    private List<SimpleRecipe> recipes = new ArrayList<>();
    private List<String> favorites = new ArrayList<>(); // Store as "userId_recipeId"
    private List<String> reviews = new ArrayList<>(); // Store as "userId_recipeId_rating_comment"
    
    private User currentUser;
    private int nextUserId = 1;
    private int nextRecipeId = 1;
    
    int x = 210;
    
    // Simple User class
    class User {
        private int userId;
        private String username;
        private String email;
        private String password;
        
        public User(int userId, String username, String email, String password) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.password = password;
        }
        
        // Getters
        public int getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }
    
    // Simple Recipe inner class
    class SimpleRecipe {
        private int recipeId;
        private int userId;
        private String title;
        private String cuisine;
        private String cookingTime;
        private int servingSize;
        private String ingredients;
        private String steps;
        private String category;
        
        public SimpleRecipe(int recipeId, int userId, String title, String cuisine, 
                           String cookingTime, int servingSize, String ingredients, 
                           String steps, String category) {
            this.recipeId = recipeId;
            this.userId = userId;
            this.title = title;
            this.cuisine = cuisine;
            this.cookingTime = cookingTime;
            this.servingSize = servingSize;
            this.ingredients = ingredients;
            this.steps = steps;
            this.category = category;
        }
        
        // Getters
        public int getRecipeId() { return recipeId; }
        public int getUserId() { return userId; }
        public String getTitle() { return title; }
        public String getCuisine() { return cuisine; }
        public String getCookingTime() { return cookingTime; }
        public int getServingSize() { return servingSize; }
        public String getIngredients() { return ingredients; }
        public String getSteps() { return steps; }
        public String getCategory() { return category; }
    }

    Connection sqlConn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int q, i, id, deleteItem;
    
     public Menu() {
        initComponents();
        addUserFeatures();
        
        // Add some sample data
        addSampleData();
        
//        addWindowListener(new java.awt.event.WindowAdapter() {
//        @Override
//        public void windowOpened(java.awt.event.WindowEvent e) {
//            setScaledImage("C:\\Users\\thoan\\OneDrive\\Documents\\NetBeansProjects\\FinalProject\\src\\Images\\bars-1.png", BarLabel);
//        }
//    });

//    BarLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
//        @Override
//        public void componentResized(java.awt.event.ComponentEvent e) {
//            setScaledImage("C:\\Users\\thoan\\OneDrive\\Documents\\NetBeansProjects\\FinalProject\\src\\Images\\bars-1.png", BarLabel);
//        }
//    });
          jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Cuisine", "Time", "Ingredients", "Steps"
            }
        ));
        upDateTable();
    }
    
 // === ADD SAMPLE DATA ===
    private void addSampleData() {
        // Add a sample user
        users.add(new User(1, "admin", "admin@example.com", "password"));
        
        // Add sample recipes
        recipes.add(new SimpleRecipe(1, 1, "Spaghetti Carbonara", "Italian", 
            "30 mins", 4, "Spaghetti, Eggs, Bacon, Cheese", 
            "1. Cook pasta 2. Mix eggs and cheese 3. Combine with bacon", "Main Courses"));
        
        recipes.add(new SimpleRecipe(2, 1, "Chocolate Cake", "American", 
            "60 mins", 8, "Flour, Sugar, Cocoa, Eggs, Milk", 
            "1. Mix dry ingredients 2. Add wet ingredients 3. Bake at 350°F", "Desserts"));
        
        nextUserId = 2;
        nextRecipeId = 3;
    }

 // === PURE JAVA USER MANAGEMENT ===
    
    private void addUserFeatures() {
    JPanel userPanel = new JPanel(new GridLayout(6, 1, 5, 5));
    
    JButton btnLogin = new JButton("Login");
    btnLogin.addActionListener(e -> {
        if (currentUser == null) {
            showLoginDialog();
        } else {
            JOptionPane.showMessageDialog(this, "Already logged in as: " + currentUser.getUsername());
        }
    });
    userPanel.add(btnLogin);
    
    JButton btnRegister = new JButton("Register");
    btnRegister.addActionListener(e -> showRegisterDialog());
    userPanel.add(btnRegister);
    
    JButton btnEnhancedAdd = new JButton("Add Enhanced Recipe");
    btnEnhancedAdd.addActionListener(e -> showEnhancedRecipeDialog());
    userPanel.add(btnEnhancedAdd);
    
    JButton btnAddFavorite = new JButton("Add to Favorites");
    btnAddFavorite.addActionListener(e -> addToFavorites());
    userPanel.add(btnAddFavorite);
    
    JButton btnFavorites = new JButton("My Favorites");
    btnFavorites.addActionListener(e -> showFavorites());
    userPanel.add(btnFavorites);
    
    JButton btnAddReview = new JButton("Add Review");
    btnAddReview.addActionListener(e -> addReview());
    userPanel.add(btnAddReview);
    
    // FIX: Add with proper constraints
    jPanel2.add(userPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 190, 200));
    jPanel2.revalidate();
    jPanel2.repaint();
}
    
     private boolean showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        
        panel.add(new JLabel("Username:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password:"));
        panel.add(txtPassword);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Login", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            
            // Simple authentication - check users list
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = user;
                    setTitle("Recipe Manager - Welcome " + user.getUsername());
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    return true;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
        return false;
    }
     
     
      private void showRegisterDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        JTextField txtUsername = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();
        
        panel.add(new JLabel("Username:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Password:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(txtConfirmPassword);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Register", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }
            
            // Check if username already exists
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                    return;
                }
            }
            
            // Create new user
            User newUser = new User(nextUserId++, username, email, password);
            users.add(newUser);
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
        }
    }
      
      
      // === PURE JAVA RECIPE MANAGEMENT ===
    
    private void showEnhancedRecipeDialog() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login first.");
            return;
        }
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JTextField txtTitle = new JTextField();
        JTextField txtCuisine = new JTextField();
        JTextField txtTime = new JTextField();
        JTextField txtServing = new JTextField();
        JTextArea txtIngredients = new JTextArea(3, 20);
        JTextArea txtSteps = new JTextArea(3, 20);
        
        JComboBox<String> cmbCategory = new JComboBox<>(new String[]{
            "Select Category", "Appetizers", "Main Courses", "Desserts", "Salads", 
            "Soups", "Side Dishes", "Breakfast", "Beverages"
        });
        
        panel.add(new JLabel("Title*:"));
        panel.add(txtTitle);
        panel.add(new JLabel("Cuisine:"));
        panel.add(txtCuisine);
        panel.add(new JLabel("Cooking Time:"));
        panel.add(txtTime);
        panel.add(new JLabel("Serving Size:"));
        panel.add(txtServing);
        panel.add(new JLabel("Category*:"));
        panel.add(cmbCategory);
        panel.add(new JLabel("Ingredients:"));
        panel.add(new JScrollPane(txtIngredients));
        panel.add(new JLabel("Steps:"));
        panel.add(new JScrollPane(txtSteps));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Enhanced Recipe", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            if (txtTitle.getText().trim().isEmpty() || cmbCategory.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Title and Category are required.");
                return;
            }
            
            // Create new recipe
            SimpleRecipe newRecipe = new SimpleRecipe(
                nextRecipeId++,
                currentUser.getUserId(),
                txtTitle.getText().trim(),
                txtCuisine.getText().trim(),
                txtTime.getText().trim(),
                txtServing.getText().isEmpty() ? 0 : Integer.parseInt(txtServing.getText().trim()),
                txtIngredients.getText().trim(),
                txtSteps.getText().trim(),
                cmbCategory.getSelectedItem().toString()
            );
            
            recipes.add(newRecipe);
            JOptionPane.showMessageDialog(this, "Recipe saved successfully!");
            upDateTable();
        }
    }
    
    
    private void addToFavorites() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login first.");
            return;
        }
        
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a recipe first.");
            return;
        }
        
        try {
            int recipeId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
            String favoriteKey = currentUser.getUserId() + "_" + recipeId;
            
            if (!favorites.contains(favoriteKey)) {
                favorites.add(favoriteKey);
                JOptionPane.showMessageDialog(this, "Added to favorites!");
            } else {
                JOptionPane.showMessageDialog(this, "Already in favorites!");
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding to favorites: " + ex.getMessage());
        }
    }
    
    private void showFavorites() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login first.");
            return;
        }
        
        StringBuilder favoritesList = new StringBuilder();
        for (String favoriteKey : favorites) {
            if (favoriteKey.startsWith(currentUser.getUserId() + "_")) {
                int recipeId = Integer.parseInt(favoriteKey.split("_")[1]);
                for (SimpleRecipe recipe : recipes) {
                    if (recipe.getRecipeId() == recipeId) {
                        favoritesList.append("• ").append(recipe.getTitle())
                                .append(" (").append(recipe.getCuisine()).append(")\n");
                        break;
                    }
                }
            }
        }
        
        if (favoritesList.length() == 0) {
            JOptionPane.showMessageDialog(this, "You have no favorite recipes yet.");
        } else {
            JOptionPane.showMessageDialog(this, "Your Favorite Recipes:\n\n" + favoritesList.toString());
        }
    }
    
    private void addReview() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login first.");
            return;
        }
        
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a recipe first.");
            return;
        }
        
        try {
            int recipeId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
            String recipeName = jTable1.getValueAt(selectedRow, 1).toString();
            
            JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
            
            JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            JTextArea commentArea = new JTextArea(3, 30);
            
            panel.add(new JLabel("Rating for '" + recipeName + "':"));
            panel.add(ratingCombo);
            panel.add(new JLabel("Comment:"));
            panel.add(new JScrollPane(commentArea));
            
            int result = JOptionPane.showConfirmDialog(this, panel, "Add Review", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String reviewKey = currentUser.getUserId() + "_" + recipeId + "_" + 
                                 ratingCombo.getSelectedItem() + "_" + commentArea.getText().trim();
                reviews.add(reviewKey);
                JOptionPane.showMessageDialog(this, "Review added successfully!");
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding review: " + ex.getMessage());
        }
    }
    
    
     // === UPDATE TABLE METHOD ===
    public void upDateTable(){
        DefaultTableModel RecordTable = (DefaultTableModel) jTable1.getModel();
        RecordTable.setRowCount(0);

        for (SimpleRecipe recipe : recipes) {
            Vector<Object> columnData = new Vector<>();
            columnData.add(recipe.getRecipeId());
            columnData.add(recipe.getTitle());
            columnData.add(recipe.getCuisine());
            columnData.add(recipe.getCookingTime());
            columnData.add(recipe.getIngredients());
            columnData.add(recipe.getSteps());
            RecordTable.addRow(columnData);
        }
    }
    
    
    //====func===//
    public void upDateDB(){
        try {
        // 1️⃣ Load MySQL driver and connect
        sqlConn = DBConnection.getConnection();

        // 2️⃣ Query data
        pst = sqlConn.prepareStatement("SELECT * FROM connector");
        ResultSet rs = pst.executeQuery();
        ResultSetMetaData stData = rs.getMetaData();
        int q = stData.getColumnCount();

        // 3️⃣ Clear current JTable rows
        DefaultTableModel RecordTable = (DefaultTableModel) jTable1.getModel();
        RecordTable.setRowCount(0);

        // 4️⃣ Add database rows to JTable
        while (rs.next()) {
            Vector<Object> columnData = new Vector<>();

            // Loop through all columns in the result
            for (int i = 1; i <= q; i++) {
                columnData.add(rs.getString(i));
            }

            // Add this row to the JTable
            RecordTable.addRow(columnData);
        }

        // 5️⃣ Close resources
        rs.close();
        pst.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            ex.printStackTrace();
        }
    }
    
    //====end func===//
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        BarLabel = new javax.swing.JLabel();
        labelGoogleIcon2 = new icon.LabelGoogleIcon();
        labelGoogleIcon1 = new icon.LabelGoogleIcon();
        labelGoogleIcon4 = new icon.LabelGoogleIcon();
        labelGoogleIcon5 = new icon.LabelGoogleIcon();
        labelGoogleIcon6 = new icon.LabelGoogleIcon();
        labelGoogleIcon3 = new icon.LabelGoogleIcon();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        cuisineTF = new javax.swing.JTextField();
        ingredientsTF = new javax.swing.JTextField();
        timeTF = new javax.swing.JTextField();
        nameTF = new javax.swing.JTextField();
        stepsLabel = new icon.LabelGoogleIcon();
        cuisineLabel = new icon.LabelGoogleIcon();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        stepsTF = new javax.swing.JTextField();
        ingredientsLabel = new icon.LabelGoogleIcon();
        timeLabel = new icon.LabelGoogleIcon();
        nameLabel = new icon.LabelGoogleIcon();
        jPanel5 = new javax.swing.JPanel();
        jbtnAdd = new javax.swing.JButton();
        jbnPrint = new javax.swing.JButton();
        jbnReset = new javax.swing.JButton();
        jbntDelete = new javax.swing.JButton();
        jbtnExit = new javax.swing.JButton();
        jbnUpdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(54, 70, 78));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BarLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BarLabelMouseClicked(evt);
            }
        });
        jPanel2.add(BarLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 40, 30));

        labelGoogleIcon2.setGoogleIcon(icon.GoogleMaterialDesignIcons.MENU);
        labelGoogleIcon2.setIconColor(new java.awt.Color(153, 204, 255));
        labelGoogleIcon2.setIconSize(50.0F);
        labelGoogleIcon2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelGoogleIcon2MouseClicked(evt);
            }
        });
        jPanel2.add(labelGoogleIcon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        labelGoogleIcon1.setForeground(new java.awt.Color(153, 204, 255));
        labelGoogleIcon1.setText("Chat");
        labelGoogleIcon1.setToolTipText("");
        labelGoogleIcon1.setFocusable(false);
        labelGoogleIcon1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelGoogleIcon1.setGoogleIcon(icon.GoogleMaterialDesignIcons.CHAT);
        labelGoogleIcon1.setIconColor(new java.awt.Color(153, 204, 255));
        jPanel2.add(labelGoogleIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 40));

        labelGoogleIcon4.setForeground(new java.awt.Color(153, 204, 255));
        labelGoogleIcon4.setText("Countries");
        labelGoogleIcon4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelGoogleIcon4.setGoogleIcon(icon.GoogleMaterialDesignIcons.LANGUAGE);
        labelGoogleIcon4.setIconColor(new java.awt.Color(153, 204, 255));
        jPanel2.add(labelGoogleIcon4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 40));

        labelGoogleIcon5.setForeground(new java.awt.Color(153, 204, 255));
        labelGoogleIcon5.setText("Search");
        labelGoogleIcon5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelGoogleIcon5.setGoogleIcon(icon.GoogleMaterialDesignIcons.SEARCH);
        labelGoogleIcon5.setIconColor(new java.awt.Color(153, 204, 255));
        labelGoogleIcon5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelGoogleIcon5MouseClicked(evt);
            }
        });
        jPanel2.add(labelGoogleIcon5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 40));

        labelGoogleIcon6.setForeground(new java.awt.Color(153, 204, 255));
        labelGoogleIcon6.setText("Add Recipe");
        labelGoogleIcon6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelGoogleIcon6.setGoogleIcon(icon.GoogleMaterialDesignIcons.ADD);
        labelGoogleIcon6.setIconColor(new java.awt.Color(153, 204, 255));
        jPanel2.add(labelGoogleIcon6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 210, 522));

        labelGoogleIcon3.setGoogleIcon(icon.GoogleMaterialDesignIcons.MENU);
        labelGoogleIcon3.setIconColor(new java.awt.Color(102, 102, 102));
        labelGoogleIcon3.setIconSize(50.0F);
        labelGoogleIcon3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelGoogleIcon3MouseClicked(evt);
            }
        });
        jPanel1.add(labelGoogleIcon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cuisineTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuisineTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuisineTFActionPerformed(evt);
            }
        });
        jPanel4.add(cuisineTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 280, 50));

        ingredientsTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ingredientsTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingredientsTFActionPerformed(evt);
            }
        });
        jPanel4.add(ingredientsTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 190, 280, 50));

        timeTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        timeTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeTFActionPerformed(evt);
            }
        });
        jPanel4.add(timeTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 280, 50));

        nameTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        nameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTFActionPerformed(evt);
            }
        });
        jPanel4.add(nameTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 280, 50));

        stepsLabel.setText("Steps");
        stepsLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel4.add(stepsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 120, 50));

        cuisineLabel.setText("Cuisine");
        cuisineLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel4.add(cuisineLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 120, 50));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Cuisine", "Time", "Ingredients", "Steps"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 500, 140));

        stepsTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        stepsTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepsTFActionPerformed(evt);
            }
        });
        jPanel4.add(stepsTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, 280, 120));

        ingredientsLabel.setText("Ingredients");
        ingredientsLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel4.add(ingredientsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 120, 50));

        timeLabel.setText("Time");
        timeLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel4.add(timeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 120, 50));

        nameLabel.setText("Name");
        nameLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel4.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 120, 50));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, 520));

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jbtnAdd.setText("Add New");
        jbtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddActionPerformed(evt);
            }
        });

        jbnPrint.setText("Print");
        jbnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnPrintActionPerformed(evt);
            }
        });

        jbnReset.setText("Reset");
        jbnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnResetActionPerformed(evt);
            }
        });

        jbntDelete.setText("Delete");
        jbntDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbntDeleteActionPerformed(evt);
            }
        });

        jbtnExit.setText("Exit");
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExitActionPerformed(evt);
            }
        });

        jbnUpdate.setText("Update");
        jbnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbntDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jbtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbntDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbtnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 230, 520));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 790, 520));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
  
 
  
    private void BarLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BarLabelMouseClicked
    // TODO add your handling code here:
        if(x == 210){
            jPanel2.setSize(210, 522);
            Thread th = new Thread(){
                @Override
                public void run(){
                    try{
                    for(int i = 210; i >= 0; i--){
                        Thread.sleep(1);
                        jPanel2.setSize(i, 522);
                    }
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            };th.start();
        x=0;
        }
    }//GEN-LAST:event_BarLabelMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
//        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
//        IconFontSwing.register(FontAwesome.getIconFont());
//        BarLabel.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.MENU, BarLabel.getWidth()-4, Color.GRAY));
//        label1.setIcon(IconFontSwing.buildIcon(FontAwesome.GLOBE, 100, Color.GRAY));
    }//GEN-LAST:event_formWindowOpened

    private void labelGoogleIcon2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelGoogleIcon2MouseClicked
        if(x == 210){
            jPanel2.setSize(210, 522);
            Thread th = new Thread(){
                @Override
                public void run(){
                    try{
                    for(int i = 210; i >= 0; i--){
                        Thread.sleep(1);
                        jPanel2.setSize(i, 522);
                    }
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            };th.start();
        x=0;
        }
    }//GEN-LAST:event_labelGoogleIcon2MouseClicked
    private JFrame frame;
    private void stepsTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepsTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stepsTFActionPerformed

    private void nameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTFActionPerformed

    private void cuisineTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuisineTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuisineTFActionPerformed

    private void timeTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeTFActionPerformed

    private void ingredientsTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingredientsTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ingredientsTFActionPerformed

    private void jbtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExitActionPerformed
        // TODO add your handling code here:
        frame = new JFrame("Exit");
        if(JOptionPane.showConfirmDialog(frame, "Confirm if you want to exit", "MySQL Connector ",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }//GEN-LAST:event_jbtnExitActionPerformed

    private void jbnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnResetActionPerformed
        // TODO add your handling code here:
        nameTF.setText("");
        cuisineTF.setText("");
        timeTF.setText("");
        ingredientsTF.setText("");
        stepsTF.setText("");
        
    }//GEN-LAST:event_jbnResetActionPerformed

    private void jbtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddActionPerformed
        // TODO add your handling code here:
        try{
            sqlConn = DBConnection.getConnection();
            
            pst = sqlConn.prepareStatement("insert into connector(Name, Cuisine, Time, Ingredients, Steps)value(?, ?, ?, ?, ?)");
        
            pst.setString(1, nameTF.getText());
            pst.setString(2, cuisineTF.getText());
            pst.setString(3, timeTF.getText());
            pst.setString(4, ingredientsTF.getText());
            pst.setString(5, stepsTF.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record Added");
            upDateDB();
        }
        catch(SQLException ex){
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jbtnAddActionPerformed

    private void labelGoogleIcon3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelGoogleIcon3MouseClicked
        // TODO add your handling code here:
        if(x==0){
            jPanel2.show();
            jPanel2.setSize(x, 522);
            Thread th = new Thread(){
                @Override
                public void run(){
                    try{
                        for(int i = 0; i <= x; i++){
                        Thread.sleep(1);
                        jPanel2.setSize(i, 522);
                        }
                    } catch(Exception e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            };th.start();
            x=210;
        }
    }//GEN-LAST:event_labelGoogleIcon3MouseClicked

    private void jbnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnUpdateActionPerformed
        // TODO add your handling code here:
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConn = DBConnection.getConnection();
            
            pst = sqlConn.prepareStatement("update connector set Name = ?, Cuisine = ?, Time = ?, Ingredients = ?, Steps = ? where ID = ?");
        
            pst.setString(1, nameTF.getText());
            pst.setString(2, cuisineTF.getText());
            pst.setString(3, timeTF.getText());
            pst.setString(4, ingredientsTF.getText());
            pst.setString(5, stepsTF.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record Updated");
            upDateDB();
        }
        catch(ClassNotFoundException ex){
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbnUpdateActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel RecordTable = (DefaultTableModel)jTable1.getModel();
        int SelectedRows = jTable1.getSelectedRow();
        //id = Integer.parseInt(RecordTable.getValueAt(SelectedRows, 0).toString());
        Object nameVal = RecordTable.getValueAt(SelectedRows, 1);
        Object cuisineVal = RecordTable.getValueAt(SelectedRows, 2);
        Object timeVal = RecordTable.getValueAt(SelectedRows, 3);
        Object ingVal = RecordTable.getValueAt(SelectedRows, 4);
        Object stepVal = RecordTable.getValueAt(SelectedRows, 5);

        nameTF.setText(nameVal != null ? nameVal.toString() : "");
        cuisineTF.setText(cuisineVal != null ? cuisineVal.toString() : "");
        timeTF.setText(timeVal != null ? timeVal.toString() : "");
        ingredientsTF.setText(ingVal != null ? ingVal.toString() : "");
        stepsTF.setText(stepVal != null ? stepVal.toString() : "");
    }//GEN-LAST:event_jTable1MouseClicked

    private void jbnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnPrintActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat("Printing in progess");
        MessageFormat footer = new MessageFormat("Page {0, number, integer}");
        try{
            jTable1.print(JTable.PrintMode.NORMAL,header,footer);
        }
        catch(java.awt.print.PrinterException e){
            System.err.format("No Printer found", e.getMessage());
        }
    }//GEN-LAST:event_jbnPrintActionPerformed

    private void jbntDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbntDeleteActionPerformed
        DefaultTableModel RecordTable = (DefaultTableModel)jTable1.getModel();
        int SelectedRows = jTable1.getSelectedRow();
        // 2. Check if a row is actually selected
        if (SelectedRows == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Deletion Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // 3. Get the ID, handling potential NullPointerException if the cell is empty
        Object idValue = RecordTable.getValueAt(SelectedRows, 0);
        if (idValue == null) {
            JOptionPane.showMessageDialog(this, "Selected row does not contain a valid ID.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Parse the ID for the DELETE query
            id = Integer.parseInt(idValue.toString());
            // Confirmation dialog
            int deleteItem = JOptionPane.showConfirmDialog(null, "Confirm if you want to delete this recipe?",
                    "Warning: Delete Record", JOptionPane.YES_NO_OPTION);
            if (deleteItem == JOptionPane.YES_OPTION) {
                // Note: Use the modern driver name 'com.mysql.cj.jdbc.Driver'
                Class.forName("com.mysql.cj.jdbc.Driver");
                sqlConn = DBConnection.getConnection();
                // Prepare and execute the DELETE statement
                pst = sqlConn.prepareStatement("delete from connector where ID=?");
                pst.setInt(1, id);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Recipe Record Deleted successfully!");

                // Refresh table and clear text fields
                upDateDB();
                jbnResetActionPerformed(null); // Reuse the reset function to clear fields
            }
        } 
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Selected ID is not a valid number.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            // Log the error using the correct class name 'Menu'
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex) {
            // Log the error using the correct class name 'Menu'
            logger.log(java.util.logging.Level.SEVERE, "MySQL Driver not found.", ex);
        } 
        catch (SQLException ex) {
            // Log the error using the correct class name 'Menu'
            logger.log(java.util.logging.Level.SEVERE, "Database access error during deletion.", ex);
        }
    }//GEN-LAST:event_jbntDeleteActionPerformed

    private void labelGoogleIcon5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelGoogleIcon5MouseClicked
        Search searchWindow = new Search();
    searchWindow.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_labelGoogleIcon5MouseClicked
private void setScaledImage(String imagePath, javax.swing.JLabel label) {
    ImageIcon icon = new ImageIcon(imagePath);
    Image img = icon.getImage();

    int labelWidth = label.getWidth();
    int labelHeight = label.getHeight();

    if (labelWidth <= 0 || labelHeight <= 0) return;

    int imgWidth = icon.getIconWidth();
    int imgHeight = icon.getIconHeight();

    double widthRatio = (double) labelWidth / imgWidth;
    double heightRatio = (double) labelHeight / imgHeight;
    double scale = Math.min(widthRatio, heightRatio);

    int newWidth = (int) (imgWidth * scale);
    int newHeight = (int) (imgHeight * scale);

    Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    label.setIcon(new ImageIcon(scaledImg));
}


  /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
            
            // Auto-show login dialog on startup
            if (!menu.showLoginDialog()) {
                int result = JOptionPane.showConfirmDialog(menu, 
                    "No user logged in. Would you like to register?", 
                    "Welcome", JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION) {
                    menu.showRegisterDialog();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BarLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private icon.LabelGoogleIcon cuisineLabel;
    private javax.swing.JTextField cuisineTF;
    private icon.LabelGoogleIcon ingredientsLabel;
    private javax.swing.JTextField ingredientsTF;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbnPrint;
    private javax.swing.JButton jbnReset;
    private javax.swing.JButton jbnUpdate;
    private javax.swing.JButton jbntDelete;
    private javax.swing.JButton jbtnAdd;
    private javax.swing.JButton jbtnExit;
    private icon.LabelGoogleIcon labelGoogleIcon1;
    private icon.LabelGoogleIcon labelGoogleIcon2;
    private icon.LabelGoogleIcon labelGoogleIcon3;
    private icon.LabelGoogleIcon labelGoogleIcon4;
    private icon.LabelGoogleIcon labelGoogleIcon5;
    private icon.LabelGoogleIcon labelGoogleIcon6;
    private icon.LabelGoogleIcon nameLabel;
    private javax.swing.JTextField nameTF;
    private icon.LabelGoogleIcon stepsLabel;
    private javax.swing.JTextField stepsTF;
    private icon.LabelGoogleIcon timeLabel;
    private javax.swing.JTextField timeTF;
    // End of variables declaration//GEN-END:variables
}
