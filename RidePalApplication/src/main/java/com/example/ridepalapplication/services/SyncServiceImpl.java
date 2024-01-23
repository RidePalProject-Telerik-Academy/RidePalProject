package com.example.ridepalapplication.services;

import com.example.ridepalapplication.consumers.DeezerApiConsumer;
import com.example.ridepalapplication.models.SynchronizationDetails;
import com.example.ridepalapplication.repositories.SyncGenresRepository;
import com.example.ridepalapplication.services.contracts.SyncService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SyncServiceImpl implements SyncService {
    private final SyncGenresRepository syncGenresRepository;
    private final DeezerApiConsumer apiConsumer;
    @Autowired
    public SyncServiceImpl(SyncGenresRepository syncGenresRepository, DeezerApiConsumer apiConsumer) {
        this.syncGenresRepository = syncGenresRepository;
        this.apiConsumer = apiConsumer;
    }

    @Override
    public void synchronize()  {

        LocalDateTime lastSyncTime = LocalDateTime.now();
        SynchronizationDetails synchronizationDetails = new SynchronizationDetails();

        try {
            apiConsumer.populateGenres();

            synchronizationDetails.setStatus("success");
            synchronizationDetails.setSyncTime(lastSyncTime);
            syncGenresRepository.save(synchronizationDetails);
        } catch (ParseException e){
            synchronizationDetails.setStatus("failure");
            synchronizationDetails.setSyncTime(lastSyncTime);
            syncGenresRepository.save(synchronizationDetails);

        }
    }

    @Override
    public List<SynchronizationDetails> mostRecent() {
        return syncGenresRepository.getMostRecent();
    }

}
