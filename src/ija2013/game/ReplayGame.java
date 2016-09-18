/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   ReplayGame
 */

package ija2013.game;

import ija2013.basis.Figure;
import ija2013.basis.Desk;
import ija2013.basis.Position;
import ija2013.gui.GReplayFrame;
import java.util.List;


/**
 * Prehravaci hra. Tato trida obsahuje metody urcene jen k prehravani ulozene
 * hry.
 *
 * @author Frantisek Nemec (xnemec61)
 */
public class ReplayGame {
    protected GReplayFrame repFrame; // graficke rozhrani prehravane hry
    protected Moves moves; // ulozene tahu
    protected Desk desk; // deska na ktere je prehravani proveden
    
    protected boolean autoPlaySign = false; // automaticke prehravani
    
    private char playingPlayer = 'w'; // prave hrajici hrac
    
    /**
     * Konstrukce prehravajici hry.
     * 
     * @param movesString Tahy v zakladni notaci.
     */
    public ReplayGame(String movesString) {
        desk = new Desk(8);
        moves = new Moves(desk);
        moves.loadWithBasic(movesString); // naplneni tahu 
    }
    

    /**
     * Vykonani tahu v prehravajici hre. Je provedena kontrola tahu, jeho 
     * nasledne provedeni a rozbrazeni v gui.
     * 
     * @param m Konany tah.
     * @return True - tah byl uspesne proveden. False - nevalidni tah
     */
    public boolean makeMove(Move m) {
        // provedeni tahu
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

        // Provedeni tahu
        validPosList = m.getFrom().getFigure().getPlayableMoves();
        
        if (!validPosList.contains(m.getTo())) {
            return false;
        }

        m.getFrom().getFigure().move(m.getTo()); // provedeni tahu
        repFrame.getGDesk().refreshMove(m); // obnoveni grafickeho rozhrani
        switchPlayingPlayer();
        
        return true;
    }
    

    /**
     * Skok na zadany tah.
     * 
     * @param num Cislo pozadovaneho tahu
     */
    public void goToMove(int num,int interval) {
        int actMoveNum = moves.getActualMoveNum();
        int maxMoveNum = moves.getMaxMoveNum();
        
        
        if (num < 0 || num > maxMoveNum) { // Spatne hodnoty
            return; 
        } 

        autoPlaySign = true;
        
        // Provedeni vsech tahy mezi auktualni a zadanym tahem
        if (num > actMoveNum) {
            while(moves.getActualMoveNum() != num) {
                if (autoPlaySign) {
                    replayNextMove();
                    if (interval != 0) {
                        try {
                            Thread.sleep(interval);
                        }
                        catch (Exception e) {
                            return;
                        }
                    }
                }
            }
        }
        else if (num < actMoveNum) {
            while(moves.getActualMoveNum() != num) {
                if (autoPlaySign) {
                    replayUndoMove();
                    if (interval != 0) {
                        try {
                            Thread.sleep(interval);
                        }
                        catch (Exception e) {
                            return;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Tah zpet.
     */
    public void replayUndoMove() {
        
        int actSaveMove = moves.getActualMoveNum();
        moves.resetNum();
        
        desk.clear();
        desk.initDesk(); 
        repFrame.getGDesk().clearGDesk();
        repFrame.getGDesk().initRefresh();
        playingPlayer = 'w';
        
        while (moves.getActualMoveNum() < (actSaveMove - 1)){
            replayNextMove();
        }

    }
    
    
    /**
     * Prehrani nasledujici tahy.
     *
     * @return 0 - tah lze provest, 1 - jsme na konci v seznamu tahu
     *         2 - tah nelze provest
     */
    public int replayNextMove() {
        Move move;
        
        
        move = moves.getNextMove(); // ziskani tahu
        
        if (move == null) { // jsme na konci seznamu s tahy
            return 1;
        }

        if (makeMove(move) == false) { // Tah nelze provest
            return 2;
        }

        return 0;
    }
    
    /**
     * Vymena prave hrajicich hracu.
     */
    private void switchPlayingPlayer() {
        if (playingPlayer == 'w') {
            playingPlayer = 'b';
        }
        else {
            playingPlayer = 'w';
        }
    }
    
    /**
     * Tisk vsech tahu. Metoda je volana pri rozbrazeni hraci desky prehravane
     * hry
     */
    public void printAllMoves(){
        repFrame.printStr(moves.getMovesInBasicNotation());
    }
    /**
     * Vraci aktualni stav desky
     * @return deska
     */    
    public Desk getDesk() {
        return desk;
    }
    
    /**
     * Ziskani poctu pohybu nactene hry. Pouziva se pri kontrole zda neni 
     * zadan tah vetsi nez je pocet tahu.
     * 
     * @return Pocet tahu nactene hry.
     */
    public int getMaxMoveNum() {
       return moves.getMaxMoveNum();
    }

    /**
     * Nastaveni grafickeho rozhrani prehravani hry.
     * 
     * @param rFrame Graficke rozhrani
     */
    public void setRepFrame(GReplayFrame rFrame) {
        this.repFrame = rFrame;
    }

    /**
     * Zjisti, zda je zapnute automaticke prehravani
     * @return true/false
     */
    public boolean autoPlaying(){
        return autoPlaySign;
    }
    
    /**
     * Pozastaveni automatickeho prehravani.
     */
    public void stopGoTo(){
        autoPlaySign = false;
    }
    
    
}
