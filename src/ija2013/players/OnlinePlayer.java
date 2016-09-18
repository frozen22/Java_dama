/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   OnlinePlayer
 */

package ija2013.players;

import ija2013.basis.Position;
import ija2013.game.Move;
import ija2013.game.Player;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/** 
 * Online hrac.
 * 
 * @author Frantisek Nemec
 */
public class OnlinePlayer extends Player {
    private InputStream sockInput;
    private OutputStream sockOutput;
    
    /**
     * Konstruktor Online hrace
     * @param c - barva hrace
     * @param input - vstupni stream
     * @param output -vystupni stream 
     */
    public OnlinePlayer(char c,InputStream input,OutputStream output) {
        super(c);
        sockInput = input;
        sockOutput = output;
    }
    /**
     * Ziskani tahu.
     * 
     * @return Tah hrace
     */   
    @Override
    public Move getMove() {
        String strRead = null;
        byte[] buf = new byte[1024];
        int bytes_read;

        try {
            // cteni ze socketu
            bytes_read = sockInput.read(buf, 0, buf.length);
            if (bytes_read < 0) {
                return null;
            }
            
            // prevedeny tahu
            strRead = new String(buf, 0, bytes_read);
        }
        catch (IOException e) {
            System.out.println("NEPRITEL SE ODPOJIL");
            e.printStackTrace(System.err);
            return null;
        }

        return decodeStringToMove(strRead);
    }
    
    /**
     * Prevedeni tahu Move do textove podoby. 
     * 
     * @param move Konverovany tah
     * @return Retezec s prevedenym tahem
     */
    private String codeMoveToString(Move move) {
        return move.getFrom().toString()+move.getTo();
    }
    
    /**
     * Prevedeni tahu v textove podobe do tridy Move.
     * 
     * @param strMove Tah v zakladni notaci
     * @return Move
     */
    private Move decodeStringToMove(String strMove) {
                                                  // move example: a3b4
        String fromStr = strMove.substring(0, 2); // a3
        String toStr = strMove.substring(2, 4);   // b4
        
        Position from = game.getDesk().getPositionAt(fromStr.charAt(0), fromStr.charAt(1) - '0');
        Position to = game.getDesk().getPositionAt(toStr.charAt(0), toStr.charAt(1) - '0');
        
        return new Move(from, to);
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
    public void remindPlayer(Move move) {
        String s = codeMoveToString(move);
        byte[] data = s.getBytes();
        
        try {
            // Odeslani tahu oponentovy
            sockOutput.write(data, 0, data.length);
            sockOutput.flush();
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
    
}
