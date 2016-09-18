/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Moves
 */

package ija2013.game;

import ija2013.basis.Position;
import ija2013.basis.Desk;
import java.util.ArrayList;
import java.util.List;


/**
 * Soubor obsahujici vyuzivane informace k ulozeni tahu hry.
 * 
 * @author Frantisek Nemec
 */
public class Moves {

    protected List<Move> moveList;        // Seznam tahu
    protected Desk desk;                  // Deska na ktere byli tahy provedeny
    protected int maxMoveNum;             // Pocet tahu 
    protected String movesInBasicNotation;//retezec reprezentujici zakladni notaci tahu
    private int moveNum;                  //identifikator soucasneho pohybu

    /**
     * Inicializacni konstruktor
     */
    public Moves() {
        moveList = new ArrayList<>();
        moveNum = 0;
        maxMoveNum = 0;
    }

    /**
     * konstruktor nastavujici seznam tahu k instanci hraci desky
     */
    public Moves(Desk desk) {
        this.desk = desk;
        moveList = new ArrayList<>();
        moveNum = 0;
        maxMoveNum = 0;
    }
    

    /**
     * Naplneni seznamu z retezce tahu v zakladni notaci.
     * 
     * @param str Retezec ktery obsahuje zaznam v zakladni notaci.
     */
    public void loadWithBasic(String str) {
        Position from, to;
        String sub;
        
        movesInBasicNotation = ""+str; // Ulozeni retezce
        maxMoveNum = 0;
        
        // Upraveni retezce
        str = str.replaceAll("\r\n", "\n");
        str = str.replaceAll("\t", "");
        str = str.replaceAll("\n", "");
        str = str.replaceAll(" +", "");

        // Dokud je co zpracovavat
        while (str.length() > 6) { // a2-b3

            // odstraneni cisla tahu
            str = str.substring(str.indexOf(".") + 1);

            // ziskani tahu 
            sub = str.substring(0, 5);
            // zpracovani tahu
            from = desk.getPositionAt(sub.charAt(0), sub.charAt(1) - '0');
            to = desk.getPositionAt(sub.charAt(3), sub.charAt(4) - '0');
            
            addMove(new Move(from, to));
            maxMoveNum++;
            
            // odstraneni tahu, ktery byl prave zpracovan z retezce
            str = str.substring(5);

            
            if (str.length() < 5) { // byl zadan jen move z turnu
                return;
            }

            //zpracovani dalsiho tahu v turnu
            sub = str.substring(0, 5);
            from = desk.getPositionAt(sub.charAt(0), sub.charAt(1) - '0');
            to = desk.getPositionAt(sub.charAt(3), sub.charAt(4) - '0');
            addMove(new Move(from, to));
            maxMoveNum++;
            str = str.substring(5);
        }

    }

    /**
     * Nastaveni citace na nulu. Ziskavani tahu bude od prvniho tahu.
     */
    public void resetNum(){
        moveNum = 0;
    }
    
    /**
     * Pridani tahu do seznamu.
     * 
     * @param move Pridany hrat
     */
    public void addMove(Move move){
        moveList.add(move);
    }
    
    /**
     * Ziskani tahu pred posledne ziskanym.
     * 
     * @return Predposledne ziskany tah. Null - pokud jsme na zacatku seznamu.
     */
    public Move getPrevMove() {
        Move move;
        
        if (moveNum > 0) {
            moveNum--;
            move = moveList.get(moveNum);
            return move;
        }
        else { // Cteni pred zacatek seznamu
            return null;
        }
    }
    
    /**
     * Ziskani nasledujiciho tahu.
     * 
     * @return Dalsi tah v seznamu. Null - pokud jsme na konci seznamu
     */
    public Move getNextMove() {
        Move move;
        if (moveNum < moveList.size()) {
            move = moveList.get(moveNum);
            moveNum++;
            return move;
        }
        else { // Cteni za koncem seznamu
            return null;
        }
    }

    /**
     * Ziskani poradoveho cisla aktualniho tahu
     * @return Cisla aktualniho tahu
     */    
    public int getActualMoveNum() {
        return moveNum;
    }

    /**
     * Ziskani maximalniho poctu tahu
     * @return Maximalni pocet tahu
     */       
    public int getMaxMoveNum() {
        return maxMoveNum;
    }
    
    /**
     * Ziskani zakladni notace seznamu tahu
     * @return Retezec zakladni notace
     */     
    public String getMovesInBasicNotation() {
        return movesInBasicNotation;
    }

    /**
     * Prirazeni desky k seznamu
     * @param desk Prirazovana deska
     */    
    public void setDesk(Desk desk) {
        this.desk = desk;
    }
}
