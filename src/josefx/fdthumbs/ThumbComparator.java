/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package josefx.fdthumbs;

import java.util.Comparator;

/**
 * Compares ThumbData based on the original file path.
 * @author josefx
 */
public class ThumbComparator implements Comparator<ThumbData> {

    @Override
    public int compare(ThumbData o1, ThumbData o2) {
        return o1.getOriginal().compareTo(o2.getOriginal());
    }
    
}
