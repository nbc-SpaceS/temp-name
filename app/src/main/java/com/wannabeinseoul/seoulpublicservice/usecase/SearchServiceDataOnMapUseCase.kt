package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row

class SearchServiceDataOnMapUseCase(
    private val reservationRepository: ReservationRepository,
    private val dbMemoryRepository: DbMemoryRepository,
) {
    operator fun invoke(word: String, savedOptions: List<List<String>>): HashMap<Pair<String, String>, List<Row>> {
        val hash: HashMap<Pair<String, String>, List<Row>> = hashMapOf()
//                var item = RoomRowMapper.mappingRoomToRow(
//                    reservationRepository.getFilter(
//                        loadedData.subList(0, 5).flatten(),
//                        loadedData.subList(5, 7).flatten(),
//                        loadedData[7],
//                        loadedData[8],
//                    )
//                )

        val item = dbMemoryRepository.getFilteredPlusWord(
            word,
            savedOptions.subList(0, 5).flatten(),
            savedOptions.subList(5, 7).flatten(),
            savedOptions[7],
            savedOptions[8],
        )

        for (i in item) {
            if (hash.containsKey(Pair(i.y, i.x))) {
                hash[Pair(i.y, i.x)] = hash[Pair(i.y, i.x)].orEmpty().toMutableList() + i
            } else {
                hash[Pair(i.y, i.x)] = listOf(i)
            }
        }

        return hash
    }
}
