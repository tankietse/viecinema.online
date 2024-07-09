document.addEventListener("DOMContentLoaded", () => {
  const csrfHeader = document.querySelector(
    'meta[name="_csrf_header"]'
  ).content;
  const csrfToken = document.querySelector('meta[name="_csrf"]').content;
  const bookingSummary = {
    theaterName: document.getElementById("summary-theater-name"),
    date: document.getElementById("summary-date"),
    screenType: document.getElementById("summary-screen-type"),
    time: document.getElementById("summary-time"),
  };
  let selectedTimeButton = null;
  let screenPrice,
    rows,
    cols,
    selectedSeats = [];

  const bookNowButton = document.getElementById("book-now");
  const modal = document.getElementById("seat-modal");
  const overlay = document.getElementById("seat-overlay");
  const cancelButton = document.getElementById("cancel");
  const leftSeatsContainer = document.querySelector(".left-seats");
  const rightSeatsContainer = document.querySelector(".right-seats");
  const totalDisplay = document.getElementById("total");
  const selectedSeatsDisplay = document.getElementById("selected-seats");

  // Xử lý sự kiện cho các nút ngày
  const dateButtons = document.querySelectorAll(".date");
  if (dateButtons.length > 0) {
    const firstSelectedDateButton = document.querySelector(".date.selected");
    if (firstSelectedDateButton) {
      fetchScreeningsForDate(firstSelectedDateButton);
    }

    dateButtons.forEach((button) => {
      button.addEventListener("click", () => {
        dateButtons.forEach((btn) => btn.classList.remove("selected"));
        button.classList.add("selected");
        fetchScreeningsForDate(button);
      });
    });
  }

  // Xử lý cho các nút thời gian
  document.body.addEventListener("click", (event) => {
    const timeButton = event.target.closest(".times button");
    if (timeButton) {
      console.log("Time button clicked!", timeButton.dataset);
      updateBookingSummary(timeButton);

      screenPrice = parseInt(timeButton.dataset.screenPrice);
      rows = parseInt(timeButton.dataset.seatRow);
      cols = parseInt(timeButton.dataset.seatCol);

      selectedSeats = [];
      updateInfo();
    }
  });

  // Các hàm hỗ trợ
  function fetchScreeningsForDate(button) {
    const selectedDate = button.getAttribute("data-date");
    const showtimeContainer = button.closest("[data-showtime-id]");
    const showtimeId = showtimeContainer
      ? showtimeContainer.getAttribute("data-showtime-id")
      : null;
    const cinemasContainer = document.querySelector(".cinemas");

    if (showtimeId && cinemasContainer) {
      fetch(
        `/get-screenings-by-date?selectedDate=${encodeURIComponent(
          selectedDate
        )}&showtimeId=${encodeURIComponent(showtimeId)}`,
        {
          method: "POST",
          headers: {
            [csrfHeader]: csrfToken,
            "Content-Type": "application/json",
          },
        }
      )
        .then((response) => {
          if (!response.ok)
            throw new Error(
              "Network response was not ok " + response.statusText
            );
          return response.json();
        })
        .then((screeningsByTheater) => {
          renderScreenings(screeningsByTheater, cinemasContainer);
        })
        .catch((error) => {
          console.error("Error fetching screenings:", error);
          cinemasContainer.innerHTML =
            "<p>Có lỗi xảy ra khi tải thông tin suất chiếu. Vui lòng thử lại sau.</p>";
        });
    }
  }

  function renderScreenings(screeningsByTheater, container) {
    container.innerHTML = "";
    if (!screeningsByTheater || Object.keys(screeningsByTheater).length === 0) {
      container.innerHTML = "<p>Không có suất chiếu nào.</p>";
      return;
    }

    for (const [theaterName, screenings] of Object.entries(
      screeningsByTheater
    )) {
      const cinemaDiv = document.createElement("div");
      cinemaDiv.classList.add("cinema");
      const theaterAddress =
        screenings.length > 0 ? screenings[0].screenInfo.theaterAddress : "";
      cinemaDiv.innerHTML = `<h3>${theaterName}</h3><p>${theaterAddress}</p>`;

      const screenTypes = {};
      screenings.forEach((screening) => {
        const screenType = screening.screenInfo.screenType;
        if (!screenTypes[screenType]) screenTypes[screenType] = [];
        screenTypes[screenType].push(screening);
      });

      for (const [screenType, screenTypeScreenings] of Object.entries(
        screenTypes
      )) {
        const screenTypeDiv = document.createElement("div");
        screenTypeDiv.classList.add("screen-type");
        screenTypeDiv.innerHTML = `
            <h3 class="type">${screenType}</h3>
            <p class="price">${screenTypeScreenings[0].screenInfo.screenPrice.toLocaleString(
              "vi-VN",
              { style: "currency", currency: "VND" }
            )}</p>
          `;
        cinemaDiv.appendChild(screenTypeDiv);

        const timesDiv = document.createElement("div");
        timesDiv.classList.add("times");

        screenTypeScreenings.forEach((screening) => {
          const timeButton = document.createElement("button");
          timeButton.textContent = formatTime(screening.startTime);
          Object.assign(timeButton.dataset, {
            theaterName,
            theaterAddress,
            screenType,
            screenName: screening.screenInfo.screenName,
            startTime: formatTime(screening.startTime),
            screenPrice: screening.screenInfo.screenPrice,
            seatRow: screening.screenInfo.seatRow,
            seatCol: screening.screenInfo.seatCol,
          });
          timesDiv.appendChild(timeButton);
        });

        cinemaDiv.appendChild(timesDiv);
      }

      container.appendChild(cinemaDiv);
    }
  }

  function formatTime(time) {
    if (Array.isArray(time) && time.length === 2) {
      return `${time[0].toString().padStart(2, "0")}:${time[1]
        .toString()
        .padStart(2, "0")}`;
    } else if (typeof time === "string") {
      return time;
    } else if (time instanceof Date) {
      return time.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
    } else if (
      time &&
      typeof time === "object" &&
      time.hour !== undefined &&
      time.minute !== undefined
    ) {
      return `${time.hour.toString().padStart(2, "0")}:${time.minute
        .toString()
        .padStart(2, "0")}`;
    } else {
      return "Thời gian không hợp lệ";
    }
  }

  function updateBookingSummary(button) {
    console.log("Updating Booking Summary");
    if (selectedTimeButton) {
      selectedTimeButton.classList.remove("selected");
    }
    button.classList.add("selected");
    selectedTimeButton = button;

    const theaterNameElement = document.getElementById("summary-theater-name");
    const dateElement = document.getElementById("summary-date");
    const screenTypeElement = document.getElementById("summary-screen-type");
    const timeElement = document.getElementById("summary-time");

    if (theaterNameElement)
      theaterNameElement.textContent = button.dataset.theaterName;
    if (dateElement)
      dateElement.textContent = new Date().toLocaleDateString("vi-VN", {
        weekday: "long",
        year: "numeric",
        month: "numeric",
        day: "numeric",
      });
    if (screenTypeElement)
      screenTypeElement.textContent = button.dataset.screenType;
    if (timeElement)
      timeElement.textContent = button.dataset.startTime.replace(",", ":");

    console.log("Booking Summary Updated");
  }
  function generateSeats(container, side) {
    while (container.firstChild) {
      container.removeChild(container.firstChild);
    }
    container.style.display = 'grid';
    const totalCols = Math.floor(cols / 2); // Assuming left and right sections are equal in columns
    container.style.gridTemplateColumns = `repeat(${totalCols}, 1fr)`;

    for (let row = 0; row < rows; row++) {
      for (let col = 0; col < totalCols; col++) {
        const seat = document.createElement('div');
        seat.classList.add('seat');
        seat.textContent = String.fromCharCode(65 + row) + ((side === 'left' ? col + 1 : col + totalCols + 1));

        seat.addEventListener('click', () => {
          if (seat.classList.contains('sold')) return;

          seat.classList.toggle('selected');
          const seatID = seat.textContent;

          if (seat.classList.contains('selected')) {
            selectedSeats.push(seatID);
          } else {
            selectedSeats = selectedSeats.filter(s => s !== seatID);
          }

          updateInfo();
        });

        container.appendChild(seat);
      }
    }
  }

  function updateInfo() {
    const total = selectedSeats.length * screenPrice;
    totalDisplay.textContent = `${total.toLocaleString()} vnd`;
    selectedSeatsDisplay.textContent = selectedSeats.join(", ");
  }

  // Event listeners
    bookNowButton.addEventListener("click", () => {
      if (!selectedTimeButton) {
        alert("Vui lòng chọn thời gian chiếu");
        return;
      }

      generateSeats(leftSeatsContainer, "left");
      generateSeats(rightSeatsContainer, "right");

      modal.style.display = "block";
      overlay.style.display = "block";
    });


  cancelButton.addEventListener("click", () => {
    modal.style.display = "none";
    overlay.style.display = "none";
  });

  // Debugging
  console.log("Cinemas container:", document.getElementById("cinemas-0"));
  console.log("Time buttons:", document.querySelectorAll(".times button"));
});

// MutationObserver để theo dõi các thay đổi DOM
const observer = new MutationObserver((mutations) => {
  mutations.forEach((mutation) => {
    if (mutation.type === "childList") {
      mutation.addedNodes.forEach((node) => {
        if (node.nodeType === Node.ELEMENT_NODE) {
          const timeButtons = node.querySelectorAll(".times button");
          timeButtons.forEach((button) => {
            console.log("New time button added:", button);
          });
        }
      });
    }
  });
});

observer.observe(document.body, { childList: true, subtree: true });
