package josefx.fdthumbs;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.SwingWorker;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class ThumbsUpdater extends SwingWorker<Void, ThumbData> {

 
    private final ThumbTableModel tableModel;

    public ThumbsUpdater(ThumbTableModel thtm) {
        this.tableModel = thtm;
        
    }

    @Override
    protected Void doInBackground() throws Exception {
        final File tfolder = new File(System.getProperty("user.home") + File.separator + ".thumbnails");
        if (!tfolder.exists() || !tfolder.isDirectory()) {
            assert false;
            return null;
        }
        final List<File> files = new ArrayList<File>();
        readFileLists(tfolder, files);
        for (File f : files) {
            ImageInputStream iis = ImageIO.createImageInputStream(f);
            Iterator<ImageReader> reads = ImageIO.getImageReaders(iis);
            if (!reads.hasNext()) {
                iis.close();
                continue;
            }
            ImageReader r = null;
            while (reads.hasNext() && r == null) {
                r = reads.next();
                if (r.isIgnoringMetadata()) {
                    r = null;
                }
            }
            if (r == null) {
                continue;
            }
            r.setInput(iis);
            final IIOMetadata meta = r.getImageMetadata(0);
            iis.close();
            if (meta == null) {
                continue;
            }
            String uri = grabUri(meta);
            uri = uri != null ? uri : "";
            final ThumbData td = new ThumbData();
            final File fff = new File(new URI(uri));
            td.setOriginal(fff.getAbsolutePath());
            td.setThumb(f.getAbsolutePath());
            this.publish(td);
        }
        return null;
    }

    private String grabUri(IIOMetadata meta) {
        String uri = null;
        Node na = meta.getAsTree("javax_imageio_1.0");
        NodeList nl = na.getChildNodes();
        escape:
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals("Text")) {
                Node n = nl.item(i);
                for (int x = 0; x < n.getChildNodes().getLength(); ++x) {
                    Node ch = n.getChildNodes().item(x);
                    for (int attrIndex = 0; attrIndex < ch.getAttributes().getLength(); attrIndex++) {
                        if (ch.getAttributes().item(attrIndex).getNodeValue().equals("Thumb::URI")) {
                            uri = ch.getAttributes().item(attrIndex + 1).getNodeValue();
                            break escape;
                        }
                    }
                }
            }
        }
        return uri;
    }

    private void readFileLists(File f, List<File> l) {
        for (File ff : f.listFiles()) {
            if (ff.isFile()) {
                l.add(ff);
            } else if (ff.isDirectory()) {
                readFileLists(ff, l);
            }
        }
    }

    @Override
    protected void process(List<ThumbData> chunks) {
        super.process(chunks);
        for (ThumbData td : chunks) {
            tableModel.add(td);
        }
    }
}
