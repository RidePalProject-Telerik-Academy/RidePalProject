package com.example.ridepalapplication.services;

import com.example.ridepalapplication.models.SynchronizationDetails;
import org.json.simple.parser.ParseException;

import java.util.List;

public interface SyncService {

    void synchronize() throws ParseException;
    List<SynchronizationDetails> mostRecent();
}
