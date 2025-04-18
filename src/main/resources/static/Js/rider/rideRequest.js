// Establish WebSocket connection
//    const notification=false;

//to check if a customer has been picked or not

function loadRequests(update) {
  localStorage.setItem("riderPickedUpStatus", "false");
  fetch("/ride/requests")
    .then((response) => response.json())
    .then((data) => {
      console.log("Initial ride requests:", data); // Log to check the response
      if (update) {
        showRideRequests(data, false, true);
      } else {
        showRideRequests(data, false, false);
      }
    })
    .catch((error) => console.error("Error fetching initial bookings:", error));
}
document.addEventListener("DOMContentLoaded", function () {
  loadRequests(false);

  setInterval(function () {
    loadRequests(false);
  }, 900000); //run the loadRequests(false) function every 15 minutes,
  //adjust the interval to 900,000 milliseconds (since 15 minutes = 15 * 60 * 1000 milliseconds).
  // -------------------------initialize map---------------------------------
  initializeMainMap();
  const socket = new SockJS("/ws"); // Match with WebSocket endpoint in Spring
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log("Connected to WebSocket: " + frame);

    // Subscribe to ride requests to get Incoming request
    stompClient.subscribe("/all/bookings", function (message) {
      const rideRequest = JSON.parse(message.body);
      console.log("Received rideRequest: ", rideRequest);
      // If the WebSocket message is not an array, wrap it in an array
      if (rideRequest == null || rideRequest.length == 0) {
        document.getElementById("message").innerHTML =
          "No new ride requests for now!!!";
      } else if (!Array.isArray(rideRequest)) {
        showRideRequests([rideRequest], true, false);
      } else {
        showRideRequests(rideRequest, true, false);
      }
    });
    // listen to if a rider accepts a requests
    stompClient.subscribe("/all/riderAccepted/updateList", function () {
      console.log("Trying to update the list of ride requests");
      loadRequests(true);
    });
  });
  startRealtimeLocationTracking();
  listenToAcceptedRide();
});
let driverMarker;

function startRealtimeLocationTracking() {
  console.log("checking the rider's real time location");

  if (!navigator.geolocation) {
    alert("Geolocation is not supported by your browser!");
    return;
  }

  // Get initial location
  navigator.geolocation.getCurrentPosition(
    (position) => {
      const { latitude, longitude } = position.coords;

      // Center map on rider's location
      mainMapInstance.setView([latitude, longitude], 18);

      // Add rider marker
      if (driverMarker) {
        mainMapInstance.removeLayer(driverMarker);
      }

      driverMarker = L.marker([latitude, longitude], {
        icon: driverIcon,
        draggable: false,
      })
        .addTo(mainMapInstance)
        .bindPopup("Your Location")
        .openPopup();
    },
    (error) => {
      console.error("Error getting location:", error);
      alert("Unable to retrieve your location.");
    },
    {
      enableHighAccuracy: true,
    }
  );

  // // Start continuous location tracking
  // locationUpdateInterval = navigator.geolocation.watchPosition(
  //   (position) => {
  //     const { latitude, longitude } = position.coords;

  //     // Update marker position
  //     if (driverMarker) {
  //       driverMarker.setLatLng([latitude, longitude]);
  //     }

  //     // Send location to server
  //     updateServerLocation(latitude, longitude);
  //   },
  //   (error) => {
  //     console.error("Location tracking error:", error);
  //   },
  //   {
  //     enableHighAccuracy: true,
  //     maximumAge: 10000,
  //     timeout: 5000,
  //   }
  // );
}
function listenToAcceptedRide() {
  const socket = new SockJS("/ws"); // Match with WebSocket endpoint in Spring
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log("Connected to WebSocket: " + frame);
    stompClient.subscribe("/all/riderAccepted/updateList", function () {
      console.log("Trying to update the list of ride requests");
      loadRequests(true);
    });
  });
}
// Show all ride request in the UI
function showRideRequests(rideRequest, newRequest, update) {
  console.log("displaying the results...");
  const rideRequestsContainer = document.getElementById("rideRequests");

  if (rideRequest.length === 0) {
    console.log("Empty ride request: No ride request available...");

    rideRequestsContainer.innerHTML = `
      <div id="messageContainer" class="text-center bg-yellow-400 border border-yellow-600 text-white font-bold px-4 py-3 rounded relative" role="alert">
        <span class="block sm:inline">No ride requests yet</span>
      </div>
    `;
    return;
  }

  // Ensure rideRequest is an array
  let requests = Array.isArray(rideRequest) ? rideRequest : [rideRequest];

  // Initialize the HTML string for new requests
  let rideRequestsHtml = "";

  requests.forEach(function (booking) {
    console.log(booking);

    // Styling for new requests
    const bgColorClass = newRequest
      ? "border-2 border-red-500 bg-gray-100 shadow-lg"
      : "bg-gray-100";
    const badgeHtml = newRequest
      ? `<p class=" bg-red-500 mb-2 w-4 text-white text-sm px-3 py-1 rounded-full shadow-lg">New</p>`
      : "";

    let requestHtml = `
      <div class="relative ${bgColorClass} p-4 rounded-lg shadow mb-4 transition-all duration-300 transform scale-95">
        ${badgeHtml}
        <p class="hidden" id="bookingId">${booking.bookingId}</p>
        <p><strong>Pickup:</strong> ${booking.pickupLocation}</p>
        <p><strong>Dropoff:</strong> ${booking.dropoffLocation}</p>
        <p><strong>Time:</strong> ${booking.bookingTime}</p>
        <p><strong>Fare:</strong> ${booking.fare} frs</p>
        <div class="flex flex-wrap justify-between mt-4">
          <button class="bg-green-500 text-white px-4 py-2 rounded font-bold hover:bg-green-600" onclick="acceptRide('${booking.pickupLocation}', '${booking.dropoffLocation}', '${booking.fare}', '${booking.bookingId}')">Accept</button>
          <button class="bg-blue-500 text-white px-4 py-2 rounded font-bold hover:bg-blue-600" onclick="viewRideDetails('${booking.pickupLocation}', '${booking.dropoffLocation}')">View</button>
          <button class="bg-red-500 text-white px-4 py-2 rounded font-bold hover:bg-red-600">Reject</button>
        </div>
      </div>
    `;

    // Add new requests to the **top**
    rideRequestsHtml = requestHtml + rideRequestsHtml;
  });

  // Prepend the new requests at the top
  const msgBox = document.getElementById("messageContainer");
  if (msgBox) {
    msgBox.classList.add("hidden");
  }
  rideRequestsContainer.innerHTML =
    rideRequestsHtml + rideRequestsContainer.innerHTML;

  // Apply animation effect
  setTimeout(() => {
    console.log("Applying the animation...");

    document.querySelectorAll(".transition-all").forEach((el) => {
      el.classList.remove("scale-95");
      el.classList.add("scale-100");
    });
  }, 100);
}

// Initialize OpenStreetMap

let markers = [];
let routingControl = null;
let mainMapInstance;
let map2Instance;
function initializeMainMap() {
  if (!mainMapInstance) {
    mainMapInstance = L.map("map").setView([51.505, -0.09], 18);
  }
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution:
      '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
  }).addTo(mainMapInstance);
}
//------------------view rides----------------------------------------
//---------------get the rider's actual location---------------

// Function to clear all markers
function clearMarkers() {
  markers.forEach((marker) => mainMapInstance.removeLayer(marker)); // Remove all markers
  markers = [];
  if (routingControl) {
    mainMapInstance.removeControl(routingControl); // Remove the routing control if it exists
    routingControl = null;
  }
}

// Function to get user's current location as he moves
function getCurrentLocation(callback) {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      callback,
      () => {
        //            navigator.geolocation.watchPosition(callback, () => {
        alert("Unable to retrieve your location.");
      },
      {
        enableHighAccuracy: true,
      }
    );
  } else {
    alert("Geolocation is not supported by this browser.");
  }
}
// Function to convert address to coordinates
function getCoordinates(address, callback) {
  fetch(
    `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
      address
    )}`
  )
    .then((response) => response.json())
    .then((data) => {
      if (data.length > 0) {
        const coords = [parseFloat(data[0].lat), parseFloat(data[0].lon)];
        callback(coords);
      } else {
        alert("No coordinates found for address: " + address);
      }
    })
    .catch((error) => console.error("Error fetching coordinates: ", error));
}
//    customized icons:
// Define custom icons using FontAwesome and Tailwind CSS
const driverIcon = L.divIcon({
  html: '<i class="fas fa-biking text-blue-500 text-2xl"></i>',
  className: "text-center", // Tailwind CSS class for additional styling if needed
  iconSize: [30, 30],
  iconAnchor: [15, 30],
  popupAnchor: [0, -30],
});
const userIcon = L.divIcon({
  html: '<i class="fas fa-user text-blue-500 text-2xl"></i>',
  className: "text-center", // Tailwind CSS class for additional styling if needed
  iconSize: [30, 30],
  iconAnchor: [15, 30],
  popupAnchor: [0, -30],
});
// Function to view ride details and plot on map
const mainMapView = document.getElementById("map");
function viewRideDetails(pickupAddress, dropoffAddress) {
  initializeMainMap();
  //        when clicked, it should scroll till the map
  mainMapView.scrollIntoView({ behaviour: "smooth" });

  getCurrentLocation(function (position) {
    const driverLocation = [
      position.coords.latitude,
      position.coords.longitude,
    ];

    clearMarkers(); // Clear previous markers

    // Add marker for driver's location
    const driverMarker = L.marker(driverLocation, {
      icon: driverIcon,
      draggable: false,
    })
      .addTo(mainMapInstance)
      .bindPopup("Your Location")
      .openPopup();
    markers.push(driverMarker);

    // Get coordinates for pickup location
    getCoordinates(pickupAddress, function (pickupCoords) {
      const pickupMarker = L.marker(pickupCoords, {
        icon: userIcon,
        draggable: false,
      })
        .addTo(mainMapInstance)
        .bindPopup("Pickup Location")
        .openPopup();
      markers.push(pickupMarker);

      // Get coordinates for dropoff location
      getCoordinates(dropoffAddress, function (dropoffCoords) {
        const dropoffMarker = L.marker(dropoffCoords, { draggable: false })
          .addTo(mainMapInstance)
          .bindPopup("Dropoff Location")
          .openPopup();
        markers.push(dropoffMarker);

        // Adjust mainMapInstance view to include all markers
        const bounds = L.latLngBounds([
          driverLocation,
          pickupCoords,
          dropoffCoords,
        ]);
        mainMapInstance.fitBounds(bounds);

        // Draw the route
        routingControl = L.Routing.control({
          waypoints: [
            L.latLng(driverLocation),
            L.latLng(pickupCoords),
            L.latLng(dropoffCoords),
          ],
          routeWhileDragging: true,
          createMarker: () => null, // Prevent adding default markers
        }).addTo(mainMapInstance);
      });
    });
  });
}
function displayMap2(pickupAddress, dropoffAddress) {
  alert("yo");
  const map = document.querySelector(".mapContainer");
  map.classList.toggle("scale");
  // Check if the class has been toggled
  if (map.classList.contains("scale")) {
    console.log("The scale class has been added.");
    if (mainMapInstance) {
      mainMapInstance.remove();
      mainMapInstance = null;
    }
    // ----------------initialize the 2nd map if it doesn't exist---------------------
    if (!map2Instance) {
      map2Instance = L.map("map2").setView([51.505, -0.09], 18);
    }
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(map2Instance);
    // Adjust map size after display
    setTimeout(() => {
      map2Instance.invalidateSize();
    }, 300);
    // Tile Layer (OpenStreetMap)
    getCurrentLocation(function (position) {
      const driverLocation = [
        position.coords.latitude,
        position.coords.longitude,
      ];

      //clearMarkers(); // Clear previous markers

      // Add marker for driver's location
      const driverMarker = L.marker(driverLocation, {
        icon: driverIcon,
        draggable: false,
      })
        .addTo(map2Instance)
        .bindPopup("Your Location")
        .openPopup();
      markers.push(driverMarker);

      // Get coordinates for pickup location
      getCoordinates(pickupAddress, function (pickupCoords) {
        const pickupMarker = L.marker(pickupCoords, {
          icon: userIcon,
          draggable: false,
        })
          .addTo(map2Instance)
          .bindPopup("Pickup Location")
          .openPopup();
        markers.push(pickupMarker);

        // Get coordinates for dropoff location
        getCoordinates(dropoffAddress, function (dropoffCoords) {
          const dropoffMarker = L.marker(dropoffCoords, { draggable: false })
            .addTo(map2Instance)
            .bindPopup("Dropoff Location")
            .openPopup();
          markers.push(dropoffMarker);

          // Adjust map view to include all markers
          const bounds = L.latLngBounds([
            driverLocation,
            pickupCoords,
            dropoffCoords,
          ]);
          map2Instance.fitBounds(bounds);

          // Draw the route
          routingControl = L.Routing.control({
            waypoints: [
              L.latLng(driverLocation),
              L.latLng(pickupCoords),
              L.latLng(dropoffCoords),
            ],
            routeWhileDragging: true,
            createMarker: () => null, // Prevent adding default markers
          }).addTo(map2Instance);
        });
      });
    });
  } else {
    console.log("The scale class has been removed.");
    console.log("Hiding popup map.");
    if (map2Instance) {
      map2Instance.remove();
      map2Instance = null; // Clear the reference
    }
    // Optionally reinitialize the main map
    initializeMainMap();
  }
}
// Accept Ride
// let pickupLat; let pickupLng;
function setCusLocations(address) {
  fetch(
    `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
      address
    )}`
  )
    .then((response) => response.json())
    .then((data) => {
      if (data.length > 0) {
        // const coords = [parseFloat(data[0].lat), parseFloat(data[0].lon)];
        pickupLat = parseFloat(data[0].lat);
        pickupLng = parseFloat(data[0].lon);

        console.log(`${pickupLat} && ${pickupLng}`);
      } else {
        alert("No coordinates found for address: " + address);
      }
    })
    .catch((error) => console.error("Error fetching coordinates: ", error));
}
async function acceptRide(pickup, dropoff, fare, bookingId) {
  // setCusLocations(pickup);
  console.log("In ride acceptance, the Booking Id is " + bookingId);
  document.getElementById("currentRide").classList.remove("hidden");
  document.getElementById("pickupLocation").textContent = pickup;
  document.getElementById("dropoffLocation").textContent = dropoff;
  document.getElementById("fareAmount").textContent = fare;

  //  TODO: Once accepted, send the information to the db, updating the riderId.
  try {
    const response = await fetch("/ride/accept", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({ bookingId }),
    });
    if (response.ok) {
      const res = await response.json();
      console.log("Ride accepted", res);
      //        display the map
      displayMap2(pickup, dropoff);
      //----start sharing location in realtime ------------
      startLocationSharing(bookingId);
      // listen to if a rider accepts a requests
      listenToAcceptedRide();
    } else {
      alert("Failed to accept the ride. Please try again.");
    }
  } catch (error) {
    console.error("Error accepting ride ", error);
  }
}
let pickupLat;
let pickupLng;
let riderPickedUpStatus = localStorage.getItem("riderPickedUpStatus");
console.log(riderPickedUpStatus);

function startLocationSharing(bookingId) {
  if (!navigator.geolocation) {
    alert("Geolocation is not supported by your browser!!!");
    return;
  }
  const locationInterval = setInterval(() => {
    navigator.geolocation.getCurrentPosition(async (position) => {
      const { latitude, longitude } = position.coords;
      pickupLat = position.coords.latitude;
      pickupLng = position.coords.longitude;
      console.log(`in the sharing location, lat ${pickupLat} lng ${pickupLng}`);

      try {
        await fetch("/ride/location", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({ latitude, longitude }),
        });
      } catch (error) {
        console.error("Failed to share location: ", error);
      }
    });

    console.log(riderPickedUpStatus);
    console.log(typeof riderPickedUpStatus);
    if (riderPickedUpStatus === "false" || !riderPickedUpStatus) {
      checkProximity();
    }
  }, 9000); //update location every after 9 sec
}
// Function to calculate distance using Haversine formula
function getDistance(lat1, lon1, lat2, lon2) {
  const R = 6371; // Radius of the Earth in km
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c * 1000; // Convert to meters
}

// Proximity detection

function checkProximity() {
  console.log("Checking the approximity in the rider section");

  const riderPickedUpStatus =
    localStorage.getItem("riderPickedUpStatus") === "true";
  if (riderPickedUpStatus) {
    // If rider has already picked up customer, don't check proximity
    return;
  }
  try {
    // proximityCheckInterval = setInterval(() => {
    if (driverMarker) {
      const riderLat = driverMarker.getLatLng().lat;
      const riderLng = driverMarker.getLatLng().lng;
      const distance = getDistance(pickupLat, pickupLng, riderLat, riderLng);
      console.log(`the distance is ${distance}`);

      if (distance <= 1.5) {
        // 1.5 meters radius
        // clearInterval(proximityCheckInterval);
        window.showRiderPickupConfirmation();
      }
    }
    // }, 3000); // Check every 3 seconds
  } catch (error) {
    console.error(`Error checking the distance ${error}`);
  }
}
// function setPickedUp(value){
//     picked = value;
//     console.log("The pickup value is "+picked);
// }

// Toggle rider availability
document
  .getElementById("toggleAvailability")
  .addEventListener("click", function () {
    const availabilityStatus = document.getElementById("availabilityStatus");
    if (availabilityStatus.textContent === "Available") {
      availabilityStatus.textContent = "Unavailable";
      availabilityStatus.classList.replace("text-green-500", "text-red-500");
    } else {
      availabilityStatus.textContent = "Available";
      availabilityStatus.classList.replace("text-red-500", "text-green-500");
    }
  });
