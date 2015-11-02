package io.bloc.android.blocspot.api.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 11/2/2015.
 */
public abstract class NetworkRequest<Result> {

        //Error integer codes
    public static final int ERROR_IO = 1;
    public static final int ERROR_MALFORMED_URL = 2;

        //error code, if error occurs
    private int mErrorCode;

        //---------Setters--------
    protected void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

        //---------Getters---------
    public int getErrorCode() {
        return mErrorCode;
    }

        //------Abstract Methods----------
    public abstract Result performRequest();

        //open the url and return the stream
    protected InputStream openStream(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            setErrorCode(ERROR_MALFORMED_URL);
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            setErrorCode(ERROR_IO);
            return null;
        }
        return inputStream;
    }
}
