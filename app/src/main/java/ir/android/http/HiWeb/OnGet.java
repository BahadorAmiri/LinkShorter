package ir.android.http.HiWeb;

import java.util.Map;

public interface OnGet {

    default void onStart() {
    }

    default void onProgress(Integer values) {
    }

    default Map<String, String> getHeader() {
        return null;
    }

    void onResult(String result);

    default void onError(int errorCode) {
    }

    default void onException(Exception e) {
    }

    default void onEnd() {
    }

}
