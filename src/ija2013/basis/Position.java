/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Pozice na hraci desce
 */
package ija2013.basis;

/**
 * Pozice.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public class Position {

    protected char col;
    protected int row;
    protected Desk desk;
    protected Figure fig;

    /**
     * Konstruktor Pozice
     * 
     * @param d Deska
     * @param c Radek
     * @param r Sloupec
     */ 
    public Position(Desk d, char c, int r) {
        this.col = c;
        this.row = r;
        this.desk = d;
        this.fig = null;
    }

    /**
     * Metoda vraci pozici posunutou dC sloupcu a dR radku od this pozice
     * 
     * @param dC Rozdil sloupcu 
     * @param dR Rozdil radku
     * @return Posunuta pozice
     */
    public Position nextPosition(int dC, int dR) {
        char newC;
        int newR;

        newC = (char) (dC + this.col);
        newR = this.row + dR;

        if (desk.inBorders(desk.getPositionAt(newC, newR))) {
            return desk.getPositionAt(newC, newR);
        }
        else {
            return null;
        }
    }

    /**
     * Vlozeni figurky na this pozici. Pokud se jiz na pozici nejaka figurka
     * vyskytuje je vracena.
     *
     * @param f Vkladana figurka
     * @return Jiz vyskytujici se figurka
     */
    public Figure putFigure(Figure f) {
        Figure retFig = this.fig;
        this.fig = f;
        f.pos = this;
        return retFig;
    }

    /**
     * Odstraneni figurky na this pozici.
     *
     * @return Odstranena figurka
     */
    public Figure removeFigure() {
        Figure retFig = this.fig;
        this.fig = null;
        return retFig;
    }

    /**
     * Zjisteni zda mezi pozici this a parametrem p lezi figurka. Pokud se zadna
     * figurka nenachazi je vracena figurka na pozici this. Kdyz je nalezeno
     * vice figurek je vracen null.
     *
     * @param p Hranicni pozice
     * @return Vnitrni figurka
     */
    public Figure figBetween(Position p) {
        int colEnd;
        int rowDiff, colDiff;
        int c, r;
        boolean thereIs = false;
        Figure midFig = null;

        //!! ZMENA PO PRESPANI DO CPP
        // Lezi pozice na stejne diagonale?
        if (Math.abs(Math.abs(p.getRow()) - Math.abs(this.row)) != Math.abs(Math.abs(p.getCol()) - Math.abs(this.col))) {
            return null;
        }
        
        // zjisteni smeru kontroly
        rowDiff = (p.getRow() - this.row) < 0 ? -1 : 1;
        colDiff = (p.getCol() - this.col) < 0 ? -1 : 1;

        
        colEnd = p.getCol();

        c = this.col + colDiff;
        r = this.row + rowDiff;

        // samotna kontrola vsech pozic mezi zadanymi pozicemi
        while (c != colEnd) {
            if (desk.getFigureAt((char) (c), r) != null) {
                if (thereIs == true) { // vyskyt vice jak jedne figurky
                    return null;
                }
                midFig = this.desk.getFigureAt((char) (c), r);
                thereIs = true;
            }

            c += colDiff;
            r += rowDiff;
        }

        if (thereIs == true) {
            return midFig; // jedna figurka se vyskytuje
        }
        else { // zadna figurka nebyla nalezena
            return this.fig;
        }
    }

    /**
     * Test zda na teto pozici neni figurka.
     * 
     * @return true/false
     */
    public boolean isEmpty () {
        if (fig == null) {
            return true;
        }
        else {
            return false;
        }
    }
    
   /**
    * Ziskani figurky na this pozici.
    * 
    * @return Figurka.
    */   
    public Figure getFigure() {
        return this.fig;
    }
    
    /**
     * Ziskani desky ke ktere patri this pozice.
     *
     * @return Deska.
     */
    public Desk getDesk() {
        return this.desk;
    }

    /**
     * Zjisteni sloupce pozice.
     * 
     * @return Pismeno sloupce.
     */ 
    public char getCol() {
        return this.col;
    }

    /**
     * Zjisteni radku pozice.
     * 
     * @return Radek sloupce.
     */ 
    public int getRow() {
        return this.row;
    }
    
    /**
     * Vytvari retezec z objektu Position pro vypis
     */ 
    @Override
    public String toString() {
        return "" + col + "" + row;
    }

    /**
     * Uprava equals pro Position.
     * 
     * @param obj - Objekt instance Position
     * @return true/false
     */ 
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position p = (Position) obj;
            return ((p.getCol() == this.col && p.getRow() == this.row) ? true : false);
        }
        else {
            return false;
        }
    }

    /**
     * Uprava funkce HashCode
     */ 
    @Override
    public int hashCode() {
        return (this.col * 1000 + this.row);
    }
}
