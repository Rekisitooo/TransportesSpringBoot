package com.transports.spring.dto.generatefiles;

import com.transports.spring.model.templategeneration.common.EnumTemplateFileDirectory;
import lombok.Data;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Data
public class DtoTemplateFileDir {
    private final Map<EnumTemplateFileDirectory, Path> pathMap;

    public DtoTemplateFileDir() {
        this.pathMap = new EnumMap<>(EnumTemplateFileDirectory.class);
    }

    public void put(final EnumTemplateFileDirectory fileDir, final Path filePathDir) {
        this.pathMap.put(fileDir, filePathDir);
    }

    public Path get(final EnumTemplateFileDirectory fileDir) {
        return this.pathMap.get(fileDir);
    }

    public void addDirectories(final DtoTemplateFileDir dtoTemplateFileDir) {
        final Map<EnumTemplateFileDirectory, Path> map = dtoTemplateFileDir.getPathMap();
        for (final Map.Entry<EnumTemplateFileDirectory, Path> enumTemplateFileDirectoryPathEntry : map.entrySet()) {
            final EnumTemplateFileDirectory key = enumTemplateFileDirectoryPathEntry.getKey();
            final Path value = enumTemplateFileDirectoryPathEntry.getValue();

            this.pathMap.put(key, value);
        }
    }
}
