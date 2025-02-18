package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public final class AnimatorSet extends Animator {
    private ArrayList<Animator> mPlayingSet = new ArrayList<>();
    private HashMap<Animator, Node> mNodeMap = new HashMap<>();
    private ArrayList<Node> mNodes = new ArrayList<>();
    private ArrayList<Node> mSortedNodes = new ArrayList<>();
    private boolean mNeedsSort = true;
    private AnimatorSetListener mSetListener = null;
    boolean mTerminated = false;
    private boolean mStarted = false;
    private long mStartDelay = 0;
    private ValueAnimator mDelayAnim = null;
    private long mDuration = -1;

    public void playTogether(Animator... animatorArr) {
        if (animatorArr != null) {
            this.mNeedsSort = true;
            Builder play = play(animatorArr[0]);
            for (int i = 1; i < animatorArr.length; i++) {
                play.with(animatorArr[i]);
            }
        }
    }

    public void playTogether(Collection<Animator> collection) {
        if (collection == null || collection.size() <= 0) {
            return;
        }
        this.mNeedsSort = true;
        Builder builder = null;
        for (Animator animator : collection) {
            if (builder == null) {
                builder = play(animator);
            } else {
                builder.with(animator);
            }
        }
    }

    public void playSequentially(Animator... animatorArr) {
        if (animatorArr != null) {
            this.mNeedsSort = true;
            int i = 0;
            if (animatorArr.length == 1) {
                play(animatorArr[0]);
                return;
            }
            while (i < animatorArr.length - 1) {
                Builder play = play(animatorArr[i]);
                i++;
                play.before(animatorArr[i]);
            }
        }
    }

    public void playSequentially(List<Animator> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.mNeedsSort = true;
        int i = 0;
        if (list.size() == 1) {
            play(list.get(0));
            return;
        }
        while (i < list.size() - 1) {
            Builder play = play(list.get(i));
            i++;
            play.before(list.get(i));
        }
    }

    public ArrayList<Animator> getChildAnimations() {
        ArrayList<Animator> arrayList = new ArrayList<>();
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().animation);
        }
        return arrayList;
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setTarget(Object obj) {
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            Animator animator = it.next().animation;
            if (animator instanceof AnimatorSet) {
                ((AnimatorSet) animator).setTarget(obj);
            } else if (animator instanceof ObjectAnimator) {
                ((ObjectAnimator) animator).setTarget(obj);
            }
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setInterpolator(Interpolator interpolator) {
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            it.next().animation.setInterpolator(interpolator);
        }
    }

    public Builder play(Animator animator) {
        if (animator == null) {
            return null;
        }
        this.mNeedsSort = true;
        return new Builder(animator);
    }

    @Override // com.nineoldandroids.animation.Animator
    public void cancel() {
        this.mTerminated = true;
        if (isStarted()) {
            ArrayList arrayList = null;
            if (this.mListeners != null) {
                arrayList = (ArrayList) this.mListeners.clone();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ((Animator.AnimatorListener) it.next()).onAnimationCancel(this);
                }
            }
            ValueAnimator valueAnimator = this.mDelayAnim;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mDelayAnim.cancel();
            } else if (this.mSortedNodes.size() > 0) {
                Iterator<Node> it2 = this.mSortedNodes.iterator();
                while (it2.hasNext()) {
                    it2.next().animation.cancel();
                }
            }
            if (arrayList != null) {
                Iterator it3 = arrayList.iterator();
                while (it3.hasNext()) {
                    ((Animator.AnimatorListener) it3.next()).onAnimationEnd(this);
                }
            }
            this.mStarted = false;
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void end() {
        this.mTerminated = true;
        if (isStarted()) {
            if (this.mSortedNodes.size() != this.mNodes.size()) {
                sortNodes();
                Iterator<Node> it = this.mSortedNodes.iterator();
                while (it.hasNext()) {
                    Node next = it.next();
                    if (this.mSetListener == null) {
                        this.mSetListener = new AnimatorSetListener(this);
                    }
                    next.animation.addListener(this.mSetListener);
                }
            }
            ValueAnimator valueAnimator = this.mDelayAnim;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (this.mSortedNodes.size() > 0) {
                Iterator<Node> it2 = this.mSortedNodes.iterator();
                while (it2.hasNext()) {
                    it2.next().animation.end();
                }
            }
            if (this.mListeners != null) {
                Iterator it3 = ((ArrayList) this.mListeners.clone()).iterator();
                while (it3.hasNext()) {
                    ((Animator.AnimatorListener) it3.next()).onAnimationEnd(this);
                }
            }
            this.mStarted = false;
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public boolean isRunning() {
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            if (it.next().animation.isRunning()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.nineoldandroids.animation.Animator
    public boolean isStarted() {
        return this.mStarted;
    }

    @Override // com.nineoldandroids.animation.Animator
    public long getStartDelay() {
        return this.mStartDelay;
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setStartDelay(long j) {
        this.mStartDelay = j;
    }

    @Override // com.nineoldandroids.animation.Animator
    public long getDuration() {
        return this.mDuration;
    }

    @Override // com.nineoldandroids.animation.Animator
    public AnimatorSet setDuration(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("duration must be a value of zero or greater");
        }
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            it.next().animation.setDuration(j);
        }
        this.mDuration = j;
        return this;
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setupStartValues() {
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            it.next().animation.setupStartValues();
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setupEndValues() {
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            it.next().animation.setupEndValues();
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void start() {
        this.mTerminated = false;
        this.mStarted = true;
        sortNodes();
        int size = this.mSortedNodes.size();
        for (int i = 0; i < size; i++) {
            Node node = this.mSortedNodes.get(i);
            ArrayList<Animator.AnimatorListener> listeners = node.animation.getListeners();
            if (listeners != null && listeners.size() > 0) {
                Iterator it = new ArrayList(listeners).iterator();
                while (it.hasNext()) {
                    Animator.AnimatorListener animatorListener = (Animator.AnimatorListener) it.next();
                    if ((animatorListener instanceof DependencyListener) || (animatorListener instanceof AnimatorSetListener)) {
                        node.animation.removeListener(animatorListener);
                    }
                }
            }
        }
        final ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < size; i2++) {
            Node node2 = this.mSortedNodes.get(i2);
            if (this.mSetListener == null) {
                this.mSetListener = new AnimatorSetListener(this);
            }
            if (node2.dependencies == null || node2.dependencies.size() == 0) {
                arrayList.add(node2);
            } else {
                int size2 = node2.dependencies.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    Dependency dependency = node2.dependencies.get(i3);
                    dependency.node.animation.addListener(new DependencyListener(this, node2, dependency.rule));
                }
                node2.tmpDependencies = (ArrayList) node2.dependencies.clone();
            }
            node2.animation.addListener(this.mSetListener);
        }
        if (this.mStartDelay <= 0) {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                Node node3 = (Node) it2.next();
                node3.animation.start();
                this.mPlayingSet.add(node3.animation);
            }
        } else {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mDelayAnim = ofFloat;
            ofFloat.setDuration(this.mStartDelay);
            this.mDelayAnim.addListener(new AnimatorListenerAdapter() { // from class: com.nineoldandroids.animation.AnimatorSet.1
                boolean canceled = false;

                @Override // com.nineoldandroids.animation.AnimatorListenerAdapter, com.nineoldandroids.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.canceled = true;
                }

                @Override // com.nineoldandroids.animation.AnimatorListenerAdapter, com.nineoldandroids.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (this.canceled) {
                        return;
                    }
                    int size3 = arrayList.size();
                    for (int i4 = 0; i4 < size3; i4++) {
                        Node node4 = (Node) arrayList.get(i4);
                        node4.animation.start();
                        AnimatorSet.this.mPlayingSet.add(node4.animation);
                    }
                }
            });
            this.mDelayAnim.start();
        }
        if (this.mListeners != null) {
            ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
            int size3 = arrayList2.size();
            for (int i4 = 0; i4 < size3; i4++) {
                ((Animator.AnimatorListener) arrayList2.get(i4)).onAnimationStart(this);
            }
        }
        if (this.mNodes.size() == 0 && this.mStartDelay == 0) {
            this.mStarted = false;
            if (this.mListeners != null) {
                ArrayList arrayList3 = (ArrayList) this.mListeners.clone();
                int size4 = arrayList3.size();
                for (int i5 = 0; i5 < size4; i5++) {
                    ((Animator.AnimatorListener) arrayList3.get(i5)).onAnimationEnd(this);
                }
            }
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    /* renamed from: clone */
    public AnimatorSet mo10clone() {
        AnimatorSet animatorSet = (AnimatorSet) super.mo10clone();
        animatorSet.mNeedsSort = true;
        animatorSet.mTerminated = false;
        animatorSet.mStarted = false;
        animatorSet.mPlayingSet = new ArrayList<>();
        animatorSet.mNodeMap = new HashMap<>();
        animatorSet.mNodes = new ArrayList<>();
        animatorSet.mSortedNodes = new ArrayList<>();
        HashMap hashMap = new HashMap();
        Iterator<Node> it = this.mNodes.iterator();
        while (it.hasNext()) {
            Node next = it.next();
            Node m11clone = next.m11clone();
            hashMap.put(next, m11clone);
            animatorSet.mNodes.add(m11clone);
            animatorSet.mNodeMap.put(m11clone.animation, m11clone);
            ArrayList arrayList = null;
            m11clone.dependencies = null;
            m11clone.tmpDependencies = null;
            m11clone.nodeDependents = null;
            m11clone.nodeDependencies = null;
            ArrayList<Animator.AnimatorListener> listeners = m11clone.animation.getListeners();
            if (listeners != null) {
                Iterator<Animator.AnimatorListener> it2 = listeners.iterator();
                while (it2.hasNext()) {
                    Animator.AnimatorListener next2 = it2.next();
                    if (next2 instanceof AnimatorSetListener) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(next2);
                    }
                }
                if (arrayList != null) {
                    Iterator it3 = arrayList.iterator();
                    while (it3.hasNext()) {
                        listeners.remove((Animator.AnimatorListener) it3.next());
                    }
                }
            }
        }
        Iterator<Node> it4 = this.mNodes.iterator();
        while (it4.hasNext()) {
            Node next3 = it4.next();
            Node node = (Node) hashMap.get(next3);
            if (next3.dependencies != null) {
                Iterator<Dependency> it5 = next3.dependencies.iterator();
                while (it5.hasNext()) {
                    Dependency next4 = it5.next();
                    node.addDependency(new Dependency((Node) hashMap.get(next4.node), next4.rule));
                }
            }
        }
        return animatorSet;
    }

    private static class DependencyListener implements Animator.AnimatorListener {
        private AnimatorSet mAnimatorSet;
        private Node mNode;
        private int mRule;

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        public DependencyListener(AnimatorSet animatorSet, Node node, int i) {
            this.mAnimatorSet = animatorSet;
            this.mNode = node;
            this.mRule = i;
        }

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.mRule == 1) {
                startIfReady(animator);
            }
        }

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.mRule == 0) {
                startIfReady(animator);
            }
        }

        private void startIfReady(Animator animator) {
            if (this.mAnimatorSet.mTerminated) {
                return;
            }
            Dependency dependency = null;
            int size = this.mNode.tmpDependencies.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                Dependency dependency2 = this.mNode.tmpDependencies.get(i);
                if (dependency2.rule == this.mRule && dependency2.node.animation == animator) {
                    animator.removeListener(this);
                    dependency = dependency2;
                    break;
                }
                i++;
            }
            this.mNode.tmpDependencies.remove(dependency);
            if (this.mNode.tmpDependencies.size() == 0) {
                this.mNode.animation.start();
                this.mAnimatorSet.mPlayingSet.add(this.mNode.animation);
            }
        }
    }

    private class AnimatorSetListener implements Animator.AnimatorListener {
        private AnimatorSet mAnimatorSet;

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        AnimatorSetListener(AnimatorSet animatorSet) {
            this.mAnimatorSet = animatorSet;
        }

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (AnimatorSet.this.mTerminated || AnimatorSet.this.mPlayingSet.size() != 0 || AnimatorSet.this.mListeners == null) {
                return;
            }
            int size = AnimatorSet.this.mListeners.size();
            for (int i = 0; i < size; i++) {
                AnimatorSet.this.mListeners.get(i).onAnimationCancel(this.mAnimatorSet);
            }
        }

        @Override // com.nineoldandroids.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            animator.removeListener(this);
            AnimatorSet.this.mPlayingSet.remove(animator);
            boolean z = true;
            ((Node) this.mAnimatorSet.mNodeMap.get(animator)).done = true;
            if (AnimatorSet.this.mTerminated) {
                return;
            }
            ArrayList arrayList = this.mAnimatorSet.mSortedNodes;
            int size = arrayList.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                if (!((Node) arrayList.get(i)).done) {
                    z = false;
                    break;
                }
                i++;
            }
            if (z) {
                if (AnimatorSet.this.mListeners != null) {
                    ArrayList arrayList2 = (ArrayList) AnimatorSet.this.mListeners.clone();
                    int size2 = arrayList2.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        ((Animator.AnimatorListener) arrayList2.get(i2)).onAnimationEnd(this.mAnimatorSet);
                    }
                }
                this.mAnimatorSet.mStarted = false;
            }
        }
    }

    private void sortNodes() {
        if (this.mNeedsSort) {
            this.mSortedNodes.clear();
            ArrayList arrayList = new ArrayList();
            int size = this.mNodes.size();
            for (int i = 0; i < size; i++) {
                Node node = this.mNodes.get(i);
                if (node.dependencies == null || node.dependencies.size() == 0) {
                    arrayList.add(node);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            while (arrayList.size() > 0) {
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    Node node2 = (Node) arrayList.get(i2);
                    this.mSortedNodes.add(node2);
                    if (node2.nodeDependents != null) {
                        int size3 = node2.nodeDependents.size();
                        for (int i3 = 0; i3 < size3; i3++) {
                            Node node3 = node2.nodeDependents.get(i3);
                            node3.nodeDependencies.remove(node2);
                            if (node3.nodeDependencies.size() == 0) {
                                arrayList2.add(node3);
                            }
                        }
                    }
                }
                arrayList.clear();
                arrayList.addAll(arrayList2);
                arrayList2.clear();
            }
            this.mNeedsSort = false;
            if (this.mSortedNodes.size() != this.mNodes.size()) {
                throw new IllegalStateException("Circular dependencies cannot exist in AnimatorSet");
            }
            return;
        }
        int size4 = this.mNodes.size();
        for (int i4 = 0; i4 < size4; i4++) {
            Node node4 = this.mNodes.get(i4);
            if (node4.dependencies != null && node4.dependencies.size() > 0) {
                int size5 = node4.dependencies.size();
                for (int i5 = 0; i5 < size5; i5++) {
                    Dependency dependency = node4.dependencies.get(i5);
                    if (node4.nodeDependencies == null) {
                        node4.nodeDependencies = new ArrayList<>();
                    }
                    if (!node4.nodeDependencies.contains(dependency.node)) {
                        node4.nodeDependencies.add(dependency.node);
                    }
                }
            }
            node4.done = false;
        }
    }

    private static class Dependency {
        static final int AFTER = 1;
        static final int WITH = 0;
        public Node node;
        public int rule;

        public Dependency(Node node, int i) {
            this.node = node;
            this.rule = i;
        }
    }

    private static class Node implements Cloneable {
        public Animator animation;
        public ArrayList<Dependency> dependencies = null;
        public ArrayList<Dependency> tmpDependencies = null;
        public ArrayList<Node> nodeDependencies = null;
        public ArrayList<Node> nodeDependents = null;
        public boolean done = false;

        public Node(Animator animator) {
            this.animation = animator;
        }

        public void addDependency(Dependency dependency) {
            if (this.dependencies == null) {
                this.dependencies = new ArrayList<>();
                this.nodeDependencies = new ArrayList<>();
            }
            this.dependencies.add(dependency);
            if (!this.nodeDependencies.contains(dependency.node)) {
                this.nodeDependencies.add(dependency.node);
            }
            Node node = dependency.node;
            if (node.nodeDependents == null) {
                node.nodeDependents = new ArrayList<>();
            }
            node.nodeDependents.add(this);
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Node m11clone() {
            try {
                Node node = (Node) super.clone();
                node.animation = this.animation.mo10clone();
                return node;
            } catch (CloneNotSupportedException unused) {
                throw new AssertionError();
            }
        }
    }

    public class Builder {
        private Node mCurrentNode;

        Builder(Animator animator) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(animator);
            this.mCurrentNode = node;
            if (node == null) {
                this.mCurrentNode = new Node(animator);
                AnimatorSet.this.mNodeMap.put(animator, this.mCurrentNode);
                AnimatorSet.this.mNodes.add(this.mCurrentNode);
            }
        }

        public Builder with(Animator animator) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(animator);
            if (node == null) {
                node = new Node(animator);
                AnimatorSet.this.mNodeMap.put(animator, node);
                AnimatorSet.this.mNodes.add(node);
            }
            node.addDependency(new Dependency(this.mCurrentNode, 0));
            return this;
        }

        public Builder before(Animator animator) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(animator);
            if (node == null) {
                node = new Node(animator);
                AnimatorSet.this.mNodeMap.put(animator, node);
                AnimatorSet.this.mNodes.add(node);
            }
            node.addDependency(new Dependency(this.mCurrentNode, 1));
            return this;
        }

        public Builder after(Animator animator) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(animator);
            if (node == null) {
                node = new Node(animator);
                AnimatorSet.this.mNodeMap.put(animator, node);
                AnimatorSet.this.mNodes.add(node);
            }
            this.mCurrentNode.addDependency(new Dependency(node, 1));
            return this;
        }

        public Builder after(long j) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setDuration(j);
            after(ofFloat);
            return this;
        }
    }
}
