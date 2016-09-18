/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Graficke zobrazeni hraci desky
 */
 
package ija2013.gui.ggame;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ija2013.basis.Position;
import ija2013.basis.Figure;
import ija2013.basis.Desk;
import ija2013.game.Move;





/**
 * Graficke zobrazeni hraci desky. 
 */
public class GDesk extends JPanel {
    public static final long serialVersionUID = 42424208L;
    
    // stavy automatu
    private enum getMoveStates {
        waiting, // cekani na pozadavek o dalsi tah
        // je pozadovan tah z hraci plochy
        getFrom, // povoleni obsahu metody figClicked()
        getTo; // povoleni obsahu metody deskPanelClicked()
    }
    
    
    protected Desk desk; // Deska kterou zobrazujeme
    
    // Semafor urceny k pozastaveni metody getMove() dokud neni Move nacten
    private Semaphore semaphore; 
    private Move move; // Do teto promene se postupne konstruuje Move
    private char playerColor; // Vymenik barvy mezi getMove() a figClicked()
    private getMoveStates state;
    
    // Seznam zvyraznenych figurek. Pouzito pri znevyraznovani figurek.
    private List<GFigure> highlightedFigs;
    
    // komponenty gui
    private GRawDeskPanel gDesk;   
    
    GGameFrame gGameFrame;
    
    /**
     * Konstruktor. Ulozeni hraci desky, nastaveni semaforu a inicializace 
     * komponent, ulozeni framu ve kterem se deska nachazi.
     * 
     * @param desk Hraci deska. 
     * @param gameFrame Frame ve kterem se hraci deska nachazi
     */
    public GDesk(Desk desk, GGameFrame gameFrame) {
        this(desk);
        gGameFrame = gameFrame;
    }
    
    /**
     * Konstruktor. Ulozeni hraci desky, nastaveni semaforu a inicializace 
     * komponent.
     * 
     * @param desk Hraci deska. 
     */
    public GDesk(Desk desk) {
        semaphore = new Semaphore(1);
        this.desk = desk;
        highlightedFigs = new ArrayList<>();
        initComponents();
        initRefresh(); // pocatecni nacteny figurek na plochu
    }
    
  /**
     * Inicializace komponent na hraci plose.
     */
    private void initComponents() {
        gDesk = new GRawDeskPanel(); // hraci deska s pozadim

        gDesk.setLayout(new GridLayout(8, 8));
        gDesk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                deskClicked(evt);
            }
        });

        add(gDesk);
    }
    
    /**
     * Ziskani graficke figurky, podle pozice figurky.
     * 
     * @param p Pozice figurky.
     * @return Graficka figurka
     */
    public GFigure getGFigure(Position p) {
        Component comp = gDesk.getComponent(posToIndex(p));
        if (comp instanceof GFigure) {
            return (GFigure)comp;
        }
        else {
            return null;
        }
    }

    /**
     * Ziskani tahu z hraci plochy. Ceka se na oznaceni figurky a pole.
     * 
     * @param color Barva prave hrajiciho hrace
     * @return Tah figurky
     */
    public synchronized Move getMove(char color) {
        state = getMoveStates.getFrom;
        playerColor = color;
        try {
            // cekani na zpracovani tahu
            semaphore.acquire(); // uzamknuti semaforu a cekani na metodu 
            semaphore.acquire(); // deskClicked az dokonci zpracovani tahu
            semaphore.release(); // a odemkne semafor
            // semafor je odemcen az je tah nacten
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        
        state = getMoveStates.waiting;
        return move;
    }

    /**
     * Vytvoreni grafickeho zobrazeni figurky.
     * @param fig Zobrazena figuka
     * @return Graficka figurka
     */
    private GFigure makeGFigure(Figure fig) {
        final GFigure gFig = new GFigure(fig);
        
        // pridani eventu
        gFig.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                figClicked(gFig);
            }
        });
        
        return gFig;
    }
    
    /**
     * Zvyrazneni figurek.
     * 
     * @param posList Pozice figurek, ktere maji byt zvyrazneny.
     */
    public void highlight(List<Position> posList) {
        deHighLight();
        Position auxPos;
        GFigure gFig;
        for (Iterator<Position> i = posList.iterator(); i.hasNext();) {
            auxPos = i.next();
            gFig = getGFigure(auxPos);
            gFig.highLight();
            highlightedFigs.add(gFig);
        }
    }

    /**
     * Znevyrazneni figurek. Seznam figurek je ulozen v seznamu highlightedFigs.
     */
    public void deHighLight() {
        GFigure gFig;
        if (!highlightedFigs.isEmpty()) {
            for (Iterator<GFigure> i = highlightedFigs.iterator(); i.hasNext();) {
                gFig = i.next();
                gFig.deHighLight();
            }
        }
        highlightedFigs.clear();
    }
    
    
    /**
     * Prevod pozice Position na index komponenty na gui.
     * 
     * @param p Pozice
     * @return Index komponenty
     */
    public int posToIndex(Position p) {
        int dim = p.getDesk().getDim();
        return ((p.getCol() - ('a')) + (dim - p.getRow()) * dim);
        
    }    
    
    /**
     * Metoda prekresli hraci plochu podle zadaneho tahu. Metoda prekresluje 
     * jen pozice, ktere jsou obsazeny v parametru Move m
     * 
     * @param m Tah ktery byl proveden
     */
    public void refreshMove(Move m) {
        deHighLight();
        // upraveni policka from
        removeGFigure(m.getFrom());
        
        // upraveni policka to
        putGFigure(m.getTo().getFigure(), m.getTo());
        
        // upraveni policka taken
        if (m.getTaken() != null) {
            removeGFigure(m.getTaken());
        }
        
        gDesk.validate();
        gDesk.repaint();
    }
    
    /**
     * Zruseni tahu.
     * 
     * @param m Ruseny tah
     */
    public void undoMove(Move m) {
        putGFigure(m.getTo().getFigure(), m.getFrom());
        removeGFigure(m.getTo());
        
        if (m.getTaken() != null) {
            putGFigure( m.getTakenFig(), m.getTaken());
        }
        
        gDesk.validate();
        gDesk.repaint();
    }
    
    /**
     * Vlozeni graficke figurky na danou pozici
     * 
     * @param fig Figurka
     * @param pos Pozice 
     */
    public void putGFigure(Figure fig, Position pos){
        int index = posToIndex(pos);
        gDesk.remove(index);
        gDesk.add(makeGFigure(fig), index);
    }

    /**
     * Odstraneni figurky z grafickeho hraciho pole.
     * 
     * @param pos Pozice figurky
     */
    public void removeGFigure(Position pos) {
        int index = posToIndex(pos);
        gDesk.remove(index);
        gDesk.add(new JLabel(), index);
    }

    
    /**
     * Pocatecni zobrazeni figurek na hraci plose.
     */
    public final void initRefresh() {
        Figure auxFig;
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if ((auxFig = desk.getFigureAt((char) (j + 'a'), i + 1)) != null) {
                    // vytvoreni a umisteni figurky
                    gDesk.add(makeGFigure(auxFig)); 
                }
                else { // prazdne pole
                    gDesk.add(new JLabel());
                }
            }
        }
        gDesk.validate();
        gDesk.repaint();
    }
    
    public void clearGDesk(){
        gDesk.removeAll();
        gDesk.validate();
    }

    /**
     * Metoda spustena pri kliknuti na figurku. Obsahuje cast ziskani tahu 
     * pokud je pozadovan tah od hrace
     * 
     * @param gFig Figurka na kterou bylo kliknuto
     */
    private void figClicked(GFigure gFig) {
        // zpracovani from tahu
        if (state == getMoveStates.getFrom || state == getMoveStates.getTo) {
            deHighLight(); 
            highlightedFigs.add(gFig);
            // Ziskani validnich pozic s kterymi lze po manipulovat
            List<Position> validFromPos = desk.getPlayablePositions(playerColor);
            move = new Move(); 

            // nelze provest dalsi tah
            if (validFromPos.isEmpty()) { 
                move = null;
                semaphore.release();
                return;
            }
            
            // Nevalidni tah
            if (!validFromPos.contains(gFig.fig.getPosition())) {
                noticeWrongMove();
                return;
            }

            // ulozeni pozice
            move.setFrom(gFig.fig.getPosition());
            // prechod do stavu ziskavani ciloveho policka
            state = getMoveStates.getTo;
            gFig.highLight();
        }
    }

    private void noticeWrongMove() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String origText = gGameFrame.getLT();
                gGameFrame.setLT("CHYBNY TAH!");
                try {
                    Thread.sleep(1000);
                } 
                catch(InterruptedException e) {
                    return;
                }
                
                if (gGameFrame.getLT().equals("CHYBNY TAH!")) {
                    gGameFrame.setLT(origText);
                }
            }
        }).start();
    }
    
    
    /**
     * Kliknuti na hraci desku. Obsahuje cast zpracovani tahu resp. zpracovani
     * ciloveho policka tahu.
     * 
     * @param evt 
     */
    private void deskClicked(java.awt.event.MouseEvent evt) {

        if (state == getMoveStates.getTo) {
            
            // ziskani ciloveho policka tahu
            // Pomocne promenne
            int x, y;
            char col;
            int row;
            // graficka velikost jednoho policka
            int posSize = evt.getComponent().getHeight() / 8;

            // prevod souradnic klidnuti na Position
            x = evt.getX();
            y = evt.getComponent().getHeight() - evt.getY();

            // samotny prevod
            col = (char) ('a' + x / posSize);
            row = 1 + y / posSize;

            // seznam validnic pozic, kam lze s figurkou tahnout
            List<Position> validFromPos = move.getFrom().getFigure().getPlayableMoves();

            // nevalidni tah
            if (!validFromPos.contains(desk.getPositionAt(col, row))) {
                noticeWrongMove();
                return;
            }

            // ulozeni ciloveho policka
            move.setTo(desk.getPositionAt(col, row));
            
            // ukonceni nacitani tahu
            semaphore.release(); // odemknuti zamku v metode getMove()
        }

    }
}
