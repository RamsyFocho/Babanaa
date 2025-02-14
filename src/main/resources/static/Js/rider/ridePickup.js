// let accepted = localStorage.getItem("riderPickedUpStatus");
// console.log(`The value of accepted is ${accepted}`);
function showRiderPickupConfirmation() {
  console.log("====================================");
  console.log("Displaying the message");
  console.log("====================================");

  // Check if rider has already confirmed pickup
  const riderPickedUpStatus =
    localStorage.getItem("riderPickedUpStatus") === "true";
  if (riderPickedUpStatus) {
    return; // Exit if already picked up
  }
  if (confirm("Has the customer been picked?")) {
    localStorage.setItem("riderPickedUpStatus", "true");

    console.log(localStorage.getItem("riderPickedUpStatus"));
    clearInterval(proximityCheckInterval);
    alert("Thanks for the feedback. Drive safely!");
    window.rideSetStatus("pickedUp");
  } else {
    alert("Please wait for the rider.");
  }
}
