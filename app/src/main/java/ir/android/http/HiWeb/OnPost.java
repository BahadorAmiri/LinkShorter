package ir.android.http.HiWeb;

import java.util.Map;

public interface OnPost {

        default void onStart() {
        }

        default Map<String, String> getHeader() {
            return null;
        }

        default String getBody() {
            return null;
        }

        void onResult(String result);

        default void onError(int errorCode) {
        }

        default void onProgress(Integer values) {
        }

        default void onException(Exception e) {
        }

        default void onEnd() {
        }

    }