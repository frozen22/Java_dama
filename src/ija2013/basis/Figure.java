/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Figurka
 */

package ija2013.basis;

import ija2013.figures.Pawn;
import ija2013.figures.Queen;

import java.util.List;

/**
 * Figurka.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public abstract class Figure {

    protected Position pos;
    protected char color; // 'w' == white, 'b' == black
    
    /**
     * Konfigurace figurky(Konstruktor)
     * 
     * @param p - pozice, kde se figurka umisti
     * @param player - barva hrace, ktery figurku vlastni
     */
    public Figure(Position p, char player) {
        this.color = player;
        this.pos = p;
        this.pos.putFigure(this);
    }

    /**
     * Presunuti figurky na pozici p. Obsahuje kontrolu hranic hraci desky, zda
     * pozice neni jiz obsazena a jestli figurka muze provest dany tah. Zaroven
     * neni povolen tah na stejne pole (tento fakt je kontrolovan v metodach
     * canMove jednotlivych figurek).
     *
     * @param p Cilova pozice
     * @return Bool hodnota zda je tah legalni a provedl se
     */
    public boolean move(Position p) {
        Figure midFig;

        if (p != null && p.isEmpty() && canMove(p)) {
            // Odstraneni figurky mezi pozicemi skoku
            if ((midFig = this.pos.figBetween(p)) != null && midFig != this) {
            midFig.removeFigure();
            }

            // presun figurky
            this.removeFigure();
            p.putFigure(this);
            this.pos = p;

            // povyseni Pesce na Kralovnu
            if (this instanceof Pawn && (this.color == 'w' && p.row == p.desk.dim || this.color == 'b' && p.row == 1)) {
                promoteRook();
            }

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Povyseni pesce na kralovnu. Tato metoda je volana z metodu move pokud
     * jsou splnena pravidla pro povyseni.
     */
    public void promoteRook() {
        removeFigure();
        pos.putFigure(new Queen(pos, color));
    }

    /**
     * Odstraneni figurky.
     *
     * @return figurka
     */
    public Figure removeFigure() {
        this.pos.fig = null;
        return this;
    }

    /**
     * Test zda je mozne presunout figurku na danou pozici. Neobsahuje test
     * hranic hraci plochy nebo zda je pozice uz obsazena.
     *
     * @param p Nova pozice
     * @return true/false
     */
    public abstract boolean canMove(Position p);
    
    /**
     * Seznam pozic kam lze tahnou danou figurkou. Pokud muze figurka nekterym
     * svym tahem odstranit nepratelovu figurku jsou vraceny jen tahy ktere 
     * odstranuji nepratelske figurky.
     * 
     * @return Seznam cilovych pozic. Pokud nebyl nalezen zadny tah - null.
     */
    public abstract List<Position> getPlayableMoves();
    
    /**
     * Metoda vrati typ pohybu ktery muze figurka vykonat. Pokud lze vykonat 
     * pohyb pri odstraneni figurky a zaroven normalni pohyb je vraceno cislo 1.
     * 
     * @return 0 == nelze vykonat pohyb.
     *         1 == pohyb pri kterem je odstranena nepratelska figurka.
     *         2 == jen pohyb.
     */
    public abstract int getTypeOfMoves();

    /**
     * Ziskani barvy figurky.
     */ 
    public char getColor() {
        return this.color;
    }

    /**
     * Ziskani pozice figurky.
     */ 
    public Position getPosition() {
        return this.pos;
    }
}
