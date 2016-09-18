/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   LocalPlayer
 */

package ija2013.players;

import ija2013.game.Move;
import ija2013.game.Player;

/**
 * Lokalni hrac. Tahy jsou nacitany z grafickeho rozhrani
 * 
 * @author Frantisek Nemec
 */
public class LocalPlayer extends Player  {

  /**
   * Konstruktor Lokalniho hrace
   * @param c - barva hrace
   */    
    public LocalPlayer(char c) {
        super(c);
    }
    
    /**
     * Ziskani tahu. Tah je ziskavan z grafickeho rozhrani.
     * @return Tah hrace
     */
    @Override
    public Move getMove() {
        //!!! pridano po prepsani do CPP
        
        // Nelze provest zadny tah
        if (game.getDesk().getPlayablePositions(color).isEmpty()){
            return null;
        }
        
        // pozadavek o tah, grafickemu rozhrani
        Move move = game.getGGame().getGDesk().getMove(color);
        return move;
    }
    
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
    @Override
    public void remindPlayer(Move move) {}
}
