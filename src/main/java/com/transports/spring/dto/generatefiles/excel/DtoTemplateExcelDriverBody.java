package com.transports.spring.dto.generatefiles.excel;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.model.TransportDateByTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoTemplateExcelDriverBody {
    private List<DtoDriverTransport> allTemplateDriverTransports;
}
