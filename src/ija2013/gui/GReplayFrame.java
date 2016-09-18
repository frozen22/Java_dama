/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Replay frame
 */

package ija2013.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import ija2013.gui.ggame.GDesk;
import ija2013.game.ReplayGame;
import java.awt.Toolkit;


/**
 * Frame spravujici prehravani ulozene hry. Pri zobrazeni obsahuje panel nacteni
 * ulozene hry a po stisku tlacitka "Pokracovat" je zobrazena hraci plocha
 * s tlacitky pro ovladani prehravani.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GReplayFrame extends JFrame {
    public static final long serialVersionUID = 42424206L;
    
    // Prehravana hra
    protected ReplayGame rGame;
    private GDesk gDesk;
    
    private JTextArea textAreaMoves;
    private JPanel controlPanel;
    private JButton btnNext;
    private JButton btnUndo;
    private JButton btnGoTo;
    private JButton btnEnd;
    private JButton btnStop;
    private JTextField textMoveNum;
    private JTextField textAutoPlayInterval;
    private GLoadGame loadGamePanel;
    
    /**
     * Kontruktora. Nastaveni atributu a komponent okna.
     */
    public GReplayFrame() {
        // nastaveni atributu okna
        setTitle("Prehrani hry");
        setLayout(new FlowLayout());
        setResizable(false);
        
        initComponents();
        
        pack();
        
    }
    
    /**
     * Inicializace komponent na hraci plose.
     */
    private void initComponents() {
        final Component thisComp = this;
        controlPanel = new JPanel();
        textMoveNum = new JTextField("0");
        textAutoPlayInterval = new JTextField("500");
        textAreaMoves = new javax.swing.JTextArea();
        
        btnNext = new JButton("Next");
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (rGame.replayNextMove() == 2) { // neplatny tah
                    illegalMoveShowMsg();
                }
            }
        });

        btnUndo = new JButton("Undo");
        btnUndo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rGame.replayUndoMove();
            }
        });

        btnGoTo = new JButton("Go To");
        btnGoTo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                // Delat vlakno na automaticke prehravani nebyla vhodna volba
                // mnohem lepsi by bylo pouzit timer, avsak cas je neuprosny
                // a je potreba ladit jine veci a fungujici veci ponechat 
                // i presto ze jsou nevhodne napsane.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int moveNumber;
                        try {
                            moveNumber = Integer.parseInt(textMoveNum.getText());
                            if (moveNumber < 0 || moveNumber > rGame.getMaxMoveNum()) {
                                throw new NumberFormatException();
                            }
                        }
                        catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(thisComp, "Spatne zadane cislo tahu!");
                            return;
                        }


                        int inter = 0;
                        if (!textAutoPlayInterval.getText().equals("")) {
                            try {
                                inter = Integer.parseInt(textAutoPlayInterval.getText());
                                if (inter < 0) {
                                    throw new NumberFormatException();
                                }
                            }
                            catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(thisComp, "Spatne zadany interval prehravani!");
                                return;
                            }
                        }
                        setEnableControlComponents(false);
                        rGame.goToMove(moveNumber, inter);
                        setEnableControlComponents(true);
                    }
                }).start();
            }
        });
        
        btnStop = new JButton("Stop");
        btnStop.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rGame.stopGoTo();
                setEnableControlComponents(true);
            }
        });
        
        btnEnd = new JButton("Konec");
        btnEnd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
            }
        });
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(4, 2,1,1));
        
        bottomPanel.add(btnUndo);
        bottomPanel.add(btnNext);
        bottomPanel.add(new JLabel("Interval (ms):"));
        bottomPanel.add(textAutoPlayInterval);
        bottomPanel.add(btnGoTo);
        bottomPanel.add(textMoveNum);
        bottomPanel.add(btnStop);
        bottomPanel.add(btnEnd);

        
        textAreaMoves.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaMoves);
        scrollPane.setPreferredSize(new Dimension(180,370));
        

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(scrollPane);
        controlPanel.add(bottomPanel);
                
        // pocatecni zobrazeni nacteni panelu pro nacteni hry
        loadGamePanel = new GLoadGameReplay(this);
        add(loadGamePanel);
        setLocation(100, 100);
    }
    
    /**
     * Zobrazeni diaologu oznamujici provedeni neplatneho tahu.
     */
    public void illegalMoveShowMsg() {
        JOptionPane.showMessageDialog(getParent(), "Neplatny tah");
    }
    
    /**
     * Zapoceti prehravani hry.
     * 
     * @param replayGame Hra kterou chceme zacit.
     */
    public void startReplayRGame(ReplayGame replayGame) {
        this.rGame = replayGame;
        gDesk = new GDesk(rGame.getDesk());
        showDesk();
        center();
    }
    
    /**
     * Odstraneni loadFile panelu a zobrazeni hraci desky s ovladanim.
     */
    public void showDesk(){
        remove(loadGamePanel);
        add(gDesk);
        add(controlPanel);
        gDesk.setVisible(true);
        controlPanel.setVisible(true);
        pack();
    }

    /**
     * Tisk stringu do textoveho pole.
     * 
     * @param str Retezec ktery pozdujeme vytisknout.
     */
    public void printStr(String str){
        textAreaMoves.setText(textAreaMoves.getText() + str);
    }
    
    /**
     * Ziskani graficke desky.
     * 
     * @return Graficka deska.
     */
    public GDesk getGDesk(){
        return gDesk;
    }

    /**
     * Povoleni/zakazani pouvani tlacitket pri automatickem prehravani.
     * 
     * @param enable True/False
     */
    private void setEnableControlComponents(boolean enable) {
        btnGoTo.setEnabled(enable);
        btnNext.setEnabled(enable);
        btnUndo.setEnabled(enable);
        textMoveNum.setEnabled(enable);
        textAutoPlayInterval.setEnabled(enable);
    }
    
    /**
     * Umisteni okna do prostred obrazovky.
     */
    public final void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ziskani rozmeru obrazovky
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2); // posunuti okna
    }

}
