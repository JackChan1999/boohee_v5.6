package org.eclipse.mat.hprof;

import com.baidu.location.LocationClientOption;
import com.loopj.android.http.AsyncHttpClient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.IteratorLong;
import org.eclipse.mat.hprof.extension.IParsingEnhancer;
import org.eclipse.mat.parser.IIndexBuilder;
import org.eclipse.mat.parser.IPreliminaryIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IndexWriter.LongIndexStreamer;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;
import org.eclipse.mat.util.MessageUtil;
import org.eclipse.mat.util.SimpleMonitor;
import org.eclipse.mat.util.SimpleMonitor.Listener;

public class HprofIndexBuilder implements IIndexBuilder {
    private List<IParsingEnhancer> enhancers;
    private File file;
    private IOne2LongIndex id2position;
    private String prefix;

    private static final class IndexIterator implements IteratorLong {
        private final IOne2LongIndex id2position;
        private int nextIndex;
        private final int[] purgedMapping;

        private IndexIterator(IOne2LongIndex id2position, int[] purgedMapping) {
            this.nextIndex = -1;
            this.id2position = id2position;
            this.purgedMapping = purgedMapping;
            findNext();
        }

        public boolean hasNext() {
            return this.nextIndex < this.purgedMapping.length;
        }

        public long next() {
            long answer = this.id2position.get(this.nextIndex);
            findNext();
            return answer;
        }

        protected void findNext() {
            this.nextIndex++;
            while (this.nextIndex < this.purgedMapping.length && this.purgedMapping[this.nextIndex] < 0) {
                this.nextIndex++;
            }
        }
    }

    public void init(File file, String prefix) {
        this.file = file;
        this.prefix = prefix;
        this.enhancers = new ArrayList();
    }

    public void fill(IPreliminaryIndex preliminary, IProgressListener listener) throws SnapshotException, IOException {
        SimpleMonitor monitor = new SimpleMonitor(MessageUtil.format(Messages.HprofIndexBuilder_Parsing, this.file.getAbsolutePath()), listener, new int[]{500, AsyncHttpClient.DEFAULT_RETRY_SLEEP_TIME_MILLIS});
        listener.beginTask(MessageUtil.format(Messages.HprofIndexBuilder_Parsing, this.file.getName()), (int) LocationClientOption.MIN_SCAN_SPAN_NETWORK);
        IHprofParserHandler handler = new HprofParserHandlerImpl();
        handler.beforePass1(preliminary.getSnapshotInfo());
        Listener mon = (Listener) monitor.nextMonitor();
        mon.beginTask(MessageUtil.format(Messages.HprofIndexBuilder_Scanning, this.file.getAbsolutePath()), (int) (this.file.length() / 1000));
        new Pass1Parser(handler, mon).read(this.file);
        if (listener.isCanceled()) {
            throw new OperationCanceledException();
        }
        mon.done();
        handler.beforePass2(listener);
        mon = (Listener) monitor.nextMonitor();
        mon.beginTask(MessageUtil.format(Messages.HprofIndexBuilder_ExtractingObjects, this.file.getAbsolutePath()), (int) (this.file.length() / 1000));
        new Pass2Parser(handler, mon).read(this.file);
        if (listener.isCanceled()) {
            throw new OperationCanceledException();
        }
        mon.done();
        if (listener.isCanceled()) {
            throw new OperationCanceledException();
        }
        for (IParsingEnhancer enhancer : this.enhancers) {
            enhancer.onParsingCompleted(handler.getSnapshotInfo());
        }
        this.id2position = handler.fillIn(preliminary);
    }

    public void clean(int[] purgedMapping, IProgressListener listener) throws IOException {
        File indexFile = new File(this.prefix + "o2hprof.index");
        listener.subTask(MessageUtil.format(Messages.HprofIndexBuilder_Writing, indexFile.getAbsolutePath()));
        try {
            new LongIndexStreamer().writeTo(indexFile, new IndexIterator(this.id2position, purgedMapping)).close();
        } catch (IOException e) {
        }
        try {
            this.id2position.close();
        } catch (IOException e2) {
        }
        this.id2position.delete();
        this.id2position = null;
    }

    public void cancel() {
        if (this.id2position != null) {
            try {
                this.id2position.close();
            } catch (IOException e) {
            }
            this.id2position.delete();
        }
    }
}
