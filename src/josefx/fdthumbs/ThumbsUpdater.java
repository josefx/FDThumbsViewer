package josefx.fdthumbs;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
            //TODO move error to swing gui, do something else here
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                   JOptionPane.showMessageDialog(null,
                           "No thumbnail directory found in "+tfolder.getAbsolutePath(),
                           "Could not load thumbnails", JOptionPane.ERROR_MESSAGE);
                }
            }); 
            return null;
        }
        final List<File> files = readFileLists(new File(tfolder.getAbsolutePath()+File.separator+"normal"));
        for (File f : files) {
            final ImageInputStream iis = ImageIO.createImageInputStream(f);
            final Iterator<ImageReader> reads = ImageIO.getImageReaders(iis);
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
                iis.close();
                continue;
            }
            r.setInput(iis);
            final IIOMetadata meta = r.getImageMetadata(0);
            r.dispose();
            iis.close();
            if (meta == null) {
                continue;
            }
            String uri = grabUri(meta);
            uri = uri != null ? uri : "";
            final ThumbData td = new ThumbData();
            try{
            
                final File fff = new File(new URI(uri));
                td.setOriginal(fff.getAbsolutePath());
                td.setThumb(f.getAbsolutePath());
            }catch(Exception ex){
                td.setOriginal("Invalid Pathdata: "+uri);
                td.setThumb(f.getAbsolutePath());
            }
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

    private List<File> readFileLists(File f) {
        
        return Arrays.asList(f.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        }));
    }

    @Override
    protected void process(List<ThumbData> chunks) {
        super.process(chunks);
        for (ThumbData td : chunks) {
            tableModel.add(td);
        }
    }
}
