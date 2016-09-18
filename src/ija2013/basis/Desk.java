/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Hraci deska
 */

package ija2013.basis;

// Import kvuli metode print
import ija2013.figures.Pawn;
import ija2013.figures.Queen;

import java.util.ArrayList;
import java.util.List;

/**
 * Hraci deska.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public class Desk {

    protected Position[][] desk; // jednotliva pole hraci desky
    protected int dim;           // rozmer desky

    /**
      * Kontruktor hraci plochy
      * 
      * @param d - velikost vytvarene plochy 
      */
    public Desk(int d) {
        // Kontrola zda neni dimenze zaporna nebude potreba. Hraci plocha bude
        // vzdy konstantni.
        desk = new Position[d][d];

        // naplneni pole pozicemi
        for (char col = 0; col < d; col++) {
            for (int row = 0; row < d; row++) {
                this.desk[col][row] = new Position(this, (char) (col + 'a'), row + 1);
            }
        }

        this.dim = d;
        initDesk(); //!!! init desk presunut
    }
    
    /**
     * Nalezeni pozic figurek s kterymi je mozno hrat. Pokud nektera figurka 
     * muze brat nepratelovu figurku jsou vraceny jen tahy kde tomu tak je.
     *
     * @param color Barva prave hrajiciho hrace
     * @return Seznam moznych pozic
     */
    public List<Position> getPlayablePositions(char color) {
        List<Position> takePosList = new ArrayList<>();
        List<Position> movePosList = new ArrayList<>();
        int retCode;
        Figure auxFig;

        // pruchod pres vsechny pole hraci desky (Nalezeni figurek)
        for (char col = 'a'; col < dim + 'a'; col++) {
            for (int row = 1; row <= dim; row++) {

                // Nachazi se na teto pozici figurka? a zadane barvy?
                if ((auxFig = getFigureAt(col, row)) != null && auxFig.color == color) {

                    // Ziskani moznych tahu figurky
                    retCode = auxFig.getTypeOfMoves();
                    
                    if (retCode == 0) // figurka nemuze tahnout
                        continue;
                    else if (retCode == 1){ // figurka muze preskocit nepritele
                        takePosList.add(auxFig.pos); 
                    }
                    else { // figurka nemuze odstrani nepritelovu figurku
                        movePosList.add(auxFig.pos);
                    }
                } 
            }
        }
        
        
        if(takePosList.isEmpty()) { // muze nektera figurka odstranit nepritele?
            return movePosList;
        }
        else {
            return takePosList;
        }
    }

    /**
     * Metoda vraci objekt pozice ze zadanych parametru. Pokud nejsou zadany
     * validni souradnice je vracem null.
     *
     * @param c Col (sloupec)
     * @param r Row (radek)
     * @return Objekt pozice pri uspechu jinak null
     */
    public Position getPositionAt(char c, int r) {
        if (inBordersSimple(c, r)) {
            return desk[c - 'a'][r - 1];
        }
        else {
            return null;
        }
    }

    /**
     * Test zda je na zadane pozici figurka.
     *
     * @param p Pozadovana pozice
     * @return true/false
     */
    public boolean isEmpty(Position p) {
        if (desk[p.getCol() - 'a'][p.getRow() - 1].getFigure() == null) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Vrati figurku na danych souradnicich. Pokud se na zadane pozici
     * nevyskytuje zadna figurka nebo jsou zadany souradnice mimo rozmery hraci
     * desky je vracen null.
     *
     * @param c Col (Sloupec)
     * @param r Row (radek)
     * @return Hledana figurka
     */
    public Figure getFigureAt(char c, int r) {
        if (inBordersSimple(c, r)) {
            return desk[c - 'a'][r - 1].getFigure();
        }
        else {
            return null;
        }
    }

    /**
     * Kontrola souradnic zda nejsou mimo hranice hraci desky.
     *
     * @param c Col testovane pozice (Sloupec)
     * @param r Row testovane pozice (Radek)
     * @return true/false
     */
    private boolean inBordersSimple(char c, int r) {
        // ord('`') == 96; ord('a') == 97
        c -= '`'; // zarovnani c
        if (r < 1 || r > dim || c < 1 || c > dim) {
            return false;
        }
        else {
            return true;
        }

    }

    /**
     * Temer identicka metoda jako inBordersSimple. Pro lepsi manipulaci tato
     * metoda pozaduje objekt pozice misto souradnic.
     *
     * @param p Testovana pozice
     * @return true/false
     */
    public boolean inBorders(Position p) {
        if (p == null) {
            return false;
        }
        
        int r = p.getRow();
        char c = p.getCol();

        return inBordersSimple(c, r);
    }

    /**
     * Odstraneni vsech figurek z hraci desky.
     */
    public void clear() {
        for (char col = 0; col < dim; col++) {
            for (int row = 0; row < dim; row++) {
                this.desk[col][row].removeFigure();
            }
        }
    }

    /**
     * Zjisteni velikosti hraci desky.
     * 
     * @return velikost hraci plochy
     */
    public int getDim() {
        return this.dim;
    }
    
   /**
     * Pocatecni rozestaveni figurek.
     */
    public final void initDesk() {
        // nove instance figurek neni potreba ukladat, protoze jsou ulozeny 
        // pri jejich kontrukci do zadanych pozic
        new Pawn(getPositionAt('a', 1),'w');
        new Pawn(getPositionAt('c', 1),'w');
        new Pawn(getPositionAt('e', 1),'w');
        new Pawn(getPositionAt('g', 1),'w');
        new Pawn(getPositionAt('b', 2),'w');
        new Pawn(getPositionAt('d', 2),'w');
        new Pawn(getPositionAt('f', 2),'w');
        new Pawn(getPositionAt('h', 2),'w');
        new Pawn(getPositionAt('a', 3),'w');
        new Pawn(getPositionAt('c', 3),'w');
        new Pawn(getPositionAt('e', 3),'w');
        new Pawn(getPositionAt('g', 3),'w');
        
        
        new Pawn(getPositionAt('b', 6),'b');
        new Pawn(getPositionAt('d', 6),'b');
        new Pawn(getPositionAt('f', 6),'b');
        new Pawn(getPositionAt('h', 6),'b');
        new Pawn(getPositionAt('a', 7),'b');
        new Pawn(getPositionAt('c', 7),'b');
        new Pawn(getPositionAt('e', 7),'b');
        new Pawn(getPositionAt('g', 7),'b');
        new Pawn(getPositionAt('b', 8),'b');
        new Pawn(getPositionAt('d', 8),'b');
        new Pawn(getPositionAt('f', 8),'b');
        new Pawn(getPositionAt('h', 8),'b');
    }
    
    /**
     * Tisk hraci desky na stdout.
     */
    public void print() {
        // naplneni pole pozicemi
        Figure fig;
        char figType = '-';

        
        System.out.println("+-----------------+");
        System.out.println("  A B C D E F G H  ");
        for (int row = (dim - 1); row >= 0; row--) {
            System.out.print((row + 1) + " ");
            for (char col = 0; col < dim; col++) {
                fig = this.desk[col][row].getFigure();

                if (fig == null) {
                    figType = '-';
                }
                else if (fig instanceof Pawn) {
                    if (fig.getColor() == 'w') {
                        figType = 'r';
                    }
                    else {
                        figType = 'R';
                    }
                }
                else if (fig instanceof Queen) {
                    if (fig.getColor() == 'w') {
                        figType = 'q';
                    }
                    else {
                        figType = 'Q';
                    }
                }

                System.out.print(figType);
                System.out.print(" ");
            }

            System.out.print(row+1);
            if (row == 2)     {System.out.print("  Q - black queen");}
            else if (row == 3){System.out.print("  q - white queen");}
            else if (row == 4){System.out.print("  R - black pawn");}
            else if (row == 5){System.out.print("  r - white pawn");}
            else if (row == 7){System.out.print("  Pr. format zadani tahu: a3 b6 c8 a6 g8 h1");}    
            
            System.out.println();
            
        }
        System.out.println("  A B C D E F G H  ");
        System.out.println("+-----------------+");
    }
}
