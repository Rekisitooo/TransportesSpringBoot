package com.transports.spring.service;

import com.transports.spring.model.Month;
import com.transports.spring.repository.IMonthRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MonthService {

    private final IMonthRepository monthRepository;

    public MonthService(final IMonthRepository monthRepository) {
        this.monthRepository = monthRepository;
    }

    public Month findById(final int monthId) {
        final Optional<Month> optionalMonth = this.monthRepository.findById(monthId);
        Month month = new Month();
        if (optionalMonth.isPresent()) {
            month = optionalMonth.get();
        }

        return month;
    }
}
