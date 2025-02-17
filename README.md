<a href="https://play.google.com/store/apps/details?id=com.jinproject.twomillustratedbook">
	<img src="https://img.shields.io/badge/PlayStore-v2.4.0-4285F4?style=for-the-badge&logo=googleplay&logoColor=white&link=https://play.google.com/store/apps/details?id=com.jinproject.twomillustratedbook" />
</a>

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.10-blue.svg)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/gradle-8.5.0-green.svg)](https://gradle.org/)
[![minSdkVersion](https://img.shields.io/badge/minSdkVersion-26-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![targetSdkVersion](https://img.shields.io/badge/targetSdkVersion-34-orange)](https://developer.android.com/distribute/best-practices/develop/target-sdk)

자세한 설명은 [깃블로그](https://jowunnal.github.io/categories/#projects "블로그 링크") 에 있습니다.

# 앱 소개

글로벌 모바일 게임 ‘아이모’ 의 팬메이드 앱으로써 아이모를 즐기는 유저들이 게임을 할때 필요한 정보들을 보여주고, 몬스터가 재생성(Regeneration) 될 때를 간편하게 알람 설정하는 등 게임을 하는데 유용한 기능들을 제공해주는 앱 입니다.

지역화(필리핀어, 미국어, 한국어) 되어 글로벌로 출시되었으며, 배너 및 리워드광고와 인앱 업데이트, 인앱결제가 포함된 어플입니다.

# Why?

모바일 게임 아이모는 MMORPG 이며, 필드에서 PK 가 가능한 게임 입니다. 게임의 특성상 모든 필드의 보스 몬스터들은 길드 단위로 경쟁하고 있습니다. 여러 유저가 모여 만들어진 길드에서는 과거 “메신저봇R” 을 이용하여 몬스터 재 생성 시간을 자동으로 계산하고 유저들이 모두 볼 수 있도록 채팅방 “공지” 로 설정하고 있었습니다. 하지만 “나는 원숭이다” 사건 이후 카카오에서 카톡봇에 대한 제재 수위를 높이고 업데이트로 원활한 “메신저봇R” 의 이용이 불가 해지면서 대안이 필요해졌고, 몬스터 알람 뿐만 아니라 게임 유저들 에게 더 나은 가치를 제공해 줄 수 있는 안드로이드 앱 개발을 시작하게 되었습니다.

# 주요 기능

- **몬스터 도감** : 사용자는 사냥터를 기준으로 해당 지역의 출몰 몬스터를 상세 조회할 수 있다.
- **아이템 도감** : 사용자는 카테고리(무기, 방어구, 잡템, 코스튬) 기준으로 분류된 아이템을 상세 조회 및 필터링 할 수 있다.
- **몬스터 알람** : 아이모 내 보스 몬스터들의 재생성 시간을 계산하지 않고 편리하게 알람을 생성 할 수 있다.
- **현재시간 항상 보기 기능** : 사용자는 "다른앱 위에 그리기" 로 현재시간과 등록된 몬스터 알람 시간을 선택적으로 함께 볼 수 있다.
- **길드 마크 변환 기능** : 앱 외부 저장소의 이미지 파일을 가져와 갤러리 형태로 노출하고, 사용자가 임의로 선택하여 해당 이미지를 12*12 픽셀로 변환할 수 있다.
- **강화 시뮬레이터** : 사용자는 랜덤 수치의 스텟을 가진 무기, 방어구 아이템을 획득할 수 있고, Drag-And-Drop 으로 강화할 수 있다.
- **AI 길드 마크 이미지 생성 기능** : Dalle3 API 를 활용하여, 로그인 한 유저가 최적화된 프롬프트로 길드 마크로 변환할 이미지를 생성할 수 있다.
- **회원 정보 기능** : 인앱결제가 필요한 AI 길드 마크 이미지 생성 과 길드 마크 변환 기능을 서버로 저장하기 위해 Firebase-Storage 를 이용하고, 유저를 식별하기 위해 Firebase-Authentication 을 이용하여 로그인 및 회원관리를 제공한다.

# Stacks

| Category | Skill Set |
| ----- | ----- |
| Language | Kotlin |
| UI toolkit | Compose |
| Architecture | Clean Architecture |
| Design Pattern | MVVM |
| Android Component | Activity, Service, BroadcastReceiver |
| Jetpack | Lifecycle, Navigation(Compose), AlarmManager |
| Asynchronous | Kotlinx.Coroutines, Kotlinx.Coroutines.Flow |
| Dependency Injection | Hilt |
| Data | Room, DataStore(proto3) |
| Google | InAppPurchase, InAppUpdate, Admob |
| Firebase | Firebase-Analytics, Firebase-Storage, Firebase-RealtimeDatabase, Firebase-Crashlytics |
| Unit Test | Junit, Kotest, mockk |

# 개발 기간

**2022.01** ~ **현재진행중**

# Diagram

### Module Dependency

<img src="miscellaneoustool/documentation/module_diagram.PNG" />

### Database Logical Design

<img src="miscellaneoustool/documentation/db_logical_diagram.png" />

# As-Is / Challenge / To-Be

<details>
<summary>안드로이드 백그라운드 정책 과 배터리 정책 가이드라인에 따라 몬스터 알람 기능을 구현하고 단위테스트 작성</summary>
<div markdown="1">

### As-Is
- 몬스터 이름을 Spinner에서 선택한 뒤 "추가하기" 버튼을 누르면 “자주 사용하는 몬스터 리스트”에 등록된다.
  - 등록된 몬스터들은 버튼으로 노출되며, 클릭하면 죽은 시간을 입력할 수 있는 NumberPicker 가 BottomSheet 로 노출된다.
  - 죽은 시간을 입력한 뒤 "시작하기" 버튼을 누르면 몬스터의 재 생성 시간을 계산한 뒤 **알람을 생성**한다.
- 생성된 알람들은 현재 진행중인 알람 내역에서 조회할 수 있다.
- 몬스터 알람 간격은 NumberPicker 로 변경할 수 있다. 기본적으로 두개의 알람이 각각 5분, 10분 전 으로 생성된다.
- 사용자가 선택하여 현재시간과 함께 등록된 몬스터 알람의 재생성 시간을 Overlay 로 볼 수 있다.
- 알람은 지정된 시간에 Notification 을 생성하고, Notification 에서는 "알람 재생성" 버튼으로 현재 시간을 기준으로 몬스터 알람을 재 생성 할 수 있다.
- 알람을 생성하기 위한 비즈니스 로직에 대한 검증을 위해 단위테스트를 작성한다.

### Challenge
- 데이터
  - 몬스터 이름으로 Database 에 저장되어 있는 몬스터 정보의 재 생성 시간을 가져옵니다.
  - 생성된 모든 알람들은 Database 에 몬스터 이름을 Primary Key 로 저장합니다. 이미 생성된 몬스터 알람이 있다면, update 하게 됩니다.
- 알람 생성
  - 몬스터의 재 생성 시간은 수분 ~ 수일 까지 걸릴 수 있고, 앱이 **백그라운드**에 있거나 디바이스가 **도즈모드**에 진입해도 **정시** 에 울려야 합니다.
  - 따라서 **AlarmManager#setAlarmClock** 으로 알람을 생성하고 **BroadcastReceiver** 에서 수신하여 **Notification** 을 생성합니다.
    - **BroadcastReceiver#onReceive(Intent)** 는 코드를 모두 빠르게 수행시키고 프로세스가 종료되기 때문에 비동기 작업을 이 안에서 처리하지 않고, intent 에 필요한 모든 정보를 담아와서 처리합니다.
- 알람 생성 로직
  - 입력 받은 몬스터 이름으로, Database 에서 몬스터 정보를 가져와 재 생성 시간을 현재 시간을 기준으로 계산하여 AlarmManager#setAlarmClock 으로 알람을 생성합니다.
  - 몬스터 이름으로 "등록되어 있는 알람 리스트" 에 있다면, update API 를 호출하고 없다면, insert API 를 호출합니다.
  - 알람이 생성 완료되었다면 Dialog 를 띄워주고, "등록되어 있는 알람 리스트" 목록을 갱신합니다.
- 알람 재 생성
  - **NotificationCompat.Builder#addAction** 으로 "알람 재생성" 버튼을 생성하고, **PendingIntent#getService** 으로 서비스를 실행하여 알람을 재 생성합니다.
    - Notification은 NotificationService 에서 intent 를 실행하기 때문에 PendingIntent 가 필수적이며, Scheduling API 를 사용할 수 없습니다.
    - 또한, 알람 재생성 버튼 클릭시 백그라운드에서 알람이 생성되기 때문에 getService 또는 getBroadcast 로 처리해야 합니다.
    - BroadcastReceiver 에서 처리하기에 알람 생성 및 알람 리스트 DB 쿼리 로직의 소요 시간이 길기 때문에 특성상 적합하지 않아 service 로 처리하도록 판단하였습니다.
    - 또한, 알람 재생성 버튼을 누른다는 것은 앱이 도즈모드에 있지 않다는 것이므로 foregroundService 에서 실행하지 않아도 되기 때문에 **PendingIntent#getService** 로 서비스 호출후 내부에서 알람 재생성을 실행하였습니다.
- 단위테스트 작성
  - 알람을 생성하기 위해 몬스터 이름으로 몬스터 정보를 가져오고, 정보의 재 생성 시간을 이용하여 현재 시간으로 부터 몬스터의 다음 생성 시간을 계산하는 과정의 단위테스트를 kotlin 언어 기반 라이브러리인 **kotest** 와 **mockk**를 이용하여 작성하였습니다.

### To-Be
- 사용자들은 더 이상의 몬스터 재 생성 시간 계산의 실수를 방지하고, 알람 생성으로 정확한 시간에 잊지 않고 몬스터를 사냥할 수 있게 되었습니다.
- 몬스터 재 생성 시간을 계산하는 담당자의 계산 실수로 인한 길드 내의 유저들간에 불화를 방지할 수 있었습니다.

</div>
</details>

<details>
<summary>데이터들을 개념 및 논리 설계하여 데이터베이스 구축 후 Jetpack Room 이용, DB Migration 대응 및 테스트 작성</summary>
<div markdown="1">

### As-Is
- 몬스터 도감, 아이템 도감, 몬스터 알람, 강화 시뮬레이터 기능에 사용할 데이터들이 필요하다.

### Challenge
- 데이터 저장은 Local **Database** 선택
  - 해당 앱은 사용자 디바이스(Local) 기반의 앱 이므로 서버로 부터 데이터를 요청하지 않습니다.
  - **DataStore** 를 사용하기에는 많은 양의 데이터를 복잡한 데이터 구조로 관리해야 합니다.
  - 따라서, 디바이스에 **Database** 를 이용하여 데이터를 저장하고 관리하는 방법을 선택했습니다.
- Database 를 위한 **Jetpack Room** 선택
  - 안드로이드의 DBMS 는 **SQLite** 를 이용합니다.
  - **SQLite** 를 직접 이용하기에는 상용구 들의 직접 작성으로 **보일러 플레이트** 가 발생하고, Migration 과 같은 **DB 관리에 어려움**이 발생했습니다.
  - **어노테이션**으로 보일러 플레이트를 줄이고, **Kotlinx.Coroutines 를 지원**하여 DB 관리에 다양한 API 를 제공해주는 **JetPack Room** 을 채택했습니다.
  - 몬스터, 아이템, 도감에 대해 개념적 설계로 **E-R 다이어그램**을 산출하고, 논리적 설계로 테이블 관계도를 구성했습니다.
- Room DB Migration
  - **Jetpack Room** 은 **DB Migration** 에 대해 **수동이전**과 **자동이전**을 지원하고 있습니다.
  - 리펙토링 과정에 DB에 **중대한 변경점들이 크게 발생**하여 **수동이전** 코드를 작성하고, **crash 를 방지하기 위해** Migration 에 대한 테스트코드를 작성하였습니다. 

### To-Be
- 앱내에서 Local 데이터를 저장하는 방법인 **SharedPerferences**, **DataStore** 와 **Room** 에 대해 학습, 상황에 따라 적절히 선택할 수 있습니다.

</div>
</details>

<details>
<summary>Localization 로 다국어(미국,필리핀,한국)를 지원</summary>
<div markdown="1">

### As-Is
- 모바일 게임 아이모는 글로벌 서버가 있고, 주요 국가인 미국, 필리핀 에 대한 서비스를 지원해야 한다.

### Challenge
- UI string 요소들의 지역화는 **resource 의 string.xml** 을 이용하여 지역화 하였습니다.
- 데이터베이스로 부터 가져온 데이터들의 지역화는 string.xml 로 지역화 할 수 없는 문제가 발생했습니다.
- 이를 Database 를 **언어에 따라 생성한 뒤**, Data layer 에서 ApplicationContext 를 주입받아 **앱의 Locale 로 분기**하여 가져오도록 지역화 하였습니다.

### To-Be
- 앱의 이용자들중 10% 만큼의 글로벌 유저들을 추가적으로 유치할 수 있었습니다.

</div>
</details>

<details>
<summary>사용자가 디바이스 내의 선택한 이미지를 12*12 픽셀로 변환 하는 기능 구현</summary>
<div markdown="1">

### As-Is
- 앱 외부의 디바이스 저장 공간에 있는 이미지들을 커스텀 갤러리 화면에 노출한다.
    - 갤러리의 모든 이미지들은 확대하기 버튼이 함께 노출되며, 클릭시 상세 이미지 화면으로 전환된다.
    - 100개 단위의 이미지를 페이징 처리하여 **무한 스크롤**로 가져온다.
    - 갤러리에는 **스크롤 바** 와 "최상단으로 이동하기" 버튼이 있다.
- 사용자는 임의의 이미지를 클릭하면, "길드 마크 심볼 화면"으로 이동한다.
- "길드 마크 심볼 화면"에서 이미지의 원본과 함께 12*12 픽셀로 변환된 작은 크기의 미리보기를 제공한다.
- 사용자는 미리보기 이미지를 확인하고 변환을 원한다면, 변환하기 버튼을 누른다.
- 변환을 위해서는 인앱 결제가 실행되며, 결제가 완료 되면 다음 4가지가 노출된다.
    - 이미지의 변환된 12*12 픽셀
        - 12*12 픽셀들은 인게임에서 함께 보기 위해 Overlay 로 노출된다.
    - 사용된 색상들을 “색상 팔레트”로 노출
        - 색상 팔레트에 있는 색상을 클릭하면, 해당 색상이 사용된 픽셀만 표시된다.
    - 색상들을 공통화 하기 위한 “색상 정밀도” 의 입력 SeekBar
        - 색상 정밀도에 따라 색상 팔레트의 색상이 공통화 되어 노출된다.
    - 12*12 픽셀로 변환된 작은 형태의 미리보기

### Challenge
- 커스텀 갤러리
  - why?
    - 안드로이드 13 버전 부터 **Photo Picker** 가 등장했고, 안드로이드 14 버전 부터 저장 장소로 부터 이미지를 가져오는 권한에 대한 제한이 강화되면서 **READ_MEDIA_VISUAL_USER_SELECTED** 권한이 추가되고 특별한 사용사례가 아니면 **Photo Picker** 를 사용하는 것이 강제 되었습니다.
    - 해당 앱의 "길드 마크 심볼 생성" 기능은 앱의 핵심 기능이고, 사용자의 접근이 빈번하게 이루어질 수 있으며, 이미지에 대한 변환을 수행하는 기능이 제공되기 때문에 해당 권한을 앱에서 사용할 수 있도록 승인 되었습니다.
    - 또한 Dynamic 한 UI Component 를 개발하고자 하는 목적이 있었으므로 **Photo Picker** 대신 **커스텀 갤러리**를 구현하는 방법을 채택하였습니다.
  - How?
    - **READ_MEDIA_IMAGES** 와 안드로이드 14 버전 이상 이라면 추가로 **READ_MEDIA_VISUAL_USER_SELECTED** 에 대한 권한을 요청 합니다.
    - 승인된 권한에 맞게 앱 외부의 저장소로 부터 **ContentResolver** 로 가장 최근에 수정된 이미지 순서대로 100개를 가져옵니다.
    - 갤러리 LazyList 의 한 아이템 view size 에 이미지 개수를 곱하여 **스크롤 바의 위치**를 계산하여 표시합니다.
    - "최상단으로 이동하기" 버튼을 클릭시 **LazyListState#animateScrollToItem** 으로 이동합니다.
    - 버튼과 스크롤바는 **코루틴을 활용한 타이머**로 3초간 스크롤이 발생하지 않으면 사라지도록 하였습니다.
- 결제
  - 인앱 결제는 **gms** 의 **InAppPurchase** 를 이용하여 구현하였습니다.
- 12*12 픽셀의 이미지 변환
  - 사용자가 선택한 이미지의 contentURI 로 **ImageDecoder#decodeBitmap** 을 이용하여 비트맵 객체를 생성합니다.
  - 해당 비트맵을 **Bitmap#createScaledBitmap** 을 이용하여 12*12 픽셀 로 변환합니다.
  - 변환된 비트맵을 **Bitmap#getPixels** 을 이용하여 색상 배열을 추출하고, 색상 정밀도 범위 내에서 비슷한 **색상들을 공통화** 하여 노출합니다.
    - 비슷한 색상들을 공통화 하는 이유는 UX를 고려하여 육안으로 구분하기 힘들 정도의 비슷한 색상들이 "색상 팔레트" 에 나뉘어져 존재하는 문제 때문입니다.
    - **색상 공통화 알고리즘**
      - "색상 팔레트" 로 담을 리스트를 생성합니다.
      - 12*12 크기의 변환된 색상 배열에 대해 완전 탐색합니다.
        - 해당 색상과 "색상 팔레트" 리스트의 색상들과의 rgb 값 차이가 "색상 정밀도" 수치 범위 내에 있다면 리스트에 추가하지 않고 반복을 종료합니다. 색상 리스트의 끝까지 없다면, 리스트에 추가합니다.
      - 마지막으로 "색상 팔레트" 리스트를 rgb 값 순서대로 정렬하여 반환합니다.


### To-Be
- **Bitmap(래스터)** 과 **Vector(백터)** 이미지 파일 포맷의 종류인 **png, jpg, webp, svg** 에 대한 장단점 및 차이를 알고 적용할 수 있었습니다.
  - 기존의 png 이미지를 구글에서 개발한 webp 이미지로 변환하여 앱의 크기를 경량화 할 수 있었습니다. (R8 활성화와 함께 앱크기 기존 65mb -> 20mb으로 약 70% 개선)
- **android.graphics.Bitmap** 의 다양한 API 들을 활용하면서 **이미지 처리에 대한 이해**를 넓힐 수 있었습니다.

</div>
</details>
 
# UI

### 몬스터 도감

https://github.com/user-attachments/assets/4d696520-1b30-4461-8b9d-33bf462cd38a

### 아이템 도감

https://github.com/user-attachments/assets/a504ef63-807f-44cf-89de-c569d927b852

### 길드마크 심볼

https://github.com/user-attachments/assets/812ede40-702f-4d9f-b6ce-15047ca50a22

### 몬스터 알람

https://github.com/user-attachments/assets/7c8a1346-ee18-46ac-a06f-be0dc61d3ca4
