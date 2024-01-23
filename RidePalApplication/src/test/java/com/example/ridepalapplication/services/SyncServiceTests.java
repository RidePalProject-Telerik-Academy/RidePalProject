package com.example.ridepalapplication.services;

import com.example.ridepalapplication.consumers.DeezerApiConsumer;
import com.example.ridepalapplication.repositories.SyncGenresRepository;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.argThat;


@ExtendWith(MockitoExtension.class)
public class SyncServiceTests {

    @Mock
    SyncGenresRepository repository;

    @Mock
    DeezerApiConsumer apiConsumer;

    @InjectMocks
    SyncServiceImpl syncService;

    @Test
    public void mostRecent_Should_Call_Repository_When_Called(){

        syncService.mostRecent();

        Mockito.verify(repository,Mockito.times(1)).getMostRecent();

    }

    @Test
    public void synchronize_Should_Log_Success_When_No_Exception() throws ParseException {
        Mockito.doNothing().when(apiConsumer).populateGenres();
        syncService.synchronize();
        Mockito.verify(apiConsumer,Mockito.times(1)).populateGenres();
        Mockito.verify(repository, Mockito.times(1)).save(argThat
                (synchronizationDetails -> "success".equals(synchronizationDetails.getStatus()) &&
                        synchronizationDetails.getSyncTime() != null));

    }
    @Test
    public void synchronize_Should_Log_Failed_When_Exception() throws ParseException {
        Mockito.doThrow(ParseException.class).when(apiConsumer).populateGenres();
        syncService.synchronize();
        Mockito.verify(apiConsumer,Mockito.times(1)).populateGenres();
        Mockito.verify(repository, Mockito.times(1)).save(argThat
                (synchronizationDetails -> "failure".equals(synchronizationDetails.getStatus()) &&
                        synchronizationDetails.getSyncTime() != null));

    }
}
