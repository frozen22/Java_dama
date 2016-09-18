/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   RandomAI
 */

package ija2013.players;

import ija2013.game.Player;
import ija2013.game.Move;
import ija2013.basis.Position;

import java.util.Random;
import java.util.List;

/**
 * Umela inteligence s nahodnym vyberem tahu.
 * 
 * @author Frantisek Nemec
 */
public class RandomAI  extends Player {
    /**
     * Konsstruktor Umele inteligence
     * @param c - barva
     */
    public RandomAI(char c) {
        super(c);
    }
    
    /**
     * Ziskani tahu.
     * 
     * @return Tah hrace
     */
    @Override
    public Move getMove() {
        Random rand = new Random();
        int randNum;
        List<Position> posList;
        Position from;
        Position to;
        
        // ziskani pozic figurek s kterymi lze hrat
        posList = game.getDesk().getPlayablePositions(color);
        
        // nelze provest zadny tah
        if (posList.isEmpty()) {
            return null;
        }
        
        // jen jeden tah na vyber
        if (posList.size() == 1) {
            from = posList.get(0);
        }
        else { // nahodny vyber
            randNum = rand.nextInt(posList.size() - 1);
            from = posList.get(randNum);
        }
        
        // nalezeni moznych pohuby s figurkouy
        posList = from.getFigure().getPlayableMoves();
        
        // jen jeden pohyb na vyber
        if (posList.size() == 1) {
            to = posList.get(0);
        }
        else { // nahodny vyber
            randNum = rand.nextInt(posList.size()-1);
            to = posList.get(randNum);
        }
        
        try{
            Thread.sleep(200);
        }
        catch(Exception e) {}
        
        return new Move(from, to);
    }
    
    @Override
    public void remindPlayer(Move move) {}
}
