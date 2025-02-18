package com.xyzlf.share.library.request;

import android.os.AsyncTask;

/* loaded from: classes2.dex */
public abstract class AbstractAsyncTask<T> extends AsyncTask<Void, Integer, T> {
    private T data;
    private Exception exception;

    protected abstract T doLoadData() throws Exception;

    public void onException(Exception exc) {
    }

    public void onFinally() {
    }

    public void onSuccess(T t) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public T doInBackground(Void... voidArr) {
        try {
            this.data = doLoadData();
            this.exception = null;
        } catch (Exception e) {
            this.data = null;
            this.exception = e;
        }
        return getData();
    }

    public Exception getException() {
        return this.exception;
    }

    public T getData() {
        return this.data;
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(T t) {
        try {
            try {
                if (this.exception == null) {
                    onSuccess(t);
                } else {
                    onException(this.exception);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            onFinally();
        }
    }
}
