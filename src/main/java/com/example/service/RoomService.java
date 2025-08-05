package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.dto.RoomRequest;
import com.example.dto.RoomResponse;

public interface RoomService {

	List<RoomResponse> getAllRooms();

	Optional<RoomResponse> getRoomById(Long id);

	RoomResponse createRoom(RoomRequest roomRequest);

	Optional<RoomResponse> updateRoom(Long id, RoomRequest roomRequest);

	boolean deleteRoom(Long id);
}
