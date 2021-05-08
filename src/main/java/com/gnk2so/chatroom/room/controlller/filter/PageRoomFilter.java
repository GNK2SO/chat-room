package com.gnk2so.chatroom.room.controlller.filter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRoomFilter {

    private int page;
    private int size;
    private String title;

    public Pageable getPageConfig() {
        return PageRequest.of(
            page >= 0 ? page : 1, 
            size > 0 ? size : 24
        );
    }

    public boolean hasTitle() {
        return title != null;
    }
}
