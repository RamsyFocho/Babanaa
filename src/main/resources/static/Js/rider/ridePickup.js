let status = "pickedUp";
async function showRiderPickupConfirmation() {
  console.log("====================================");
  console.log("Displaying the message");
  console.log("====================================");
  if (confirm("Has the customer been picked?")) {
    try {
      window.setPickedUp(true);
      const response = await fetch("/ride/status", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ status }),
      });
      if (response.ok) {
        const res = await response.json();
        if (res.status == "success") {
          // Mark the ride as picked up
          alert("Ride marked as picked up!");
        } else {
          alert("Failed to update the ride");
        }
      }
    } catch (error) {
      console.error("The error encountered is " + error);
    }
  } else {
    alert("Please wait for the rider.");
  }
}
