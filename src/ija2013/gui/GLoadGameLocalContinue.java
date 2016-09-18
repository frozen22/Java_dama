/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   LoadGameLocalContinue
 */


package ija2013.gui;

/**
 * Pokracovani v ulozene hre pri lokalni hre.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GLoadGameLocalContinue extends GLoadGame {
    public static final long serialVersionUID = 42424215L;

    
     public String textAreaContent;
     protected GMenuFrame parFrame;
     
    public GLoadGameLocalContinue(GMenuFrame frame) {
        super(frame);
        parFrame = frame;
    }
    
    /**
     * Metoda, ktera je provedena pri stisknuti tlacitka "Pokracovat". Je nacten
     * obsah textoveho pole a je vytvorena hra, ktera provede tahy z textoveho
     * pole.
     */
    @Override
    public void btnContinueClicked() {
        textAreaContent = textAreaBasic.getText();
        parFrame.createGame(textAreaContent);
        
        parFrame.showMainMenu(this);
        parFrame.pack();
    }
}

