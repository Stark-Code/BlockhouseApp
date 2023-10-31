package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.repository.GymRepository;
import com.movsoftware.blockhouse.route_tracker.service.GymService;

@Service
public class GymServiceImplementation implements GymService {

    @Autowired
    private GymRepository gymRepository;

    @Override
    public Gym saveGym(Gym gym) {
        return gymRepository.save(gym);
    }

    @Override
    public Optional<Gym> findById(String id) {
        return gymRepository.findById(id);
    }

    @Override
    public List<Gym> findAllGyms() {
        return (List<Gym>) gymRepository.findAll();
    }

    @Override
    public void deleteGymById(String id) {
        gymRepository.deleteById(id);
    }
}
