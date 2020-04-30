import javax.swing.*;
import java.awt.*;
import java.util.*;

class HighlightableRowsPanel extends JPanel{
    ArrayList<Integer> rows;
    private int rowHeight;
    private CSVGrid grid;

    public HighlightableRowsPanel(CSVGrid grid, int rowHeight){
        rows = new ArrayList<>();
        this.rowHeight = rowHeight;
        this.grid = grid;
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
            g.fillRect(0, (int) grid.getRowPosition(row), this.getWidth(), rowHeight);
        }

    }

}