// 이미지 모달 요소 찾기
const imgModal = document.querySelector("#imgModal");

// 모달창이 뜨면 data-file 값 가져오기 : 원본 이미지 경로
if (imgModal) {
  // 모달창을 제어하는 방식
  imgModal.addEventListener("show.bs.modal", (e) => {
    // 모달 창 제어
    // 모달창과 연결된 요소 가져오기
    const thumLi = e.relatedTarget;

    // li 에 data- 에 담은 파일 url 가져오기
    const file = thumLi.getAttribute("data-file");

    // 모달 창 요소 찾아오기
    const modalTitle = imgModal.querySelector(".modal-title");
    const modalBodyInput = imgModal.querySelector(".modal-body");

    // 모달에 보여줄 내용 삽입
    modalTitle.textContent = `${title}`;
    modalBodyInput.innerHTML = `<img src="/upload/display?fileName=${file}&size=1">`;
  });
}
