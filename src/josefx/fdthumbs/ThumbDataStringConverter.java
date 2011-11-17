/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package josefx.fdthumbs;

import javax.swing.table.TableModel;
import javax.swing.table.TableStringConverter;

/**
 *
 * @author josefx
 */
public class ThumbDataStringConverter extends TableStringConverter{

    @Override
    public String toString(TableModel model, int row, int column) {
        ThumbTableModel mod = (ThumbTableModel)model;
        if(column == 0){
            return ((ThumbData)model.getValueAt(row, 0)).getOriginal();
        }
        else{
            final Object o =  model.getValueAt(row, column);
            if(o == null) return "";
            return o.toString();
        }
    }
    
}
