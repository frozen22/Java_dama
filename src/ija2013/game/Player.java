/**
 * Predmet: Seminar Javy (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Abstraktni trida hrace
 */

package ija2013.game;

/**
 * Player.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public abstract class Player {
    protected Game game; // hra ve ktere hrac particupuje
    protected char color; // barva hrace
    
    /**
     * Konstruktor Hrace
     * @param c  - barva hrace
     */
    public Player(char c) {
        color = c;
    }
    
    /**
     * Pripojeni hrace ke hre
     * @param g Hra ke ktere je hrac pripojen.
     * @return this
     */
    public Player initPlayer(Game g) {
        game = g;
        return this;
    }
    
    /**
     * Ziskani tahu.
     * 
     * @return Tah hrace
     */
    public abstract Move getMove();
    
    /**
     * Odeslani zpravy o provedeni tahu online hraci. Metoda slouzi k odeslani 
     * tahu online hraci, ktery jej provede na sve desce. Implementace je 
     * obsazena jen u tridy onlinePlayer. U ostatnich typu hracu je metoda
     * prazdna.
     * 
     * Neni to nejlepsi reseni oznamovani tahy online hraci, ale nejjednoduseji
     * implementovatelne.
     * 
     * @param move Tah ktery chceme odeslat.
     */
    public abstract void remindPlayer(Move move);
    
    /**
     * Ziskani barvy hrace
     * @return barva hrace
     */    
    public char getColor() {
        return color;
    }
}
