package com.app.dixon.resourceparser.core.pub.parser;

import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 请求数据
 */

public class Request<T> {

    private String mUrl;
    private Response.Listener<T> mResponseListener;
    private Response.ErrorListener mErrorListener;
    private ParseListener<T> mParseListener;
    private Map<String, String> postData;
    private String charSet; //Request.setCharSet赋值 or Action.charSet赋值

    public Request(String url, ParseListener<T> parseListener, Response.Listener<T> responseListener, Response.ErrorListener errorListener) {
        this.mUrl = url;
        this.mParseListener = parseListener;
        this.mResponseListener = responseListener;
        this.mErrorListener = errorListener;
    }

    public Request(String url, ParseListener<T> parseListener, Response.Listener<T> responseListener) {
        this(url, parseListener, responseListener, null);
    }

    public Request(String url, Map<String, String> postData, ParseListener<T> parseListener, Response.Listener<T> responseListener) {
        this(url, postData, parseListener, responseListener, null);
    }

    public Request(String url, Map<String, String> postData, ParseListener<T> parseListener, Response.Listener<T> responseListener, Response.ErrorListener errorListener) {
        this(url, parseListener, responseListener, errorListener);
        this.postData = postData;
    }

    public Request(Action<T> action) {
        this(action.url(), action, action, null);
        setCharSet(action.charSet());
    }

    public Request(Action1<T> action) {
        this(action.url(), action, action, action);
        setCharSet(action.charSet());
    }

    public Request(ActionPost<T> action) {
        this(action.url(), action.postData(), action, action, null);
        setCharSet(action.charSet());
    }

    public Request(ActionPost1<T> action) {
        this(action.url(), action.postData(), action, action, action);
        setCharSet(action.charSet());
    }

    protected void deliverResponse(T response) {
        if (mResponseListener != null) {
            mResponseListener.onResponse(response);
        }
    }

    protected void deliverError(ParseError error) {
        if (mErrorListener != null) {
            mErrorListener.onErrorResponse(error);
        }
    }

    //解析 耗时操作
    public interface ParseListener<T> {
        T onParse(Document doc);
    }

    protected T parse(Document doc) {
        if (mParseListener != null) {
            return mParseListener.onParse(doc);
        }
        return null;
    }

    public String getUrl() {
        return mUrl;
    }

    public Map<String, String> getPostData() {
        return postData;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getCharSet() {
        return charSet;
    }
}
