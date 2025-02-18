package com.baidu.trace;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import com.baidu.mapapi.UIMsg;
import com.baidu.trace.api.bos.BosGeneratePresignedUrlRequest;
import com.baidu.trace.api.bos.BosGeneratePresignedUrlResponse;
import com.baidu.trace.api.bos.BosGetObjectResponse;
import com.baidu.trace.api.bos.BosObjectRequest;
import com.baidu.trace.api.bos.BosObjectResponse;
import com.baidu.trace.api.bos.BosObjectType;
import com.baidu.trace.api.bos.BosPutObjectRequest;
import com.baidu.trace.api.bos.BosPutObjectResponse;
import com.baidu.trace.api.bos.ImageProcessCommand;
import com.baidu.trace.api.bos.ImageWatermarkCommand;
import com.baidu.trace.api.bos.OnBosListener;
import com.baidu.trace.api.bos.TextWatermarkCommand;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.StatusCodes;
import com.baidubce.BceClientException;
import com.baidubce.BceServiceException;
import com.baidubce.Protocol;
import com.baidubce.auth.DefaultBceSessionCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.BosObjectInputStream;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class h {
    private static BosClient a = null;
    private static boolean b = false;

    protected static String a(BosGeneratePresignedUrlRequest bosGeneratePresignedUrlRequest) {
        StringBuffer stringBuffer = new StringBuffer();
        ImageProcessCommand imageProcessCommand = bosGeneratePresignedUrlRequest.getImageProcessCommand();
        ImageWatermarkCommand imageWatermarkCommand = bosGeneratePresignedUrlRequest.getImageWatermarkCommand();
        TextWatermarkCommand textWatermarkCommand = bosGeneratePresignedUrlRequest.getTextWatermarkCommand();
        if (imageProcessCommand != null) {
            if (com.baidu.trace.c.e.a(imageProcessCommand.getScale(), 0, 2)) {
                stringBuffer.append("s_" + imageProcessCommand.getScale());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageProcessCommand.getMaxWidth(), 1, 4096)) {
                stringBuffer.append("w_" + imageProcessCommand.getMaxWidth());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageProcessCommand.getMaxHeight(), 1, 4096)) {
                stringBuffer.append("h_" + imageProcessCommand.getMaxHeight());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageProcessCommand.getQuality(), 1, 100)) {
                stringBuffer.append("q_" + imageProcessCommand.getQuality());
                stringBuffer.append(",");
            }
            if (imageProcessCommand.getFormat() != null) {
                stringBuffer.append("f_" + imageProcessCommand.getFormat().name());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageProcessCommand.getAngle(), -360, 360)) {
                stringBuffer.append("a_" + imageProcessCommand.getAngle());
                stringBuffer.append(",");
            }
            if (imageProcessCommand.getDisplay() != null) {
                stringBuffer.append("d_" + imageProcessCommand.getDisplay().name());
                stringBuffer.append(",");
            }
            stringBuffer.append("l_");
            stringBuffer.append(imageProcessCommand.isLimit() ? 1 : 0);
            stringBuffer.append("|");
            if (com.baidu.trace.c.e.a(imageProcessCommand.getOffsetX(), 0, 4096)) {
                stringBuffer.append("x_");
                stringBuffer.append(imageProcessCommand.getOffsetX());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageProcessCommand.getOffsetY(), 0, 4096)) {
                stringBuffer.append("y_");
                stringBuffer.append(imageProcessCommand.getOffsetY());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageProcessCommand.getCropWidth(), 1, 4096)) {
                stringBuffer.append("w_");
                stringBuffer.append(imageProcessCommand.getCropWidth());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageProcessCommand.getCropHeight(), 1, 4096)) {
                stringBuffer.append("h_");
                stringBuffer.append(imageProcessCommand.getCropHeight());
                stringBuffer.append(",");
            }
            stringBuffer.append("c_");
            stringBuffer.append(imageProcessCommand.isCrop() ? 1 : 0);
        }
        if (imageWatermarkCommand != null) {
            if (!TextUtils.isEmpty(stringBuffer.toString())) {
                stringBuffer.append("|");
            }
            if (!TextUtils.isEmpty(imageWatermarkCommand.getObjectKey())) {
                stringBuffer.append("k_");
                stringBuffer.append(imageWatermarkCommand.getObjectKey());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageWatermarkCommand.getGravity(), 1, 9)) {
                stringBuffer.append("g_");
                stringBuffer.append(imageWatermarkCommand.getGravity());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageWatermarkCommand.getGravityX(), UIMsg.m_AppUI.MSG_SENSOR, 4096)) {
                stringBuffer.append("x_");
                stringBuffer.append(imageWatermarkCommand.getGravityX());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageWatermarkCommand.getGravityY(), UIMsg.m_AppUI.MSG_SENSOR, 4096)) {
                stringBuffer.append("y_");
                stringBuffer.append(imageWatermarkCommand.getGravityY());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageWatermarkCommand.getAngle(), -360, 360)) {
                stringBuffer.append("a_");
                stringBuffer.append(imageWatermarkCommand.getAngle());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(imageWatermarkCommand.getOpacity(), 1, 100)) {
                stringBuffer.append("o_");
                stringBuffer.append(imageWatermarkCommand.getOpacity());
                stringBuffer.append(",");
            }
            stringBuffer.append("wm_1");
        }
        if (textWatermarkCommand != null) {
            if (!TextUtils.isEmpty(stringBuffer.toString())) {
                stringBuffer.append("|");
            }
            if (!TextUtils.isEmpty(textWatermarkCommand.getText())) {
                stringBuffer.append("t_");
                stringBuffer.append(textWatermarkCommand.getText());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(textWatermarkCommand.getGravity(), 1, 9)) {
                stringBuffer.append("g_");
                stringBuffer.append(textWatermarkCommand.getGravity());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(textWatermarkCommand.getGravityX(), UIMsg.m_AppUI.MSG_SENSOR, 4096)) {
                stringBuffer.append("x_");
                stringBuffer.append(textWatermarkCommand.getGravityX());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(textWatermarkCommand.getGravityY(), UIMsg.m_AppUI.MSG_SENSOR, 4096)) {
                stringBuffer.append("y_");
                stringBuffer.append(textWatermarkCommand.getGravityY());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(textWatermarkCommand.getAngle(), -360, 360)) {
                stringBuffer.append("a_");
                stringBuffer.append(textWatermarkCommand.getAngle());
                stringBuffer.append(",");
            }
            if (com.baidu.trace.c.e.a(textWatermarkCommand.getFontSize(), 1, 1024)) {
                stringBuffer.append("sz_");
                stringBuffer.append(textWatermarkCommand.getFontSize());
                stringBuffer.append(",");
            }
            if (!TextUtils.isEmpty(textWatermarkCommand.getFontColor())) {
                stringBuffer.append("fc_");
                stringBuffer.append(textWatermarkCommand.getFontColor());
                stringBuffer.append(",");
            }
            if (textWatermarkCommand.getFontFamily() != null) {
                stringBuffer.append("ff_");
                stringBuffer.append(Base64.encodeToString(textWatermarkCommand.getFontFamily().name().getBytes(), 2));
                stringBuffer.append(",");
            }
            if (textWatermarkCommand.getFontStyle() != null) {
                stringBuffer.append("fs_");
                stringBuffer.append(textWatermarkCommand.getFontStyle().name());
                stringBuffer.append(",");
            }
            stringBuffer.append("wm_2");
        }
        return stringBuffer.toString();
    }

    protected static void a() {
        BosClientConfiguration bosClientConfiguration = new BosClientConfiguration();
        bosClientConfiguration.setCredentials(new DefaultBceSessionCredentials(g.a, g.c, g.d));
        bosClientConfiguration.setEndpoint("gz.bcebos.com");
        bosClientConfiguration.setProtocol(Protocol.HTTPS);
        a = new BosClient(bosClientConfiguration);
        b = true;
    }

    private static void a(int i, BosObjectResponse bosObjectResponse, OnBosListener onBosListener, Handler handler) {
        handler.post(new k(i, onBosListener, bosObjectResponse));
    }

    private static void a(int i, String str, BosObjectRequest bosObjectRequest, BosObjectResponse bosObjectResponse, OnBosListener onBosListener, Handler handler) {
        int i2;
        if (a == null) {
            bosObjectResponse.setStatus(StatusCodes.BOS_UNINITIALIZED);
            bosObjectResponse.setMessage(StatusCodes.MSG_BOS_UNINITIALIZED);
            a(i, bosObjectResponse, onBosListener, handler);
            b = false;
            if (bc.a().c()) {
                return;
            }
            LBSTraceClient.a.a();
            return;
        }
        if (b) {
            long j = g.b;
            try {
                if (i != 1) {
                    if (i != 2) {
                        return;
                    }
                    a(str, bosObjectRequest, (BosGetObjectResponse) bosObjectResponse, onBosListener, handler);
                    return;
                }
                BosPutObjectRequest bosPutObjectRequest = (BosPutObjectRequest) bosObjectRequest;
                BosPutObjectResponse bosPutObjectResponse = (BosPutObjectResponse) bosObjectResponse;
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(bosPutObjectRequest.getObjectSize());
                if (BosObjectType.image == bosPutObjectRequest.getObjectType()) {
                    objectMetadata.setContentType("image/jpeg");
                }
                long serviceId = bosPutObjectRequest.getServiceId();
                PutObjectResponse putObjectResponse = null;
                if (bosPutObjectRequest.getFile() != null) {
                    putObjectResponse = a.putObject(str, "/" + String.valueOf(serviceId) + "/" + bosPutObjectRequest.getObjectKey(), bosPutObjectRequest.getFile(), objectMetadata);
                } else if (bosPutObjectRequest.getStreamData() != null) {
                    putObjectResponse = a.putObject(str, "/" + String.valueOf(serviceId) + "/" + bosPutObjectRequest.getObjectKey(), bosPutObjectRequest.getStreamData(), objectMetadata);
                } else if (bosPutObjectRequest.getByteArray() != null) {
                    putObjectResponse = a.putObject(str, "/" + String.valueOf(serviceId) + "/" + bosPutObjectRequest.getObjectKey(), bosPutObjectRequest.getByteArray(), objectMetadata);
                } else if (bosPutObjectRequest.getStringData() != null) {
                    putObjectResponse = a.putObject(str, "/" + String.valueOf(serviceId) + "/" + bosPutObjectRequest.getObjectKey(), bosPutObjectRequest.getStringData(), objectMetadata);
                }
                if (putObjectResponse != null) {
                    bosPutObjectResponse.setStatus(0);
                    bosPutObjectResponse.setMessage(StatusCodes.MSG_SUCCESS);
                    bosPutObjectResponse.setETag(putObjectResponse.getETag());
                }
                handler.post(new l(onBosListener, bosPutObjectResponse));
                return;
            } catch (BceServiceException e) {
                synchronized (h.class) {
                    if (400 == e.getStatusCode() && b && j == g.b) {
                        b = false;
                        if (!bc.a().c()) {
                            LBSTraceClient.a.a();
                        }
                    }
                    String str2 = StatusCodes.MSG_REQUEST_FAILED;
                    if (404 == e.getStatusCode()) {
                        i2 = StatusCodes.NOT_EXIST_OBJECT_KEY;
                        str2 = StatusCodes.MSG_NOT_EXIST_OBJECT_KEY;
                    } else {
                        i2 = StatusCodes.REQUEST_FAILED;
                    }
                    bosObjectResponse.setStatus(i2);
                    bosObjectResponse.setMessage(str2);
                }
            } catch (BceClientException | IOException unused) {
                bosObjectResponse.setStatus(StatusCodes.REQUEST_FAILED);
            }
        } else {
            bosObjectResponse.setStatus(StatusCodes.REQUEST_FAILED);
        }
        bosObjectResponse.setMessage(StatusCodes.MSG_REQUEST_FAILED);
        a(i, bosObjectResponse, onBosListener, handler);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void a(BaseRequest baseRequest, int i, boolean z, int i2, String str, OnBosListener onBosListener, Handler handler) {
        Runnable iVar;
        int i3;
        BosPutObjectResponse bosPutObjectResponse;
        InputStream streamData;
        int tag = baseRequest.getTag();
        BosObjectRequest bosObjectRequest = (BosObjectRequest) baseRequest;
        StringBuffer stringBuffer = new StringBuffer();
        if (i == 1) {
            BosPutObjectResponse bosPutObjectResponse2 = new BosPutObjectResponse(tag, i2, str);
            if (z) {
                bosPutObjectResponse2.setObjectKey(bosObjectRequest.getObjectKey());
                bosPutObjectResponse2.setObjectType(bosObjectRequest.getObjectType());
                if (a(str, stringBuffer, bosPutObjectResponse2)) {
                    i3 = 1;
                    bosPutObjectResponse = bosPutObjectResponse2;
                    a(i3, stringBuffer.toString(), bosObjectRequest, bosPutObjectResponse, onBosListener, handler);
                    return;
                }
            }
            iVar = new i(onBosListener, bosPutObjectResponse2);
            handler.post(iVar);
        } else if (i == 2) {
            BosGetObjectResponse bosGetObjectResponse = new BosGetObjectResponse(tag, i2, str);
            if (z) {
                bosGetObjectResponse.setObjectKey(bosObjectRequest.getObjectKey());
                bosGetObjectResponse.setObjectType(bosObjectRequest.getObjectType());
                if (a(str, stringBuffer, bosGetObjectResponse)) {
                    i3 = 2;
                    bosPutObjectResponse = bosGetObjectResponse;
                    a(i3, stringBuffer.toString(), bosObjectRequest, bosPutObjectResponse, onBosListener, handler);
                    return;
                }
            }
            iVar = new j(onBosListener, bosGetObjectResponse);
            handler.post(iVar);
        } else if (i == 3) {
            BosGeneratePresignedUrlResponse bosGeneratePresignedUrlResponse = new BosGeneratePresignedUrlResponse(tag, i2, str);
            if (z) {
                bosGeneratePresignedUrlResponse.setObjectKey(bosObjectRequest.getObjectKey());
                bosGeneratePresignedUrlResponse.setObjectType(bosObjectRequest.getObjectType());
                if (a(str, stringBuffer, bosGeneratePresignedUrlResponse)) {
                    bosGeneratePresignedUrlResponse.setStatus(0);
                    bosGeneratePresignedUrlResponse.setMessage(StatusCodes.MSG_SUCCESS);
                }
            }
            onBosListener.onGeneratePresignedUrlCallback(bosGeneratePresignedUrlResponse);
        }
        if (!(baseRequest instanceof BosPutObjectRequest) || (streamData = ((BosPutObjectRequest) baseRequest).getStreamData()) == null) {
            return;
        }
        try {
            streamData.close();
        } catch (Exception unused) {
        }
    }

    private static void a(String str, BosObjectRequest bosObjectRequest, BosGetObjectResponse bosGetObjectResponse, OnBosListener onBosListener, Handler handler) throws IOException {
        BosObject object = a.getObject(str, "/" + String.valueOf(bosObjectRequest.getServiceId()) + "/" + bosObjectRequest.getObjectKey());
        bosGetObjectResponse.setMetaData(object.getObjectMetadata());
        BosObjectInputStream objectContent = object.getObjectContent();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[4096];
        if (objectContent != null) {
            while (true) {
                int read = objectContent.read(bArr, 0, 4096);
                if (read <= 0) {
                    break;
                } else {
                    byteArrayOutputStream.write(bArr, 0, read);
                }
            }
            bosGetObjectResponse.setStatus(0);
            bosGetObjectResponse.setMessage(StatusCodes.MSG_SUCCESS);
            bosGetObjectResponse.setObjectContent(byteArrayOutputStream);
        }
        handler.post(new m(onBosListener, bosGetObjectResponse));
        try {
            byteArrayOutputStream.close();
            if (objectContent != null) {
                objectContent.close();
            }
        } catch (IOException unused) {
        }
    }

    private static boolean a(String str, StringBuffer stringBuffer, BosObjectResponse bosObjectResponse) {
        if (TextUtils.isEmpty(str)) {
            bosObjectResponse.setStatus(StatusCodes.REQUEST_FAILED);
            bosObjectResponse.setMessage(StatusCodes.MSG_REQUEST_FAILED);
            return false;
        }
        if (StatusCodes.MSG_PARSE_RESPONSE_FAILED.equals(str)) {
            bosObjectResponse.setStatus(StatusCodes.PARSE_RESPONSE_FAILED);
            bosObjectResponse.setMessage(StatusCodes.MSG_PARSE_RESPONSE_FAILED);
            return false;
        }
        if (StatusCodes.MSG_REQUEST_FAILED.equals(str)) {
            bosObjectResponse.setStatus(StatusCodes.REQUEST_FAILED);
            bosObjectResponse.setMessage(StatusCodes.MSG_REQUEST_FAILED);
            return false;
        }
        if (!a.b(str, bosObjectResponse)) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("status") && jSONObject.getInt("status") == 0) {
                if (jSONObject.has("url") && (bosObjectResponse instanceof BosGeneratePresignedUrlResponse)) {
                    ((BosGeneratePresignedUrlResponse) bosObjectResponse).setUrl(jSONObject.getString("url"));
                }
                if (!jSONObject.has("bucket")) {
                    return true;
                }
                stringBuffer.append(jSONObject.getString("bucket"));
                return true;
            }
            if (!jSONObject.has("status")) {
                bosObjectResponse.setStatus(StatusCodes.PARSE_RESPONSE_FAILED);
                bosObjectResponse.setMessage(StatusCodes.MSG_PARSE_RESPONSE_FAILED);
                return false;
            }
            bosObjectResponse.setStatus(jSONObject.getInt("status"));
            if (jSONObject.has("message")) {
                bosObjectResponse.setMessage(jSONObject.getString("message"));
            }
            return false;
        } catch (JSONException unused) {
            bosObjectResponse.setStatus(StatusCodes.PARSE_RESPONSE_FAILED);
            bosObjectResponse.setMessage(StatusCodes.MSG_PARSE_RESPONSE_FAILED);
            return false;
        }
    }
}
