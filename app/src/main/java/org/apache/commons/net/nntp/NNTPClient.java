package org.apache.commons.net.nntp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.io.DotTerminatedMessageReader;
import org.apache.commons.net.io.DotTerminatedMessageWriter;
import org.apache.commons.net.io.Util;

/* loaded from: classes2.dex */
public class NNTPClient extends NNTP {
    private void __parseArticlePointer(String str, ArticleInfo articleInfo) throws MalformedServerReplyException {
        String[] split = str.split(" ");
        if (split.length >= 3) {
            try {
                articleInfo.articleNumber = Long.parseLong(split[1]);
                articleInfo.articleId = split[2];
                return;
            } catch (NumberFormatException unused) {
            }
        }
        throw new MalformedServerReplyException("Could not parse article pointer.\nServer reply: " + str);
    }

    private static void __parseGroupReply(String str, NewsgroupInfo newsgroupInfo) throws MalformedServerReplyException {
        String[] split = str.split(" ");
        if (split.length >= 5) {
            try {
                newsgroupInfo._setArticleCount(Long.parseLong(split[1]));
                newsgroupInfo._setFirstArticle(Long.parseLong(split[2]));
                newsgroupInfo._setLastArticle(Long.parseLong(split[3]));
                newsgroupInfo._setNewsgroup(split[4]);
                newsgroupInfo._setPostingPermission(0);
                return;
            } catch (NumberFormatException unused) {
            }
        }
        throw new MalformedServerReplyException("Could not parse newsgroup info.\nServer reply: " + str);
    }

    static NewsgroupInfo __parseNewsgroupListEntry(String str) {
        String[] split = str.split(" ");
        if (split.length < 4) {
            return null;
        }
        NewsgroupInfo newsgroupInfo = new NewsgroupInfo();
        newsgroupInfo._setNewsgroup(split[0]);
        try {
            long parseLong = Long.parseLong(split[1]);
            long parseLong2 = Long.parseLong(split[2]);
            newsgroupInfo._setFirstArticle(parseLong2);
            newsgroupInfo._setLastArticle(parseLong);
            if (parseLong2 == 0 && parseLong == 0) {
                newsgroupInfo._setArticleCount(0L);
            } else {
                newsgroupInfo._setArticleCount((parseLong - parseLong2) + 1);
            }
            char charAt = split[3].charAt(0);
            if (charAt != 'M') {
                if (charAt != 'N') {
                    if (charAt == 'Y' || charAt == 'y') {
                        newsgroupInfo._setPostingPermission(2);
                    } else if (charAt != 'm') {
                        if (charAt != 'n') {
                            newsgroupInfo._setPostingPermission(0);
                        }
                    }
                    return newsgroupInfo;
                }
                newsgroupInfo._setPostingPermission(3);
                return newsgroupInfo;
            }
            newsgroupInfo._setPostingPermission(1);
            return newsgroupInfo;
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    static Article __parseArticleEntry(String str) {
        Article article = new Article();
        article.setSubject(str);
        String[] split = str.split("\t");
        if (split.length > 6) {
            try {
                article.setArticleNumber(Long.parseLong(split[0]));
                article.setSubject(split[1]);
                article.setFrom(split[2]);
                article.setDate(split[3]);
                article.setArticleId(split[4]);
                article.addReference(split[5]);
            } catch (NumberFormatException unused) {
            }
        }
        return article;
    }

    private NewsgroupInfo[] __readNewsgroupListing() throws IOException {
        DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
        Vector vector = new Vector(2048);
        while (true) {
            try {
                String readLine = dotTerminatedMessageReader.readLine();
                if (readLine != null) {
                    NewsgroupInfo __parseNewsgroupListEntry = __parseNewsgroupListEntry(readLine);
                    if (__parseNewsgroupListEntry != null) {
                        vector.addElement(__parseNewsgroupListEntry);
                    } else {
                        throw new MalformedServerReplyException(readLine);
                    }
                } else {
                    dotTerminatedMessageReader.close();
                    int size = vector.size();
                    if (size < 1) {
                        return new NewsgroupInfo[0];
                    }
                    NewsgroupInfo[] newsgroupInfoArr = new NewsgroupInfo[size];
                    vector.copyInto(newsgroupInfoArr);
                    return newsgroupInfoArr;
                }
            } catch (Throwable th) {
                dotTerminatedMessageReader.close();
                throw th;
            }
        }
    }

    private BufferedReader __retrieve(int i, String str, ArticleInfo articleInfo) throws IOException {
        if (str != null) {
            if (!NNTPReply.isPositiveCompletion(sendCommand(i, str))) {
                return null;
            }
        } else if (!NNTPReply.isPositiveCompletion(sendCommand(i))) {
            return null;
        }
        if (articleInfo != null) {
            __parseArticlePointer(getReplyString(), articleInfo);
        }
        return new DotTerminatedMessageReader(this._reader_);
    }

    private BufferedReader __retrieve(int i, long j, ArticleInfo articleInfo) throws IOException {
        if (!NNTPReply.isPositiveCompletion(sendCommand(i, Long.toString(j)))) {
            return null;
        }
        if (articleInfo != null) {
            __parseArticlePointer(getReplyString(), articleInfo);
        }
        return new DotTerminatedMessageReader(this._reader_);
    }

    public BufferedReader retrieveArticle(String str, ArticleInfo articleInfo) throws IOException {
        return __retrieve(0, str, articleInfo);
    }

    public Reader retrieveArticle(String str) throws IOException {
        return retrieveArticle(str, (ArticleInfo) null);
    }

    public Reader retrieveArticle() throws IOException {
        return retrieveArticle((String) null);
    }

    public BufferedReader retrieveArticle(long j, ArticleInfo articleInfo) throws IOException {
        return __retrieve(0, j, articleInfo);
    }

    public BufferedReader retrieveArticle(long j) throws IOException {
        return retrieveArticle(j, (ArticleInfo) null);
    }

    public BufferedReader retrieveArticleHeader(String str, ArticleInfo articleInfo) throws IOException {
        return __retrieve(3, str, articleInfo);
    }

    public Reader retrieveArticleHeader(String str) throws IOException {
        return retrieveArticleHeader(str, (ArticleInfo) null);
    }

    public Reader retrieveArticleHeader() throws IOException {
        return retrieveArticleHeader((String) null);
    }

    public BufferedReader retrieveArticleHeader(long j, ArticleInfo articleInfo) throws IOException {
        return __retrieve(3, j, articleInfo);
    }

    public BufferedReader retrieveArticleHeader(long j) throws IOException {
        return retrieveArticleHeader(j, (ArticleInfo) null);
    }

    public BufferedReader retrieveArticleBody(String str, ArticleInfo articleInfo) throws IOException {
        return __retrieve(1, str, articleInfo);
    }

    public Reader retrieveArticleBody(String str) throws IOException {
        return retrieveArticleBody(str, (ArticleInfo) null);
    }

    public Reader retrieveArticleBody() throws IOException {
        return retrieveArticleBody((String) null);
    }

    public BufferedReader retrieveArticleBody(long j, ArticleInfo articleInfo) throws IOException {
        return __retrieve(1, j, articleInfo);
    }

    public BufferedReader retrieveArticleBody(long j) throws IOException {
        return retrieveArticleBody(j, (ArticleInfo) null);
    }

    public boolean selectNewsgroup(String str, NewsgroupInfo newsgroupInfo) throws IOException {
        if (!NNTPReply.isPositiveCompletion(group(str))) {
            return false;
        }
        if (newsgroupInfo == null) {
            return true;
        }
        __parseGroupReply(getReplyString(), newsgroupInfo);
        return true;
    }

    public boolean selectNewsgroup(String str) throws IOException {
        return selectNewsgroup(str, null);
    }

    public String listHelp() throws IOException {
        if (!NNTPReply.isInformational(help())) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
        Util.copyReader(dotTerminatedMessageReader, stringWriter);
        dotTerminatedMessageReader.close();
        stringWriter.close();
        return stringWriter.toString();
    }

    public String[] listOverviewFmt() throws IOException {
        if (!NNTPReply.isPositiveCompletion(sendCommand("LIST", "OVERVIEW.FMT"))) {
            return null;
        }
        DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
        ArrayList arrayList = new ArrayList();
        while (true) {
            String readLine = dotTerminatedMessageReader.readLine();
            if (readLine != null) {
                arrayList.add(readLine);
            } else {
                dotTerminatedMessageReader.close();
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
        }
    }

    public boolean selectArticle(String str, ArticleInfo articleInfo) throws IOException {
        if (str != null) {
            if (!NNTPReply.isPositiveCompletion(stat(str))) {
                return false;
            }
        } else if (!NNTPReply.isPositiveCompletion(stat())) {
            return false;
        }
        if (articleInfo == null) {
            return true;
        }
        __parseArticlePointer(getReplyString(), articleInfo);
        return true;
    }

    public boolean selectArticle(String str) throws IOException {
        return selectArticle(str, (ArticleInfo) null);
    }

    public boolean selectArticle(ArticleInfo articleInfo) throws IOException {
        return selectArticle((String) null, articleInfo);
    }

    public boolean selectArticle(long j, ArticleInfo articleInfo) throws IOException {
        if (!NNTPReply.isPositiveCompletion(stat(j))) {
            return false;
        }
        if (articleInfo == null) {
            return true;
        }
        __parseArticlePointer(getReplyString(), articleInfo);
        return true;
    }

    public boolean selectArticle(long j) throws IOException {
        return selectArticle(j, (ArticleInfo) null);
    }

    public boolean selectPreviousArticle(ArticleInfo articleInfo) throws IOException {
        if (!NNTPReply.isPositiveCompletion(last())) {
            return false;
        }
        if (articleInfo == null) {
            return true;
        }
        __parseArticlePointer(getReplyString(), articleInfo);
        return true;
    }

    public boolean selectPreviousArticle() throws IOException {
        return selectPreviousArticle((ArticleInfo) null);
    }

    public boolean selectNextArticle(ArticleInfo articleInfo) throws IOException {
        if (!NNTPReply.isPositiveCompletion(next())) {
            return false;
        }
        if (articleInfo == null) {
            return true;
        }
        __parseArticlePointer(getReplyString(), articleInfo);
        return true;
    }

    public boolean selectNextArticle() throws IOException {
        return selectNextArticle((ArticleInfo) null);
    }

    public NewsgroupInfo[] listNewsgroups() throws IOException {
        if (NNTPReply.isPositiveCompletion(list())) {
            return __readNewsgroupListing();
        }
        return null;
    }

    public Iterable<String> iterateNewsgroupListing() throws IOException {
        if (NNTPReply.isPositiveCompletion(list())) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("LIST command failed: " + getReplyString());
    }

    public Iterable<NewsgroupInfo> iterateNewsgroups() throws IOException {
        return new NewsgroupIterator(iterateNewsgroupListing());
    }

    public NewsgroupInfo[] listNewsgroups(String str) throws IOException {
        if (NNTPReply.isPositiveCompletion(listActive(str))) {
            return __readNewsgroupListing();
        }
        return null;
    }

    public Iterable<String> iterateNewsgroupListing(String str) throws IOException {
        if (NNTPReply.isPositiveCompletion(listActive(str))) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("LIST ACTIVE " + str + " command failed: " + getReplyString());
    }

    public Iterable<NewsgroupInfo> iterateNewsgroups(String str) throws IOException {
        return new NewsgroupIterator(iterateNewsgroupListing(str));
    }

    public NewsgroupInfo[] listNewNewsgroups(NewGroupsOrNewsQuery newGroupsOrNewsQuery) throws IOException {
        if (NNTPReply.isPositiveCompletion(newgroups(newGroupsOrNewsQuery.getDate(), newGroupsOrNewsQuery.getTime(), newGroupsOrNewsQuery.isGMT(), newGroupsOrNewsQuery.getDistributions()))) {
            return __readNewsgroupListing();
        }
        return null;
    }

    public Iterable<String> iterateNewNewsgroupListing(NewGroupsOrNewsQuery newGroupsOrNewsQuery) throws IOException {
        if (NNTPReply.isPositiveCompletion(newgroups(newGroupsOrNewsQuery.getDate(), newGroupsOrNewsQuery.getTime(), newGroupsOrNewsQuery.isGMT(), newGroupsOrNewsQuery.getDistributions()))) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("NEWGROUPS command failed: " + getReplyString());
    }

    public Iterable<NewsgroupInfo> iterateNewNewsgroups(NewGroupsOrNewsQuery newGroupsOrNewsQuery) throws IOException {
        return new NewsgroupIterator(iterateNewNewsgroupListing(newGroupsOrNewsQuery));
    }

    public String[] listNewNews(NewGroupsOrNewsQuery newGroupsOrNewsQuery) throws IOException {
        if (!NNTPReply.isPositiveCompletion(newnews(newGroupsOrNewsQuery.getNewsgroups(), newGroupsOrNewsQuery.getDate(), newGroupsOrNewsQuery.getTime(), newGroupsOrNewsQuery.isGMT(), newGroupsOrNewsQuery.getDistributions()))) {
            return null;
        }
        Vector vector = new Vector();
        DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
        while (true) {
            try {
                String readLine = dotTerminatedMessageReader.readLine();
                if (readLine == null) {
                    break;
                }
                vector.addElement(readLine);
            } catch (Throwable th) {
                dotTerminatedMessageReader.close();
                throw th;
            }
        }
        dotTerminatedMessageReader.close();
        int size = vector.size();
        if (size < 1) {
            return new String[0];
        }
        String[] strArr = new String[size];
        vector.copyInto(strArr);
        return strArr;
    }

    public Iterable<String> iterateNewNews(NewGroupsOrNewsQuery newGroupsOrNewsQuery) throws IOException {
        if (NNTPReply.isPositiveCompletion(newnews(newGroupsOrNewsQuery.getNewsgroups(), newGroupsOrNewsQuery.getDate(), newGroupsOrNewsQuery.getTime(), newGroupsOrNewsQuery.isGMT(), newGroupsOrNewsQuery.getDistributions()))) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("NEWNEWS command failed: " + getReplyString());
    }

    public boolean completePendingCommand() throws IOException {
        return NNTPReply.isPositiveCompletion(getReply());
    }

    public Writer postArticle() throws IOException {
        if (NNTPReply.isPositiveIntermediate(post())) {
            return new DotTerminatedMessageWriter(this._writer_);
        }
        return null;
    }

    public Writer forwardArticle(String str) throws IOException {
        if (NNTPReply.isPositiveIntermediate(ihave(str))) {
            return new DotTerminatedMessageWriter(this._writer_);
        }
        return null;
    }

    public boolean logout() throws IOException {
        return NNTPReply.isPositiveCompletion(quit());
    }

    public boolean authenticate(String str, String str2) throws IOException {
        if (authinfoUser(str) != 381 || authinfoPass(str2) != 281) {
            return false;
        }
        this._isAllowedToPost = true;
        return true;
    }

    private BufferedReader __retrieveArticleInfo(String str) throws IOException {
        if (NNTPReply.isPositiveCompletion(xover(str))) {
            return new DotTerminatedMessageReader(this._reader_);
        }
        return null;
    }

    public BufferedReader retrieveArticleInfo(long j) throws IOException {
        return __retrieveArticleInfo(Long.toString(j));
    }

    public BufferedReader retrieveArticleInfo(long j, long j2) throws IOException {
        return __retrieveArticleInfo(j + "-" + j2);
    }

    public Iterable<Article> iterateArticleInfo(long j, long j2) throws IOException {
        BufferedReader retrieveArticleInfo = retrieveArticleInfo(j, j2);
        if (retrieveArticleInfo == null) {
            throw new IOException("XOVER command failed: " + getReplyString());
        }
        return new ArticleIterator(new ReplyIterator(retrieveArticleInfo, false));
    }

    private BufferedReader __retrieveHeader(String str, String str2) throws IOException {
        if (NNTPReply.isPositiveCompletion(xhdr(str, str2))) {
            return new DotTerminatedMessageReader(this._reader_);
        }
        return null;
    }

    public BufferedReader retrieveHeader(String str, long j) throws IOException {
        return __retrieveHeader(str, Long.toString(j));
    }

    public BufferedReader retrieveHeader(String str, long j, long j2) throws IOException {
        return __retrieveHeader(str, j + "-" + j2);
    }

    @Deprecated
    public Reader retrieveHeader(String str, int i, int i2) throws IOException {
        return retrieveHeader(str, i, i2);
    }

    @Deprecated
    public Reader retrieveArticleInfo(int i, int i2) throws IOException {
        return retrieveArticleInfo(i, i2);
    }

    @Deprecated
    public Reader retrieveHeader(String str, int i) throws IOException {
        return retrieveHeader(str, i);
    }

    @Deprecated
    public boolean selectArticle(int i, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        boolean selectArticle = selectArticle(i, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return selectArticle;
    }

    @Deprecated
    public Reader retrieveArticleInfo(int i) throws IOException {
        return retrieveArticleInfo(i);
    }

    @Deprecated
    public boolean selectArticle(int i) throws IOException {
        return selectArticle(i);
    }

    @Deprecated
    public Reader retrieveArticleHeader(int i) throws IOException {
        return retrieveArticleHeader(i);
    }

    @Deprecated
    public Reader retrieveArticleHeader(int i, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        BufferedReader retrieveArticleHeader = retrieveArticleHeader(i, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return retrieveArticleHeader;
    }

    @Deprecated
    public Reader retrieveArticleBody(int i) throws IOException {
        return retrieveArticleBody(i);
    }

    @Deprecated
    public Reader retrieveArticle(int i, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        BufferedReader retrieveArticle = retrieveArticle(i, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return retrieveArticle;
    }

    @Deprecated
    public Reader retrieveArticle(int i) throws IOException {
        return retrieveArticle(i);
    }

    @Deprecated
    public Reader retrieveArticleBody(int i, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        BufferedReader retrieveArticleBody = retrieveArticleBody(i, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return retrieveArticleBody;
    }

    @Deprecated
    public Reader retrieveArticle(String str, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        BufferedReader retrieveArticle = retrieveArticle(str, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return retrieveArticle;
    }

    @Deprecated
    public Reader retrieveArticleBody(String str, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        BufferedReader retrieveArticleBody = retrieveArticleBody(str, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return retrieveArticleBody;
    }

    @Deprecated
    public Reader retrieveArticleHeader(String str, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        BufferedReader retrieveArticleHeader = retrieveArticleHeader(str, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return retrieveArticleHeader;
    }

    @Deprecated
    public boolean selectArticle(String str, ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        boolean selectArticle = selectArticle(str, __ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return selectArticle;
    }

    @Deprecated
    public boolean selectArticle(ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        boolean selectArticle = selectArticle(__ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return selectArticle;
    }

    @Deprecated
    public boolean selectNextArticle(ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        boolean selectNextArticle = selectNextArticle(__ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return selectNextArticle;
    }

    @Deprecated
    public boolean selectPreviousArticle(ArticlePointer articlePointer) throws IOException {
        ArticleInfo __ap2ai = __ap2ai(articlePointer);
        boolean selectPreviousArticle = selectPreviousArticle(__ap2ai);
        __ai2ap(__ap2ai, articlePointer);
        return selectPreviousArticle;
    }

    private ArticleInfo __ap2ai(ArticlePointer articlePointer) {
        if (articlePointer == null) {
            return null;
        }
        return new ArticleInfo();
    }

    private void __ai2ap(ArticleInfo articleInfo, ArticlePointer articlePointer) {
        if (articlePointer != null) {
            articlePointer.articleId = articleInfo.articleId;
            articlePointer.articleNumber = (int) articleInfo.articleNumber;
        }
    }
}
