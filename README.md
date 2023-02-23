# 소개

아이모 도감&드랍아이템 및 알람타이머 앱 입니다.

도감&드랍아이템을 각각 분류별로(아이템단위,맵단위) recyclerView로 보여줍니다.

각 아이템 혹은 도감정보를 상단 돋보기버튼을 누르고 검색 하여 필터링된 결과를 볼수있습니다.

지정된 몬스터들에 대해서 선택하여 타이머를 등록&수정&삭제 할수 있습니다.

타이머가 설정되면, 선택된 타이머들에 대해 선택하여 Overlay로 보여줄수 있습니다.

Overlay 에서는 현재시간을 매초마다 갱신하여 볼수있고, 하단에 등록한 몬스터들의 리젠타임을
함께 확인할 수 있습니다.

# 사용된 스택

 - Compose
 - Kotlin.Coroutines.Flow
 - Databinding
 - Navigation ( Single Activity )
 - Room
 - DataStore
 - AlarmManager
 - Hilt

# Architecture ( MVVM )

 - Data
  - Room
  - DataStore
  - Serializer
  - Repository Interface

 - Domain
  - Repository Implementation
  - Model

 - Presenter
  - Activity & Fragment
   - BindingAdapter
   - State
  - Broadcast Receiver
  - Service
  - Compose Component
   - State

 - Utils

# 스택 변화

 - 명령형 -> Compose(선언형)
 - LiveData -> Flow
 - SharedPreferences -> DataStore ( proto )
 
# UI

![KakaoTalk_20230223_015847465_09](https://user-images.githubusercontent.com/75519689/220841698-0dd9ebb2-766c-4311-9935-b01f1cd3d532.jpg)
![KakaoTalk_20230223_015847465_08](https://user-images.githubusercontent.com/75519689/220842377-8ba5f4e9-efe0-4f0d-b980-ffa84c1b3bdb.jpg)
![KakaoTalk_20230223_015847465_03](https://user-images.githubusercontent.com/75519689/220842383-72f124dc-cdb3-4fa8-9104-3a732f66ae3d.jpg)
![KakaoTalk_20230223_015847465_07](https://user-images.githubusercontent.com/75519689/220842374-c465f719-434e-4b23-989a-92b8a225f9e7.jpg)
![KakaoTalk_20230223_015847465_02](https://user-images.githubusercontent.com/75519689/220842379-e4783def-6deb-441a-869c-f9ecb3cf9058.jpg)
![KakaoTalk_20230223_015847465_06](https://user-images.githubusercontent.com/75519689/220842371-0831c0c3-44ee-4c77-b21a-d6dc9b82fdec.jpg)
![KakaoTalk_20230223_015847465_05](https://user-images.githubusercontent.com/75519689/220842369-cd54f511-182a-44f9-8342-92747a57d316.jpg)
![KakaoTalk_20230223_015847465_04](https://user-images.githubusercontent.com/75519689/220842364-af3a9a00-c474-4f9a-a630-d8af683ea0cc.jpg)

# 패치버전

### 1.0~1.2
- 앱에 광고 unitID를 test값으로 등록을했다가 수정후 재등록
- 알람을 정확한시간동작 에서 5분전 동작으로 변경후 재등록
### 1.3
- 도감내 코스튬 이 추가
- 기존 도감버튼클릭시 바로 전체도감 출력 에서 도감분류fragment 추가후 분류View에서 잡탬류,무기류,코스튬류 를 선택하여 볼수있도록 분류View 추가
- 알람부분 UI구성을 좀더 보기좋게 정렬함
### 1.4
- 알람부분에서 보스타입 spinner 선택후 타입에따른 몬스터들을 나타내는 spinner 에서 보스타입이 named는 정상동작하지만, boss와 bigboss선택시 named 보스를 알람설정됨
- 데이터베이스내용에 일부 미지원하는 몬스터들에대해 타입을 변경해서 spinner에 나오지않도록 변경함

### 1.5.0~1.5.1
- 드랍몹(대형보스급)빅마마,우크파나,바슬라프,일루스트,마녀딜린 추가
- 알람화면 UI구성 추가 (화면중앙에 젠타임표기&제거&전체ON/OFF)
- 날짜단위 추가 및 24시 넘어갓던버그해결
- 현재진행중인 알람내역 요일-시-분-초 순으로 정렬기능추가
- 특정기기의 UI 가로넓이 짤리는부분 개선

### 1.5.2
- 알람이 5분전 단일알람에서 5분전,0분전 두가지로 변경되었습니다.
- 화면상단의 등록된 몬스터에대한 타이머출력이 일-시-분-초 순으로 정렬됩니다.
- 화면상단에 현재시간항상출력이 추가되었습니다. 개별적으로 on/off할수없습니다.
- 도감부분의 UI가 개선되었습니다. 아이탬개수가 1개일때는 더이상표기되지 않습니다.
- 알람부분의 버튼이름들이 더 직관적으로 변경되었습니다.

### 1.5.3
- 화면위에 타이머출력부분이 게임내UI와 겹쳐져 잘보이지 않던 이슈가 개선되었습니다.
- on/off 버튼을통해 등록된타이머가 없어도 현재시간을 출력할수 있습니다.
- 타이머가 모두 제거되어도, 현재시간은 계속 출력되도록 변경되었습니다.

### 1.5.4
- 화면위에그리기 Overlay 활성화 중에 업데이트 발생시 ANR 발생하는 이슈 해결

### 1.6.2
- 화면 UI구성이 전체적으로 변경되었습니다.
- 서버기능이 개설되었습니다.
- 알람발생시 알람의 액션버튼을 통해 즉시 타이머등록이 가능해졋습니다.

### 1.7.2
 - 전체적인 UI 구성이 변경되었습니다.
 - 서버기능이 삭제되었습니다.
 - 신규몬스터, 아이템도감이 대응되었습니다.

본 앱은 구글 플레이스토어에 등록된 앱입니다.
