const fileInput = document.querySelector("[name='file']");

const showUploadImages = (arr) => {
  const output = document.querySelector(".uploadResult ul");

  console.log("------------- arr -------------");
  console.log(arr);

  let tags = "";
  arr.forEach((element, idx) => {
    // DTO 를 위한 이미지 정보 담기
    tags += `<li data-name="${element.fileName}" data-path="${element.folderPath}" data-uuid="${element.uuid}">`;
    tags += `<img src="/upload/display?fileName=${element.thumbnailURL}">`;
    tags += `<a class="xButton" href="${element.imageURL}"><i class="fa-regular fa-circle-xmark"></i></a>`;
    tags += "</li>";
  });
  output.insertAdjacentHTML("beforeend", tags);
};

fileInput.addEventListener("change", (e) => {
  // 버튼 클릭 시 uploadFiles 가져오기
  const inputFile = e.target;
  const files = inputFile.files;
  console.log(files);

  // form 생성 후 업로드 된 파일 append
  let form = new FormData();

  for (let i = 0; i < files.length; i++) {
    form.append("uploadFiles", files[i]);
  }
  axios
    .post(`/upload/files`, form, {
      headers: {
        "X-CSRF-TOKEN": csrf,
      },
    })
    .then((res) => {
      console.log(res.data);
      showUploadImages(res.data);
    });
});

// a 태그를 클릭하는 개념 X 클릭시 태그 중지
// a href 가져오기
document.querySelector(".uploadResult").addEventListener("click", (e) => {
  e.preventDefault();

  // 이벤트 대상
  console.log(e.target);
  console.log(e.currentTarget);

  // 이벤트 대상과 가까운 태그 가져오기
  const aTag = e.target.closest("a");
  const liTag = e.target.closest("li");

  // 속성 접근 : . or getAttribute("속성명")
  // img.src : img 태그의 src 가져오기
  console.log(aTag.getAttribute("href"));

  // href 값 가져오기
  const fileName = aTag.getAttribute("href");

  // 파일 지우기
  let form = new FormData();
  form.append("fileName", fileName);

  if (!confirm("Are you sure, delete it?")) {
    return;
  }

  axios
    .post(`/upload/removeFile`, form, {
      headers: {
        "X-CSRF-TOKEN": csrf,
      },
    })
    .then((res) => {
      console.log(res.data);

      if (res.data) {
        liTag.remove();
      }
    });
});
