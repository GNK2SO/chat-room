package com.gnk2so.chatroom.room.repository;

import java.util.Optional;

import com.gnk2so.chatroom.room.model.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByChannel(String channel);
    Page<Room> findByTitleContaining(String title, Pageable pageable);
}
