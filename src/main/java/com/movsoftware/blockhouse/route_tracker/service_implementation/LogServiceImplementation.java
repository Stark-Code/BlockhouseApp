package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Log;
import com.movsoftware.blockhouse.route_tracker.repository.LogRepository;
import com.movsoftware.blockhouse.route_tracker.service.LogService;

@Service
public class LogServiceImplementation implements LogService{
    
    @Autowired
    private LogRepository logRepository;

    @Override
    public Log saveLog(Log log) {
        return logRepository.save(log);
    }

    @Override
    public List<Log> getLogsByUserId(String userId) {
        return logRepository.findByUserUid(userId);
    }
}
