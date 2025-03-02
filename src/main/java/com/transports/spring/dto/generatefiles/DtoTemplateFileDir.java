package com.transports.spring.dto.generatefiles;

import com.transports.spring.model.templategeneration.common.EnumTemplateFileDirectory;
import lombok.Data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Data
public class DtoTemplateFileDir {
    private final Map<EnumTemplateFileDirectory, Path> pathMap;

    public DtoTemplateFileDir() {
        this.pathMap = new HashMap<>();
    }

    public void put(final EnumTemplateFileDirectory fileDir, final Path filePathDir) {
        this.pathMap.put(fileDir, filePathDir);
    }

    public Path get(final EnumTemplateFileDirectory fileDir) {
        return this.pathMap.get(fileDir);
    }
}
