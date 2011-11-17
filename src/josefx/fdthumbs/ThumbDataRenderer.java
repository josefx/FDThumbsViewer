/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package josefx.fdthumbs;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;
import java.util.HashSet;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author josefx
 */
public class ThumbDataRenderer implements TableCellRenderer,TableCellEditor{
    private final ThumbDataRenderComponent comp = new ThumbDataRenderComponent();
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean hasFocus, int irow, int i1col) {
        comp.setBackground(isSelected?jtable.getSelectionBackground():jtable.getBackground());
        
        final ThumbData td = (ThumbData)o;
        comp.setFilePath(td.getOriginal());
        comp.setThumbPath(td.getThumb());
        return comp;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comp.setBackground(table.getSelectionBackground());
        comp.setOpaque(true);
        final ThumbData td = (ThumbData)value;
        comp.setFilePath(td.getOriginal());
        comp.setThumbPath(td.getThumb());
        return comp;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {}

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listeners.add(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listeners.remove(l);
    }
    private final HashSet<CellEditorListener> listeners = new HashSet<CellEditorListener>();
    
}
