package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.ShsConfigEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ShsConfigServiceTest {
    private ShsConfigService shsConfigService;
    private ShsConfigRepository shsConfigRepository;

    @BeforeEach
    public void setUp() {
        shsConfigRepository = mock(ShsConfigRepository.class);
        shsConfigService = new ShsConfigService(shsConfigRepository);
    }

    @Test
    public void getEnrollEndDateReturnsCorrectDateWhenConfigExists() {
        final ShsConfigEntity config = new ShsConfigEntity();
        config.setKey("enrollEndDate");
        config.setValue(Long.toString(1672531200000L)); // Example timestamp

        when(shsConfigRepository.findById("enrollEndDate")).thenReturn(Optional.of(config));

        Date result = shsConfigService.getEnrollEndDate();
        assertEquals(new Date(1672531200000L), result);

        verify(shsConfigRepository, times(1)).findById("enrollEndDate");
    }

    @Test
    public void getEnrollEndDateReturnsEpochWhenConfigDoesNotExist() {
        when(shsConfigRepository.findById("enrollEndDate")).thenReturn(Optional.empty());

        Date result = shsConfigService.getEnrollEndDate();
        assertEquals(new Date(0), result);

        verify(shsConfigRepository, times(1)).findById("enrollEndDate");
    }

    @Test
    public void setEnrollEndDateSavesConfigWithCorrectValues() {
        final Date date = new Date(1672531200000L);

        shsConfigService.setEnrollEndDate(date);

        verify(shsConfigRepository, times(1)).save(argThat(config ->
                "enrollEndDate".equals(config.getKey()) && Long.toString(1672531200000L).equals(config.getValue())
        ));
    }
}
