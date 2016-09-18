/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Game frame
 */

package ija2013.gui.ggame;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.FlowLayout;

import ija2013.game.Game;
import ija2013.game.Move;
import javax.swing.JFrame;


/**
 * Okno hry. Zastresuje hraci desku a ovladani hry.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GGameFrame extends JFrame {
    public static final long serialVersionUID = 42424210L;

    private Game game;
    
    // komponenty 
    private GDesk gDesk;
    private GControl gControl;

    /**
     * Konstruktor. Ulozeny hry a inicializace komponent.
     * 
     * @param g Hra
     */
    public GGameFrame(Game g) {
        game = g;
        game.setGGame(this);
        
        setLayout(new FlowLayout());
        setResizable(false);
        setTitle("Hra Dama");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        pack();
        center();
        
        gControl.startGame();
    }

    /**
     * Inicializace komponent. Pridani hraci desky a ovladani.
     */
    private void initComponents() {
        gDesk = new GDesk(game.getDesk(),this);
        gControl = new GControl(game, this);
        add(gDesk);
        add(gControl);
    }
    
    /**
     * Zapoceti hry.
     */
    public void startGame(){
        gControl.startGame();
    }
    
    /**
     * Tisk do textoveho pole.
     * 
     * @param str Tisknuty text.
     */
    public void printIntoTA(String str) {
         gControl.printIntoTextArea(str);
    }
    
    /**
     * Nastaveni textu informacniho labelu.
     * 
     * @param str Text.
     */
    public void setLT(String str){
        gControl.setLabelText(str);
    }
    
    /**
     * Ziskani obsahu labelTextu. Pouziva se pri upozornovani na spatny tah.
     * 
     * @return Text labelTextu
     */
    public String getLT(){
        return gControl.getLabelText();
    }
    
    
    /**
     * Provedeni tahy na graficke hraci desce.
     * 
     * @param m Provedeny tah.
     */
    public void refreshM(Move m){
        gDesk.refreshMove(m);
    }
    
    
    /**
     * Ziskani graficke hraci desky.
     * 
     * @return Hraci deska
     */
    public GDesk getGDesk() {
        return gDesk;
    }
    
    /**
     * Posunuti okna do prostred obrazovky
     */
    private void center() {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    }
}
