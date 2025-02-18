package com.baidu.trace;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.baidu.trace.IListener;
import com.baidu.trace.model.TraceLocation;

/* loaded from: classes.dex */
public interface IService extends IInterface {

    public static abstract class Stub extends Binder implements IService {
        private static final String DESCRIPTOR = "com.baidu.trace.IService";
        static final int TRANSACTION_handleExtendedOperate = 11;
        static final int TRANSACTION_handleLocalFence = 10;
        static final int TRANSACTION_queryRealTimeLoc = 6;
        static final int TRANSACTION_registerListener = 12;
        static final int TRANSACTION_setCacheSize = 4;
        static final int TRANSACTION_setInterval = 3;
        static final int TRANSACTION_setLocTimeOffset = 5;
        static final int TRANSACTION_setLocationMode = 2;
        static final int TRANSACTION_setProtocolType = 1;
        static final int TRANSACTION_startGather = 8;
        static final int TRANSACTION_stopGather = 9;
        static final int TRANSACTION_stopTrace = 7;
        static final int TRANSACTION_unregisterListener = 13;

        static class Proxy implements IService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.baidu.trace.IService
            public void handleExtendedOperate(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public boolean handleLocalFence(int i, long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public boolean queryRealTimeLoc(TraceLocation traceLocation) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    if (obtain2.readInt() != 0) {
                        traceLocation.readFromParcel(obtain2);
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public void registerListener(IListener iListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iListener != null ? iListener.asBinder() : null);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public boolean setCacheSize(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public boolean setInterval(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public boolean setLocTimeOffset(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public boolean setLocationMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public boolean setProtocolType(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public int startGather() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public int stopGather() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public void stopTrace(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.baidu.trace.IService
            public void unregisterListener() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IService)) ? new Proxy(iBinder) : (IService) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean protocolType = setProtocolType(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(protocolType ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean locationMode = setLocationMode(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(locationMode ? 1 : 0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean interval = setInterval(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(interval ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean cacheSize = setCacheSize(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(cacheSize ? 1 : 0);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean locTimeOffset = setLocTimeOffset(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(locTimeOffset ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    TraceLocation traceLocation = new TraceLocation();
                    boolean queryRealTimeLoc = queryRealTimeLoc(traceLocation);
                    parcel2.writeNoException();
                    parcel2.writeInt(queryRealTimeLoc ? 1 : 0);
                    parcel2.writeInt(1);
                    traceLocation.writeToParcel(parcel2, 1);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    stopTrace(parcel.readLong(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    int startGather = startGather();
                    parcel2.writeNoException();
                    parcel2.writeInt(startGather);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    int stopGather = stopGather();
                    parcel2.writeNoException();
                    parcel2.writeInt(stopGather);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean handleLocalFence = handleLocalFence(parcel.readInt(), parcel.readLong(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(handleLocalFence ? 1 : 0);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    handleExtendedOperate(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerListener(IListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterListener();
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void handleExtendedOperate(int i) throws RemoteException;

    boolean handleLocalFence(int i, long j, String str) throws RemoteException;

    boolean queryRealTimeLoc(TraceLocation traceLocation) throws RemoteException;

    void registerListener(IListener iListener) throws RemoteException;

    boolean setCacheSize(int i) throws RemoteException;

    boolean setInterval(int i, int i2) throws RemoteException;

    boolean setLocTimeOffset(int i) throws RemoteException;

    boolean setLocationMode(int i) throws RemoteException;

    boolean setProtocolType(int i) throws RemoteException;

    int startGather() throws RemoteException;

    int stopGather() throws RemoteException;

    void stopTrace(long j, String str) throws RemoteException;

    void unregisterListener() throws RemoteException;
}
