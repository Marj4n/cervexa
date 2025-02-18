package org.apache.commons.net.telnet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.net.SocketClient;

/* loaded from: classes2.dex */
class Telnet extends SocketClient {
    static final int DEFAULT_PORT = 23;
    protected static final int TERMINAL_TYPE = 24;
    protected static final int TERMINAL_TYPE_IS = 0;
    protected static final int TERMINAL_TYPE_SEND = 1;
    static final int _DO_MASK = 2;
    static final int _REQUESTED_DO_MASK = 8;
    static final int _REQUESTED_WILL_MASK = 4;
    static final int _WILL_MASK = 1;
    static final boolean debug = false;
    static final boolean debugoptions = false;
    private TelnetNotificationHandler __notifhand;
    int[] _doResponse;
    int[] _options;
    int[] _willResponse;
    private volatile boolean aytFlag;
    private final Object aytMonitor;
    private final TelnetOptionHandler[] optionHandlers;
    private volatile OutputStream spyStream;
    private String terminalType;
    static final byte[] _COMMAND_DO = {-1, -3};
    static final byte[] _COMMAND_DONT = {-1, -2};
    static final byte[] _COMMAND_WILL = {-1, -5};
    static final byte[] _COMMAND_WONT = {-1, -4};
    static final byte[] _COMMAND_SB = {-1, -6};
    static final byte[] _COMMAND_SE = {-1, -16};
    static final byte[] _COMMAND_IS = {24, 0};
    static final byte[] _COMMAND_AYT = {-1, -10};

    Telnet() {
        this.terminalType = null;
        this.aytMonitor = new Object();
        this.aytFlag = true;
        this.spyStream = null;
        this.__notifhand = null;
        setDefaultPort(23);
        this._doResponse = new int[256];
        this._willResponse = new int[256];
        this._options = new int[256];
        this.optionHandlers = new TelnetOptionHandler[256];
    }

    Telnet(String str) {
        this.terminalType = null;
        this.aytMonitor = new Object();
        this.aytFlag = true;
        this.spyStream = null;
        this.__notifhand = null;
        setDefaultPort(23);
        this._doResponse = new int[256];
        this._willResponse = new int[256];
        this._options = new int[256];
        this.terminalType = str;
        this.optionHandlers = new TelnetOptionHandler[256];
    }

    boolean _stateIsWill(int i) {
        return (this._options[i] & 1) != 0;
    }

    boolean _stateIsWont(int i) {
        return !_stateIsWill(i);
    }

    boolean _stateIsDo(int i) {
        return (this._options[i] & 2) != 0;
    }

    boolean _stateIsDont(int i) {
        return !_stateIsDo(i);
    }

    boolean _requestedWill(int i) {
        return (this._options[i] & 4) != 0;
    }

    boolean _requestedWont(int i) {
        return !_requestedWill(i);
    }

    boolean _requestedDo(int i) {
        return (this._options[i] & 8) != 0;
    }

    boolean _requestedDont(int i) {
        return !_requestedDo(i);
    }

    void _setWill(int i) throws IOException {
        int[] iArr = this._options;
        iArr[i] = iArr[i] | 1;
        if (_requestedWill(i)) {
            TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
            if (telnetOptionHandlerArr[i] != null) {
                telnetOptionHandlerArr[i].setWill(true);
                int[] startSubnegotiationLocal = this.optionHandlers[i].startSubnegotiationLocal();
                if (startSubnegotiationLocal != null) {
                    _sendSubnegotiation(startSubnegotiationLocal);
                }
            }
        }
    }

    void _setDo(int i) throws IOException {
        int[] iArr = this._options;
        iArr[i] = iArr[i] | 2;
        if (_requestedDo(i)) {
            TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
            if (telnetOptionHandlerArr[i] != null) {
                telnetOptionHandlerArr[i].setDo(true);
                int[] startSubnegotiationRemote = this.optionHandlers[i].startSubnegotiationRemote();
                if (startSubnegotiationRemote != null) {
                    _sendSubnegotiation(startSubnegotiationRemote);
                }
            }
        }
    }

    void _setWantWill(int i) {
        int[] iArr = this._options;
        iArr[i] = iArr[i] | 4;
    }

    void _setWantDo(int i) {
        int[] iArr = this._options;
        iArr[i] = iArr[i] | 8;
    }

    void _setWont(int i) {
        int[] iArr = this._options;
        iArr[i] = iArr[i] & (-2);
        TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
        if (telnetOptionHandlerArr[i] != null) {
            telnetOptionHandlerArr[i].setWill(false);
        }
    }

    void _setDont(int i) {
        int[] iArr = this._options;
        iArr[i] = iArr[i] & (-3);
        TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
        if (telnetOptionHandlerArr[i] != null) {
            telnetOptionHandlerArr[i].setDo(false);
        }
    }

    void _setWantWont(int i) {
        int[] iArr = this._options;
        iArr[i] = iArr[i] & (-5);
    }

    void _setWantDont(int i) {
        int[] iArr = this._options;
        iArr[i] = iArr[i] & (-9);
    }

    void _processCommand(int i) {
        TelnetNotificationHandler telnetNotificationHandler = this.__notifhand;
        if (telnetNotificationHandler != null) {
            telnetNotificationHandler.receivedNegotiation(5, i);
        }
    }

    void _processDo(int i) throws IOException {
        String str;
        TelnetNotificationHandler telnetNotificationHandler = this.__notifhand;
        if (telnetNotificationHandler != null) {
            telnetNotificationHandler.receivedNegotiation(1, i);
        }
        boolean z = false;
        TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
        if (telnetOptionHandlerArr[i] != null) {
            z = telnetOptionHandlerArr[i].getAcceptLocal();
        } else if (i == 24 && (str = this.terminalType) != null && str.length() > 0) {
            z = true;
        }
        int[] iArr = this._willResponse;
        if (iArr[i] > 0) {
            iArr[i] = iArr[i] - 1;
            if (iArr[i] > 0 && _stateIsWill(i)) {
                int[] iArr2 = this._willResponse;
                iArr2[i] = iArr2[i] - 1;
            }
        }
        if (this._willResponse[i] == 0 && _requestedWont(i)) {
            if (z) {
                _setWantWill(i);
                _sendWill(i);
            } else {
                int[] iArr3 = this._willResponse;
                iArr3[i] = iArr3[i] + 1;
                _sendWont(i);
            }
        }
        _setWill(i);
    }

    void _processDont(int i) throws IOException {
        TelnetNotificationHandler telnetNotificationHandler = this.__notifhand;
        if (telnetNotificationHandler != null) {
            telnetNotificationHandler.receivedNegotiation(2, i);
        }
        int[] iArr = this._willResponse;
        if (iArr[i] > 0) {
            iArr[i] = iArr[i] - 1;
            if (iArr[i] > 0 && _stateIsWont(i)) {
                this._willResponse[i] = r0[i] - 1;
            }
        }
        if (this._willResponse[i] == 0 && _requestedWill(i)) {
            if (_stateIsWill(i) || _requestedWill(i)) {
                _sendWont(i);
            }
            _setWantWont(i);
        }
        _setWont(i);
    }

    void _processWill(int i) throws IOException {
        TelnetNotificationHandler telnetNotificationHandler = this.__notifhand;
        if (telnetNotificationHandler != null) {
            telnetNotificationHandler.receivedNegotiation(3, i);
        }
        TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
        boolean acceptRemote = telnetOptionHandlerArr[i] != null ? telnetOptionHandlerArr[i].getAcceptRemote() : false;
        int[] iArr = this._doResponse;
        if (iArr[i] > 0) {
            iArr[i] = iArr[i] - 1;
            if (iArr[i] > 0 && _stateIsDo(i)) {
                this._doResponse[i] = r1[i] - 1;
            }
        }
        if (this._doResponse[i] == 0 && _requestedDont(i)) {
            if (acceptRemote) {
                _setWantDo(i);
                _sendDo(i);
            } else {
                int[] iArr2 = this._doResponse;
                iArr2[i] = iArr2[i] + 1;
                _sendDont(i);
            }
        }
        _setDo(i);
    }

    void _processWont(int i) throws IOException {
        TelnetNotificationHandler telnetNotificationHandler = this.__notifhand;
        if (telnetNotificationHandler != null) {
            telnetNotificationHandler.receivedNegotiation(4, i);
        }
        int[] iArr = this._doResponse;
        if (iArr[i] > 0) {
            iArr[i] = iArr[i] - 1;
            if (iArr[i] > 0 && _stateIsDont(i)) {
                this._doResponse[i] = r0[i] - 1;
            }
        }
        if (this._doResponse[i] == 0 && _requestedDo(i)) {
            if (_stateIsDo(i) || _requestedDo(i)) {
                _sendDont(i);
            }
            _setWantDont(i);
        }
        _setDont(i);
    }

    void _processSuboption(int[] iArr, int i) throws IOException {
        if (i > 0) {
            TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
            if (telnetOptionHandlerArr[iArr[0]] != null) {
                _sendSubnegotiation(telnetOptionHandlerArr[iArr[0]].answerSubnegotiation(iArr, i));
            } else if (i > 1 && iArr[0] == 24 && iArr[1] == 1) {
                _sendTerminalType();
            }
        }
    }

    final synchronized void _sendTerminalType() throws IOException {
        if (this.terminalType != null) {
            this._output_.write(_COMMAND_SB);
            this._output_.write(_COMMAND_IS);
            this._output_.write(this.terminalType.getBytes(getCharset()));
            this._output_.write(_COMMAND_SE);
            this._output_.flush();
        }
    }

    final synchronized void _sendSubnegotiation(int[] iArr) throws IOException {
        if (iArr != null) {
            this._output_.write(_COMMAND_SB);
            for (int i : iArr) {
                byte b = (byte) i;
                if (b == -1) {
                    this._output_.write(b);
                }
                this._output_.write(b);
            }
            this._output_.write(_COMMAND_SE);
            this._output_.flush();
        }
    }

    final synchronized void _sendCommand(byte b) throws IOException {
        this._output_.write(255);
        this._output_.write(b);
        this._output_.flush();
    }

    final synchronized void _processAYTResponse() {
        if (!this.aytFlag) {
            synchronized (this.aytMonitor) {
                this.aytFlag = true;
                this.aytMonitor.notifyAll();
            }
        }
    }

    @Override // org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        for (int i = 0; i < 256; i++) {
            this._doResponse[i] = 0;
            this._willResponse[i] = 0;
            this._options[i] = 0;
            TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
            if (telnetOptionHandlerArr[i] != null) {
                telnetOptionHandlerArr[i].setDo(false);
                this.optionHandlers[i].setWill(false);
            }
        }
        super._connectAction_();
        this._input_ = new BufferedInputStream(this._input_);
        this._output_ = new BufferedOutputStream(this._output_);
        for (int i2 = 0; i2 < 256; i2++) {
            TelnetOptionHandler[] telnetOptionHandlerArr2 = this.optionHandlers;
            if (telnetOptionHandlerArr2[i2] != null) {
                if (telnetOptionHandlerArr2[i2].getInitLocal()) {
                    _requestWill(this.optionHandlers[i2].getOptionCode());
                }
                if (this.optionHandlers[i2].getInitRemote()) {
                    _requestDo(this.optionHandlers[i2].getOptionCode());
                }
            }
        }
    }

    final synchronized void _sendDo(int i) throws IOException {
        this._output_.write(_COMMAND_DO);
        this._output_.write(i);
        this._output_.flush();
    }

    final synchronized void _requestDo(int i) throws IOException {
        if ((this._doResponse[i] == 0 && _stateIsDo(i)) || _requestedDo(i)) {
            return;
        }
        _setWantDo(i);
        int[] iArr = this._doResponse;
        iArr[i] = iArr[i] + 1;
        _sendDo(i);
    }

    final synchronized void _sendDont(int i) throws IOException {
        this._output_.write(_COMMAND_DONT);
        this._output_.write(i);
        this._output_.flush();
    }

    final synchronized void _requestDont(int i) throws IOException {
        if ((this._doResponse[i] == 0 && _stateIsDont(i)) || _requestedDont(i)) {
            return;
        }
        _setWantDont(i);
        int[] iArr = this._doResponse;
        iArr[i] = iArr[i] + 1;
        _sendDont(i);
    }

    final synchronized void _sendWill(int i) throws IOException {
        this._output_.write(_COMMAND_WILL);
        this._output_.write(i);
        this._output_.flush();
    }

    final synchronized void _requestWill(int i) throws IOException {
        if ((this._willResponse[i] == 0 && _stateIsWill(i)) || _requestedWill(i)) {
            return;
        }
        _setWantWill(i);
        int[] iArr = this._doResponse;
        iArr[i] = iArr[i] + 1;
        _sendWill(i);
    }

    final synchronized void _sendWont(int i) throws IOException {
        this._output_.write(_COMMAND_WONT);
        this._output_.write(i);
        this._output_.flush();
    }

    final synchronized void _requestWont(int i) throws IOException {
        if ((this._willResponse[i] == 0 && _stateIsWont(i)) || _requestedWont(i)) {
            return;
        }
        _setWantWont(i);
        int[] iArr = this._doResponse;
        iArr[i] = iArr[i] + 1;
        _sendWont(i);
    }

    final synchronized void _sendByte(int i) throws IOException {
        this._output_.write(i);
        _spyWrite(i);
    }

    final boolean _sendAYT(long j) throws IOException, IllegalArgumentException, InterruptedException {
        boolean z;
        synchronized (this.aytMonitor) {
            synchronized (this) {
                z = false;
                this.aytFlag = false;
                this._output_.write(_COMMAND_AYT);
                this._output_.flush();
            }
            this.aytMonitor.wait(j);
            if (this.aytFlag) {
                z = true;
            } else {
                this.aytFlag = true;
            }
        }
        return z;
    }

    void addOptionHandler(TelnetOptionHandler telnetOptionHandler) throws InvalidTelnetOptionException, IOException {
        int optionCode = telnetOptionHandler.getOptionCode();
        if (TelnetOption.isValidOption(optionCode)) {
            TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
            if (telnetOptionHandlerArr[optionCode] == null) {
                telnetOptionHandlerArr[optionCode] = telnetOptionHandler;
                if (isConnected()) {
                    if (telnetOptionHandler.getInitLocal()) {
                        _requestWill(optionCode);
                    }
                    if (telnetOptionHandler.getInitRemote()) {
                        _requestDo(optionCode);
                        return;
                    }
                    return;
                }
                return;
            }
            throw new InvalidTelnetOptionException("Already registered option", optionCode);
        }
        throw new InvalidTelnetOptionException("Invalid Option Code", optionCode);
    }

    void deleteOptionHandler(int i) throws InvalidTelnetOptionException, IOException {
        if (TelnetOption.isValidOption(i)) {
            TelnetOptionHandler[] telnetOptionHandlerArr = this.optionHandlers;
            if (telnetOptionHandlerArr[i] == null) {
                throw new InvalidTelnetOptionException("Unregistered option", i);
            }
            TelnetOptionHandler telnetOptionHandler = telnetOptionHandlerArr[i];
            telnetOptionHandlerArr[i] = null;
            if (telnetOptionHandler.getWill()) {
                _requestWont(i);
            }
            if (telnetOptionHandler.getDo()) {
                _requestDont(i);
                return;
            }
            return;
        }
        throw new InvalidTelnetOptionException("Invalid Option Code", i);
    }

    void _registerSpyStream(OutputStream outputStream) {
        this.spyStream = outputStream;
    }

    void _stopSpyStream() {
        this.spyStream = null;
    }

    void _spyRead(int i) {
        OutputStream outputStream = this.spyStream;
        if (outputStream == null || i == 13) {
            return;
        }
        if (i == 10) {
            try {
                outputStream.write(13);
            } catch (IOException unused) {
                this.spyStream = null;
                return;
            }
        }
        outputStream.write(i);
        outputStream.flush();
    }

    void _spyWrite(int i) {
        OutputStream outputStream;
        if ((_stateIsDo(1) && _requestedDo(1)) || (outputStream = this.spyStream) == null) {
            return;
        }
        try {
            outputStream.write(i);
            outputStream.flush();
        } catch (IOException unused) {
            this.spyStream = null;
        }
    }

    public void registerNotifHandler(TelnetNotificationHandler telnetNotificationHandler) {
        this.__notifhand = telnetNotificationHandler;
    }

    public void unregisterNotifHandler() {
        this.__notifhand = null;
    }
}
