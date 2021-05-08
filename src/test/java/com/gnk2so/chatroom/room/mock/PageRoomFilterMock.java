package com.gnk2so.chatroom.room.mock;

import com.gnk2so.chatroom.room.controlller.filter.PageRoomFilter;

public class PageRoomFilterMock {

    public static PageRoomFilter build() {
        return PageRoomFilter.builder()
            .page(1)
            .size(5)
            .build();
    }
    
}
