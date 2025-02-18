package com.hoho.android.usbserial.driver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumSet;

/* loaded from: classes.dex */
public abstract class CommonUsbSerialPort implements UsbSerialPort {
    private static final int DEFAULT_WRITE_BUFFER_SIZE = 16384;
    private static final int MAX_READ_SIZE = 16384;
    private static final String TAG = CommonUsbSerialPort.class.getSimpleName();
    protected final UsbDevice mDevice;
    protected final int mPortNumber;
    protected UsbEndpoint mReadEndpoint;
    protected UsbRequest mUsbRequest;
    protected UsbEndpoint mWriteEndpoint;
    protected UsbDeviceConnection mConnection = null;
    protected final Object mWriteBufferLock = new Object();
    protected byte[] mWriteBuffer = new byte[16384];

    protected abstract void closeInt();

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public abstract EnumSet<UsbSerialPort.ControlLine> getControlLines() throws IOException;

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public abstract EnumSet<UsbSerialPort.ControlLine> getSupportedControlLines() throws IOException;

    protected abstract void openInt(UsbDeviceConnection usbDeviceConnection) throws IOException;

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public abstract void setParameters(int i, int i2, int i3, int i4) throws IOException;

    public CommonUsbSerialPort(UsbDevice usbDevice, int i) {
        this.mDevice = usbDevice;
        this.mPortNumber = i;
    }

    public String toString() {
        return String.format("<%s device_name=%s device_id=%s port_number=%s>", getClass().getSimpleName(), this.mDevice.getDeviceName(), Integer.valueOf(this.mDevice.getDeviceId()), Integer.valueOf(this.mPortNumber));
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public UsbDevice getDevice() {
        return this.mDevice;
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public int getPortNumber() {
        return this.mPortNumber;
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public String getSerial() {
        return this.mConnection.getSerial();
    }

    public final void setWriteBufferSize(int i) {
        synchronized (this.mWriteBufferLock) {
            if (i == this.mWriteBuffer.length) {
                return;
            }
            this.mWriteBuffer = new byte[i];
        }
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public void open(UsbDeviceConnection usbDeviceConnection) throws IOException {
        if (this.mConnection != null) {
            throw new IOException("Already open");
        }
        this.mConnection = usbDeviceConnection;
        try {
            openInt(usbDeviceConnection);
            if (this.mReadEndpoint == null || this.mWriteEndpoint == null) {
                throw new IOException("Could not get read & write endpoints");
            }
            UsbRequest usbRequest = new UsbRequest();
            this.mUsbRequest = usbRequest;
            usbRequest.initialize(this.mConnection, this.mReadEndpoint);
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.mConnection == null) {
            throw new IOException("Already closed");
        }
        try {
            this.mUsbRequest.cancel();
        } catch (Exception unused) {
        }
        this.mUsbRequest = null;
        try {
            closeInt();
        } catch (Exception unused2) {
        }
        try {
            this.mConnection.close();
        } catch (Exception unused3) {
        }
        this.mConnection = null;
    }

    protected void testConnection() throws IOException {
        if (this.mConnection.controlTransfer(128, 0, 0, 0, new byte[2], 2, 200) < 0) {
            throw new IOException("USB get_status request failed");
        }
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public int read(byte[] bArr, int i) throws IOException {
        return read(bArr, i, true);
    }

    protected int read(byte[] bArr, int i, boolean z) throws IOException {
        int position;
        if (this.mConnection == null) {
            throw new IOException("Connection closed");
        }
        if (bArr.length <= 0) {
            throw new IllegalArgumentException("Read buffer to small");
        }
        if (i != 0) {
            long currentTimeMillis = z ? System.currentTimeMillis() + i : 0L;
            position = this.mConnection.bulkTransfer(this.mReadEndpoint, bArr, Math.min(bArr.length, 16384), i);
            if (position == -1 && z && System.currentTimeMillis() < currentTimeMillis) {
                testConnection();
            }
        } else {
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            if (!this.mUsbRequest.queue(wrap, bArr.length)) {
                throw new IOException("Queueing USB request failed");
            }
            if (this.mConnection.requestWait() == null) {
                throw new IOException("Waiting for USB request failed");
            }
            position = wrap.position();
        }
        if (position > 0) {
            return position;
        }
        return 0;
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public int write(byte[] bArr, int i) throws IOException {
        int min;
        byte[] bArr2;
        int bulkTransfer;
        if (this.mConnection == null) {
            throw new IOException("Connection closed");
        }
        int i2 = 0;
        while (i2 < bArr.length) {
            synchronized (this.mWriteBufferLock) {
                min = Math.min(bArr.length - i2, this.mWriteBuffer.length);
                if (i2 == 0) {
                    bArr2 = bArr;
                } else {
                    System.arraycopy(bArr, i2, this.mWriteBuffer, 0, min);
                    bArr2 = this.mWriteBuffer;
                }
                bulkTransfer = this.mConnection.bulkTransfer(this.mWriteEndpoint, bArr2, min, i);
            }
            if (bulkTransfer <= 0) {
                throw new IOException("Error writing " + min + " bytes at offset " + i2 + " length=" + bArr.length);
            }
            Log.d(TAG, "Wrote amt=" + bulkTransfer + " attempted=" + min);
            i2 += bulkTransfer;
        }
        return i2;
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public boolean isOpen() {
        return this.mConnection != null;
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public boolean getCD() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public boolean getCTS() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public boolean getDSR() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public boolean getDTR() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public void setDTR(boolean z) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public boolean getRI() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public boolean getRTS() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public void setRTS(boolean z) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialPort
    public void purgeHwBuffers(boolean z, boolean z2) throws IOException {
        throw new UnsupportedOperationException();
    }
}
