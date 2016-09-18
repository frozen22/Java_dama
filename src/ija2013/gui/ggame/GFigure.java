/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Graficke zobrazeni figurky
 */
 

package ija2013.gui.ggame;

import ija2013.figures.Pawn;
import ija2013.basis.Figure;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

 
/**
 * Graficke zobrazeni figurky.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GFigure extends JPanel {
    public static final long serialVersionUID = 42424209L;
    
    Figure fig;
    Image imgBasic;
    Image imgHighLight;
    Image img;
    
    /**
     * Kontruktor. Ulozeni obsazku figurky.
     * 
     * @param fig Figurka
     */
    public GFigure(Figure fig) {
        this.fig = fig;
        
        if (fig.getColor() == 'w') { // white
            if(fig instanceof Pawn) {
                imgBasic = new ImageIcon(getClass().getResource("/ija2013/gui/images/whitePawn.png")).getImage();
                imgHighLight = new ImageIcon(getClass().getResource("/ija2013/gui/images/whitePawnHL.png")).getImage();
                img = imgBasic;
            }
            else {
                imgBasic = new ImageIcon(getClass().getResource("/ija2013/gui/images/whiteQueen.png")).getImage();
                imgHighLight = new ImageIcon(getClass().getResource("/ija2013/gui/images/whiteQueenHL.png")).getImage();
                img = imgBasic;
            }
        }
        else { // black
            if(fig instanceof Pawn) {
                imgBasic = new ImageIcon(getClass().getResource("/ija2013/gui/images/blackPawn.png")).getImage();
                imgHighLight = new ImageIcon(getClass().getResource("/ija2013/gui/images/blackPawnHL.png")).getImage();
                img = imgBasic;
            }
            else {
                imgBasic = new ImageIcon(getClass().getResource("/ija2013/gui/images/blackQueen.png")).getImage();
                imgHighLight = new ImageIcon(getClass().getResource("/ija2013/gui/images/blackQueenHL.png")).getImage();
                img = imgBasic;
            }
        }
    }
    
    /**
     * Zvyrazneni figurky.
     */
    public void highLight(){
        img = imgHighLight;
        repaint();
    }
    
    /**
     * Znevyrazneni figurky.
     */
    public void deHighLight(){
        img = imgBasic;
        repaint();
    }
    
    /**
     * Preklesneni komponenty.
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
    
}
