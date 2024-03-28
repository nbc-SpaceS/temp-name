package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository

class FilterServiceDataOnMapUseCase(
    private val reservationRepository: ReservationRepository,
    private val dbMemoryRepository: DbMemoryRepository,
) {
    operator fun invoke(savedOptions: List<List<String>>): HashMap<Pair<String, String>, List<ReservationEntity>> {
        val hash: HashMap<Pair<String, String>, List<ReservationEntity>> = hashMapOf()
//                var item = RoomRowMapper.mappingRoomToRow(
//                    reservationRepository.getFilter(
//                        loadedData.subList(0, 5).flatten(),
//                        loadedData.subList(5, 7).flatten(),
//                        loadedData[7],
//                        loadedData[8],
//                    )
//                )

        val item = dbMemoryRepository.getFiltered(
            savedOptions.subList(0, 5).flatten(),
            savedOptions.subList(5, 7).flatten(),
            savedOptions[7],
            savedOptions[8],
        )

        for (i in item) {
            if (hash.containsKey(Pair(i.Y, i.X))) {
                hash[Pair(i.Y, i.X)] = hash[Pair(i.Y, i.X)].orEmpty().toMutableList() + i
            } else {
                hash[Pair(i.Y, i.X)] = listOf(i)
            }
        }

        return hash
    }
}
