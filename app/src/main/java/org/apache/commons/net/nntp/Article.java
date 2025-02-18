package org.apache.commons.net.nntp;

import java.io.PrintStream;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class Article implements Threadable {
    private String articleId;
    private String date;
    private String from;
    public Article kid;
    public Article next;
    private ArrayList<String> references;
    private String simplifiedSubject;
    private String subject;
    private boolean isReply = false;
    private long articleNumber = -1;

    @Deprecated
    public void addHeaderField(String str, String str2) {
    }

    public void addReference(String str) {
        if (str == null || str.length() == 0) {
            return;
        }
        if (this.references == null) {
            this.references = new ArrayList<>();
        }
        this.isReply = true;
        for (String str2 : str.split(" ")) {
            this.references.add(str2);
        }
    }

    public String[] getReferences() {
        ArrayList<String> arrayList = this.references;
        return arrayList == null ? new String[0] : (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void simplifySubject() {
        /*
            r9 = this;
            java.lang.String r0 = r9.getSubject()
            int r1 = r0.length()
            r2 = 0
            r3 = 0
            r4 = 0
        Lb:
            if (r3 != 0) goto Lc4
        Ld:
            r3 = 32
            if (r4 >= r1) goto L1a
            char r5 = r0.charAt(r4)
            if (r5 != r3) goto L1a
            int r4 = r4 + 1
            goto Ld
        L1a:
            int r5 = r1 + (-2)
            if (r4 >= r5) goto L97
            char r6 = r0.charAt(r4)
            r7 = 114(0x72, float:1.6E-43)
            if (r6 == r7) goto L2e
            char r6 = r0.charAt(r4)
            r7 = 82
            if (r6 != r7) goto L97
        L2e:
            int r6 = r4 + 1
            char r7 = r0.charAt(r6)
            r8 = 101(0x65, float:1.42E-43)
            if (r7 == r8) goto L40
            char r6 = r0.charAt(r6)
            r7 = 69
            if (r6 != r7) goto L97
        L40:
            int r6 = r4 + 2
            char r7 = r0.charAt(r6)
            r8 = 58
            if (r7 != r8) goto L4e
            int r4 = r4 + 3
        L4c:
            r5 = 0
            goto L98
        L4e:
            if (r4 >= r5) goto L97
            char r5 = r0.charAt(r6)
            r7 = 91
            if (r5 == r7) goto L60
            char r5 = r0.charAt(r6)
            r6 = 40
            if (r5 != r6) goto L97
        L60:
            int r5 = r4 + 3
        L62:
            if (r5 >= r1) goto L77
            char r6 = r0.charAt(r5)
            r7 = 48
            if (r6 < r7) goto L77
            char r6 = r0.charAt(r5)
            r7 = 57
            if (r6 > r7) goto L77
            int r5 = r5 + 1
            goto L62
        L77:
            int r6 = r1 + (-1)
            if (r5 >= r6) goto L97
            char r6 = r0.charAt(r5)
            r7 = 93
            if (r6 == r7) goto L8b
            char r6 = r0.charAt(r5)
            r7 = 41
            if (r6 != r7) goto L97
        L8b:
            int r6 = r5 + 1
            char r6 = r0.charAt(r6)
            if (r6 != r8) goto L97
            int r5 = r5 + 2
            r4 = r5
            goto L4c
        L97:
            r5 = 1
        L98:
            java.lang.String r6 = r9.simplifiedSubject
            java.lang.String r7 = "(no subject)"
            boolean r6 = r7.equals(r6)
            if (r6 == 0) goto La6
            java.lang.String r6 = ""
            r9.simplifiedSubject = r6
        La6:
            r6 = r1
        La7:
            if (r6 <= r4) goto Lb4
            int r7 = r6 + (-1)
            char r7 = r0.charAt(r7)
            if (r7 >= r3) goto Lb4
            int r6 = r6 + (-1)
            goto La7
        Lb4:
            if (r4 != 0) goto Lbb
            if (r6 != r1) goto Lbb
            r9.simplifiedSubject = r0
            goto Lc1
        Lbb:
            java.lang.String r3 = r0.substring(r4, r6)
            r9.simplifiedSubject = r3
        Lc1:
            r3 = r5
            goto Lb
        Lc4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.net.nntp.Article.simplifySubject():void");
    }

    public static void printThread(Article article) {
        printThread(article, 0, System.out);
    }

    public static void printThread(Article article, PrintStream printStream) {
        printThread(article, 0, printStream);
    }

    public static void printThread(Article article, int i) {
        printThread(article, i, System.out);
    }

    public static void printThread(Article article, int i, PrintStream printStream) {
        for (int i2 = 0; i2 < i; i2++) {
            printStream.print("==>");
        }
        printStream.println(article.getSubject() + "\t" + article.getFrom() + "\t" + article.getArticleId());
        Article article2 = article.kid;
        if (article2 != null) {
            printThread(article2, i + 1);
        }
        Article article3 = article.next;
        if (article3 != null) {
            printThread(article3, i);
        }
    }

    public String getArticleId() {
        return this.articleId;
    }

    public long getArticleNumberLong() {
        return this.articleNumber;
    }

    public String getDate() {
        return this.date;
    }

    public String getFrom() {
        return this.from;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setArticleId(String str) {
        this.articleId = str;
    }

    public void setArticleNumber(long j) {
        this.articleNumber = j;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public void setFrom(String str) {
        this.from = str;
    }

    public void setSubject(String str) {
        this.subject = str;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public boolean isDummy() {
        return this.articleNumber == -1;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public String messageThreadId() {
        return this.articleId;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public String[] messageThreadReferences() {
        return getReferences();
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public String simplifiedSubject() {
        if (this.simplifiedSubject == null) {
            simplifySubject();
        }
        return this.simplifiedSubject;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public boolean subjectIsReply() {
        return this.isReply;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public void setChild(Threadable threadable) {
        this.kid = (Article) threadable;
        flushSubjectCache();
    }

    private void flushSubjectCache() {
        this.simplifiedSubject = null;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public void setNext(Threadable threadable) {
        this.next = (Article) threadable;
        flushSubjectCache();
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public Threadable makeDummy() {
        return new Article();
    }

    public String toString() {
        return this.articleNumber + " " + this.articleId + " " + this.subject;
    }

    @Deprecated
    public int getArticleNumber() {
        return (int) this.articleNumber;
    }

    @Deprecated
    public void setArticleNumber(int i) {
        this.articleNumber = i;
    }
}
