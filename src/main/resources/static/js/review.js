// ======================================================================
// 날짜 포멧 함수
const formatDate = (str) => {
  const date = new Date(str);

  return (
    date.getFullYear() +
    "/" +
    (date.getMonth() + 1) +
    "/" +
    date.getDate() +
    " " +
    date.getHours() +
    ":" +
    date.getMinutes()
  );
};

// 리뷰 영역 가져오기
const reviewDiv = document.querySelector(".reviewList");
const reviewForm = document.querySelector("#reviewForm");
const reviewCnt = document.querySelector(".review-cnt");

const reviewList = () => {
  // 리뷰 가져오기
  axios.get(`/reviews/${mno}/all`).then((res) => {
    console.log(res.data);

    const data = res.data;

    reviewCnt.innerHTML = data.length;

    let result = "";
    data.forEach((review) => {
      result += `<div class="d-flex justify-content-between py-2 border-bottom review-row" data-rno=${review.rno} data-email=${review.email}>`;
      result += `<div class="flex-grow-1 align-self-center">`;
      result += `<div><span class="font-semibold">${review.text}</span></div>`;
      result += `<div><span class="small text-muted"><span class="d-inline-block mr-3">${review.nickName}</span>`;
      result += `평점 : <span class="grade">${review.grade}</span><div class="starrr"></div></div>`;
      result += `<div class="text-muted"><span class="small">${formatDate(review.createdDate)}</span></div></div>`;
      result += `<div class="d-flex flex-column align-self-center">`;
      // 로그인 사용자 == 댓글작성자
      if (review.email == loginUser) {
        result += `<div class="mb-2"><button class="btn btn-outline-danger btn-sm">Delete</button></div>`;
        result += `<div><button class="btn btn-outline-success btn-sm">Modify</button></div>`;
      }
      result += `</div></div>`;
    });

    reviewDiv.innerHTML = result;
  });
};

// ======================================================================
// 삭제 and 수정
reviewDiv.addEventListener("click", (e) => {
  // 어느 버튼의 이벤트인가?
  const btn = e.target;
  // rno 가져오기
  const rno = btn.closest(".review-row").dataset.rno;
  console.log(rno);
  // 댓글 작성자 가져오기
  const email = btn.closest(".review-row").dataset.email;

  // 삭제 or 수정 ?
  // =========================
  if (btn.classList.contains("btn-outline-danger")) {
    // 삭제
    if (!confirm("Are you Sure, delete it?")) return;

    axios
      .delete(`/reviews/${mno}/${rno}`, {
        // data: { email: email },
        headers: {
          //   "Content-Type": "application/json",
          "X-CSRF-TOKEN": csrf,
        },
      })
      .then((res) => {
        console.log(res.data);
        // 삭제 성공시 댓글 리로딩
        reviewList();
      });

    // 수정
  } else if (btn.classList.contains("btn-outline-success")) {
    axios.get(`/reviews/${mno}/${rno}`).then((res) => {
      console.log(res.data);
      const data = res.data;

      // replyForm 안에 보여주기
      reviewForm.rno.value = data.rno;
      reviewForm.nickName.value = data.nickName;
      // mid = memberId
      reviewForm.mid.value = data.mid;
      reviewForm.querySelector(".starrr a:nth-child(" + data.grade + ")").click();
      reviewForm.text.value = data.text;
    });
  }
});

// ======================================================================
// 폼 submit => 수정 + 삽입
if (reviewForm) {
  reviewForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const form = e.target;
    const rno = form.rno.value;

    form.grade.value = grade;

    if (form.rno.value) {
      // 수정
      axios
        .put(`/reviews/${mno}/${rno}`, form, {
          headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrf,
          },
        })
        .then((res) => {
          console.log(res.data);
          alert("Your review is updated");

          // form 기존 내용 지우기
          reviewForm.rno.value = "";
          reviewForm.nickName.value = "";
          reviewForm.mid.value = "";
          reviewForm.text.value = "";
          reviewForm.querySelector(".starrr a:nth-child(" + grade + ")").click();
          // 수정 내용 반영
          reviewList();
        });
    } else {
      // 삽입
      axios
        .post(`/reviews/${mno}`, form, {
          headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrf,
          },
        })
        .then((res) => {
          console.log(res.data);
          alert("Your comment is created");

          // form 기존 내용 지우기
          reviewForm.rno.value = "";
          reviewForm.text.value = "";
          reviewForm.querySelector(".starrr a:nth-child(" + grade + ")").click();

          // 삽입 내용 반영
          reviewList();
        });
    }
  });
}

// ======================================================================
// 페이지 로드시 호출
reviewList();
