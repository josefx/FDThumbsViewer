/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package josefx.fdthumbs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * ThumbTableModel.
 * 
 * @author josefx
 */
public class ThumbTableModel implements TableModel {
    /**
     * Position of the column containing image previews.
     */
    private static final int PREVIEW_COLUMN = 1;
    /**
     * Listeners registered with this model.
     */
    private final Set<TableModelListener> listen = new HashSet<TableModelListener>();
    /**
     * Names of the columns.
     */
    private final List<String> colNames = Arrays.asList("Files","Preview");
    /**
     * The model with paths to the thumbnails and images
     */
    private final List<ThumbData> tdata = new ArrayList<ThumbData>(20000);
    /**
     * On demand management for preview images.
     */
    private final ThumbSafe ts = new ThumbSafe(this);
    
    @Override
    public void addTableModelListener(TableModelListener l) {
        listen.add(l);
    }
    /**
     * Method for the ThumbSafe to indicate an update to the cached thumbnail.
     */
    void thumbDataChanged(String th){
        for (int i = 0; i < getRowCount(); ++i) {
            if (getThumbData(i).getThumb().equals(th)) {
                for (TableModelListener tml : listen) {
                    tml.tableChanged(new TableModelEvent(this, i, i, PREVIEW_COLUMN, TableModelEvent.UPDATE));
                }
                break;
            }
        }
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex != PREVIEW_COLUMN? ThumbData.class: Icon.class;
    }

    @Override
    public int getColumnCount() {
        return colNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colNames.get(columnIndex);
    }

    @Override
    public int getRowCount() {
        return tdata.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ThumbData td = tdata.get(rowIndex);
        switch(columnIndex){
            case 0: return td;
            case PREVIEW_COLUMN: return ts.genPreview(rowIndex);
        }
        return "";
    }
    public void add(ThumbData td){
        assert td != null;
        tdata.add(td);
        for(TableModelListener tl: listen){
            tl.tableChanged(new TableModelEvent(this, tdata.size()-1, tdata.size()-1, TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT));

        }
    }
    /**
     * Removes all content from the model.
     */
    public void clear(){
        int tsize = tdata.size();
        if(tsize == 0)return;
        tdata.clear();
        for(TableModelListener tl: listen){
            tl.tableChanged(new TableModelEvent(this, 0, tsize-1,TableModelEvent.ALL_COLUMNS,TableModelEvent.DELETE));
        }
    }
    /**
     * Overwritten to enable Celleditor for column 0.
     * 
     * @param rowIndex
     * @param columnIndex
     * @return 
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listen.remove(l);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    ThumbData getThumbData(int item) {
       return this.tdata.get(item);
    }

}
