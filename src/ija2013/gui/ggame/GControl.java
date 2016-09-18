/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Control
 */

package ija2013.gui.ggame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.List;
import ija2013.basis.Position;
import ija2013.basis.XMLGameSave;
import ija2013.game.Game;
import ija2013.players.LocalPlayer;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;


/**
 * Graficke rozhrani pro ovladani hry.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GControl extends JPanel  {
    public static final long serialVersionUID = 42424207L;
    
    protected GGameFrame gGame; // rodicovsky frame
    
    protected Game game; // hrajici hra
    private Thread gameLogicss; // vlakno ve kterem je hra spustena
    
    // komponenty
    private JTextArea textAreaMoves;
    private JLabel labelInfo;
    private JButton btnHighLight;
    private JButton btnSaveBasic;
    private JButton btnSaveXML;
    private JButton btnEnd;
    
    /**
     * Kontruktor. Ulozeni hry a framu ve kterem je this GControl zobrazen.
     * @param g
     * @param gg 
     */
    public GControl(Game g, GGameFrame gg) {
        game = g;
        gGame = gg;
        initComponents();
    }

    /**
     * Inicializace komponent.
     */
    private void initComponents() {
        labelInfo = new JLabel("Tahne: bily");
        textAreaMoves = new javax.swing.JTextArea();
        
        
        btnHighLight = new JButton("Hightlight");
        btnHighLight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // zvyrazneni figurek jen pokud hraje lokalni hrac tzn. pokud je 
                // na tahu AI nebo Online hrac figurky nebudou zvyrazneny
                if ((game.getFirstPlayer().getColor() == game.getPlayingPlayer() && game.getFirstPlayer() instanceof LocalPlayer)||
                   (game.getSecondPlayer().getColor() == game.getPlayingPlayer() && game.getSecondPlayer() instanceof LocalPlayer)){
                    List<Position> posList = game.getDesk().getPlayablePositions(game.getPlayingPlayer());
                    gGame.getGDesk().highlight(posList);
                }
            }
        });
        
        btnSaveBasic = new JButton("Save Basic");
        btnSaveBasic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Ulozeni zaznamu hry v zakladni notaci
                File file = getFile();
                if (file != null) {
                    String gameNotation = textAreaMoves.getText();

                    try {
                        FileWriter fstream = new FileWriter(file);
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(gameNotation);
                        out.close();
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(textAreaMoves, "Chyba pri zapisu do souboru.");
                    }
                }

            }
        });
        
        btnSaveXML = new JButton("Save XML");
        btnSaveXML.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Ulozeni zaznamu hry v xml formatu
                File file = getFile();
                if (file != null) {
                    try {
                        XMLGameSave xmlGS = new XMLGameSave(game.getMovesDone());
                        xmlGS.printFile(file);
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(textAreaMoves, "Chyba pri zapisu do souboru." + e.getMessage());
                    }
                }

            }
        });

        btnEnd = new JButton("Konec");
        btnEnd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                gGame.dispose();
//                gameLogicss.interrupt(); //?? tady bude problem
//                gameLogicss.stop();
            }
        });

        textAreaMoves.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaMoves);
        scrollPane.setPreferredSize(new Dimension(150,375));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(5, 1,2,2));
        btnPanel.add(btnHighLight);
        btnPanel.add(btnSaveBasic);
        btnPanel.add(btnSaveXML);
        btnPanel.add(btnEnd);
        btnPanel.add(labelInfo);

        
        add(scrollPane);
        add(btnPanel);
    }
    
    /**
     * Spusteni hry.
     */
    public void startGame() {
        gameLogicss = new Thread(new Runnable() {
            @Override
            public void run() {
                char color;
                color = game.start();
                if (color == 'w') {
                    JOptionPane.showMessageDialog(getParent(), "White won or enemy disconnected");
                }
                else {
                    JOptionPane.showMessageDialog(getParent(), "Black won or enemy disconnected");
                }
            }
        });
        
        gameLogicss.start();
    }
    
    /**
     * Zobrazeni dialogu pro vyber mista, kam se ma ulozit zaznam hry.
     * 
     * @return Vybrany soubor
     */
    public File getFile() {
        JFileChooser filesave = new JFileChooser("./examples/");

        int ret = filesave.showSaveDialog(this);

        if (ret == JFileChooser.APPROVE_OPTION) {
            return filesave.getSelectedFile();
        }

        return null;
    }

    /**
     * Vypis retezce do textoveho pole.
     * 
     * @param str Vypisovany retezec
     */
    public void printIntoTextArea(String str) {
        textAreaMoves.setText(textAreaMoves.getText() + str);  // tisk tahu
    }

    /**
     * Nastaveni informacniho labelu.
     * 
     * @param str Zobrazeny retezec
     */
    public void setLabelText(String str) {
        labelInfo.setText(str);
    }
    
    
   /**
     * Ziskani obsahu labelTextu. Pouziva se pri upozornovani na spatny tah.
     * 
     * @return Text labelTextu
     */
    public String getLabelText() {
        return labelInfo.getText();
    } 
}
