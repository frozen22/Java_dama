/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   LoadGameOnlineContinue
 */

package ija2013.gui.onlinegame;

import ija2013.game.Game;
import ija2013.game.Moves;
import ija2013.gui.GLoadGame;
import ija2013.players.LocalPlayer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ija2013.gui.GMenuFrame;

/**
 * Pokracovani v ulozene hre pri online hre.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GLoadGameOnlineContinue extends GLoadGame {
    public static final long serialVersionUID = 42424217L;  
    
    public String textAreaContent;
    protected GMenuFrame parFrame;

    private JLabel infoLabel;
     
    public GLoadGameOnlineContinue(GMenuFrame frame) {
        super(frame);
        parFrame = frame;
        infoLabel = new JLabel("Load saved game");
        add(infoLabel);
    }
    
    @Override
    public void btnContinueClicked() {
        textAreaContent = textAreaBasic.getText();

        Moves moves = new Moves();
        Game testGame = new Game(new LocalPlayer('w'), new LocalPlayer('b'));
        infoLabel.setText("Testing moves");
        moves.setDesk(testGame.getDesk());
        moves.loadWithBasic(textAreaContent);
        if (testGame.makeMoves(moves) == false) {
            JOptionPane.showMessageDialog(parFrame, "Zadana hra obsahuje neplatny tah");
            return;
        }
        infoLabel.setText("Testing completed");

        char hisColor = parFrame.getColorNotChosen();

        infoLabel.setText("Sending game metadata");
        
        // odeslani metaDat oponentovy
        parFrame.client.send(""+hisColor+"c"+textAreaContent.length());
        // odeslani ulozene hry oponentovy
        infoLabel.setText("Sending savedgame");
        parFrame.client.send(textAreaContent);
        
        
        infoLabel.setText("Waiting for ACK");
        
        String revMsg;
        revMsg = parFrame.client.read();
        if (!revMsg.equals("ACK")) {
            infoLabel.setText("Expected ACK, received something i dont understand");
            return;
        }
        
        infoLabel.setText("Sending done (ACK received). Creating game");
        parFrame.createGame(textAreaContent);
        
        parFrame.showMainMenu(this);
        parFrame.pack();

    }
}

