package org.apache.commons.net.nntp;

import java.util.Iterator;

/* loaded from: classes2.dex */
class NewsgroupIterator implements Iterator<NewsgroupInfo>, Iterable<NewsgroupInfo> {
    private final Iterator<String> stringIterator;

    @Override // java.lang.Iterable
    public Iterator<NewsgroupInfo> iterator() {
        return this;
    }

    public NewsgroupIterator(Iterable<String> iterable) {
        this.stringIterator = iterable.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.stringIterator.hasNext();
    }

    @Override // java.util.Iterator
    public NewsgroupInfo next() {
        return NNTPClient.__parseNewsgroupListEntry(this.stringIterator.next());
    }

    @Override // java.util.Iterator
    public void remove() {
        this.stringIterator.remove();
    }
}
