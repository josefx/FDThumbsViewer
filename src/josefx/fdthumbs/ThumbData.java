/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package josefx.fdthumbs;

/**
 *
 * @author josefx
 */
public class ThumbData {
    private String original = "";
    private String thumb = "";
    private String type = "";

    /**
     * @return the original
     */
    public String getOriginal() {
        return original;
    }

    /**
     * @return the thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param original the original to set
     */
    public void setOriginal(String original) {
        this.original = original;
    }

    /**
     * @param thumb the thumb to set
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
