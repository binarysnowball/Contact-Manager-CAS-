import javax.swing.*;
import java.awt.*;
import java.util.*;

class HighlightableRowsPanel extends JPanel{
    private ArrayList<Integer> rows;
    private int rowHeight;

    public HighlightableRowsPanel(int rowHeight){
        rows = new ArrayList<>();
        this.rowHeight = rowHeight;
    }

    public void highlightRow(int row){
        rows.add(row);
        this.paintImmediately(0, row * rowHeight, this.getWidth(), rowHeight);
    }

    public void unhighlightRow(int row){
        rows.remove((Integer) row);
        this.paintImmediately(0, row * rowHeight, this.getWidth(), rowHeight);
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new java.awt.Color(0, 51, 153));
        for(int row : rows){
            g.fillRect(0, row * rowHeight, this.getWidth(), rowHeight);
        }

    }

}