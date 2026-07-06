package com.example.purepawapp.ui.common;

public abstract class UiState<T> {

    private UiState() {
    }

    public static final class Loading<T> extends UiState<T> {
    }

    public static final class Success<T> extends UiState<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public static final class Error<T> extends UiState<T> {
        private final String message;
        private final Throwable throwable;

        public Error(String message, Throwable throwable) {
            this.message = message;
            this.throwable = throwable;
        }

        public String getMessage() {
            return message;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    public static final class Empty<T> extends UiState<T> {
    }
}
