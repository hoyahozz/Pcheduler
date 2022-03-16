# Pcheduler

![Pcheduler_logo](https://user-images.githubusercontent.com/85336456/139528980-8b65bcac-c843-4be2-9aae-38ef973bf781.png)

계획 수립 어려움을 겪는 MBTI `P` 들에게 도움을 주고자, 최대한 간단하게 UI/UX를 구성한 스케줄러 어플리케이션입니다.
아직 미완성 작품이며 시간이 날 때마다 보완 및 개발할 예정입니다.

## Function

<div align="center"> 

![Pcheduler Test](https://user-images.githubusercontent.com/85336456/158527533-b27a6873-eb47-4f01-8747-d14266c81798.gif)
![Pcheduler Test 2](https://user-images.githubusercontent.com/85336456/158527536-7899adec-b38d-405f-91e7-7fd147aa12b8.gif)
  
![Pcheduler Test 3](https://user-images.githubusercontent.com/85336456/158527540-040aa837-e284-42a3-a843-da982a9fce9a.gif)  ![Pcheduler Test 4](https://user-images.githubusercontent.com/85336456/158527949-9ca00264-ca21-45fc-8c97-109264d901ab.gif)

</div>

## Tech Stack
`Kotlin`,  `JetPack`, `AAC`, `ViewBinding`, `ViewModel`, `LiveData`, `Coroutine`, `Room`, `Alarm Manager`, `ListAdapter`

## Environment

- Android Studio Bumblebee | 2021.1.1
- minSdkVersion 26
- targetSdkVersion 31
- Test Device | Galaxy Note 8

## Learned
- `Room`을 사용하며 처음으로 안드로이드 내부 데이터베이스를 구축해보았습니다.
- `AAC` 컴포넌트를 처음으로 사용하며 `MVVM` 패턴을 익힐 수 있었습니다.
- `Alarm Manager` 를 활용하여 이용자에게 알람을 보내는 방법을 익힐 수 있었습니다.
- `PendingIntent`를 활용하여 정해진 시간에 인텐트를 실행하는 방법을 익힐 수 있었습니다.
- `Material CalendarView`를 상황에 맞게 커스텀하고, 이벤트에 따라 원하는 기능을 구현하는 방법을 익힐 수 있었습니다.
- `Fragment Result API`를 활용하여 프래그먼트 간 통신 방법을 익힐 수 있었습니다.
- 오픈 소스인 `SingleAndTimePicker`의 활용 방법을 익힐 수 있었습니다.
- `ListAdapter`를 활용하여 리사이클러뷰의 일부 데이터 변화 시 모든 아이템을 변경하는 것이 아닌, 특정 아이템만 변화가 일어나게 설정하는 방법을 익혔습니다.
- `ItemTouchHelper`를 활용하여 리사이클러뷰에 애니메이션을 추가하는 방법을 익힐 수 있었습니다.

## Velog
- [RecyclerView swipe to show button 구현 중 UI 초기화가 되지 않는 현상](https://velog.io/@hoyaho/RecyclerView-swipe-to-show-button-%EA%B5%AC%ED%98%84-%EC%A4%91-UI%EA%B0%80-%EC%B4%88%EA%B8%B0%ED%99%94-%EB%90%98%EC%A7%80-%EC%95%8A%EB%8A%94-%ED%98%84%EC%83%81)
- [ListAdapter 를 사용 중 UI 업데이트가 되지 않는 현상](https://velog.io/@hoyaho/%EC%82%BD%EC%A7%88-%EB%85%B8%ED%8A%B8-DiffUtill-Error)
