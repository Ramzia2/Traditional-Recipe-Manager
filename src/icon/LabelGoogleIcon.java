/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icon;

import java.awt.Color;
import javax.swing.JLabel;
import jiconfont.swing.IconFontSwing;
import java.awt.Cursor;
/**
 *
 * @author thoan
 */
public class LabelGoogleIcon extends JLabel {
    GoogleMaterialDesignIcons googleIcon;

    public GoogleMaterialDesignIcons getGoogleIcon() {
        return googleIcon;
    }

    public void setGoogleIcon(GoogleMaterialDesignIcons googleIcon) {
        this.googleIcon = googleIcon;
        initIcon();
    }
    public LabelGoogleIcon() {
        // Khi rê chuột vào → hiện bàn tay
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
   
    private float iconSize = 24;

    public float getIconSize() {
        return iconSize;
    }

    public void setIconSize(float iconSize) {
        this.iconSize = iconSize;
        initIcon();
    }
    
    private Color iconColor = new Color(0,0,0);

    public Color getIconColor() {
        return iconColor;
    }

    public void setIconColor(Color iconColor) {
        this.iconColor = iconColor;
        initIcon();
    }
    
    private void initIcon(){
        if(googleIcon != null){
            IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
            setIcon(IconFontSwing.buildIcon(googleIcon, iconSize, iconColor));

        }
    }
    public void addClickListener(java.awt.event.MouseListener listener) {
        this.addMouseListener(listener);
    }
}
