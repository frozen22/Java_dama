/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Frame menu
 */
 
package ija2013.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;

import ija2013.game.Player;
import ija2013.game.Game;
import ija2013.game.Moves;
import ija2013.players.LocalPlayer;
import ija2013.players.RandomAI;
import ija2013.connection.Client;
import ija2013.connection.Server;
import ija2013.gui.ggame.GGameFrame;
import ija2013.gui.onlinegame.GHostConnectPanel;
import ija2013.gui.onlinegame.GHostSetup;
import ija2013.gui.onlinegame.GConnectSetup;

/**
 * Graficke rozhrani Menu aplikace. Stara se o prepinani jednotlivych menu.
 */
public class GMenuFrame extends JFrame {
    public static final long serialVersionUID = 42424205L;
    
    /* Typy hry. */
    public enum creatingGameTypes {
        localAI,
        localMP,
        onlineMP;
    }
    
    // Menu aplikace
    protected GMainMenu mainMenu;
    protected GGameTypeMenu gameTypeMenu;
    protected GLoadGameLocalContinue gLGC;
    protected GHostConnectPanel hostConnectPanel;
    protected GHostSetup hostSetup;
    protected GConnectSetup connectSetup;
    
    // promene pro vytvoreni Online hry
    // Jejich nastaveni primo provadeji ostatni menu.
    public Client client;
    public Server server;
    public Player onlinePlayer1;
    public Player onlinePlayer2;
    
    // promena pro ulozeni volby hry v hlavnim menu
    protected creatingGameTypes gameType;
    
    
    /**
     * Konstruktor. Vytvoreni jednotlivych menu. Nastaveni atributu okna.
     */
    public GMenuFrame () {
        // vytvoreni vetsiny menu
        mainMenu = new GMainMenu(this);
        gameTypeMenu = new GGameTypeMenu(this);
        hostConnectPanel = new GHostConnectPanel(this);
        hostSetup = new GHostSetup(this);
        connectSetup = new GConnectSetup(this);
        
        // nastaveni atributu okna
        setTitle("Dama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(250,500));
        
        // pridani hlavniho menu
        add(mainMenu);
        
        pack();
        
        // umisteni do prosted obrazovky
        center(); 
    }
    
    /**
     * Vytvoreni nove hry.
     */
    public void createGame(){
        Player p1;
        Player p2;
        Game game;
        
        // nastaveni hracu podle zvolene hry
        if (gameType == creatingGameTypes.localAI){ // Lokalni hra proti AI
            p1 = new LocalPlayer(gameTypeMenu.getChosenColor());
            p2 = new RandomAI(gameTypeMenu.getNotChosenColor());
        } 
        else if (gameType == creatingGameTypes.localMP){
            p1 = new LocalPlayer(gameTypeMenu.getChosenColor());
            p2 = new LocalPlayer(gameTypeMenu.getNotChosenColor());
        }
        else { // gameType == creatingGameTypes.onlineMP
            // nastaveni onlineHracu je provedeno v ostatnich menu aplikace
            p1 = onlinePlayer1;
            p2 = onlinePlayer2;
        }
        
        game = new Game(p1, p2); // vytvoreni hry
        GGameFrame gGame = new GGameFrame(game); // vytvoreni gui pro hry
        gGame.setVisible(true); // zobrazeni gui
    }
    

    /**
     * Pokracovani v ulozene hre hre.
     * 
     * @param gameMovesBasicNotation Zaznam hry v zakladni notaci ve ktere se 
     * pokracuje.
     */
    public void createGame(String gameMovesBasicNotation) {
        Player p1;
        Player p2;
        Game game;

        if (gameType == creatingGameTypes.localAI) { // Local AI
            p1 = new LocalPlayer(gameTypeMenu.getChosenColor());
            p2 = new RandomAI(gameTypeMenu.getNotChosenColor());
        }
        else if (gameType == creatingGameTypes.localMP) {
            p1 = new LocalPlayer(gameTypeMenu.getChosenColor());
            p2 = new LocalPlayer(gameTypeMenu.getNotChosenColor());
        }
        else { // gameType == creatingGameTypes.onlineMP
            // nastaveni onlineHracu je provedeno v ostatnich menu aplikace
            p1 = onlinePlayer1;
            p2 = onlinePlayer2;
        }

        // vytvoreni hry
        game = new Game(p1, p2); 
        
        // provedeni ulozenych tahu pred zacatkem hry
        Moves moves = new Moves(game.getDesk());
        moves.loadWithBasic(gameMovesBasicNotation);
        game.makeMoves(moves);
        
        // vytvoreni gui pro hry
        GGameFrame gGame = new GGameFrame(game); 
        gGame.printIntoTA(gameMovesBasicNotation);

        // zobrazeni gui
        gGame.setVisible(true); 
    }
    
    
    /**
     * Ukonceni serveru online hry.
     */
     public void serverClose(){
         if (server != null) {
             server.close();
         }
     }
     
     /**
      * Ukonceni clientke casti online hry.
      */
     public void clientClose(){
         if (client != null) {
             client.close();
         }
     }
    
    
    /* Metody urcene k zobrazovani jednotlivych menu. */
    
    /**
     * Ukriti komponenty na hlavnim framu. Metoda je parovana s showThis().
     * 
     * @param compToHide Komponenta, kterou chceme skryt.
     */
    public void hideThis(Component compToHide) {
        remove(compToHide);
    }

    /**
     * Zobrazeni komponenty na hlavni frame. Metoda je parovana s hideThis().
     * 
     * @param compToShow Komponenta kterou chceme zobrazit.
     */
    public void showThis(Component compToShow) {
        add(compToShow);
        pack();
    }
    

    /**
     * Zobrazeni hlavniho menu.
     * @param comp Menu ktere je prave zobrazeno
     */
    public void showMainMenu(Component comp) {
        remove(comp);
        add(mainMenu);
        pack();
        repaint();
    }
    
    /**
     * Zobrazeni vyberu typu hry.
     * 
     * @param comp Menu ktere je prave zobrazeno
     */
    public void showTypeOfGameMenu(Component comp) {
        remove(comp);
        gameTypeMenu.showAtMenu();
        pack();
        repaint();
    }

    /**
     * Zobrazeni menu s vyberem hostovani nebo pripojeni online hry.
     * 
     * @param comp Menu ktere je prave zobrazeno
     */
    public void showHostConnectMenu(Component comp){
        remove(comp);
        add(hostConnectPanel);
        pack();
        repaint();
    }
    
    /**
     * Zobrazeni menu s nastavenim pripojeni k vyckavajicimu hostitelovy .
     * 
     * @param comp Menu ktere je prave zobrazeno
     */
    public void showConnectSetup(Component comp){
        remove(comp);
        add(connectSetup);
        pack();
        repaint();
    }
    
    /**
     * Zobrazeni menu s nastavenim hostitele hry.
     * 
     * @param comp Menu ktere je prave zobrazeno
     */
    public void showHostSetup(Component comp){
        remove(comp);
        add(hostSetup);
        pack();
        repaint();
    }

    /**
     * Umisteni okna do prostred obrazovky.
     */
    public final void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ziskani rozmeru obrazovky
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2); // posunuti okna
    }
    
    /**
     * Ziskani barvy, ktera byla zvolena v GGameTypeMenu.
     * 
     * @return Zvolena barva.
     */
    public char getColorChosen(){
        return gameTypeMenu.getChosenColor();
    }
    
    /**
     * Ziskani barvy, ktera nebyla byla zvolena v GGameTypeMenu. Pouziva se pri
     * odesilani zpravy oponentovy, kde je potreba znak opacnou barvu nez jsme
     * zvolili pro sebe.
     * 
     * @return Nezvolena barva
     */
    public char getColorNotChosen(){
        return gameTypeMenu.getNotChosenColor();
    }

    /**
     * Ziskani informace o tom jaka volba hry byla zvolena v hlavnim menu.
     * Metoda je pouzivana v GGameTypeMenu pri rozhodovani jaky GLoadGamePanel
     * je nutne zobrazit.
     */
    public boolean gameTypeIsOnline() {
        return gameType == creatingGameTypes.onlineMP ? true : false;
    }
    
    /**
     * Ziskani informace o tom jestli byla v hlavnim menu zvolena volba 
     * lokalni multiplayer hry. Metoda je pouzica pri zobrazovani GameTypeMenu 
     * a to deaktivaci vyberu barvy.
     * 
     * @return true/false
     */
    public boolean gameTypeIsLocalMP() {
        return gameType == creatingGameTypes.localMP ? true : false;
    }


    /**
     * Ulozeni typu hry. Metoda je volana z hlavniho menu, kde uzivatel 
     * rozhoduje mezi LocalAI, LocalMP a OnlineMP hrou.
     * 
     * @param typeofgame Typ hry
     */
    public void setTypeOfGame(creatingGameTypes typeofgame) {
        gameType = typeofgame;
    }

}
