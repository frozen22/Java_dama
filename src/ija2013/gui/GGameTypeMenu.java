/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   GameTypeMenu
 */


package ija2013.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import ija2013.players.LocalPlayer;
import ija2013.players.OnlinePlayer;
import ija2013.gui.onlinegame.GLoadGameOnlineContinue;


/**
 * Menu (panel) zvoleni nove nebo pokracovani ve hre.
 * Toto menu take obsahuje vyber barvy hrace.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GGameTypeMenu extends JPanel {
    public static final long serialVersionUID = 42424202L;
    
    // hlavni menu (frame) ve kterem se tato komponenta nachazi
    protected GMenuFrame parFrame;

    // komponenty
    private GColorChooser colorChooser;
    private JButton btnNewGame;
    private JButton btnContinueGame;
    private JButton btnBack;

    /**
     * Konstruktor. Inicializa komponent, ulozeni rodicovskeho framu.
     *
     * @param parFrame Frame do ktereho je tato komponenta vlozena 
     */
    public GGameTypeMenu(GMenuFrame parFrame) {
        this.parFrame = parFrame;
        initComponents();
    }

    
    /**
     * Inicializace komponent. Vyber barvy, tlacitko "Nova hra", tlacitko 
     * "Pokracovat ve hre" a tlacitko "Zpet".
     */
    private void initComponents() {
        
        // urceno pro predani odkazu na tuto komponentu do udalosti stisknuti 
        // na tlacitka
        final Component thisComp = this; 
        
        colorChooser = new GColorChooser();
        btnNewGame = new JButton("New Game");
        btnContinueGame = new JButton("Continue Game");
        btnBack = new JButton("Back");
        
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (parFrame.gameTypeIsOnline()) {
                    parFrame.onlinePlayer1 = new LocalPlayer(colorChooser.getChosenColor());
                    parFrame.onlinePlayer2 = new OnlinePlayer(colorChooser.getNotChosenColor(),parFrame.client.getInputSocket(),parFrame.client.getOutputSocket());
                    // odeslani informaci o me volbe barvy a nove hry oponentovy
                    parFrame.client.send("" + parFrame.gameTypeMenu.getNotChosenColor() + "n");
                    //?!? pridat ACK zpravu
                    parFrame.createGame();
                }
                else { // gameType is local
                    parFrame.showMainMenu(thisComp);
                    parFrame.createGame();
                }
            }
        });

        btnContinueGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                 
                if (parFrame.gameTypeIsOnline()) {
                    parFrame.onlinePlayer1 = new LocalPlayer(colorChooser.getChosenColor());
                    parFrame.onlinePlayer2 = new OnlinePlayer(colorChooser.getNotChosenColor(),parFrame.client.getInputSocket(),parFrame.client.getOutputSocket());

                    // zobrazeni panelu pro vyberu hry v ktere se bude pokracovat
                    GLoadGameOnlineContinue loadGameOnlineCont = new GLoadGameOnlineContinue(parFrame);

                    parFrame.hideThis(thisComp);
                    parFrame.showThis(loadGameOnlineCont);
                }
                else { // gameType is local
                    // zobrazeni panelu pro vyberu hry v ktere se bude pokracovat
                    GLoadGameLocalContinue loadGameLocCont = new GLoadGameLocalContinue(parFrame);
                    parFrame.hideThis(thisComp);
                    parFrame.showThis(loadGameLocCont);
                }

            }
        });
        
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parFrame.showMainMenu(thisComp);
            }
        });
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel btnPanel = new JPanel(new GridLayout(3, 1,5,5));
        btnPanel.setPreferredSize(new Dimension(200,140));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        btnPanel.add(btnNewGame);
        btnPanel.add(btnContinueGame);
        btnPanel.add(btnBack);
        
        add(colorChooser);
        add(btnPanel);
    }

    /**
     * Zobraceni teto komponenty do menu (parFrame). Obsahuje spravne nastaveni
     * pouzitelnosti vyberu barvy, ktere je v local multiplayer hre zakazano.
     */
    public void showAtMenu(){
        if (parFrame.gameTypeIsLocalMP()) {
            colorChooser.setEnabled(false);
        }
        else {
            colorChooser.setEnabled(true);
        }
        
        parFrame.add(this);
    }

    /**
     * Ziskani vybrane barvy. Tranzitivni volani metody samotne komponenty
     * colorCHooser.
     * 
     * @return Vybrana barva ('w' nebo 'b') 
     */
    public char getChosenColor(){
        return colorChooser.getChosenColor();
    }
    
    /**
     * Ziskani nevybrane barvy. Tranzitivni volani metody samotne komponenty
     * colorCHooser.
     * 
     * @return Nevybrana barva ('w' nebo 'b') 
     */
    public char getNotChosenColor() {
        return colorChooser.getNotChosenColor();
    }
}
