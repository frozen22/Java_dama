/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Color Chooser
 */


package ija2013.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
 
/**
 * Panel vyberu barvy hrace.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GColorChooser extends JPanel {
    public static final long serialVersionUID = 42424201L;
    
    // zvolena barva
    protected char chosenColor;
    
    // komponenty
    JRadioButton rBtnWhite;
    JRadioButton rBtnBlack;
    JPanel radioPanel;
    
    /**
     * Konstruktor. Nastaveni vychovy barvy (white).
     */
    public GColorChooser() {
        chosenColor = 'w';
        initComponents();
    }

    /**
     * Inicializace komponent panelu.
     */
    public final void initComponents() {
        rBtnWhite = new JRadioButton("White", true);
        rBtnBlack = new JRadioButton("Black", false);

        // nastaveni skupiny
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(rBtnWhite);
        bgroup.add(rBtnBlack);
        
        rBtnWhite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                chosenColor = 'w';
            }
        });
        
        rBtnBlack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                chosenColor = 'b';
            }
        });

        // Kvuli moznosti nastavovat Enable cele komponenty (vsechny rButtony
        // a ramecku) je vse uzavreno do dalsiho panelu
        radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(1, 2));
        radioPanel.add(rBtnWhite);
        radioPanel.add(rBtnBlack);
        radioPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Choose your Color"));

        radioPanel.setPreferredSize(new Dimension(183,45));
        add(radioPanel);
    
    }
    
    /**
     * Nastaveni povoleni interakce s komponentamy.
     * 
     * @param enable True - povolit, false - zakazat. 
     */
    @Override
    public void setEnabled(boolean enable) {
        radioPanel.setEnabled(enable);
        rBtnBlack.setEnabled(enable);
        rBtnWhite.setEnabled(enable);    
    }
    
    /**
     * Ziskani zvolene barvy.
     * 
     * @return Zvolena barva ('w' nebo 'b') 
     */
    public char getChosenColor() {
        return chosenColor;
    }
    
    /**
     * Ziskani nezvolene barvy.
     * 
     * @return Nezvolena barva ('b' nebo 'w') 
     */
    public char getNotChosenColor() {
        return chosenColor == 'w' ? 'b' : 'w';
    }
    
}
