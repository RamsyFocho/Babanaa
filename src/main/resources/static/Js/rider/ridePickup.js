function showRiderPickupConfirmation() {
  console.log('====================================');
  console.log("Displaying the message");
  console.log('====================================');
  if (confirm("Has the customer been picked?")) {
    // Mark the ride as picked up
    alert("Ride marked as picked up!");
    // TODO: Send confirmation to the server
  } else {
    alert("Please wait for the rider.");
  }
}