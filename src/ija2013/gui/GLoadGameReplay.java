/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   LoadGameReplay
 */

package ija2013.gui;

import ija2013.game.ReplayGame;


/**
 * Nacteni ulozene hry pro ucely jeho prehrani.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GLoadGameReplay extends GLoadGame {
    public static final long serialVersionUID = 42424216L;
    
    private GReplayFrame rFrame;
     
    public GLoadGameReplay(GReplayFrame replayFrame) {
        super(replayFrame);
        rFrame = replayFrame;
    }
    
    /**
     * Metoda, ktera je provedena pri stisknuti tlacitka "Pokracovat". Nacteni 
     * obsahu textoveho pole a nasledne vytvoreni Replay Simulace hry.
     */
    @Override
    public void btnContinueClicked() {
        ReplayGame rGame;
        String str = textAreaBasic.getText();

        // vytvoreni hry
        rGame = new ReplayGame(str);
        rGame.setRepFrame(rFrame);
        rFrame.startReplayRGame(rGame);
        rGame.printAllMoves();
	rFrame.pack();
    }
}
