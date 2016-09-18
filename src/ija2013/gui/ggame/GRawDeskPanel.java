/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   GRawDeskPanel 
 */
 
package ija2013.gui.ggame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel hraci desky. Obsahuje jen namapovani obrazku na panel.
 * @author Remder
 */
class GRawDeskPanel extends JPanel {
    public static final long serialVersionUID = 42424211L;

    private Image img;
    
    /**
     * Konstruktor. Nacteni obsazku, nastveni rozmeru panelu.
     */
    public GRawDeskPanel() {
        img = new ImageIcon(getClass().getResource("/ija2013/gui/images/plocha.png")).getImage();
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    /**
     * Vyskresleni panelu s obrazkem
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}