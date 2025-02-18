package com.jieli.stream.dv.running2.task;

import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/* loaded from: classes.dex */
public class SDPServer extends Thread {
    private int mFramePerSecond;
    private int mRtpAudioPort;
    private int mRtpVideoPort;
    private boolean mRunning;
    private int mSampleRate;
    private ServerSocket mServerSocket;
    private int mVideoFormat;
    private String tag;

    public SDPServer(int i) {
        this(i, 1);
    }

    public SDPServer(int i, int i2) {
        this.tag = getClass().getSimpleName();
        this.mRunning = false;
        this.mVideoFormat = 1;
        this.mSampleRate = IConstant.AUDIO_SAMPLE_RATE_DEFAULT;
        this.mFramePerSecond = 30;
        this.mRtpVideoPort = IConstant.RTP_VIDEO_PORT1;
        this.mRtpAudioPort = IConstant.RTP_AUDIO_PORT1;
        this.mVideoFormat = i2;
        try {
            this.mServerSocket = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerSocket serverSocket = this.mServerSocket;
        if (serverSocket != null) {
            try {
                serverSocket.setReuseAddress(true);
            } catch (SocketException e2) {
                e2.printStackTrace();
            }
            try {
                this.mServerSocket.bind(new InetSocketAddress(i));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void setSampleRate(int i) {
        this.mSampleRate = i;
    }

    public void setFrameRate(int i) {
        this.mFramePerSecond = i;
    }

    public void stopRunning() {
        this.mRunning = false;
        ServerSocket serverSocket = this.mServerSocket;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.mServerSocket = null;
    }

    public void setRtpVideoPort(int i) {
        this.mRtpVideoPort = i;
    }

    public void setRtpAudioPort(int i) {
        this.mRtpAudioPort = i;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int i;
        String str;
        super.run();
        this.mRunning = true;
        if (this.mVideoFormat == 1) {
            i = 96;
            str = "H264";
        } else {
            i = 26;
            str = "JPEG";
        }
        String str2 = "c=IN IP4 127.0.0.1\nm=audio " + this.mRtpAudioPort + " RTP/AVP 97\na=rtpmap:97 L16/" + this.mSampleRate + "/1\na=ptime:20\nm=video " + this.mRtpVideoPort + " RTP/AVP " + i + "\na=rtpmap:" + i + " " + str + "/90000\na=framerate:" + this.mFramePerSecond;
        Dbug.w(this.tag, "SDP:\n" + str2);
        while (this.mRunning) {
            ServerSocket serverSocket = this.mServerSocket;
            if (serverSocket != null) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (IOException unused) {
                }
                if (socket != null) {
                    try {
                        socket.getOutputStream().write(str2.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.getOutputStream().flush();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        stopRunning();
        Dbug.w(this.tag, "SDP Server exit.");
    }
}
