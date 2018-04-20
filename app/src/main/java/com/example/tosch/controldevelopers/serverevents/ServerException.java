package com.example.tosch.controldevelopers.serverevents;

public class ServerException extends RuntimeException {

        private static final long serialVersionUID = -346348954L;

        public ServerException() {
        }

        public ServerException(String message) {
            super(message);
        }

        public ServerException(String message, Throwable cause) {
            super(message, cause);
        }

        public ServerException(Throwable cause) {
            super(cause);
        }


}
