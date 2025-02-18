package org.apache.commons.net.nntp;

import java.util.Iterator;

/* loaded from: classes2.dex */
class ArticleIterator implements Iterator<Article>, Iterable<Article> {
    private final Iterator<String> stringIterator;

    @Override // java.lang.Iterable
    public Iterator<Article> iterator() {
        return this;
    }

    public ArticleIterator(Iterable<String> iterable) {
        this.stringIterator = iterable.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.stringIterator.hasNext();
    }

    @Override // java.util.Iterator
    public Article next() {
        return NNTPClient.__parseArticleEntry(this.stringIterator.next());
    }

    @Override // java.util.Iterator
    public void remove() {
        this.stringIterator.remove();
    }
}
