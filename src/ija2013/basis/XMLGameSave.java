/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   XMLGameSave
 */
package ija2013.basis;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.SAXReader;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import java.util.Iterator;
import java.io.*;

import ija2013.game.Moves;
import ija2013.game.Move;


/* Ukazka XML formatu hry
 <?xml version="1.0" encoding="UTF-8"?> 
 <game> 
   <move number=1 color="w"> 
     <from>d3</from>
     <to>d4</to>
     <taken>d5</taken>
   </move> 
 </game>
 */

/**
 * XMLGameSave.
 *
 * @author Frantisek Nemec (xnemec61)
 */
public class XMLGameSave {
    
    protected Document doc;
    private int maxNum;

    
    /**
     * Vytvori prazdnou strukturu s elementem game.
     */
    public XMLGameSave() {
        doc = DocumentHelper.createDocument();
        doc.addElement("game"); // root
        maxNum = 1;
    }

    /**
     * Vytvori strukturu s elementem game a tahy Moves.
     * 
     * @param moves Tahy, kterymi bude dokument naplnen.
     */
    public XMLGameSave(Moves moves) {
        doc = DocumentHelper.createDocument();
        doc.addElement("game"); // root
        maxNum = 1;
        String from, to, taken;
        Move move;
        char color = 'w';
        while ((move = moves.getNextMove()) != null) {
            from = move.getFrom().toString();
            to = move.getTo().toString();
            
            if (move.getTaken() != null) {
                taken = move.getTaken().toString();
            }
            else {
                taken = "";
            }
            
            
            addMove(color, from, to, taken);
            if (color == 'w') { color = 'b'; } else { color = 'w'; }
        }
    }
    
    /**
     * Prevod XML formatu do zakladni notace.
     * 
     * @return Retezec v zakladni notaci.
     */
    public String getBasic() {
        if (doc == null) {
            return null;
        }
        
        Element root = doc.getRootElement();
        String movesStr = "";
        int turnCount = 1;
        int i = 1;
        
        for (Iterator<Element> it = root.elementIterator(); it.hasNext(); it.next()) {
            if (i % 2 == 1) {
                movesStr += "" + turnCount + ". " + getMove(i) + " ";
            }
            else {
                movesStr += getMove(i) + "\n";
                turnCount++;
            }
            i++;
        }
        return movesStr;
    }
    
   /**
    * Vytvoreni XML stuktury z retezce, ktery obsahuje obsah souboru v XML
    * formatu.
    * 
    * @param fileContent Obsah souboru
    */
    public XMLGameSave(String fileContent) {
        try{
            doc = DocumentHelper.parseText(fileContent);
        }
        catch(Exception e) {
            doc = null;
        }
    }

    /**
     * Vraceni n-teho tahu hry.
     * 
     * @param num Oznaceni pozadopvaneho tahu
     * @return Retezec dat z pozadovaneho tahu pro dalsi zpracovani
     */
    public String getMove(int num) {
        String from = "";
        String to = "";
        String taken = "";

        Element root = doc.getRootElement();
        Element moveEle;
        Element innerEle;

        for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
            moveEle = it.next();
            if (moveEle.attribute("number").getText().compareTo((""+num)) == 0) {
                Iterator<Element> j = moveEle.elementIterator();
                
                // load from
                innerEle = j.next();
                from = innerEle.getText();
                
                // load to
                innerEle = j.next();
                to = innerEle.getText();
                
                // load taken
                innerEle = j.next();
                taken = innerEle.getText();
            }
        }

        if (taken.equals("")) {
           return from+"-"+to; 
        }
        else {
            return from+"x"+to; 
        }

    }
    
    /**
     * Prida do XML interni struktury udaj o dalsim tahu
     * 
     * @param color Oznaceni hrace ktery tah vykonal
     * @param from Pozice, z ktere se figurka premistila
     * @param to Pozice, na kterou se figurka premistila
     * @param taken Oznaceni druhu tahu("x" - skok, "-" - obycejny pohyb)
     */
    public final void addMove(char color, String from, String to, String taken) {
        Element root = doc.getRootElement();
        Element moveElement = root.addElement("move");
        moveElement.addAttribute("number", "" + maxNum);
        moveElement.addAttribute("color", "" + color);
        Element fromElement = moveElement.addElement("from");
        fromElement.setText(from);
        Element toElement = moveElement.addElement("to");
        toElement.setText(to);
        Element takenElement = moveElement.addElement("taken");
        takenElement.setText(taken);
        maxNum++;
    }

    /**
     * Provadi tisk do vystupniho souboru.
     * 
     * @param file Vystupni soubor
     */
    public void printFile(File file) {
        try {
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(strGetPrettyFormat());
            out.close();
        }
        catch (Exception e) {
        }
    }
    
    /**
     * Pomocna metoda k obsluze ziskani uhledneho xml.
     * 
     * @return Zformatovane xml
     */
    public String strGetPrettyFormat() {
        return prettyFormat(doc.asXML(),2);
    }
    
    /**
     * Vytvori prijatelny vystupni format
     * 
     * @param input Nezpracovany vstupni retezec
     * @param indent Velikost odsazeny
     * @return Upraveny vstup
     */
    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }
}