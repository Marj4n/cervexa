package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.net.util.Charsets;

/* loaded from: classes2.dex */
public class FTPListParseEngine {
    private ListIterator<String> _internalIterator;
    private List<String> entries;
    private final FTPFileEntryParser parser;
    private final boolean saveUnparseableEntries;

    public FTPListParseEngine(FTPFileEntryParser fTPFileEntryParser) {
        this(fTPFileEntryParser, null);
    }

    FTPListParseEngine(FTPFileEntryParser fTPFileEntryParser, FTPClientConfig fTPClientConfig) {
        LinkedList linkedList = new LinkedList();
        this.entries = linkedList;
        this._internalIterator = linkedList.listIterator();
        this.parser = fTPFileEntryParser;
        if (fTPClientConfig != null) {
            this.saveUnparseableEntries = fTPClientConfig.getUnparseableEntries();
        } else {
            this.saveUnparseableEntries = false;
        }
    }

    public void readServerList(InputStream inputStream, String str) throws IOException {
        this.entries = new LinkedList();
        readStream(inputStream, str);
        this.parser.preParse(this.entries);
        resetIterator();
    }

    private void readStream(InputStream inputStream, String str) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charsets.toCharset(str)));
        String readNextEntry = this.parser.readNextEntry(bufferedReader);
        while (readNextEntry != null) {
            this.entries.add(readNextEntry);
            readNextEntry = this.parser.readNextEntry(bufferedReader);
        }
        bufferedReader.close();
    }

    public FTPFile[] getNext(int i) {
        LinkedList linkedList = new LinkedList();
        while (i > 0 && this._internalIterator.hasNext()) {
            String next = this._internalIterator.next();
            FTPFile parseFTPEntry = this.parser.parseFTPEntry(next);
            if (parseFTPEntry == null && this.saveUnparseableEntries) {
                parseFTPEntry = new FTPFile(next);
            }
            linkedList.add(parseFTPEntry);
            i--;
        }
        return (FTPFile[]) linkedList.toArray(new FTPFile[linkedList.size()]);
    }

    public FTPFile[] getPrevious(int i) {
        LinkedList linkedList = new LinkedList();
        while (i > 0 && this._internalIterator.hasPrevious()) {
            String previous = this._internalIterator.previous();
            FTPFile parseFTPEntry = this.parser.parseFTPEntry(previous);
            if (parseFTPEntry == null && this.saveUnparseableEntries) {
                parseFTPEntry = new FTPFile(previous);
            }
            linkedList.add(0, parseFTPEntry);
            i--;
        }
        return (FTPFile[]) linkedList.toArray(new FTPFile[linkedList.size()]);
    }

    public FTPFile[] getFiles() throws IOException {
        return getFiles(FTPFileFilters.NON_NULL);
    }

    public FTPFile[] getFiles(FTPFileFilter fTPFileFilter) throws IOException {
        ArrayList arrayList = new ArrayList();
        for (String str : this.entries) {
            FTPFile parseFTPEntry = this.parser.parseFTPEntry(str);
            if (parseFTPEntry == null && this.saveUnparseableEntries) {
                parseFTPEntry = new FTPFile(str);
            }
            if (fTPFileFilter.accept(parseFTPEntry)) {
                arrayList.add(parseFTPEntry);
            }
        }
        return (FTPFile[]) arrayList.toArray(new FTPFile[arrayList.size()]);
    }

    public boolean hasNext() {
        return this._internalIterator.hasNext();
    }

    public boolean hasPrevious() {
        return this._internalIterator.hasPrevious();
    }

    public void resetIterator() {
        this._internalIterator = this.entries.listIterator();
    }

    @Deprecated
    public void readServerList(InputStream inputStream) throws IOException {
        readServerList(inputStream, null);
    }
}
