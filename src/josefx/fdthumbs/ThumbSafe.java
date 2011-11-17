package josefx.fdthumbs;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import javax.swing.plaf.metal.MetalIconFactory.FileIcon16;

/**
 * This class is a cache for thumbnails. It is based on Weak and Soft references
 * to allow collection of unused Images.
 * @author josefx
 */
class ThumbSafe {

    Map<String, SoftReference<Icon>> icons = new WeakHashMap<String, SoftReference<Icon>>();
    ThumbTableModel outer;

    ThumbSafe(ThumbTableModel outer) {
        this.outer = outer;
    }
    static final Icon tempIcon= new FileIcon16();
    /**
     * This method returns a preview of the thumbnail if cached otherwise it
     * enqueques a request for the thumb and returns null.
     * @param rowIndex
     * @return 
     */
    public Icon genPreview(int rowIndex) {
        final ThumbData td = outer.getThumbData(rowIndex);
        final SoftReference<Icon> i = icons.get(td.getThumb());
        final Icon ic;
        if (i != null) {
            ic = i.get();
            if (ic == null) {
                icons.remove(td.getThumb());
            }
        } else {
            ic = null;
        }
        
        if (ic != null) {
            return ic;
        } else {
            requestIconData(td.getThumb());
            return tempIcon;
        }
    }
    final HashSet<String> requests = new HashSet<String>();

    private void requestIconData(final String th) {
        if (requests.contains(th)) {
            return;
        }
        requests.add(th);
        final SwingWorker<Void, Icon> icon = new SwingWorker<Void, Icon>() {

            @Override
            protected Void doInBackground() throws Exception {
                ImageIcon ii = new ImageIcon(th);
                publish(ii);
                return null;
            }

            @Override
            protected void process(List<Icon> chunks) {
                super.process(chunks);
                for (Icon i : chunks) {
                    requests.remove(th);
                    addThumbIcon(th, i);
                }
            }
        };
        icon.execute();
    }

    private void addThumbIcon(String th, Icon ai) {
        this.icons.put(th, new SoftReference<Icon>(ai));
        outer.thumbDataChanged( th);
        
    }
}
