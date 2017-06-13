package com.yusong.configlibrary.http;

/**
 * Created by wenxin
 * contact with yangwenxin711@gmail.com
 */
public class HttpResult<T> {
    private boolean error;

    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
