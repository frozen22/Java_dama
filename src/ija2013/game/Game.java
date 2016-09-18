/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Game
 */

package ija2013.game;

import ija2013.basis.Figure;
import ija2013.basis.Desk;
import ija2013.basis.Position;
import ija2013.gui.ggame.GGameFrame;
import java.util.List;


/**
 * Hra.
 *
 * @author Frantisek Nemec (xnemec61)
 */
public class Game {
    
    protected GGameFrame gGame; // Graficke rozhrani hry
    protected Desk desk;        // hraci deska na ktere je hra hrana
    protected Player player1;   // hraci hry
    protected Player player2;
    protected Moves movesDone;  // pouziva se pri ulozeni hry do XML
    private char playingPlayer; // hrac na tahu
    private int turnCount;      // cislo tahu
    

    /**
     * Konstruktor.
     * 
     * @param p1 Prvni hrac hry
     * @param p2 Druhy hrac hry
     */
    public Game(Player p1, Player p2) {
        desk = new Desk(8);
        movesDone = new Moves();
        
        //!!! initDesk byl presunut do konstruktoru desky
        
        // upraveni hracu tak aby player1 byl vzdy 'w' barvy
        if (p1.getColor() == 'w') {
            player1 = p1.initPlayer(this);
            player2 = p2.initPlayer(this);

        }
        else {
            player1 = p2.initPlayer(this);;
            player2 = p1.initPlayer(this);
        }
        
        playingPlayer = 'w';
        turnCount = 1;
    }
    
    /**
     * Vykonani tahu. Vykonani tahu obsahuje: samotne vykonani tahu, prekresleni
     * grafickeho rozhrani a tisk tahu do tiskove pole u hraci desky
     * 
     * @param m Konany tah.
     * @return True - tah byl uspesne proveden. False - nevalidni tah
     */
    public boolean makeMove(Move m) {
        //!!!! makemove byl predelan po prevedeni do cpp
        
        // Test pozic v Move
        if (m.getFrom() == null || m.getTo() == null || m.getFrom().getFigure() == null) {
            return false;
        }

        // Test figurek mezi skoky
        Figure midFig = m.getFrom().figBetween(m.getTo());
        if (midFig == null) { // neplatny tah, mezi skoky je vice figurek
            return false;
        }
        
        // ulozeni odstranene figurky do Move.
        // figurka je pouzita pri vypisovani tahu (a1-c3, a1xc3)
        if (midFig != m.getFrom().getFigure()) {
            m.setTaken(midFig.getPosition());
            m.setTakenFig(midFig);
        }


        // Test validnosti zadaneho tahu
        // Test oznacene figurky
        List<Position> validPosList = desk.getPlayablePositions(playingPlayer);
        if (!validPosList.contains(m.getFrom())) {
            return false;
        }
        // Test cile skoku figurky
        validPosList = m.getFrom().getFigure().getPlayableMoves();
        if (!validPosList.contains(m.getTo())) {
            return false;
        }

        // Provedeni tahu
        m.getFrom().getFigure().move(m.getTo());
        
        
        // Upraveni promenych hrajiciho hrace
        if (playingPlayer == 'w') {
            if(gGame != null) { // v continueGame delam tahy jeste predtim nez je nastaveno gGame
                gGame.setLT("Tahne: cerny");
                gGame.printIntoTA(turnCount + ". " + m.moveToString() + ' ');
            }
            
            playingPlayer = 'b';
        }
        else {
            if(gGame != null) { // v continueGame delam tahy jeste predtim nez je nastaveno gGame
                gGame.setLT("Tahne: bily");
                gGame.printIntoTA(' ' + m.moveToString() + '\n');
            }
            
            turnCount++;
            playingPlayer = 'w';
        }

        // Ulozeni tahu. Tahy jsou pouzity pri ulozeni hry v XML formatu
        movesDone.addMove(m);
        
        if(gGame != null) { // v continueGame delam tahy jeste predtim nez je nastaveno gGame
            gGame.refreshM(m);
        }
        
        return true;
    }

    /**
     * Odstartovani hry.
     * 
     * @return Barva viteze.
     */
    public char start() {
        Move move;
        while (true) {
            
            if (playingPlayer == 'w') {
                
                move = player1.getMove();
                if (move != null) {
                    makeMove(move);
                    player2.remindPlayer(move);
                }
                else { // hrac nemuze nikam tahnout tedy prohral
                    return player2.getColor();
                }
            }
            else {
                
                move = player2.getMove();
                if (move != null) {
                    makeMove(move);
                    player1.remindPlayer(move);
                }
                else { // hrac nemuze nikam tahnout tedy prohral
                    return player1.getColor();
                }
            }
        }
    }

    /**
     * Provedeni zadanych tahu. Tato metoda je pouzita pri dokoncovani rozehrane
     * hry, kdy je nutne provest urcite tahy, jeste pred zacatkem samotne hry.
     * 
     * @param movesToDo 
     * @return True - vsechny tahy jsou validni. False - nektery tah nelze provest
     */
    public boolean makeMoves(Moves movesToDo) {
        Move move;
        
        // provedeni tahu
        while ((move = movesToDo.getNextMove()) != null) {
            if (!makeMove(move)) { 
                return false; // tah nelze provest
            }
        }
        
        return true;
    }
    
    /**
     * Ziskani prvni hrace. Pouziva se pri zvyraznovani tahu, kde neni 
     * pozadovano zvyrazneni figurek, pokud zrovna lokalni hrac neni na tahu.
     * 
     * @return Hrac 1
     */
    public Player getSecondPlayer(){
        return player2;
    }
    
    /**
     * Ziskani druheho hrace. Pouziva se pri zvyraznovani tahu, kde neni 
     * pozadovano zvyrazneni figurek, pokud zrovna lokalni hrac neni na tahu.
     * 
     * @return Hrac 2
     */
    public Player getFirstPlayer(){
        return player1;
    }
    
    /**
     * Zjisteni hrace na tahu
     * @return hrac
     */
    public char getPlayingPlayer() {
        return playingPlayer;
    }
    /**
     * Vraci desku aktualni hry
     * @return deska
     */
    public Desk getDesk() {
        return desk;
    }
    
    /**
     * Ziskava seznam vykonanych tahu
     * @return seznam tahu  - Moves
     */
    public Moves getMovesDone() {
        return movesDone;
    }
    
    public void setGGame(GGameFrame gg) {
        gGame = gg;
    }
    
    public GGameFrame getGGame() {
        return gGame;
    }
}
