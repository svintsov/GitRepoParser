package dev.gollund.gitrepoparser.exception;

import org.springframework.http.MediaType;

public class UnsupportedMediaTypeException extends RuntimeException {

    public UnsupportedMediaTypeException(MediaType mediaType) {
        super(String.format("The media type: %s for the Accept header is not supported",
                mediaType.toString()));
    }
}
