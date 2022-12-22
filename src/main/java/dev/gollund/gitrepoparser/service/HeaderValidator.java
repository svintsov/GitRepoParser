package dev.gollund.gitrepoparser.service;

import org.springframework.stereotype.Service;

@Service
public interface HeaderValidator {

    void accept(String header);
}
