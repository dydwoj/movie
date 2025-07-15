// enter 발생시 searchForm 가져오기
document.querySelector("[name='keyword']").addEventListener("keyup", (e) => {
  if (e.keyCode == 13) {
    // 폼 가져오기
    const searchForm = document.querySelector("#searchForm");
    searchForm.submit();
  }
});
