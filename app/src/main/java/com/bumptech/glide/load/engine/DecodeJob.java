package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.os.TraceCompat;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
class DecodeJob<R> implements DataFetcherGenerator.FetcherReadyCallback, Runnable, Comparable<DecodeJob<?>>, FactoryPools.Poolable {
    private static final String TAG = "DecodeJob";
    private Callback<R> callback;
    private Key currentAttemptingKey;
    private Object currentData;
    private DataSource currentDataSource;
    private DataFetcher<?> currentFetcher;
    private volatile DataFetcherGenerator currentGenerator;
    Key currentSourceKey;
    private Thread currentThread;
    private final DiskCacheProvider diskCacheProvider;
    DiskCacheStrategy diskCacheStrategy;
    private GlideContext glideContext;
    int height;
    private volatile boolean isCallbackNotified;
    private volatile boolean isCancelled;
    private EngineKey loadKey;
    private boolean onlyRetrieveFromCache;
    Options options;
    private int order;
    private final Pools.Pool<DecodeJob<?>> pool;
    private Priority priority;
    private RunReason runReason;
    Key signature;
    private Stage stage;
    private long startFetchTime;
    int width;
    final DecodeHelper<R> decodeHelper = new DecodeHelper<>();
    private final List<Exception> exceptions = new ArrayList();
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    final DeferredEncodeManager<?> deferredEncodeManager = new DeferredEncodeManager<>();
    private final ReleaseManager releaseManager = new ReleaseManager();

    interface Callback<R> {
        void onLoadFailed(GlideException glideException);

        void onResourceReady(Resource<R> resource, DataSource dataSource);

        void reschedule(DecodeJob<?> decodeJob);
    }

    interface DiskCacheProvider {
        DiskCache getDiskCache();
    }

    private enum RunReason {
        INITIALIZE,
        SWITCH_TO_SOURCE_SERVICE,
        DECODE_DATA
    }

    private enum Stage {
        INITIALIZE,
        RESOURCE_CACHE,
        DATA_CACHE,
        SOURCE,
        ENCODE,
        FINISHED
    }

    DecodeJob(DiskCacheProvider diskCacheProvider, Pools.Pool<DecodeJob<?>> pool) {
        this.diskCacheProvider = diskCacheProvider;
        this.pool = pool;
    }

    DecodeJob<R> init(GlideContext glideContext, Object obj, EngineKey engineKey, Key key, int i, int i2, Class<?> cls, Class<R> cls2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean z, boolean z2, Options options, Callback<R> callback, int i3) {
        this.decodeHelper.init(glideContext, obj, key, i, i2, diskCacheStrategy, cls, cls2, priority, options, map, z, this.diskCacheProvider);
        this.glideContext = glideContext;
        this.signature = key;
        this.priority = priority;
        this.loadKey = engineKey;
        this.width = i;
        this.height = i2;
        this.diskCacheStrategy = diskCacheStrategy;
        this.onlyRetrieveFromCache = z2;
        this.options = options;
        this.callback = callback;
        this.order = i3;
        this.runReason = RunReason.INITIALIZE;
        return this;
    }

    boolean willDecodeFromCache() {
        Stage nextStage = getNextStage(Stage.INITIALIZE);
        return nextStage == Stage.RESOURCE_CACHE || nextStage == Stage.DATA_CACHE;
    }

    void release(boolean z) {
        if (this.releaseManager.release(z)) {
            releaseInternal();
        }
    }

    private void onEncodeComplete() {
        if (this.releaseManager.onEncodeComplete()) {
            releaseInternal();
        }
    }

    private void onLoadFailed() {
        if (this.releaseManager.onFailed()) {
            releaseInternal();
        }
    }

    private void releaseInternal() {
        this.releaseManager.reset();
        this.deferredEncodeManager.clear();
        this.decodeHelper.clear();
        this.isCallbackNotified = false;
        this.glideContext = null;
        this.signature = null;
        this.options = null;
        this.priority = null;
        this.loadKey = null;
        this.callback = null;
        this.stage = null;
        this.currentGenerator = null;
        this.currentThread = null;
        this.currentSourceKey = null;
        this.currentData = null;
        this.currentDataSource = null;
        this.currentFetcher = null;
        this.startFetchTime = 0L;
        this.isCancelled = false;
        this.exceptions.clear();
        this.pool.release(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(DecodeJob<?> decodeJob) {
        int priority = getPriority() - decodeJob.getPriority();
        return priority == 0 ? this.order - decodeJob.order : priority;
    }

    private int getPriority() {
        return this.priority.ordinal();
    }

    public void cancel() {
        this.isCancelled = true;
        DataFetcherGenerator dataFetcherGenerator = this.currentGenerator;
        if (dataFetcherGenerator != null) {
            dataFetcherGenerator.cancel();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x001e, code lost:
    
        if (r0 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0020, code lost:
    
        r0.cleanup();
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0023, code lost:
    
        androidx.core.os.TraceCompat.endSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0063, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0060, code lost:
    
        if (r0 != null) goto L14;
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            r4 = this;
            java.lang.String r0 = "DecodeJob"
            java.lang.String r1 = "DecodeJob#run"
            androidx.core.os.TraceCompat.beginSection(r1)
            boolean r1 = r4.isCancelled     // Catch: java.lang.Throwable -> L27 java.lang.RuntimeException -> L29
            if (r1 == 0) goto L19
            r4.notifyFailed()     // Catch: java.lang.Throwable -> L27 java.lang.RuntimeException -> L29
            com.bumptech.glide.load.data.DataFetcher<?> r0 = r4.currentFetcher
            if (r0 == 0) goto L15
            r0.cleanup()
        L15:
            androidx.core.os.TraceCompat.endSection()
            return
        L19:
            r4.runWrapped()     // Catch: java.lang.Throwable -> L27 java.lang.RuntimeException -> L29
            com.bumptech.glide.load.data.DataFetcher<?> r0 = r4.currentFetcher
            if (r0 == 0) goto L23
        L20:
            r0.cleanup()
        L23:
            androidx.core.os.TraceCompat.endSection()
            goto L63
        L27:
            r0 = move-exception
            goto L65
        L29:
            r1 = move-exception
            r2 = 3
            boolean r2 = android.util.Log.isLoggable(r0, r2)     // Catch: java.lang.Throwable -> L27
            if (r2 == 0) goto L51
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L27
            r2.<init>()     // Catch: java.lang.Throwable -> L27
            java.lang.String r3 = "DecodeJob threw unexpectedly, isCancelled: "
            r2.append(r3)     // Catch: java.lang.Throwable -> L27
            boolean r3 = r4.isCancelled     // Catch: java.lang.Throwable -> L27
            r2.append(r3)     // Catch: java.lang.Throwable -> L27
            java.lang.String r3 = ", stage: "
            r2.append(r3)     // Catch: java.lang.Throwable -> L27
            com.bumptech.glide.load.engine.DecodeJob$Stage r3 = r4.stage     // Catch: java.lang.Throwable -> L27
            r2.append(r3)     // Catch: java.lang.Throwable -> L27
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L27
            android.util.Log.d(r0, r2, r1)     // Catch: java.lang.Throwable -> L27
        L51:
            com.bumptech.glide.load.engine.DecodeJob$Stage r0 = r4.stage     // Catch: java.lang.Throwable -> L27
            com.bumptech.glide.load.engine.DecodeJob$Stage r2 = com.bumptech.glide.load.engine.DecodeJob.Stage.ENCODE     // Catch: java.lang.Throwable -> L27
            if (r0 == r2) goto L5a
            r4.notifyFailed()     // Catch: java.lang.Throwable -> L27
        L5a:
            boolean r0 = r4.isCancelled     // Catch: java.lang.Throwable -> L27
            if (r0 == 0) goto L64
            com.bumptech.glide.load.data.DataFetcher<?> r0 = r4.currentFetcher
            if (r0 == 0) goto L23
            goto L20
        L63:
            return
        L64:
            throw r1     // Catch: java.lang.Throwable -> L27
        L65:
            com.bumptech.glide.load.data.DataFetcher<?> r1 = r4.currentFetcher
            if (r1 == 0) goto L6c
            r1.cleanup()
        L6c:
            androidx.core.os.TraceCompat.endSection()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.DecodeJob.run():void");
    }

    private void runWrapped() {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[this.runReason.ordinal()];
        if (i == 1) {
            this.stage = getNextStage(Stage.INITIALIZE);
            this.currentGenerator = getNextGenerator();
            runGenerators();
        } else if (i == 2) {
            runGenerators();
        } else {
            if (i == 3) {
                decodeFromRetrievedData();
                return;
            }
            throw new IllegalStateException("Unrecognized run reason: " + this.runReason);
        }
    }

    /* renamed from: com.bumptech.glide.load.engine.DecodeJob$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason;
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage;

        static {
            int[] iArr = new int[Stage.values().length];
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage = iArr;
            try {
                iArr[Stage.RESOURCE_CACHE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.DATA_CACHE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.SOURCE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.FINISHED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.INITIALIZE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr2 = new int[RunReason.values().length];
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason = iArr2;
            try {
                iArr2[RunReason.INITIALIZE.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[RunReason.SWITCH_TO_SOURCE_SERVICE.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[RunReason.DECODE_DATA.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    private DataFetcherGenerator getNextGenerator() {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[this.stage.ordinal()];
        if (i == 1) {
            return new ResourceCacheGenerator(this.decodeHelper, this);
        }
        if (i == 2) {
            return new DataCacheGenerator(this.decodeHelper, this);
        }
        if (i == 3) {
            return new SourceGenerator(this.decodeHelper, this);
        }
        if (i == 4) {
            return null;
        }
        throw new IllegalStateException("Unrecognized stage: " + this.stage);
    }

    private void runGenerators() {
        this.currentThread = Thread.currentThread();
        this.startFetchTime = LogTime.getLogTime();
        boolean z = false;
        while (!this.isCancelled && this.currentGenerator != null && !(z = this.currentGenerator.startNext())) {
            this.stage = getNextStage(this.stage);
            this.currentGenerator = getNextGenerator();
            if (this.stage == Stage.SOURCE) {
                reschedule();
                return;
            }
        }
        if ((this.stage == Stage.FINISHED || this.isCancelled) && !z) {
            notifyFailed();
        }
    }

    private void notifyFailed() {
        setNotifiedOrThrow();
        this.callback.onLoadFailed(new GlideException("Failed to load resource", new ArrayList(this.exceptions)));
        onLoadFailed();
    }

    private void notifyComplete(Resource<R> resource, DataSource dataSource) {
        setNotifiedOrThrow();
        this.callback.onResourceReady(resource, dataSource);
    }

    private void setNotifiedOrThrow() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCallbackNotified) {
            throw new IllegalStateException("Already notified");
        }
        this.isCallbackNotified = true;
    }

    private Stage getNextStage(Stage stage) {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[stage.ordinal()];
        if (i == 1) {
            return this.diskCacheStrategy.decodeCachedData() ? Stage.DATA_CACHE : getNextStage(Stage.DATA_CACHE);
        }
        if (i == 2) {
            return this.onlyRetrieveFromCache ? Stage.FINISHED : Stage.SOURCE;
        }
        if (i == 3 || i == 4) {
            return Stage.FINISHED;
        }
        if (i == 5) {
            return this.diskCacheStrategy.decodeCachedResource() ? Stage.RESOURCE_CACHE : getNextStage(Stage.RESOURCE_CACHE);
        }
        throw new IllegalArgumentException("Unrecognized stage: " + stage);
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback
    public void reschedule() {
        this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
        this.callback.reschedule(this);
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback
    public void onDataFetcherReady(Key key, Object obj, DataFetcher<?> dataFetcher, DataSource dataSource, Key key2) {
        this.currentSourceKey = key;
        this.currentData = obj;
        this.currentFetcher = dataFetcher;
        this.currentDataSource = dataSource;
        this.currentAttemptingKey = key2;
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.DECODE_DATA;
            this.callback.reschedule(this);
        } else {
            TraceCompat.beginSection("DecodeJob.decodeFromRetrievedData");
            try {
                decodeFromRetrievedData();
            } finally {
                TraceCompat.endSection();
            }
        }
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback
    public void onDataFetcherFailed(Key key, Exception exc, DataFetcher<?> dataFetcher, DataSource dataSource) {
        dataFetcher.cleanup();
        GlideException glideException = new GlideException("Fetching data failed", exc);
        glideException.setLoggingDetails(key, dataSource, dataFetcher.getDataClass());
        this.exceptions.add(glideException);
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
            this.callback.reschedule(this);
        } else {
            runGenerators();
        }
    }

    private void decodeFromRetrievedData() {
        if (Log.isLoggable(TAG, 2)) {
            logWithTimeAndKey("Retrieved data", this.startFetchTime, "data: " + this.currentData + ", cache key: " + this.currentSourceKey + ", fetcher: " + this.currentFetcher);
        }
        Resource<R> resource = null;
        try {
            resource = decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
        } catch (GlideException e) {
            e.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.exceptions.add(e);
        }
        if (resource != null) {
            notifyEncodeAndRelease(resource, this.currentDataSource);
        } else {
            runGenerators();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void notifyEncodeAndRelease(Resource<R> resource, DataSource dataSource) {
        if (resource instanceof Initializable) {
            ((Initializable) resource).initialize();
        }
        LockedResource lockedResource = 0;
        if (this.deferredEncodeManager.hasResourceToEncode()) {
            resource = LockedResource.obtain(resource);
            lockedResource = resource;
        }
        notifyComplete(resource, dataSource);
        this.stage = Stage.ENCODE;
        try {
            if (this.deferredEncodeManager.hasResourceToEncode()) {
                this.deferredEncodeManager.encode(this.diskCacheProvider, this.options);
            }
        } finally {
            if (lockedResource != 0) {
                lockedResource.unlock();
            }
            onEncodeComplete();
        }
    }

    private <Data> Resource<R> decodeFromData(DataFetcher<?> dataFetcher, Data data, DataSource dataSource) throws GlideException {
        if (data != null) {
            try {
                long logTime = LogTime.getLogTime();
                Resource<R> decodeFromFetcher = decodeFromFetcher(data, dataSource);
                if (Log.isLoggable(TAG, 2)) {
                    logWithTimeAndKey("Decoded result " + decodeFromFetcher, logTime);
                }
                return decodeFromFetcher;
            } finally {
                dataFetcher.cleanup();
            }
        }
        return null;
    }

    private <Data> Resource<R> decodeFromFetcher(Data data, DataSource dataSource) throws GlideException {
        return runLoadPath(data, dataSource, this.decodeHelper.getLoadPath(data.getClass()));
    }

    private <Data, ResourceType> Resource<R> runLoadPath(Data data, DataSource dataSource, LoadPath<Data, ResourceType, R> loadPath) throws GlideException {
        DataRewinder<Data> rewinder = this.glideContext.getRegistry().getRewinder(data);
        try {
            return loadPath.load(rewinder, this.options, this.width, this.height, new DecodeCallback(dataSource));
        } finally {
            rewinder.cleanup();
        }
    }

    private void logWithTimeAndKey(String str, long j) {
        logWithTimeAndKey(str, j, null);
    }

    private void logWithTimeAndKey(String str, long j, String str2) {
        String str3;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" in ");
        sb.append(LogTime.getElapsedMillis(j));
        sb.append(", load key: ");
        sb.append(this.loadKey);
        if (str2 != null) {
            str3 = ", " + str2;
        } else {
            str3 = "";
        }
        sb.append(str3);
        sb.append(", thread: ");
        sb.append(Thread.currentThread().getName());
        Log.v(TAG, sb.toString());
    }

    @Override // com.bumptech.glide.util.pool.FactoryPools.Poolable
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    private final class DecodeCallback<Z> implements DecodePath.DecodeCallback<Z> {
        private final DataSource dataSource;

        DecodeCallback(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override // com.bumptech.glide.load.engine.DecodePath.DecodeCallback
        public Resource<Z> onResourceDecoded(Resource<Z> resource) {
            Resource<Z> resource2;
            Transformation<Z> transformation;
            EncodeStrategy encodeStrategy;
            Key resourceCacheKey;
            Class<Z> resourceClass = getResourceClass(resource);
            ResourceEncoder<Z> resourceEncoder = null;
            if (this.dataSource != DataSource.RESOURCE_DISK_CACHE) {
                Transformation<Z> transformation2 = DecodeJob.this.decodeHelper.getTransformation(resourceClass);
                transformation = transformation2;
                resource2 = transformation2.transform(DecodeJob.this.glideContext, resource, DecodeJob.this.width, DecodeJob.this.height);
            } else {
                resource2 = resource;
                transformation = null;
            }
            if (!resource.equals(resource2)) {
                resource.recycle();
            }
            if (DecodeJob.this.decodeHelper.isResourceEncoderAvailable(resource2)) {
                resourceEncoder = DecodeJob.this.decodeHelper.getResultEncoder(resource2);
                encodeStrategy = resourceEncoder.getEncodeStrategy(DecodeJob.this.options);
            } else {
                encodeStrategy = EncodeStrategy.NONE;
            }
            ResourceEncoder resourceEncoder2 = resourceEncoder;
            if (!DecodeJob.this.diskCacheStrategy.isResourceCacheable(!DecodeJob.this.decodeHelper.isSourceKey(DecodeJob.this.currentSourceKey), this.dataSource, encodeStrategy)) {
                return resource2;
            }
            if (resourceEncoder2 == null) {
                throw new Registry.NoResultEncoderAvailableException(resource2.get().getClass());
            }
            if (encodeStrategy == EncodeStrategy.SOURCE) {
                resourceCacheKey = new DataCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature);
            } else if (encodeStrategy == EncodeStrategy.TRANSFORMED) {
                resourceCacheKey = new ResourceCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature, DecodeJob.this.width, DecodeJob.this.height, transformation, resourceClass, DecodeJob.this.options);
            } else {
                throw new IllegalArgumentException("Unknown strategy: " + encodeStrategy);
            }
            LockedResource obtain = LockedResource.obtain(resource2);
            DecodeJob.this.deferredEncodeManager.init(resourceCacheKey, resourceEncoder2, obtain);
            return obtain;
        }

        private Class<Z> getResourceClass(Resource<Z> resource) {
            return (Class<Z>) resource.get().getClass();
        }
    }

    private static class ReleaseManager {
        private boolean isEncodeComplete;
        private boolean isFailed;
        private boolean isReleased;

        ReleaseManager() {
        }

        synchronized boolean release(boolean z) {
            this.isReleased = true;
            return isComplete(z);
        }

        synchronized boolean onEncodeComplete() {
            this.isEncodeComplete = true;
            return isComplete(false);
        }

        synchronized boolean onFailed() {
            this.isFailed = true;
            return isComplete(false);
        }

        synchronized void reset() {
            this.isEncodeComplete = false;
            this.isReleased = false;
            this.isFailed = false;
        }

        private boolean isComplete(boolean z) {
            return (this.isFailed || z || this.isEncodeComplete) && this.isReleased;
        }
    }

    private static class DeferredEncodeManager<Z> {
        private ResourceEncoder<Z> encoder;
        private Key key;
        private LockedResource<Z> toEncode;

        DeferredEncodeManager() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        <X> void init(Key key, ResourceEncoder<X> resourceEncoder, LockedResource<X> lockedResource) {
            this.key = key;
            this.encoder = resourceEncoder;
            this.toEncode = lockedResource;
        }

        void encode(DiskCacheProvider diskCacheProvider, Options options) {
            TraceCompat.beginSection("DecodeJob.encode");
            try {
                diskCacheProvider.getDiskCache().put(this.key, new DataCacheWriter(this.encoder, this.toEncode, options));
            } finally {
                this.toEncode.unlock();
                TraceCompat.endSection();
            }
        }

        boolean hasResourceToEncode() {
            return this.toEncode != null;
        }

        void clear() {
            this.key = null;
            this.encoder = null;
            this.toEncode = null;
        }
    }
}
