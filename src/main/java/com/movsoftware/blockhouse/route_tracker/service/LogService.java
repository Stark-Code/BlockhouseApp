package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;

import com.movsoftware.blockhouse.route_tracker.entities.Log;

public interface LogService {
    Log saveLog(Log log);
    List<Log> getLogsByUserId(String userId);
}
