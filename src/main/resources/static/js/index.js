const form = document.querySelector("#form");
const rb1 = document.querySelector("#radioButton1");
const rb2 = document.querySelector("#radioButton2");

const handleUserTypeSelection = () => {
  if (form.userType.value === "PROVIDER") {
    form.providerType.hidden = false;
    form.feeType.hidden = false;
    form.price.hidden = false;
  } else {
    form.providerType.hidden = true;
    form.feeType.hidden = true;
    form.price.hidden = true;
  }
};

rb1.addEventListener("click", handleUserTypeSelection);
rb2.addEventListener("click", handleUserTypeSelection);


