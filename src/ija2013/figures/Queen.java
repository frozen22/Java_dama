/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Kralovna
 */

package ija2013.figures;

import ija2013.basis.Figure;
import ija2013.basis.Position;

import java.util.List;
import java.util.ArrayList;

/**
 * Kralovna.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public class Queen extends Figure {

    /**
     * Konstruktor figurky kralovny
     * 
     * @param p Pozice figurky
     * @param player Barva hrace, ktery figurku vlastni
     */  
    public Queen(Position p, char player) {
        super(p, player);
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
        Position toPos;// pomocna promena 
        int retCode;
        boolean moveable = false;


        // pohyb po diagonale smerem k pravemu hornimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(1, 1)) != null) {
            if ((retCode = isMoveable(toPos)) == 0) {
                continue;
            }
            else if (retCode == 1) {
                return 1;
            }
            else {
                moveable = true;
            } // retCode == 2
        }

        // pohyb po diagonale smerem k levemu hornimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(-1, 1)) != null) {
            if ((retCode = isMoveable(toPos)) == 0) {
                continue;
            }
            else if (retCode == 1) {
                return 1;
            }
            else {
                moveable = true;
            } // retCode == 2        
        }

        // pohyb po diagonale smerem k pravemu dolnimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(1, -1)) != null) {
            if ((retCode = isMoveable(toPos)) == 0) {
                continue;
            }
            else if (retCode == 1) {
                return 1;
            }
            else {
                moveable = true;
            } // retCode == 2       

        }

        // pohyb po diagonale smerem k levemu dolnimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(-1, -1)) != null) {
            if ((retCode = isMoveable(toPos)) == 0) {
                continue;
            }
            else if (retCode == 1) {
                return 1;
            }
            else {
                moveable = true;
            } // retCode == 2
        }

        if (moveable) {
            return 2;
        }
        else {
            return 0;
        }
    }

    
    /**
     * Metoda vrati typ pohybu ktery muze figurka vykonat na pozici toPos. 
     * 
     * @return 0 == nelze vykonat pohyb.
     *         1 == pohyb pri kterem je odstranena nepratelska figurka.
     *         2 == jen pohyb.
     */
    private int isMoveable(Position toPos) {
        Figure midFig;

        if (toPos.isEmpty()) {
            midFig = pos.figBetween(toPos); // zjisteni figurky mezi pozicemi

            if (midFig == this) { // Zadna figurka neni mezi skokem
                return 2;
            }
            else if (midFig != null && midFig.getColor() != color) {
                // Byla nalezena jen jedna figurka a nepratelske barvy
                return 1;
            }
        }

        return 0;
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
        List<Position> posTakeList = new ArrayList<>();
        List<Position> posMoveList = new ArrayList<>();
        Position toPos;// pomocna promena 

        // pohyb po diagonale smerem k pravemu hornimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(1, 1)) != null) {
            addPos(toPos, posTakeList, posMoveList);
        }

        // pohyb po diagonale smerem k levemu hornimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(-1, 1)) != null) {
            addPos(toPos, posTakeList, posMoveList);
        }

        // pohyb po diagonale smerem k pravemu dolnimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(1, -1)) != null) {
            addPos(toPos, posTakeList, posMoveList);
        }

        // pohyb po diagonale smerem k levemu dolnimu rohu
        toPos = pos;
        while ((toPos = toPos.nextPosition(-1, -1)) != null) {
            addPos(toPos, posTakeList, posMoveList);
        }

        if (!posTakeList.isEmpty()) {
            return posTakeList;
        }
        else {
            if (!posMoveList.isEmpty()) {
                return posMoveList;
            }
            else {// zadny tah nebyl nalezen
                return null;
            }
        }
    }

    /**
     * Zpracovani tahu. Provadi se kontrola zda je cilova pozice prazdna,
     * je-li mezi pozici figurky a cilovou pozici figurka je provedena kontrola
     * teto figurku.
     * 
     * @param toPos Testovana pozice
     * @param takeList Seznam pro pozice, pri kterych je odstranena nepratelska figurka
     * @param moveList Seznam pro pozice, pri kterych neni odstranena nepratelska figurka
     */
    private void addPos(Position toPos, List<Position> takeList, List<Position> moveList) {
        Figure midFig;
        
        if (toPos.isEmpty()) {
            midFig = pos.figBetween(toPos); // zjisteni figurky mezi pozicemi
            
            if (midFig == this) { // Zadna figurka neni mezi skokem
                moveList.add(toPos);
            }
            else if (midFig != null && midFig.getColor() != color) {
                // Byla nalezena jen jedna figurka a nepratelske barvy
                takeList.add(toPos);
            }
        }
    }
    
    /**
     * Test zda figurka muze provest tah na danou pozici.
     * 
     * @param p Cilova pozice
     * @return True/False
     */
    @Override
    public boolean canMove(Position p) {
        // Kontrola zda pozice figurky a p lezi na stejne diagonale.
        // Pohyb na stejnou pozici je zakazan.
        if (Math.abs(p.getRow() - super.pos.getRow()) != Math.abs(p.getCol() - super.pos.getCol())
                || Math.abs(p.getRow() - super.pos.getRow()) == 0) {
            return false;
        }

        Figure midFig = super.pos.figBetween(p);

        // Mezi pozicemi skoku je vice jak jedna figurka
        if (midFig == null) {
            return false;
        }

        // Pokud je vnitrni figurka nalezena nesmi byt stejneho
        if (midFig != this && super.color == midFig.getColor()) {
            return false;
        }

        return true;
    }
}
