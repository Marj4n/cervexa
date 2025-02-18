package okhttp3;

import java.net.InetAddress;
import java.util.List;

/* loaded from: classes2.dex */
abstract class EventListener {
    public static final EventListener NONE = new EventListener() { // from class: okhttp3.EventListener.1
    };

    public interface Factory {
        EventListener create(Call call);
    }

    public void connectEnd(Call call, InetAddress inetAddress, int i, String str, Throwable th) {
    }

    public void connectStart(Call call, InetAddress inetAddress, int i) {
    }

    public void dnsEnd(Call call, String str, List<InetAddress> list, Throwable th) {
    }

    public void dnsStart(Call call, String str) {
    }

    public void fetchEnd(Call call, Throwable th) {
    }

    public void fetchStart(Call call) {
    }

    public void requestBodyEnd(Call call, Throwable th) {
    }

    public void requestBodyStart(Call call) {
    }

    public void requestHeadersEnd(Call call, Throwable th) {
    }

    public void requestHeadersStart(Call call) {
    }

    public void responseBodyEnd(Call call, Throwable th) {
    }

    public void responseBodyStart(Call call) {
    }

    public void responseHeadersEnd(Call call, Throwable th) {
    }

    public void responseHeadersStart(Call call) {
    }

    public void secureConnectEnd(Call call, Handshake handshake, Throwable th) {
    }

    public void secureConnectStart(Call call) {
    }

    EventListener() {
    }

    static Factory factory(EventListener eventListener) {
        return new Factory() { // from class: okhttp3.EventListener.2
            @Override // okhttp3.EventListener.Factory
            public EventListener create(Call call) {
                return EventListener.this;
            }
        };
    }
}
