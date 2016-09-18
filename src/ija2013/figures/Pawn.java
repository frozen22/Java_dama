/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Pesec
 */
package ija2013.figures;

import ija2013.basis.Figure;
import ija2013.basis.Position;

import java.util.List;
import java.util.ArrayList;

/**
 * Pesec.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public class Pawn extends Figure {

    
    /**
     * Konstruktor figurky pesce
     * 
     * @param p Pozice figurky.
     * @param player Barva hrace ktery figurku vlastni.
     */  
    public Pawn(Position p, char player) {
        super(p, player);
    }
    
    /**
     * Seznam pozic kam lze tahnou danou figurkou. Pokud muze figurka nekterym
     * svym tahem odstranit nepratelovu figurku jsou vraceny jen tahy ktere 
     * odstranuji nepratelske figurky.
     * 
     * @return Seznam cilovych pozic. Pokud nebyl nalezen zadny tah - null.
     */
    @Override
    public List<Position> getPlayableMoves() {
        List<Position> posList = new ArrayList<>();
        Position toPos; // pomocna promena 
        Figure midFig;  // pomocna promena 
        int dR, ddR; // pomocne promene pro ulozeny rozdilu radku
        boolean moveSet = false; // priznak zda byl nalezen tah
        
        // urceni smeru kontroly tahu
        if (color == 'w') {
            dR = 1;
            ddR = 2;
        }
        else {
            dR = -1;
            ddR = -2;
        }
        
        // Tahy ktere lze provest jen pri odstraneni nepratelske figurky
        if ((toPos = pos.nextPosition(2, ddR)) != null && toPos.isEmpty()) {
            midFig = pos.figBetween(toPos); // je zde nepratelska figurka?
            if (midFig != this && color != midFig.getColor()) {
                posList.add(toPos);
            }
        }
        if ((toPos = pos.nextPosition(-2, ddR)) != null && toPos.isEmpty()) {
            midFig = pos.figBetween(toPos); // je zde nepratelska figurka?
            if (midFig != this && color != midFig.getColor()) {
                posList.add(toPos);
            }
        }
        
        if (!posList.isEmpty()) {
            return posList;
        }
        
        // Tahy pri kterych nelze odstranit figurku
        if ((toPos = pos.nextPosition(1, dR)) != null && toPos.isEmpty()) {
            posList.add(toPos);
            moveSet = true;
        }
        if ((toPos = pos.nextPosition(-1, dR)) != null && toPos.isEmpty()) {
             posList.add(toPos);
            moveSet = true;
        }

        if (moveSet) {
            return posList;
        }
        else { // zadny tah nebyl nalezen
            return null;
        }
    }
    
    /**
     * Metoda vrati typ pohybu ktery muze figurka vykonat. Pokud lze vykonat 
     * pohyb pri odstraneni figurky a zaroven normalni pohyb je vraceno cislo 1.
     * 
     * @return 0 == nelze vykonat pohyb.
     *         1 == pohyb pri kterem je odstranena nepratelska figurka.
     *         2 == jen pohyb.
     */
    @Override
    public int getTypeOfMoves() {
        Position toPos; // pomocna promena 
        Figure midFig;  // pomocna promena 
        int dR, ddR; // pomocne promene pro ulozeny rozdilu radku
        
        // urceni smeru kontroly tahu
        if (color == 'w') {
            dR = 1;
            ddR = 2;
        }
        else {
            dR = -1;
            ddR = -2;
        }

        // Tahy ktere lze provest jen pri odstraneni nepratelske figurky
        if ((toPos = pos.nextPosition(2, ddR)) != null && toPos.isEmpty()) {
            midFig = pos.figBetween(toPos); // je zde nepratelska figurka?
            if (midFig != this && color != midFig.getColor()) {
                return 1;
            }
        }
        if ((toPos = pos.nextPosition(-2, ddR)) != null && toPos.isEmpty()) {
            midFig = pos.figBetween(toPos); // je zde nepratelska figurka?
            if (midFig != this && color != midFig.getColor()) {
                return 1;
            }
        }
        
        // Tahy pri kterych nelze odstranit figurku
        if ((toPos = pos.nextPosition(1, dR)) != null && toPos.isEmpty()) {
            return 2;
        }
        if ((toPos = pos.nextPosition(-1, dR)) != null && toPos.isEmpty()) {
            return 2;
        }

        return 0;
    }

    /**
     * Test zda je mozne presunout figurku na danou pozici. Neobsahuje test
     * hranic hraci plochy nebo zda je pozice uz obsazena.
     *
     * @param p Nova pozice
     * @return true/false
     */
    @Override
    public boolean canMove(Position p) {
        int colDiff = p.getCol() - super.pos.getCol();
        int rowDiff = p.getRow() - super.pos.getRow();

        // posunuti o jedno dopredu diagonalne. White mohou "nahoru" a Black "dolu"
        if (Math.abs(colDiff) == 1 && (color == 'w' && rowDiff == 1 || color == 'b' && rowDiff == -1)) {
            return true;
        }

        // skok o dva, ale jen za predpokladu ze je mezi skoky nepratelska figurka
        if (Math.abs(colDiff) == 2 && ((super.color == 'w' && rowDiff == 2) || (super.color == 'b' && rowDiff == -2))) {

            Figure midFig = super.pos.figBetween(p);
            // Figurka neni tedy nelze provest skok o dva || figurky jsou stejneho hrace
            if (midFig == this || super.color == midFig.getColor()) {
                return false;
            }

            return true;
        }

        return false;
    }
}
