package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.ShsConfigEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShsConfigService {
    private static final String ENROLL_END_DATE = "enrollEndDate";

    private final ShsConfigRepository shsConfigRepository;

    public ShsConfigService(ShsConfigRepository shsConfigRepository) {
        this.shsConfigRepository = shsConfigRepository;
    }

    public Date getEnrollEndDate() {
        final ShsConfigEntity config = shsConfigRepository.findById(ENROLL_END_DATE).orElse(null);
        if (config != null) {
            return new Date(Long.parseLong(config.getValue()));
        }

        return new Date(0);
    }

    public void setEnrollEndDate(Date date) {
        final ShsConfigEntity config = new ShsConfigEntity();
        config.setKey(ENROLL_END_DATE);
        config.setValue(Long.toString(date.getTime()));
        shsConfigRepository.save(config);
    }
}
