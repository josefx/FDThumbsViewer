/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package josefx.fdthumbs;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * TableCellRenderer for Icons.
 * Required for system look and feel when testet 
 * (failed with NPE when not provided).
 * 
 * @author josefx
 */
public class IconRenderer extends DefaultTableCellRenderer{
    private static final long serialVersionUID = 1L;
    @Override
    public void setValue(Object value) {
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setIcon((Icon)value);
    }

}
