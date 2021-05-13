# (사전과제) 카카오페이 신용/부동산 투자 API

## 요구사항
### 일반
- [x] 요청한 사용자 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header 로 전달된다.

### 전체 투자상품 조회
- [ ] 투자 상품은 다음 내용을 포함한다.
 - [ ] 상품 ID(product_id)
 - [ ] 상품 제목(title)
 - [ ] 총 모집금액(total_investing_amount)
 - [ ] 현재 모집금액(current_investing_amount)
 - [ ] 투자자 수(investor_count)
 - [ ] 투자 모집 상태(모집중, 모집 완료)(investing_status)
 - [ ] 상품 모집기간 (stated_at, finished_at)
- [ ] 상품 모집기간 (started_at, finished_at) 내의 상품만 응답한다.

### 투자하기
- [ ] 투자 모집 상태가 '모집중' 인 상품만 투자할 수 있다.
    - [ ] '모집 완료' 인 상품에 투자할 경우 sold-out 응답을 받는다.
- [ ] 요청 데이터로 사용자 식별값, 상품 ID, 투자 금액을 입력값으로 받는다.
- [ ] 현재 모집된 금액과 투자할 금액의 합이 총 투자모집 금액(total_investing_amount)을 넘어서면 invalid-amount 상태를 응답받고 투자되지 않는다.

### 나의 투자상품 조회 API
- [ ] 내가 투자한 모든 상품을 반환한다.
- [ ] 나의 투자상품 응답은 다음 내용을 포함한다.
  - [ ] 상품 ID, 상품 제목, 총 모집금액, 나의 투자금액, 투자일시
