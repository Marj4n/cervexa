package rx;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.operators.CompletableOnSubscribeConcat;
import rx.internal.operators.CompletableOnSubscribeConcatArray;
import rx.internal.operators.CompletableOnSubscribeConcatIterable;
import rx.internal.operators.CompletableOnSubscribeMerge;
import rx.internal.operators.CompletableOnSubscribeMergeArray;
import rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray;
import rx.internal.operators.CompletableOnSubscribeMergeDelayErrorIterable;
import rx.internal.operators.CompletableOnSubscribeMergeIterable;
import rx.internal.operators.CompletableOnSubscribeTimeout;
import rx.internal.util.SubscriptionList;
import rx.internal.util.UtilityFunctions;
import rx.observers.SafeCompletableSubscriber;
import rx.observers.SafeSubscriber;
import rx.plugins.RxJavaCompletableExecutionHook;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.MultipleAssignmentSubscription;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes2.dex */
public class Completable {
    private final CompletableOnSubscribe onSubscribe;
    static final RxJavaErrorHandler ERROR_HANDLER = RxJavaPlugins.getInstance().getErrorHandler();
    static RxJavaCompletableExecutionHook HOOK = RxJavaPlugins.getInstance().getCompletableExecutionHook();
    static final Completable COMPLETE = create(new CompletableOnSubscribe() { // from class: rx.Completable.1
        @Override // rx.functions.Action1
        public void call(CompletableSubscriber completableSubscriber) {
            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
            completableSubscriber.onCompleted();
        }
    });
    static final Completable NEVER = create(new CompletableOnSubscribe() { // from class: rx.Completable.2
        @Override // rx.functions.Action1
        public void call(CompletableSubscriber completableSubscriber) {
            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
        }
    });

    public interface CompletableOnSubscribe extends Action1<CompletableSubscriber> {
    }

    public interface CompletableOperator extends Func1<CompletableSubscriber, CompletableSubscriber> {
    }

    public interface CompletableSubscriber {
        void onCompleted();

        void onError(Throwable th);

        void onSubscribe(Subscription subscription);
    }

    public interface CompletableTransformer extends Func1<Completable, Completable> {
    }

    public static Completable amb(final Completable... completableArr) {
        requireNonNull(completableArr);
        if (completableArr.length == 0) {
            return complete();
        }
        if (completableArr.length == 1) {
            return completableArr[0];
        }
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.3
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                final CompositeSubscription compositeSubscription = new CompositeSubscription();
                completableSubscriber.onSubscribe(compositeSubscription);
                final AtomicBoolean atomicBoolean = new AtomicBoolean();
                CompletableSubscriber completableSubscriber2 = new CompletableSubscriber() { // from class: rx.Completable.3.1
                    @Override // rx.Completable.CompletableSubscriber
                    public void onCompleted() {
                        if (atomicBoolean.compareAndSet(false, true)) {
                            compositeSubscription.unsubscribe();
                            completableSubscriber.onCompleted();
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onError(Throwable th) {
                        if (atomicBoolean.compareAndSet(false, true)) {
                            compositeSubscription.unsubscribe();
                            completableSubscriber.onError(th);
                        } else {
                            Completable.ERROR_HANDLER.handleError(th);
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onSubscribe(Subscription subscription) {
                        compositeSubscription.add(subscription);
                    }
                };
                for (Completable completable : completableArr) {
                    if (compositeSubscription.isUnsubscribed()) {
                        return;
                    }
                    if (completable == null) {
                        Throwable nullPointerException = new NullPointerException("One of the sources is null");
                        if (atomicBoolean.compareAndSet(false, true)) {
                            compositeSubscription.unsubscribe();
                            completableSubscriber.onError(nullPointerException);
                            return;
                        } else {
                            Completable.ERROR_HANDLER.handleError(nullPointerException);
                            return;
                        }
                    }
                    if (atomicBoolean.get() || compositeSubscription.isUnsubscribed()) {
                        return;
                    }
                    completable.unsafeSubscribe(completableSubscriber2);
                }
            }
        });
    }

    public static Completable amb(final Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.4
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                final CompositeSubscription compositeSubscription = new CompositeSubscription();
                completableSubscriber.onSubscribe(compositeSubscription);
                final AtomicBoolean atomicBoolean = new AtomicBoolean();
                CompletableSubscriber completableSubscriber2 = new CompletableSubscriber() { // from class: rx.Completable.4.1
                    @Override // rx.Completable.CompletableSubscriber
                    public void onCompleted() {
                        if (atomicBoolean.compareAndSet(false, true)) {
                            compositeSubscription.unsubscribe();
                            completableSubscriber.onCompleted();
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onError(Throwable th) {
                        if (atomicBoolean.compareAndSet(false, true)) {
                            compositeSubscription.unsubscribe();
                            completableSubscriber.onError(th);
                        } else {
                            Completable.ERROR_HANDLER.handleError(th);
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onSubscribe(Subscription subscription) {
                        compositeSubscription.add(subscription);
                    }
                };
                try {
                    Iterator it = iterable.iterator();
                    if (it == null) {
                        completableSubscriber.onError(new NullPointerException("The iterator returned is null"));
                        return;
                    }
                    boolean z = true;
                    while (!atomicBoolean.get() && !compositeSubscription.isUnsubscribed()) {
                        try {
                            if (!it.hasNext()) {
                                if (z) {
                                    completableSubscriber.onCompleted();
                                    return;
                                }
                                return;
                            }
                            if (atomicBoolean.get() || compositeSubscription.isUnsubscribed()) {
                                return;
                            }
                            try {
                                Completable completable = (Completable) it.next();
                                if (completable == null) {
                                    Throwable nullPointerException = new NullPointerException("One of the sources is null");
                                    if (atomicBoolean.compareAndSet(false, true)) {
                                        compositeSubscription.unsubscribe();
                                        completableSubscriber.onError(nullPointerException);
                                        return;
                                    } else {
                                        Completable.ERROR_HANDLER.handleError(nullPointerException);
                                        return;
                                    }
                                }
                                if (atomicBoolean.get() || compositeSubscription.isUnsubscribed()) {
                                    return;
                                }
                                completable.unsafeSubscribe(completableSubscriber2);
                                z = false;
                            } catch (Throwable th) {
                                if (atomicBoolean.compareAndSet(false, true)) {
                                    compositeSubscription.unsubscribe();
                                    completableSubscriber.onError(th);
                                    return;
                                } else {
                                    Completable.ERROR_HANDLER.handleError(th);
                                    return;
                                }
                            }
                        } catch (Throwable th2) {
                            if (atomicBoolean.compareAndSet(false, true)) {
                                compositeSubscription.unsubscribe();
                                completableSubscriber.onError(th2);
                                return;
                            } else {
                                Completable.ERROR_HANDLER.handleError(th2);
                                return;
                            }
                        }
                    }
                } catch (Throwable th3) {
                    completableSubscriber.onError(th3);
                }
            }
        });
    }

    public static Completable complete() {
        return COMPLETE;
    }

    public static Completable concat(Completable... completableArr) {
        requireNonNull(completableArr);
        if (completableArr.length == 0) {
            return complete();
        }
        if (completableArr.length == 1) {
            return completableArr[0];
        }
        return create(new CompletableOnSubscribeConcatArray(completableArr));
    }

    public static Completable concat(Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new CompletableOnSubscribeConcatIterable(iterable));
    }

    public static Completable concat(Observable<? extends Completable> observable) {
        return concat(observable, 2);
    }

    public static Completable concat(Observable<? extends Completable> observable, int i) {
        requireNonNull(observable);
        if (i < 1) {
            throw new IllegalArgumentException("prefetch > 0 required but it was " + i);
        }
        return create(new CompletableOnSubscribeConcat(observable, i));
    }

    public static Completable create(CompletableOnSubscribe completableOnSubscribe) {
        requireNonNull(completableOnSubscribe);
        try {
            return new Completable(completableOnSubscribe);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            ERROR_HANDLER.handleError(th);
            throw toNpe(th);
        }
    }

    public static Completable defer(final Func0<? extends Completable> func0) {
        requireNonNull(func0);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.5
            @Override // rx.functions.Action1
            public void call(CompletableSubscriber completableSubscriber) {
                try {
                    Completable completable = (Completable) Func0.this.call();
                    if (completable == null) {
                        completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                        completableSubscriber.onError(new NullPointerException("The completable returned is null"));
                    } else {
                        completable.unsafeSubscribe(completableSubscriber);
                    }
                } catch (Throwable th) {
                    completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                    completableSubscriber.onError(th);
                }
            }
        });
    }

    public static Completable error(final Func0<? extends Throwable> func0) {
        requireNonNull(func0);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.6
            @Override // rx.functions.Action1
            public void call(CompletableSubscriber completableSubscriber) {
                completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                try {
                    th = (Throwable) Func0.this.call();
                } catch (Throwable th) {
                    th = th;
                }
                if (th == null) {
                    th = new NullPointerException("The error supplied is null");
                }
                completableSubscriber.onError(th);
            }
        });
    }

    public static Completable error(final Throwable th) {
        requireNonNull(th);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.7
            @Override // rx.functions.Action1
            public void call(CompletableSubscriber completableSubscriber) {
                completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                completableSubscriber.onError(th);
            }
        });
    }

    public static Completable fromAction(final Action0 action0) {
        requireNonNull(action0);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.8
            @Override // rx.functions.Action1
            public void call(CompletableSubscriber completableSubscriber) {
                BooleanSubscription booleanSubscription = new BooleanSubscription();
                completableSubscriber.onSubscribe(booleanSubscription);
                try {
                    Action0.this.call();
                    if (booleanSubscription.isUnsubscribed()) {
                        return;
                    }
                    completableSubscriber.onCompleted();
                } catch (Throwable th) {
                    if (booleanSubscription.isUnsubscribed()) {
                        return;
                    }
                    completableSubscriber.onError(th);
                }
            }
        });
    }

    public static Completable fromCallable(final Callable<?> callable) {
        requireNonNull(callable);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.9
            @Override // rx.functions.Action1
            public void call(CompletableSubscriber completableSubscriber) {
                BooleanSubscription booleanSubscription = new BooleanSubscription();
                completableSubscriber.onSubscribe(booleanSubscription);
                try {
                    callable.call();
                    if (booleanSubscription.isUnsubscribed()) {
                        return;
                    }
                    completableSubscriber.onCompleted();
                } catch (Throwable th) {
                    if (booleanSubscription.isUnsubscribed()) {
                        return;
                    }
                    completableSubscriber.onError(th);
                }
            }
        });
    }

    public static Completable fromFuture(Future<?> future) {
        requireNonNull(future);
        return fromObservable(Observable.from(future));
    }

    public static Completable fromObservable(final Observable<?> observable) {
        requireNonNull(observable);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.10
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                Subscriber<Object> subscriber = new Subscriber<Object>() { // from class: rx.Completable.10.1
                    @Override // rx.Observer
                    public void onNext(Object obj) {
                    }

                    @Override // rx.Observer
                    public void onCompleted() {
                        completableSubscriber.onCompleted();
                    }

                    @Override // rx.Observer
                    public void onError(Throwable th) {
                        completableSubscriber.onError(th);
                    }
                };
                completableSubscriber.onSubscribe(subscriber);
                Observable.this.unsafeSubscribe(subscriber);
            }
        });
    }

    public static Completable fromSingle(final Single<?> single) {
        requireNonNull(single);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.11
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                SingleSubscriber<Object> singleSubscriber = new SingleSubscriber<Object>() { // from class: rx.Completable.11.1
                    @Override // rx.SingleSubscriber
                    public void onError(Throwable th) {
                        completableSubscriber.onError(th);
                    }

                    @Override // rx.SingleSubscriber
                    public void onSuccess(Object obj) {
                        completableSubscriber.onCompleted();
                    }
                };
                completableSubscriber.onSubscribe(singleSubscriber);
                Single.this.subscribe(singleSubscriber);
            }
        });
    }

    public static Completable merge(Completable... completableArr) {
        requireNonNull(completableArr);
        if (completableArr.length == 0) {
            return complete();
        }
        if (completableArr.length == 1) {
            return completableArr[0];
        }
        return create(new CompletableOnSubscribeMergeArray(completableArr));
    }

    public static Completable merge(Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new CompletableOnSubscribeMergeIterable(iterable));
    }

    public static Completable merge(Observable<? extends Completable> observable) {
        return merge0(observable, Integer.MAX_VALUE, false);
    }

    public static Completable merge(Observable<? extends Completable> observable, int i) {
        return merge0(observable, i, false);
    }

    protected static Completable merge0(Observable<? extends Completable> observable, int i, boolean z) {
        requireNonNull(observable);
        if (i < 1) {
            throw new IllegalArgumentException("maxConcurrency > 0 required but it was " + i);
        }
        return create(new CompletableOnSubscribeMerge(observable, i, z));
    }

    public static Completable mergeDelayError(Completable... completableArr) {
        requireNonNull(completableArr);
        return create(new CompletableOnSubscribeMergeDelayErrorArray(completableArr));
    }

    public static Completable mergeDelayError(Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new CompletableOnSubscribeMergeDelayErrorIterable(iterable));
    }

    public static Completable mergeDelayError(Observable<? extends Completable> observable) {
        return merge0(observable, Integer.MAX_VALUE, true);
    }

    public static Completable mergeDelayError(Observable<? extends Completable> observable, int i) {
        return merge0(observable, i, true);
    }

    public static Completable never() {
        return NEVER;
    }

    static <T> T requireNonNull(T t) {
        if (t != null) {
            return t;
        }
        throw null;
    }

    public static Completable timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    public static Completable timer(final long j, final TimeUnit timeUnit, final Scheduler scheduler) {
        requireNonNull(timeUnit);
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.12
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                MultipleAssignmentSubscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
                completableSubscriber.onSubscribe(multipleAssignmentSubscription);
                if (multipleAssignmentSubscription.isUnsubscribed()) {
                    return;
                }
                final Scheduler.Worker createWorker = Scheduler.this.createWorker();
                multipleAssignmentSubscription.set(createWorker);
                createWorker.schedule(new Action0() { // from class: rx.Completable.12.1
                    @Override // rx.functions.Action0
                    public void call() {
                        try {
                            completableSubscriber.onCompleted();
                        } finally {
                            createWorker.unsubscribe();
                        }
                    }
                }, j, timeUnit);
            }
        });
    }

    static NullPointerException toNpe(Throwable th) {
        NullPointerException nullPointerException = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
        nullPointerException.initCause(th);
        return nullPointerException;
    }

    public static <R> Completable using(Func0<R> func0, Func1<? super R, ? extends Completable> func1, Action1<? super R> action1) {
        return using(func0, func1, action1, true);
    }

    public static <R> Completable using(final Func0<R> func0, final Func1<? super R, ? extends Completable> func1, final Action1<? super R> action1, final boolean z) {
        requireNonNull(func0);
        requireNonNull(func1);
        requireNonNull(action1);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.13
            @Override // rx.functions.Action1
            public void call(CompletableSubscriber completableSubscriber) {
                try {
                    Object call = Func0.this.call();
                    try {
                        Completable completable = (Completable) func1.call(call);
                        if (completable == null) {
                            try {
                                action1.call(call);
                                completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                                completableSubscriber.onError(new NullPointerException("The completable supplied is null"));
                                return;
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                                completableSubscriber.onError(new CompositeException(Arrays.asList(new NullPointerException("The completable supplied is null"), th)));
                                return;
                            }
                        }
                        completable.unsafeSubscribe(new AnonymousClass1(new AtomicBoolean(), call, completableSubscriber));
                    } catch (Throwable th2) {
                        try {
                            action1.call(call);
                            Exceptions.throwIfFatal(th2);
                            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                            completableSubscriber.onError(th2);
                        } catch (Throwable th3) {
                            Exceptions.throwIfFatal(th2);
                            Exceptions.throwIfFatal(th3);
                            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                            completableSubscriber.onError(new CompositeException(Arrays.asList(th2, th3)));
                        }
                    }
                } catch (Throwable th4) {
                    completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                    completableSubscriber.onError(th4);
                }
            }

            /* renamed from: rx.Completable$13$1, reason: invalid class name */
            class AnonymousClass1 implements CompletableSubscriber {
                Subscription d;
                final /* synthetic */ AtomicBoolean val$once;
                final /* synthetic */ Object val$resource;
                final /* synthetic */ CompletableSubscriber val$s;

                AnonymousClass1(AtomicBoolean atomicBoolean, Object obj, CompletableSubscriber completableSubscriber) {
                    this.val$once = atomicBoolean;
                    this.val$resource = obj;
                    this.val$s = completableSubscriber;
                }

                void dispose() {
                    this.d.unsubscribe();
                    if (this.val$once.compareAndSet(false, true)) {
                        try {
                            action1.call(this.val$resource);
                        } catch (Throwable th) {
                            Completable.ERROR_HANDLER.handleError(th);
                        }
                    }
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onCompleted() {
                    if (z && this.val$once.compareAndSet(false, true)) {
                        try {
                            action1.call(this.val$resource);
                        } catch (Throwable th) {
                            this.val$s.onError(th);
                            return;
                        }
                    }
                    this.val$s.onCompleted();
                    if (z) {
                        return;
                    }
                    dispose();
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onError(Throwable th) {
                    if (z && this.val$once.compareAndSet(false, true)) {
                        try {
                            action1.call(this.val$resource);
                        } catch (Throwable th2) {
                            th = new CompositeException(Arrays.asList(th, th2));
                        }
                    }
                    this.val$s.onError(th);
                    if (z) {
                        return;
                    }
                    dispose();
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onSubscribe(Subscription subscription) {
                    this.d = subscription;
                    this.val$s.onSubscribe(Subscriptions.create(new Action0() { // from class: rx.Completable.13.1.1
                        @Override // rx.functions.Action0
                        public void call() {
                            AnonymousClass1.this.dispose();
                        }
                    }));
                }
            }
        });
    }

    protected Completable(CompletableOnSubscribe completableOnSubscribe) {
        this.onSubscribe = HOOK.onCreate(completableOnSubscribe);
    }

    public final Completable ambWith(Completable completable) {
        requireNonNull(completable);
        return amb(this, completable);
    }

    public final void await() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Throwable[] thArr = new Throwable[1];
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.14
            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                countDownLatch.countDown();
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                thArr[0] = th;
                countDownLatch.countDown();
            }
        });
        if (countDownLatch.getCount() == 0) {
            if (thArr[0] != null) {
                Exceptions.propagate(thArr[0]);
            }
        } else {
            try {
                countDownLatch.await();
                if (thArr[0] != null) {
                    Exceptions.propagate(thArr[0]);
                }
            } catch (InterruptedException e) {
                throw Exceptions.propagate(e);
            }
        }
    }

    public final boolean await(long j, TimeUnit timeUnit) {
        requireNonNull(timeUnit);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Throwable[] thArr = new Throwable[1];
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.15
            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                countDownLatch.countDown();
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                thArr[0] = th;
                countDownLatch.countDown();
            }
        });
        if (countDownLatch.getCount() == 0) {
            if (thArr[0] != null) {
                Exceptions.propagate(thArr[0]);
            }
            return true;
        }
        try {
            boolean await = countDownLatch.await(j, timeUnit);
            if (await && thArr[0] != null) {
                Exceptions.propagate(thArr[0]);
            }
            return await;
        } catch (InterruptedException e) {
            throw Exceptions.propagate(e);
        }
    }

    public final Completable compose(CompletableTransformer completableTransformer) {
        return (Completable) to(completableTransformer);
    }

    public final <T> Observable<T> andThen(Observable<T> observable) {
        requireNonNull(observable);
        return observable.delaySubscription(toObservable());
    }

    public final <T> Single<T> andThen(Single<T> single) {
        requireNonNull(single);
        return single.delaySubscription(toObservable());
    }

    public final Completable andThen(Completable completable) {
        return concatWith(completable);
    }

    public final Completable concatWith(Completable completable) {
        requireNonNull(completable);
        return concat(this, completable);
    }

    public final Completable delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    public final Completable delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    public final Completable delay(final long j, final TimeUnit timeUnit, final Scheduler scheduler, final boolean z) {
        requireNonNull(timeUnit);
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.16
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                final CompositeSubscription compositeSubscription = new CompositeSubscription();
                final Scheduler.Worker createWorker = scheduler.createWorker();
                compositeSubscription.add(createWorker);
                Completable.this.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.16.1
                    @Override // rx.Completable.CompletableSubscriber
                    public void onCompleted() {
                        compositeSubscription.add(createWorker.schedule(new Action0() { // from class: rx.Completable.16.1.1
                            @Override // rx.functions.Action0
                            public void call() {
                                try {
                                    completableSubscriber.onCompleted();
                                } finally {
                                    createWorker.unsubscribe();
                                }
                            }
                        }, j, timeUnit));
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onError(final Throwable th) {
                        if (z) {
                            compositeSubscription.add(createWorker.schedule(new Action0() { // from class: rx.Completable.16.1.2
                                @Override // rx.functions.Action0
                                public void call() {
                                    try {
                                        completableSubscriber.onError(th);
                                    } finally {
                                        createWorker.unsubscribe();
                                    }
                                }
                            }, j, timeUnit));
                        } else {
                            completableSubscriber.onError(th);
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onSubscribe(Subscription subscription) {
                        compositeSubscription.add(subscription);
                        completableSubscriber.onSubscribe(compositeSubscription);
                    }
                });
            }
        });
    }

    @Deprecated
    public final Completable doOnComplete(Action0 action0) {
        return doOnCompleted(action0);
    }

    public final Completable doOnCompleted(Action0 action0) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), action0, Actions.empty(), Actions.empty());
    }

    public final Completable doOnUnsubscribe(Action0 action0) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty(), action0);
    }

    public final Completable doOnError(Action1<? super Throwable> action1) {
        return doOnLifecycle(Actions.empty(), action1, Actions.empty(), Actions.empty(), Actions.empty());
    }

    protected final Completable doOnLifecycle(Action1<? super Subscription> action1, Action1<? super Throwable> action12, Action0 action0, Action0 action02, Action0 action03) {
        requireNonNull(action1);
        requireNonNull(action12);
        requireNonNull(action0);
        requireNonNull(action02);
        requireNonNull(action03);
        return create(new AnonymousClass17(action0, action02, action12, action1, action03));
    }

    /* renamed from: rx.Completable$17, reason: invalid class name */
    class AnonymousClass17 implements CompletableOnSubscribe {
        final /* synthetic */ Action0 val$onAfterComplete;
        final /* synthetic */ Action0 val$onComplete;
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onSubscribe;
        final /* synthetic */ Action0 val$onUnsubscribe;

        AnonymousClass17(Action0 action0, Action0 action02, Action1 action1, Action1 action12, Action0 action03) {
            this.val$onComplete = action0;
            this.val$onAfterComplete = action02;
            this.val$onError = action1;
            this.val$onSubscribe = action12;
            this.val$onUnsubscribe = action03;
        }

        @Override // rx.functions.Action1
        public void call(final CompletableSubscriber completableSubscriber) {
            Completable.this.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.17.1
                @Override // rx.Completable.CompletableSubscriber
                public void onCompleted() {
                    try {
                        AnonymousClass17.this.val$onComplete.call();
                        completableSubscriber.onCompleted();
                        try {
                            AnonymousClass17.this.val$onAfterComplete.call();
                        } catch (Throwable th) {
                            Completable.ERROR_HANDLER.handleError(th);
                        }
                    } catch (Throwable th2) {
                        completableSubscriber.onError(th2);
                    }
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onError(Throwable th) {
                    try {
                        AnonymousClass17.this.val$onError.call(th);
                    } catch (Throwable th2) {
                        th = new CompositeException(Arrays.asList(th, th2));
                    }
                    completableSubscriber.onError(th);
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onSubscribe(final Subscription subscription) {
                    try {
                        AnonymousClass17.this.val$onSubscribe.call(subscription);
                        completableSubscriber.onSubscribe(Subscriptions.create(new Action0() { // from class: rx.Completable.17.1.1
                            @Override // rx.functions.Action0
                            public void call() {
                                try {
                                    AnonymousClass17.this.val$onUnsubscribe.call();
                                } catch (Throwable th) {
                                    Completable.ERROR_HANDLER.handleError(th);
                                }
                                subscription.unsubscribe();
                            }
                        }));
                    } catch (Throwable th) {
                        subscription.unsubscribe();
                        completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                        completableSubscriber.onError(th);
                    }
                }
            });
        }
    }

    public final Completable doOnSubscribe(Action1<? super Subscription> action1) {
        return doOnLifecycle(action1, Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty());
    }

    public final Completable doOnTerminate(final Action0 action0) {
        return doOnLifecycle(Actions.empty(), new Action1<Throwable>() { // from class: rx.Completable.18
            @Override // rx.functions.Action1
            public void call(Throwable th) {
                action0.call();
            }
        }, action0, Actions.empty(), Actions.empty());
    }

    @Deprecated
    public final Completable endWith(Completable completable) {
        return andThen(completable);
    }

    @Deprecated
    public final <T> Observable<T> endWith(Observable<T> observable) {
        return andThen(observable);
    }

    public final Completable doAfterTerminate(Action0 action0) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), action0, Actions.empty());
    }

    public final Throwable get() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Throwable[] thArr = new Throwable[1];
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.19
            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                countDownLatch.countDown();
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                thArr[0] = th;
                countDownLatch.countDown();
            }
        });
        if (countDownLatch.getCount() == 0) {
            return thArr[0];
        }
        try {
            countDownLatch.await();
            return thArr[0];
        } catch (InterruptedException e) {
            throw Exceptions.propagate(e);
        }
    }

    public final Throwable get(long j, TimeUnit timeUnit) {
        requireNonNull(timeUnit);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Throwable[] thArr = new Throwable[1];
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.20
            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                countDownLatch.countDown();
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                thArr[0] = th;
                countDownLatch.countDown();
            }
        });
        if (countDownLatch.getCount() == 0) {
            return thArr[0];
        }
        try {
            if (countDownLatch.await(j, timeUnit)) {
                return thArr[0];
            }
            Exceptions.propagate(new TimeoutException());
            return null;
        } catch (InterruptedException e) {
            throw Exceptions.propagate(e);
        }
    }

    public final Completable lift(final CompletableOperator completableOperator) {
        requireNonNull(completableOperator);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.21
            @Override // rx.functions.Action1
            public void call(CompletableSubscriber completableSubscriber) {
                try {
                    Completable.this.unsafeSubscribe(Completable.HOOK.onLift(completableOperator).call(completableSubscriber));
                } catch (NullPointerException e) {
                    throw e;
                } catch (Throwable th) {
                    throw Completable.toNpe(th);
                }
            }
        });
    }

    public final Completable mergeWith(Completable completable) {
        requireNonNull(completable);
        return merge(this, completable);
    }

    public final Completable observeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.22
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                final SubscriptionList subscriptionList = new SubscriptionList();
                final Scheduler.Worker createWorker = scheduler.createWorker();
                subscriptionList.add(createWorker);
                completableSubscriber.onSubscribe(subscriptionList);
                Completable.this.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.22.1
                    @Override // rx.Completable.CompletableSubscriber
                    public void onCompleted() {
                        createWorker.schedule(new Action0() { // from class: rx.Completable.22.1.1
                            @Override // rx.functions.Action0
                            public void call() {
                                try {
                                    completableSubscriber.onCompleted();
                                } finally {
                                    subscriptionList.unsubscribe();
                                }
                            }
                        });
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onError(final Throwable th) {
                        createWorker.schedule(new Action0() { // from class: rx.Completable.22.1.2
                            @Override // rx.functions.Action0
                            public void call() {
                                try {
                                    completableSubscriber.onError(th);
                                } finally {
                                    subscriptionList.unsubscribe();
                                }
                            }
                        });
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onSubscribe(Subscription subscription) {
                        subscriptionList.add(subscription);
                    }
                });
            }
        });
    }

    public final Completable onErrorComplete() {
        return onErrorComplete(UtilityFunctions.alwaysTrue());
    }

    public final Completable onErrorComplete(final Func1<? super Throwable, Boolean> func1) {
        requireNonNull(func1);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.23
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                Completable.this.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.23.1
                    @Override // rx.Completable.CompletableSubscriber
                    public void onCompleted() {
                        completableSubscriber.onCompleted();
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onError(Throwable th) {
                        try {
                            if (((Boolean) func1.call(th)).booleanValue()) {
                                completableSubscriber.onCompleted();
                            } else {
                                completableSubscriber.onError(th);
                            }
                        } catch (Throwable th2) {
                            new CompositeException(Arrays.asList(th, th2));
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onSubscribe(Subscription subscription) {
                        completableSubscriber.onSubscribe(subscription);
                    }
                });
            }
        });
    }

    public final Completable onErrorResumeNext(final Func1<? super Throwable, ? extends Completable> func1) {
        requireNonNull(func1);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.24
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                final SerialSubscription serialSubscription = new SerialSubscription();
                Completable.this.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.24.1
                    @Override // rx.Completable.CompletableSubscriber
                    public void onCompleted() {
                        completableSubscriber.onCompleted();
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onError(Throwable th) {
                        try {
                            Completable completable = (Completable) func1.call(th);
                            if (completable == null) {
                                completableSubscriber.onError(new CompositeException(Arrays.asList(th, new NullPointerException("The completable returned is null"))));
                            } else {
                                completable.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.24.1.1
                                    @Override // rx.Completable.CompletableSubscriber
                                    public void onCompleted() {
                                        completableSubscriber.onCompleted();
                                    }

                                    @Override // rx.Completable.CompletableSubscriber
                                    public void onError(Throwable th2) {
                                        completableSubscriber.onError(th2);
                                    }

                                    @Override // rx.Completable.CompletableSubscriber
                                    public void onSubscribe(Subscription subscription) {
                                        serialSubscription.set(subscription);
                                    }
                                });
                            }
                        } catch (Throwable th2) {
                            completableSubscriber.onError(new CompositeException(Arrays.asList(th, th2)));
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onSubscribe(Subscription subscription) {
                        serialSubscription.set(subscription);
                    }
                });
            }
        });
    }

    public final Completable repeat() {
        return fromObservable(toObservable().repeat());
    }

    public final Completable repeat(long j) {
        return fromObservable(toObservable().repeat(j));
    }

    public final Completable repeatWhen(Func1<? super Observable<? extends Void>, ? extends Observable<?>> func1) {
        requireNonNull(func1);
        return fromObservable(toObservable().repeatWhen(func1));
    }

    public final Completable retry() {
        return fromObservable(toObservable().retry());
    }

    public final Completable retry(Func2<Integer, Throwable, Boolean> func2) {
        return fromObservable(toObservable().retry(func2));
    }

    public final Completable retry(long j) {
        return fromObservable(toObservable().retry(j));
    }

    public final Completable retryWhen(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> func1) {
        return fromObservable(toObservable().retryWhen(func1));
    }

    public final Completable startWith(Completable completable) {
        requireNonNull(completable);
        return concat(completable, this);
    }

    public final <T> Observable<T> startWith(Observable<T> observable) {
        requireNonNull(observable);
        return toObservable().startWith((Observable) observable);
    }

    public final Subscription subscribe() {
        final MultipleAssignmentSubscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.25
            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                multipleAssignmentSubscription.unsubscribe();
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                Completable.ERROR_HANDLER.handleError(th);
                multipleAssignmentSubscription.unsubscribe();
                Completable.deliverUncaughtException(th);
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
                multipleAssignmentSubscription.set(subscription);
            }
        });
        return multipleAssignmentSubscription;
    }

    public final Subscription subscribe(final Action0 action0) {
        requireNonNull(action0);
        final MultipleAssignmentSubscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.26
            boolean done;

            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                if (this.done) {
                    return;
                }
                this.done = true;
                try {
                    action0.call();
                } finally {
                    try {
                    } finally {
                    }
                }
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                Completable.ERROR_HANDLER.handleError(th);
                multipleAssignmentSubscription.unsubscribe();
                Completable.deliverUncaughtException(th);
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
                multipleAssignmentSubscription.set(subscription);
            }
        });
        return multipleAssignmentSubscription;
    }

    public final Subscription subscribe(final Action1<? super Throwable> action1, final Action0 action0) {
        requireNonNull(action1);
        requireNonNull(action0);
        final MultipleAssignmentSubscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.27
            boolean done;

            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                if (this.done) {
                    return;
                }
                this.done = true;
                try {
                    action0.call();
                    multipleAssignmentSubscription.unsubscribe();
                } catch (Throwable th) {
                    callOnError(th);
                }
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                if (!this.done) {
                    this.done = true;
                    callOnError(th);
                } else {
                    Completable.ERROR_HANDLER.handleError(th);
                    Completable.deliverUncaughtException(th);
                }
            }

            void callOnError(Throwable th) {
                try {
                    action1.call(th);
                } finally {
                    try {
                    } finally {
                    }
                }
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
                multipleAssignmentSubscription.set(subscription);
            }
        });
        return multipleAssignmentSubscription;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void deliverUncaughtException(Throwable th) {
        Thread currentThread = Thread.currentThread();
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
    }

    public final void unsafeSubscribe(CompletableSubscriber completableSubscriber) {
        requireNonNull(completableSubscriber);
        try {
            HOOK.onSubscribeStart(this, this.onSubscribe).call(completableSubscriber);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            Throwable onSubscribeError = HOOK.onSubscribeError(th);
            ERROR_HANDLER.handleError(onSubscribeError);
            throw toNpe(onSubscribeError);
        }
    }

    public final void subscribe(CompletableSubscriber completableSubscriber) {
        if (!(completableSubscriber instanceof SafeCompletableSubscriber)) {
            completableSubscriber = new SafeCompletableSubscriber(completableSubscriber);
        }
        unsafeSubscribe(completableSubscriber);
    }

    public final <T> void unsafeSubscribe(Subscriber<T> subscriber) {
        unsafeSubscribe(subscriber, true);
    }

    private final <T> void unsafeSubscribe(final Subscriber<T> subscriber, boolean z) {
        requireNonNull(subscriber);
        if (z) {
            try {
                subscriber.onStart();
            } catch (NullPointerException e) {
                throw e;
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                Throwable onSubscribeError = HOOK.onSubscribeError(th);
                ERROR_HANDLER.handleError(onSubscribeError);
                throw toNpe(onSubscribeError);
            }
        }
        unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.28
            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th2) {
                subscriber.onError(th2);
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
                subscriber.add(subscription);
            }
        });
        RxJavaPlugins.getInstance().getObservableExecutionHook().onSubscribeReturn(subscriber);
    }

    public final <T> void subscribe(Subscriber<T> subscriber) {
        subscriber.onStart();
        if (!(subscriber instanceof SafeSubscriber)) {
            subscriber = new SafeSubscriber(subscriber);
        }
        unsafeSubscribe(subscriber, false);
    }

    public final Completable subscribeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() { // from class: rx.Completable.29
            @Override // rx.functions.Action1
            public void call(final CompletableSubscriber completableSubscriber) {
                final Scheduler.Worker createWorker = scheduler.createWorker();
                createWorker.schedule(new Action0() { // from class: rx.Completable.29.1
                    @Override // rx.functions.Action0
                    public void call() {
                        try {
                            Completable.this.unsafeSubscribe(completableSubscriber);
                        } finally {
                            createWorker.unsubscribe();
                        }
                    }
                });
            }
        });
    }

    public final Completable timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, Schedulers.computation(), null);
    }

    public final Completable timeout(long j, TimeUnit timeUnit, Completable completable) {
        requireNonNull(completable);
        return timeout0(j, timeUnit, Schedulers.computation(), completable);
    }

    public final Completable timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, scheduler, null);
    }

    public final Completable timeout(long j, TimeUnit timeUnit, Scheduler scheduler, Completable completable) {
        requireNonNull(completable);
        return timeout0(j, timeUnit, scheduler, completable);
    }

    public final Completable timeout0(long j, TimeUnit timeUnit, Scheduler scheduler, Completable completable) {
        requireNonNull(timeUnit);
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribeTimeout(this, j, timeUnit, scheduler, completable));
    }

    public final <U> U to(Func1<? super Completable, U> func1) {
        return func1.call(this);
    }

    public final <T> Observable<T> toObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() { // from class: rx.Completable.30
            @Override // rx.functions.Action1
            public void call(Subscriber<? super T> subscriber) {
                Completable.this.unsafeSubscribe(subscriber);
            }
        });
    }

    public final <T> Single<T> toSingle(final Func0<? extends T> func0) {
        requireNonNull(func0);
        return Single.create(new Single.OnSubscribe<T>() { // from class: rx.Completable.31
            @Override // rx.functions.Action1
            public void call(final SingleSubscriber<? super T> singleSubscriber) {
                Completable.this.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.31.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // rx.Completable.CompletableSubscriber
                    public void onCompleted() {
                        try {
                            Object call = func0.call();
                            if (call == null) {
                                singleSubscriber.onError(new NullPointerException("The value supplied is null"));
                            } else {
                                singleSubscriber.onSuccess(call);
                            }
                        } catch (Throwable th) {
                            singleSubscriber.onError(th);
                        }
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onError(Throwable th) {
                        singleSubscriber.onError(th);
                    }

                    @Override // rx.Completable.CompletableSubscriber
                    public void onSubscribe(Subscription subscription) {
                        singleSubscriber.add(subscription);
                    }
                });
            }
        });
    }

    public final <T> Single<T> toSingleDefault(final T t) {
        requireNonNull(t);
        return toSingle(new Func0<T>() { // from class: rx.Completable.32
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public T call() {
                return (T) t;
            }
        });
    }

    /* renamed from: rx.Completable$33, reason: invalid class name */
    class AnonymousClass33 implements CompletableOnSubscribe {
        final /* synthetic */ Scheduler val$scheduler;

        AnonymousClass33(Scheduler scheduler) {
            this.val$scheduler = scheduler;
        }

        @Override // rx.functions.Action1
        public void call(final CompletableSubscriber completableSubscriber) {
            Completable.this.unsafeSubscribe(new CompletableSubscriber() { // from class: rx.Completable.33.1
                @Override // rx.Completable.CompletableSubscriber
                public void onCompleted() {
                    completableSubscriber.onCompleted();
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onError(Throwable th) {
                    completableSubscriber.onError(th);
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onSubscribe(final Subscription subscription) {
                    completableSubscriber.onSubscribe(Subscriptions.create(new Action0() { // from class: rx.Completable.33.1.1
                        @Override // rx.functions.Action0
                        public void call() {
                            final Scheduler.Worker createWorker = AnonymousClass33.this.val$scheduler.createWorker();
                            createWorker.schedule(new Action0() { // from class: rx.Completable.33.1.1.1
                                @Override // rx.functions.Action0
                                public void call() {
                                    try {
                                        subscription.unsubscribe();
                                    } finally {
                                        createWorker.unsubscribe();
                                    }
                                }
                            });
                        }
                    }));
                }
            });
        }
    }

    public final Completable unsubscribeOn(Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new AnonymousClass33(scheduler));
    }
}
