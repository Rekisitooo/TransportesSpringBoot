package com.transports.spring.operation;

import com.transports.spring.exception.CreatingTemplateFileException;

import java.io.IOException;

public interface IGenerateTemplateFilesService {
    void generateFiles(int templateId) throws IOException, CreatingTemplateFileException;
}
