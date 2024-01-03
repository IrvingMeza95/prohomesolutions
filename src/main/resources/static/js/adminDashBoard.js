const formAdminDashboard = document.querySelector("#filtersAdminDashboard");
const rb1AdminDashboard = document.querySelector("#adminRadioButton1");
const rb2AdminDashboard = document.querySelector("#adminRadioButton2");
const rb3AdminDashboard = document.querySelector("#adminRadioButton3");

const handleAdminDashboardFilterSelection = () => {
  if (formAdminDashboard.filter.value === "name") {
    formAdminDashboard.name.hidden = false;
    formAdminDashboard.email.hidden = TRUE;
    formAdminDashboard.tel.hidden = TRUE;
  }
  if (formAdminDashboard.filter.value === "email") {
    formAdminDashboard.name.hidden = true;
    formAdminDashboard.email.hidden = false;
    formAdminDashboard.tel.hidden = true;
  }
  if (formAdminDashboard.filter.value === "tel") {
    formAdminDashboard.name.hidden = true;
    formAdminDashboard.email.hidden = true;
    formAdminDashboard.tel.hidden = false;
  }
};

rb1AdminDashboard.addEventListener(
  "click",
  handleAdminDashboardFilterSelection
);
rb2AdminDashboard.addEventListener(
  "click",
  handleAdminDashboardFilterSelection
);
rb3AdminDashboard.addEventListener(
  "click",
  handleAdminDashboardFilterSelection
);
