package com.example.ridepalapplication.services;

import com.example.ridepalapplication.helpers.DeezerApiConsumer;
import com.example.ridepalapplication.models.SynchronizationDetails;
import com.example.ridepalapplication.repositories.SynchronizationDetailsRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SynchronizationServiceImpl implements SynchronizationService {
    private final SynchronizationDetailsRepository synchronizationDetailsRepository;
    private final DeezerApiConsumer apiConsumer;
    @Autowired
    public SynchronizationServiceImpl(SynchronizationDetailsRepository synchronizationDetailsRepository, DeezerApiConsumer apiConsumer) {
        this.synchronizationDetailsRepository = synchronizationDetailsRepository;
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
            synchronizationDetailsRepository.save(synchronizationDetails);
        } catch (ParseException e){
            synchronizationDetails.setStatus("failure");
            synchronizationDetails.setSyncTime(lastSyncTime);
            synchronizationDetailsRepository.save(synchronizationDetails);

        }
    }

    @Override
    public List<SynchronizationDetails> mostRecent() {
        return synchronizationDetailsRepository.getMostRecent();
    }

}
