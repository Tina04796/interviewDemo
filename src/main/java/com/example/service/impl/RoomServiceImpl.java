package com.example.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.RoomRequest;
import com.example.dto.RoomResponse;
import com.example.model.Room;
import com.example.repository.RoomRepository;
import com.example.service.RoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomResponse> getRoomById(Long id) {
        return roomRepository.findById(id)
            .map(this::convertToResponse);
    }

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {
        Room room = convertToEntity(roomRequest);
        Room saved = roomRepository.save(room);
        return convertToResponse(saved);
    }

    @Override
    public Optional<RoomResponse> updateRoom(Long id, RoomRequest roomRequest) {
        return roomRepository.findById(id).map(room -> {
            room.setName(roomRequest.getName());
            room.setLocation(roomRequest.getLocation());
            room.setCapacity(roomRequest.getCapacity());
            Room updated = roomRepository.save(room);
            return convertToResponse(updated);
        });
    }

    @Override
    public boolean deleteRoom(Long id) {
        return roomRepository.findById(id).map(room -> {
            roomRepository.delete(room);
            return true;
        }).orElse(false);
    }

    private Room convertToEntity(RoomRequest request) {
        Room room = new Room();
        room.setName(request.getName());
        room.setLocation(request.getLocation());
        room.setCapacity(request.getCapacity());
        return room;
    }

    private RoomResponse convertToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setLocation(room.getLocation());
        response.setCapacity(room.getCapacity());
        return response;
    }
}
