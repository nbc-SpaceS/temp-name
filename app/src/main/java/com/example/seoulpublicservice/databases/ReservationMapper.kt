package com.example.seoulpublicservice.databases

object ReservationMapper {
    fun mapperToReservation() {

    }

    fun mapperToReservationResponse() {

    }
}

// Context는 Application을 이미 상속받고 있는 SPSA가 아닌 AppContainer에 추가로 삽입하기만 하면 된다.
// usecase가 필요 없으면 override해도 되고
// usecase가 필요하면 repo를 private로 잠그고 usecase에 repo를 연결하고 usecase만 출력하게 하면 된다. fun abcUseCase