/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Move
 */

package ija2013.game;

import ija2013.basis.Position;
import ija2013.basis.Figure;

/**
 * Tah figurky.
 * 
 * @author Frantisek Nemec
 */
public class Move {
    protected Position fromPos; // Odkud
    protected Position toPos;   // Kam 
    protected Position takenPos;// Pozice odstranene figurky (Pokud nejaka byla)
    protected Figure takenFig;  // Odstranena figurka (Pokud nejaka byla)
    
    /**
     * Konstruktor naplneni prazdnymi hodnotami.
     */
    public Move () {
        fromPos = null;
        toPos = null;
        takenPos = null;
        takenFig = null;
    }
    
    /**
     * Kontruktor naplneni skoku bez odstraneni figurky.
     * 
     * @param fromP Odkud 
     * @param toP   Kam
     */
    public Move(Position fromP, Position toP) {
        fromPos = fromP;
        toPos = toP;
        takenPos = null;
        takenFig = null;
    }
    
    /**
     * Kontruktor naplneni skoku s odstraneni figurky.
     * @param fromP Odkud
     * @param toP Kam
     * @param takenP Odstraneni figurka
     */
    public Move(Position fromP, Position toP, Position takenP) {
        fromPos = fromP;
        toPos = toP;
        takenPos = takenP;
        takenFig = takenP.getFigure();
    }
    
    /**
     * Prevod tahu do zakladni notace.
     * 
     * @return Retezec v zakladni notaci.
     */
    public String moveToString(){
        String s = "";
        s += fromPos.toString();
        if (takenPos != null) {
            s += "x";
        }
        else {
            s += "-";
        }
        s += toPos.toString();
        
        return s;
    }
    
    /**
     * Nastavit odkud se figurka presouva
     * @param p Pozice odkud
     */    
    public void setFrom(Position p) {
        fromPos = p;
    }
    
    /**
     * Nastavit kam se figurka presouva
     * @param p Pozice kam
     */    
    public void setTo (Position p) {
        toPos = p;
    }

    /**
     * Nastavit pozice, kde byla odbrana figurka
     * @param p Pozice odebirane figurky
     */    
    public void setTaken (Position p) {
        takenPos = p;
    }

    /**
     * Nastavit odbranou figurku
     * @param f odebirana figurka
     */ 
    public void setTakenFig (Figure f) {
        takenFig = f;
    }

    /**
     * Ziska Pozici odkud se tahlo
     * @return Pozice odkud
     */    
    public Position getFrom(){
        return fromPos;
    }
    
    /**
     * Ziska Pozici kam se tahlo
     * @return Pozice kam
     */    
    public Position getTo(){
        return toPos;
    }
    
    /**
     * Zjisti, zda je nejaka pozice, kde se odebirala figurka
     * @return Pozice, kde byla odebrana figurka
     */    
    public Position getTaken(){
        return takenPos;
    }
    
    /**
     * Ziskani odkazu na odebranou figurku
     * @return odebirana figurka
     */    
    public Figure getTakenFig () {
        return takenFig;
    }
    
}
