package com.transports.spring.model.templategeneration;

public abstract class AbstractTemplateFile {
    protected final AbstractTemplateExcel templateExcel;
    protected final AbstractTemplateJpg templateJpg;

    protected AbstractTemplateFile(AbstractTemplateExcel templateExcel, AbstractTemplateJpg templateJpg) {
        this.templateExcel = templateExcel;
        this.templateJpg = templateJpg;
    }
}