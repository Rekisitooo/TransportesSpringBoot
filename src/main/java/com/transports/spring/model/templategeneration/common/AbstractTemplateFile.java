package com.transports.spring.model.templategeneration.common;

public abstract class AbstractTemplateFile {

    protected final AbstractTemplateJpg templateJpg;

    protected AbstractTemplateFile(AbstractTemplateJpg templateJpg) {
        this.templateJpg = templateJpg;
    }
}