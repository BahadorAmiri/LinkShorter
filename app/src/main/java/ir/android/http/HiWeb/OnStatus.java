package ir.android.http.HiWeb;

public interface OnStatus {

        default void onStart() {
        }

        void onResult(boolean isConnected);

        default void onException(Exception e) {
        }

        default void onError(int errorCode) {
        }

        default void onEnd() {
        }

    }