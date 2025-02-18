package org.apache.commons.net.nntp;

/* loaded from: classes2.dex */
public interface Threadable {
    boolean isDummy();

    Threadable makeDummy();

    String messageThreadId();

    String[] messageThreadReferences();

    void setChild(Threadable threadable);

    void setNext(Threadable threadable);

    String simplifiedSubject();

    boolean subjectIsReply();
}
