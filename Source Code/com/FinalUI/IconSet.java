/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.FinalUI;
import com.apple.eawt.Application;
import javax.swing.*;

/**
 *
 * @author AliS2019
 */

class IconSet extends JFrame {

    IconSet() {
        setIconImage(new ImageIcon("/logo128x128.png").getImage());
        Application.getApplication().setDockIconImage(
            new ImageIcon("/logo128x128.png").getImage());
    }

    public static void main(String args[]) {
            IconSet s = new IconSet();
        s.setVisible(true);
    }
}