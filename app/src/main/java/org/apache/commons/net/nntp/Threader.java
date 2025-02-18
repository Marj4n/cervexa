package org.apache.commons.net.nntp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class Threader {
    public Threadable thread(List<? extends Threadable> list) {
        return thread((Iterable<? extends Threadable>) list);
    }

    public Threadable thread(Iterable<? extends Threadable> iterable) {
        if (iterable == null) {
            return null;
        }
        HashMap<String, ThreadContainer> hashMap = new HashMap<>();
        for (Threadable threadable : iterable) {
            if (!threadable.isDummy()) {
                buildContainer(threadable, hashMap);
            }
        }
        if (hashMap.isEmpty()) {
            return null;
        }
        ThreadContainer findRootSet = findRootSet(hashMap);
        hashMap.clear();
        pruneEmptyContainers(findRootSet);
        findRootSet.reverseChildren();
        gatherSubjects(findRootSet);
        if (findRootSet.next != null) {
            throw new RuntimeException("root node has a next:" + findRootSet);
        }
        for (ThreadContainer threadContainer = findRootSet.child; threadContainer != null; threadContainer = threadContainer.next) {
            if (threadContainer.threadable == null) {
                threadContainer.threadable = threadContainer.child.threadable.makeDummy();
            }
        }
        Threadable threadable2 = findRootSet.child != null ? findRootSet.child.threadable : null;
        findRootSet.flush();
        return threadable2;
    }

    private void buildContainer(Threadable threadable, HashMap<String, ThreadContainer> hashMap) {
        String messageThreadId = threadable.messageThreadId();
        ThreadContainer threadContainer = hashMap.get(messageThreadId);
        if (threadContainer != null) {
            if (threadContainer.threadable != null) {
                messageThreadId = "<Bogus-id:1>";
                threadContainer = null;
            } else {
                threadContainer.threadable = threadable;
            }
        }
        if (threadContainer == null) {
            threadContainer = new ThreadContainer();
            threadContainer.threadable = threadable;
            hashMap.put(messageThreadId, threadContainer);
        }
        String[] messageThreadReferences = threadable.messageThreadReferences();
        int length = messageThreadReferences.length;
        int i = 0;
        ThreadContainer threadContainer2 = null;
        while (i < length) {
            String str = messageThreadReferences[i];
            ThreadContainer threadContainer3 = hashMap.get(str);
            if (threadContainer3 == null) {
                threadContainer3 = new ThreadContainer();
                hashMap.put(str, threadContainer3);
            }
            if (threadContainer2 != null && threadContainer3.parent == null && threadContainer2 != threadContainer3 && !threadContainer3.findChild(threadContainer2)) {
                threadContainer3.parent = threadContainer2;
                threadContainer3.next = threadContainer2.child;
                threadContainer2.child = threadContainer3;
            }
            i++;
            threadContainer2 = threadContainer3;
        }
        if (threadContainer2 != null && (threadContainer2 == threadContainer || threadContainer.findChild(threadContainer2))) {
            threadContainer2 = null;
        }
        if (threadContainer.parent != null) {
            ThreadContainer threadContainer4 = threadContainer.parent.child;
            ThreadContainer threadContainer5 = null;
            while (threadContainer4 != null && threadContainer4 != threadContainer) {
                threadContainer5 = threadContainer4;
                threadContainer4 = threadContainer4.next;
            }
            if (threadContainer4 == null) {
                throw new RuntimeException("Didnt find " + threadContainer + " in parent" + threadContainer.parent);
            }
            if (threadContainer5 == null) {
                threadContainer.parent.child = threadContainer.next;
            } else {
                threadContainer5.next = threadContainer.next;
            }
            threadContainer.next = null;
            threadContainer.parent = null;
        }
        if (threadContainer2 != null) {
            threadContainer.parent = threadContainer2;
            threadContainer.next = threadContainer2.child;
            threadContainer2.child = threadContainer;
        }
    }

    private ThreadContainer findRootSet(HashMap<String, ThreadContainer> hashMap) {
        ThreadContainer threadContainer = new ThreadContainer();
        Iterator<Map.Entry<String, ThreadContainer>> it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            ThreadContainer value = it.next().getValue();
            if (value.parent == null) {
                if (value.next != null) {
                    throw new RuntimeException("c.next is " + value.next.toString());
                }
                value.next = threadContainer.child;
                threadContainer.child = value;
            }
        }
        return threadContainer;
    }

    private void pruneEmptyContainers(ThreadContainer threadContainer) {
        ThreadContainer threadContainer2 = threadContainer.child;
        ThreadContainer threadContainer3 = threadContainer2.next;
        ThreadContainer threadContainer4 = null;
        while (threadContainer2 != null) {
            if (threadContainer2.threadable == null && threadContainer2.child == null) {
                if (threadContainer4 == null) {
                    threadContainer.child = threadContainer2.next;
                } else {
                    threadContainer4.next = threadContainer2.next;
                }
            } else if (threadContainer2.threadable == null && threadContainer2.child != null && (threadContainer2.parent != null || threadContainer2.child.next == null)) {
                threadContainer3 = threadContainer2.child;
                if (threadContainer4 == null) {
                    threadContainer.child = threadContainer3;
                } else {
                    threadContainer4.next = threadContainer3;
                }
                ThreadContainer threadContainer5 = threadContainer3;
                while (threadContainer5.next != null) {
                    threadContainer5.parent = threadContainer2.parent;
                    threadContainer5 = threadContainer5.next;
                }
                threadContainer5.parent = threadContainer2.parent;
                threadContainer5.next = threadContainer2.next;
            } else {
                if (threadContainer2.child != null) {
                    pruneEmptyContainers(threadContainer2);
                }
                threadContainer4 = threadContainer2;
            }
            threadContainer2 = threadContainer3;
            threadContainer3 = threadContainer2 == null ? null : threadContainer2.next;
        }
    }

    private void gatherSubjects(ThreadContainer threadContainer) {
        ThreadContainer threadContainer2;
        ThreadContainer threadContainer3;
        int i = 0;
        int i2 = 0;
        for (ThreadContainer threadContainer4 = threadContainer.child; threadContainer4 != null; threadContainer4 = threadContainer4.next) {
            i2++;
        }
        HashMap hashMap = new HashMap((int) (i2 * 1.2d), 0.9f);
        for (ThreadContainer threadContainer5 = threadContainer.child; threadContainer5 != null; threadContainer5 = threadContainer5.next) {
            Threadable threadable = threadContainer5.threadable;
            if (threadable == null) {
                threadable = threadContainer5.child.threadable;
            }
            String simplifiedSubject = threadable.simplifiedSubject();
            if (simplifiedSubject != null && simplifiedSubject.length() != 0 && ((threadContainer3 = (ThreadContainer) hashMap.get(simplifiedSubject)) == null || ((threadContainer5.threadable == null && threadContainer3.threadable != null) || (threadContainer3.threadable != null && threadContainer3.threadable.subjectIsReply() && threadContainer5.threadable != null && !threadContainer5.threadable.subjectIsReply())))) {
                hashMap.put(simplifiedSubject, threadContainer5);
                i++;
            }
        }
        if (i == 0) {
            return;
        }
        ThreadContainer threadContainer6 = threadContainer.child;
        ThreadContainer threadContainer7 = threadContainer6.next;
        ThreadContainer threadContainer8 = null;
        while (threadContainer6 != null) {
            Threadable threadable2 = threadContainer6.threadable;
            if (threadable2 == null) {
                threadable2 = threadContainer6.child.threadable;
            }
            String simplifiedSubject2 = threadable2.simplifiedSubject();
            if (simplifiedSubject2 == null || simplifiedSubject2.length() == 0 || (threadContainer2 = (ThreadContainer) hashMap.get(simplifiedSubject2)) == threadContainer6) {
                threadContainer8 = threadContainer6;
            } else {
                if (threadContainer8 == null) {
                    threadContainer.child = threadContainer6.next;
                } else {
                    threadContainer8.next = threadContainer6.next;
                }
                threadContainer6.next = null;
                if (threadContainer2.threadable == null && threadContainer6.threadable == null) {
                    ThreadContainer threadContainer9 = threadContainer2.child;
                    while (threadContainer9 != null && threadContainer9.next != null) {
                        threadContainer9 = threadContainer9.next;
                    }
                    if (threadContainer9 != null) {
                        threadContainer9.next = threadContainer6.child;
                    }
                    for (ThreadContainer threadContainer10 = threadContainer6.child; threadContainer10 != null; threadContainer10 = threadContainer10.next) {
                        threadContainer10.parent = threadContainer2;
                    }
                    threadContainer6.child = null;
                } else if (threadContainer2.threadable == null || (threadContainer6.threadable != null && threadContainer6.threadable.subjectIsReply() && !threadContainer2.threadable.subjectIsReply())) {
                    threadContainer6.parent = threadContainer2;
                    threadContainer6.next = threadContainer2.child;
                    threadContainer2.child = threadContainer6;
                } else {
                    ThreadContainer threadContainer11 = new ThreadContainer();
                    threadContainer11.threadable = threadContainer2.threadable;
                    threadContainer11.child = threadContainer2.child;
                    for (ThreadContainer threadContainer12 = threadContainer11.child; threadContainer12 != null; threadContainer12 = threadContainer12.next) {
                        threadContainer12.parent = threadContainer11;
                    }
                    threadContainer2.threadable = null;
                    threadContainer2.child = null;
                    threadContainer6.parent = threadContainer2;
                    threadContainer11.parent = threadContainer2;
                    threadContainer2.child = threadContainer6;
                    threadContainer6.next = threadContainer11;
                }
            }
            ThreadContainer threadContainer13 = threadContainer7;
            threadContainer7 = threadContainer7 == null ? null : threadContainer7.next;
            threadContainer6 = threadContainer13;
        }
        hashMap.clear();
    }

    @Deprecated
    public Threadable thread(Threadable[] threadableArr) {
        if (threadableArr == null) {
            return null;
        }
        return thread(Arrays.asList(threadableArr));
    }
}
