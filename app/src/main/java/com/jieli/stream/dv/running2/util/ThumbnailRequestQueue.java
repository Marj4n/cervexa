package com.jieli.stream.dv.running2.util;

import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.bean.RequestFileInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/* loaded from: classes.dex */
public class ThumbnailRequestQueue {
    private static final int MAX_SIZE = 10;
    private Queue<RequestFileInfo> requestFileInfoQueue = new ArrayBlockingQueue(10);

    public void put(FileInfo fileInfo, boolean z) {
        this.requestFileInfoQueue.offer(new RequestFileInfo(fileInfo, z));
    }

    public RequestFileInfo poll() {
        return this.requestFileInfoQueue.poll();
    }

    public RequestFileInfo peek() {
        return this.requestFileInfoQueue.peek();
    }

    public boolean isFull() {
        return this.requestFileInfoQueue.size() >= 10;
    }

    public boolean isEmpty() {
        return this.requestFileInfoQueue.isEmpty();
    }

    public void clear() {
        this.requestFileInfoQueue.clear();
    }

    public List<FileInfo> getFileInfos() {
        ArrayList arrayList = new ArrayList();
        while (this.requestFileInfoQueue.peek() != null) {
            RequestFileInfo poll = this.requestFileInfoQueue.poll();
            if (!poll.isContent()) {
                arrayList.add(poll.getFileInfo());
            }
        }
        return arrayList;
    }

    public int size() {
        return this.requestFileInfoQueue.size();
    }
}
