package org.apache.commons.net.telnet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes2.dex */
public class TelnetClient extends Telnet {
    private InputStream __input;
    private OutputStream __output;
    private TelnetInputListener inputListener;
    protected boolean readerThread;

    public TelnetClient() {
        super("VT100");
        this.readerThread = true;
        this.__input = null;
        this.__output = null;
    }

    public TelnetClient(String str) {
        super(str);
        this.readerThread = true;
        this.__input = null;
        this.__output = null;
    }

    void _flushOutputStream() throws IOException {
        this._output_.flush();
    }

    void _closeOutputStream() throws IOException {
        this._output_.close();
    }

    @Override // org.apache.commons.net.telnet.Telnet, org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        TelnetInputStream telnetInputStream = new TelnetInputStream(this._input_, this, this.readerThread);
        if (this.readerThread) {
            telnetInputStream._start();
        }
        this.__input = new BufferedInputStream(telnetInputStream);
        this.__output = new TelnetOutputStream(this);
    }

    @Override // org.apache.commons.net.SocketClient
    public void disconnect() throws IOException {
        InputStream inputStream = this.__input;
        if (inputStream != null) {
            inputStream.close();
        }
        OutputStream outputStream = this.__output;
        if (outputStream != null) {
            outputStream.close();
        }
        super.disconnect();
    }

    public OutputStream getOutputStream() {
        return this.__output;
    }

    public InputStream getInputStream() {
        return this.__input;
    }

    public boolean getLocalOptionState(int i) {
        return _stateIsWill(i) && _requestedWill(i);
    }

    public boolean getRemoteOptionState(int i) {
        return _stateIsDo(i) && _requestedDo(i);
    }

    public boolean sendAYT(long j) throws IOException, IllegalArgumentException, InterruptedException {
        return _sendAYT(j);
    }

    public void sendSubnegotiation(int[] iArr) throws IOException, IllegalArgumentException {
        if (iArr.length < 1) {
            throw new IllegalArgumentException("zero length message");
        }
        _sendSubnegotiation(iArr);
    }

    public void sendCommand(byte b) throws IOException, IllegalArgumentException {
        _sendCommand(b);
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void addOptionHandler(TelnetOptionHandler telnetOptionHandler) throws InvalidTelnetOptionException, IOException {
        super.addOptionHandler(telnetOptionHandler);
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void deleteOptionHandler(int i) throws InvalidTelnetOptionException, IOException {
        super.deleteOptionHandler(i);
    }

    public void registerSpyStream(OutputStream outputStream) {
        super._registerSpyStream(outputStream);
    }

    public void stopSpyStream() {
        super._stopSpyStream();
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void registerNotifHandler(TelnetNotificationHandler telnetNotificationHandler) {
        super.registerNotifHandler(telnetNotificationHandler);
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void unregisterNotifHandler() {
        super.unregisterNotifHandler();
    }

    public void setReaderThread(boolean z) {
        this.readerThread = z;
    }

    public boolean getReaderThread() {
        return this.readerThread;
    }

    public synchronized void registerInputListener(TelnetInputListener telnetInputListener) {
        this.inputListener = telnetInputListener;
    }

    public synchronized void unregisterInputListener() {
        this.inputListener = null;
    }

    void notifyInputListener() {
        TelnetInputListener telnetInputListener;
        synchronized (this) {
            telnetInputListener = this.inputListener;
        }
        if (telnetInputListener != null) {
            telnetInputListener.telnetInputAvailable();
        }
    }
}
