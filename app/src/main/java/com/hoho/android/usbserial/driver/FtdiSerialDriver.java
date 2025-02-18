package com.hoho.android.usbserial.driver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class FtdiSerialDriver implements UsbSerialDriver {
    private static final String TAG = FtdiSerialPort.class.getSimpleName();
    private final UsbDevice mDevice;
    private final List<UsbSerialPort> mPorts = new ArrayList();

    public FtdiSerialDriver(UsbDevice usbDevice) {
        this.mDevice = usbDevice;
        for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
            this.mPorts.add(new FtdiSerialPort(this.mDevice, i));
        }
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialDriver
    public UsbDevice getDevice() {
        return this.mDevice;
    }

    @Override // com.hoho.android.usbserial.driver.UsbSerialDriver
    public List<UsbSerialPort> getPorts() {
        return this.mPorts;
    }

    public class FtdiSerialPort extends CommonUsbSerialPort {
        private static final int GET_LATENCY_TIMER_REQUEST = 10;
        private static final int GET_MODEM_STATUS_REQUEST = 5;
        private static final int MODEM_CONTROL_DTR_DISABLE = 256;
        private static final int MODEM_CONTROL_DTR_ENABLE = 257;
        private static final int MODEM_CONTROL_REQUEST = 1;
        private static final int MODEM_CONTROL_RTS_DISABLE = 512;
        private static final int MODEM_CONTROL_RTS_ENABLE = 514;
        private static final int MODEM_STATUS_CD = 128;
        private static final int MODEM_STATUS_CTS = 16;
        private static final int MODEM_STATUS_DSR = 32;
        private static final int MODEM_STATUS_RI = 64;
        private static final int READ_HEADER_LENGTH = 2;
        private static final int REQTYPE_DEVICE_TO_HOST = 192;
        private static final int REQTYPE_HOST_TO_DEVICE = 64;
        private static final int RESET_ALL = 0;
        private static final int RESET_PURGE_RX = 1;
        private static final int RESET_PURGE_TX = 2;
        private static final int RESET_REQUEST = 0;
        private static final int SET_BAUD_RATE_REQUEST = 3;
        private static final int SET_DATA_REQUEST = 4;
        private static final int SET_LATENCY_TIMER_REQUEST = 9;
        private static final int USB_WRITE_TIMEOUT_MILLIS = 5000;
        private boolean baudRateWithPort;
        private boolean dtr;
        private boolean rts;

        public FtdiSerialPort(UsbDevice usbDevice, int i) {
            super(usbDevice, i);
            this.baudRateWithPort = false;
            this.dtr = false;
            this.rts = false;
        }

        @Override // com.hoho.android.usbserial.driver.UsbSerialPort
        public UsbSerialDriver getDriver() {
            return FtdiSerialDriver.this;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort
        protected void openInt(UsbDeviceConnection usbDeviceConnection) throws IOException {
            boolean z = true;
            if (!usbDeviceConnection.claimInterface(this.mDevice.getInterface(this.mPortNumber), true)) {
                throw new IOException("Could not claim interface " + this.mPortNumber);
            }
            if (this.mDevice.getInterface(this.mPortNumber).getEndpointCount() < 2) {
                throw new IOException("Not enough endpoints");
            }
            this.mReadEndpoint = this.mDevice.getInterface(this.mPortNumber).getEndpoint(0);
            this.mWriteEndpoint = this.mDevice.getInterface(this.mPortNumber).getEndpoint(1);
            int controlTransfer = this.mConnection.controlTransfer(64, 0, 0, this.mPortNumber + 1, null, 0, 5000);
            if (controlTransfer != 0) {
                throw new IOException("Reset failed: result=" + controlTransfer);
            }
            int controlTransfer2 = this.mConnection.controlTransfer(64, 1, (this.rts ? 514 : 512) | (this.dtr ? 257 : 256), this.mPortNumber + 1, null, 0, 5000);
            if (controlTransfer2 != 0) {
                throw new IOException("Init RTS,DTR failed: result=" + controlTransfer2);
            }
            byte[] rawDescriptors = usbDeviceConnection.getRawDescriptors();
            if (rawDescriptors == null || rawDescriptors.length < 14) {
                throw new IOException("Could not get device descriptors");
            }
            byte b = rawDescriptors[13];
            if (b != 7 && b != 8 && b != 9) {
                z = false;
            }
            this.baudRateWithPort = z;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort
        protected void closeInt() {
            try {
                this.mConnection.releaseInterface(this.mDevice.getInterface(this.mPortNumber));
            } catch (Exception unused) {
            }
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public int read(byte[] bArr, int i) throws IOException {
            int read;
            int i2;
            if (bArr.length <= 2) {
                throw new IllegalArgumentException("Read buffer to small");
            }
            if (i != 0) {
                long currentTimeMillis = System.currentTimeMillis() + i;
                do {
                    i2 = super.read(bArr, Math.max(1, (int) (currentTimeMillis - System.currentTimeMillis())), false);
                    if (i2 != 2) {
                        break;
                    }
                } while (System.currentTimeMillis() < currentTimeMillis);
                if (i2 <= 0 && System.currentTimeMillis() < currentTimeMillis) {
                    testConnection();
                }
            } else {
                do {
                    read = super.read(bArr, i, false);
                } while (read == 2);
                i2 = read;
            }
            return readFilter(bArr, i2);
        }

        private int readFilter(byte[] bArr, int i) throws IOException {
            int maxPacketSize = this.mReadEndpoint.getMaxPacketSize();
            int i2 = 0;
            int i3 = 0;
            while (i2 < i) {
                int i4 = i2 + maxPacketSize;
                int i5 = i2 + 2;
                int min = Math.min(i4, i) - i5;
                if (min < 0) {
                    throw new IOException("Expected at least 2 bytes");
                }
                System.arraycopy(bArr, i5, bArr, i3, min);
                i3 += min;
                i2 = i4;
            }
            return i3;
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:10:0x0050. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:31:0x00e5  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x004a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void setBaudrate(int r23) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 286
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hoho.android.usbserial.driver.FtdiSerialDriver.FtdiSerialPort.setBaudrate(int):void");
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public void setParameters(int i, int i2, int i3, int i4) throws IOException {
            if (i <= 0) {
                throw new IllegalArgumentException("Invalid baud rate: " + i);
            }
            setBaudrate(i);
            if (i2 == 5 || i2 == 6) {
                throw new UnsupportedOperationException("Unsupported data bits: " + i2);
            }
            if (i2 != 7 && i2 != 8) {
                throw new IllegalArgumentException("Invalid data bits: " + i2);
            }
            int i5 = i2 | 0;
            if (i4 != 0) {
                if (i4 == 1) {
                    i5 |= 256;
                } else if (i4 == 2) {
                    i5 |= 512;
                } else if (i4 == 3) {
                    i5 |= 768;
                } else {
                    if (i4 != 4) {
                        throw new IllegalArgumentException("Invalid parity: " + i4);
                    }
                    i5 |= 1024;
                }
            }
            if (i3 != 1) {
                if (i3 != 2) {
                    if (i3 == 3) {
                        throw new UnsupportedOperationException("Unsupported stop bits: 1.5");
                    }
                    throw new IllegalArgumentException("Invalid stop bits: " + i3);
                }
                i5 |= 4096;
            }
            int controlTransfer = this.mConnection.controlTransfer(64, 4, i5, this.mPortNumber + 1, null, 0, 5000);
            if (controlTransfer == 0) {
                return;
            }
            throw new IOException("Setting parameters failed: result=" + controlTransfer);
        }

        private int getStatus() throws IOException {
            byte[] bArr = new byte[2];
            int controlTransfer = this.mConnection.controlTransfer(REQTYPE_DEVICE_TO_HOST, 5, 0, this.mPortNumber + 1, bArr, 2, 5000);
            if (controlTransfer != 2) {
                throw new IOException("Get modem status failed: result=" + controlTransfer);
            }
            return bArr[0];
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public boolean getCD() throws IOException {
            return (getStatus() & 128) != 0;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public boolean getCTS() throws IOException {
            return (getStatus() & 16) != 0;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public boolean getDSR() throws IOException {
            return (getStatus() & 32) != 0;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public boolean getDTR() throws IOException {
            return this.dtr;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public void setDTR(boolean z) throws IOException {
            int controlTransfer = this.mConnection.controlTransfer(64, 1, z ? 257 : 256, this.mPortNumber + 1, null, 0, 5000);
            if (controlTransfer != 0) {
                throw new IOException("Set DTR failed: result=" + controlTransfer);
            }
            this.dtr = z;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public boolean getRI() throws IOException {
            return (getStatus() & 64) != 0;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public boolean getRTS() throws IOException {
            return this.rts;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public void setRTS(boolean z) throws IOException {
            int controlTransfer = this.mConnection.controlTransfer(64, 1, z ? 514 : 512, this.mPortNumber + 1, null, 0, 5000);
            if (controlTransfer != 0) {
                throw new IOException("Set DTR failed: result=" + controlTransfer);
            }
            this.rts = z;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public EnumSet<UsbSerialPort.ControlLine> getControlLines() throws IOException {
            int status = getStatus();
            EnumSet<UsbSerialPort.ControlLine> noneOf = EnumSet.noneOf(UsbSerialPort.ControlLine.class);
            if (this.rts) {
                noneOf.add(UsbSerialPort.ControlLine.RTS);
            }
            if ((status & 16) != 0) {
                noneOf.add(UsbSerialPort.ControlLine.CTS);
            }
            if (this.dtr) {
                noneOf.add(UsbSerialPort.ControlLine.DTR);
            }
            if ((status & 32) != 0) {
                noneOf.add(UsbSerialPort.ControlLine.DSR);
            }
            if ((status & 128) != 0) {
                noneOf.add(UsbSerialPort.ControlLine.CD);
            }
            if ((status & 64) != 0) {
                noneOf.add(UsbSerialPort.ControlLine.RI);
            }
            return noneOf;
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public EnumSet<UsbSerialPort.ControlLine> getSupportedControlLines() throws IOException {
            return EnumSet.allOf(UsbSerialPort.ControlLine.class);
        }

        @Override // com.hoho.android.usbserial.driver.CommonUsbSerialPort, com.hoho.android.usbserial.driver.UsbSerialPort
        public void purgeHwBuffers(boolean z, boolean z2) throws IOException {
            int controlTransfer;
            int controlTransfer2;
            if (z && (controlTransfer2 = this.mConnection.controlTransfer(64, 0, 1, this.mPortNumber + 1, null, 0, 5000)) != 0) {
                throw new IOException("purge write buffer failed: result=" + controlTransfer2);
            }
            if (!z2 || (controlTransfer = this.mConnection.controlTransfer(64, 0, 2, this.mPortNumber + 1, null, 0, 5000)) == 0) {
                return;
            }
            throw new IOException("purge read buffer failed: result=" + controlTransfer);
        }

        public void setLatencyTimer(int i) throws IOException {
            int controlTransfer = this.mConnection.controlTransfer(64, 9, i, this.mPortNumber + 1, null, 0, 5000);
            if (controlTransfer == 0) {
                return;
            }
            throw new IOException("Set latency timer failed: result=" + controlTransfer);
        }

        public int getLatencyTimer() throws IOException {
            byte[] bArr = new byte[1];
            int controlTransfer = this.mConnection.controlTransfer(REQTYPE_DEVICE_TO_HOST, 10, 0, this.mPortNumber + 1, bArr, 1, 5000);
            if (controlTransfer != 1) {
                throw new IOException("Get latency timer failed: result=" + controlTransfer);
            }
            return bArr[0];
        }
    }

    public static Map<Integer, int[]> getSupportedDevices() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(Integer.valueOf(UsbId.VENDOR_FTDI), new int[]{UsbId.FTDI_FT232R, UsbId.FTDI_FT232H, UsbId.FTDI_FT2232H, UsbId.FTDI_FT4232H, UsbId.FTDI_FT231X});
        return linkedHashMap;
    }
}
