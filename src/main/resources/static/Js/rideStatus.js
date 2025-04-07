let riderPickedUpStatus = localStorage.getItem("riderPickedUpStatus");
let customerPickedUpStatus = localStorage.getItem("CuspickedStatus");
async function rideSetStatus(status) {
  try {
    // window.setPickedUp(true);
    if (riderPickedUpStatus == "true" && customerPickedUpStatus == "true") {
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
    }
  } catch (error) {
    console.error("The error encountered is " + error);
  }
}
