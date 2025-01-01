package com.begin_a_gain.feature.main.omok_list

import com.begin_a_gain.domain.model.OmokRoom
import com.begin_a_gain.domain.enum.OmokRoomStatus
import org.joda.time.DateTime

data class OmokListState(
    val currentDate: DateTime = DateTime.now(),
    val omokRooms: List<OmokRoom> = listOf(
        OmokRoom(status = OmokRoomStatus.Todo, title = "1일 1Commit"),
        OmokRoom(status = OmokRoomStatus.Done, title = "명상하기"),
        OmokRoom(status = OmokRoomStatus.Skip, title = "블로그 쓰기")
    ) + (1..5).map {
        OmokRoom(status = OmokRoomStatus.None)
    }
)

interface OmokListSideEffect {

}