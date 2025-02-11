//   JavaScript for Form to collect data and send to the backend
document
  .getElementById("rideRequestForm")
  .addEventListener("submit", async function (e) {
    e.preventDefault();
    alert("Sent!!!");
    const formData = new FormData(this);
    const formObject = {
      pickup: formData.get("pickupLoc"),
      dropOff: formData.get("dropOff"),
      bikeType: formData.get("bikeType"),
      // TODO: uncomment it when project is almost done
      // fare: document.getElementById("fareAmount").innerText,
    };
    try {
      const response = await fetch("/ride/request", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(formObject),
      });
      if (response.ok) {
        const res = await response.json();
        console.log("Ride requested successfully:", res);
        document.getElementById("pendingMessage").classList.remove("hidden");

        //------------------ start listening for rider acceptance----------
        listenForRiderAcceptance(res.bookingId);
      } else {
        console.error("Error in response:", response);
      }
    } catch (error) {
      console.error("Request failed:", error);
    }
  });
function disableForm(disable) {
  if (disable) {
    document.getElementById("formContainer").style.display = "none";
  }
}
function listenForRiderAcceptance(bookingId) {
  const socket = new SockJS("/ws");
  const stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log("Connected to WebSocket: " + frame);
    stompClient.subscribe(
      `/all/riderAccepted/${bookingId}`,
      function (message) {
        const data = JSON.parse(message.body);
        console.log("Rider accepted the booking:", data);
        if (data != null) {
          disableForm(true);
          listenForRiderLocation(bookingId);
        }
        // Update UI with rider info
        document.getElementById("pendingMessage").classList.add("hidden");
        //TODO: displayRiderInfo(data);
      }
    );
  });
}
let picked = false;
function listenForRiderLocation(bookingId) {
    console.log("Listening for the rider's location...");
    
  const socket = new SockJS("/ws");
  const stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log("Connected to Websocket " + frame);
    stompClient.subscribe("/all/location/" + bookingId, (data) => {
      const location = JSON.parse(data.body);
      console.log("Rider's location: " + location);
      //           update Rider Marker on the map in map.js
      window.updateRiderMarker(location.latitude, location.longitude);
      // check in the /Js/customer/map.js
      if(!picked){
        window.checkProximity(); // Start checking proximity when location updates
      }
    });
  });
}
function bookingSetPickedUp(value) {
    picked = value;
    console.log('====================================');
    console.log(`The picked up value is ${picked}`);
    console.log('====================================');
}
