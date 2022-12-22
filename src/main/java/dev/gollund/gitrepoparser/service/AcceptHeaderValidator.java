package dev.gollund.gitrepoparser.service;

import dev.gollund.gitrepoparser.exception.UnsupportedMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AcceptHeaderValidator implements HeaderValidator {

    @Override
    public void accept(String header) {
        if (!MediaType.APPLICATION_JSON_VALUE.equals(header)) {
            throw new UnsupportedMediaTypeException(MediaType.parseMediaType(header));
        }
    }
}
