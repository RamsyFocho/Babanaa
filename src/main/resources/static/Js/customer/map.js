alert("Please wait for the page to load...");

let pickupLat, pickupLng; // Declare globally for access in multiple functions
let dropoffMarker; // Declare dropoffMarker globally so it can be updated
let routingControl; // Declare routingControl globally to update the route
var dropoffLocation;
var submitBtn = document.getElementById("submitBtn");

// Function to disable/enable the submit button
function buttonDisable(disable) {
  if (disable) {
    submitBtn.disabled = true;
    submitBtn.classList.add("bg-gray-500", "cursor-not-allowed", "text-black");
    submitBtn.classList.remove(
      "bg-yellow-500",
      "cursor-pointer",
      "text-white",
      "hover:bg-yellow-600"
    );
  } else {
    submitBtn.disabled = false;
    submitBtn.classList.remove(
      "bg-gray-500",
      "cursor-not-allowed",
      "text-black"
    );
    submitBtn.classList.add(
      "bg-yellow-500",
      "cursor-pointer",
      "text-white",
      "hover:bg-yellow-600"
    );
  }
}

// Initialize the map with a higher zoom level (18)
const map = L.map("map").setView([51.505, -0.09], 18); // Zoom level 18 for more details
// Load OpenStreetMap tiles with a higher zoom level (up to 19)
L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
  maxZoom: 19, // Allow up to zoom level 19 for more detail
  attribution: "© OpenStreetMap",
}).addTo(map);

// Automatically fetch the user's current location
const userIcon = L.divIcon({
  html: '<i class="fas fa-user text-blue-500 text-2xl"></i>',
  className: "text-center", // Tailwind CSS class for additional styling if needed
  iconSize: [30, 30],
  iconAnchor: [15, 30],
  popupAnchor: [0, -30],
});

if (navigator.geolocation) {
  navigator.geolocation.getCurrentPosition(
    (position) => {
      pickupLat = position.coords.latitude; // User's latitude
      pickupLng = position.coords.longitude; // User's longitude

      const pickupMarker = L.marker([pickupLat, pickupLng], {
        icon: userIcon,
        draggable: false, // Ensure pickup marker is not draggable
      })
        .addTo(map)
        .bindPopup("Your position!!!")
        .openPopup();

      // Fetch and display the place name
      getPlaceName(pickupLat, pickupLng);

      // Set map view to the user's location
      map.setView([pickupLat, pickupLng], 18);
    },
    () => {
      alert("Unable to retrieve your location.");
    },
    {
      enableHighAccuracy: true, // Request high accuracy for better GPS results
    }
  );
} else {
  alert("Geolocation is not supported by this browser.");
}

//-----------rider marker--------------
let riderMarker; // Keep track of the rider's marker on the map
function updateRiderMarker(lat, lng) {
  if (!riderMarker) {
    riderMarker = L.marker([lat, lng], {
      icon: L.divIcon({
        html: '<i class="fas fa-motorcycle text-red-500 text-2xl"></i>',
        className: "text-center",
        iconSize: [30, 30],
        iconAnchor: [15, 30],
        popupAnchor: [0, -30],
      }),
    })
      .addTo(map)
      .bindPopup("Rider's position")
      .openPopup();
  } else {
    riderMarker.setLatLng([lat, lng]);
  }
  // Route from rider to pickup to dropoff
  if (pickupLat && pickupLng && dropoffLocation) {
    updateRoute([lat, lng], [pickupLat, pickupLng], dropoffLocation);
  }
}

//convert the pickup location into the actual place
function getPlaceName(lat, lng) {
  const url = `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lng}`;

  fetch(url)
    .then((response) => response.json())
    .then((data) => {
      if (data && data.address) {
        const placeName = data.display_name;
        console.log("Place Name:", placeName);

        // Optionally update the UI
        document.getElementById("pickup").value = placeName;
      } else {
        alert("Could not retrieve place name.");
      }
    })
    .catch((error) => {
      console.error("Error with reverse geocoding:", error);
    });
}

// Handle dropoff input change
const dropoffInput = document.getElementById("dropoff");
dropoffInput.addEventListener("change", function () {
  dropoffLocation = dropoffInput.value;
  // Use a Geocoding API like Nominatim for getting coordinates from the address
  fetch(
    `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
      dropoffLocation
    )}`
  )
    .then((response) => response.json())
    .then((data) => {
      if (data.length > 0) {
        const dropoffLat = data[0].lat;
        const dropoffLng = data[0].lon;

        // If a dropoffMarker already exists, update its position instead of creating a new one
        if (dropoffMarker) {
          dropoffMarker.setLatLng([dropoffLat, dropoffLng]); // Update existing marker position
          dropoffLocation = [dropoffLat, dropoffLng]; //Store the drop off location in an array
        } else {
          // Create a marker for the dropoff location
          dropoffMarker = L.marker([dropoffLat, dropoffLng], {
            icon: L.divIcon({
              html: '<i class="fas fa-flag-checkered text-green-500 text-2xl"></i>',
              className: "text-center",
              iconSize: [30, 30],
              iconAnchor: [15, 30],
              popupAnchor: [0, -30],
            }),
            draggable: false, // Ensure dropoff marker is not draggable
          })
            .addTo(map)
            .bindPopup("Drop-off Location")
            .openPopup();
          dropoffLocation = [dropoffLat, dropoffLng]; //Store the drop off location in an array
        }

        // Routing from pickup to dropoff
        if (pickupLat && pickupLng) {
          updateRoute([pickupLat, pickupLng], dropoffLocation);
        } else {
          alert("Pickup location not available!");
        }
      } else {
        alert("Drop-off location not found!");
      }
    })
    .catch((error) => console.error("Error fetching dropoff location:", error));
});

// Function to update the route
function updateRoute(start, waypoint, end) {
  if (routingControl) {
    routingControl.setWaypoints([
      L.latLng(start[0], start[1]),
      L.latLng(waypoint[0], waypoint[1]),
      L.latLng(end[0], end[1]),
    ]); // Update route
  } else {
    // Create routing control if it doesn't exist yet
    routingControl = L.Routing.control({
      waypoints: [
        L.latLng(start[0], start[1]),
        L.latLng(waypoint[0], waypoint[1]),
        L.latLng(end[0], end[1]),
      ],
      router: L.Routing.osrmv1({
        serviceUrl: "https://router.project-osrm.org/route/v1", // Use HTTPS for security
      }),
      routeWhileDragging: false, // Disable route dragging
    })
      .on("routesfound", function (e) {
        // Get the distance in meters
        const distanceInMeters = e.routes[0].summary.totalDistance;

        // Convert meters to kilometers
        const distanceInKm = distanceInMeters / 1000;

        // Calculate fare (FCFA)
        const baseFare = 500; // Base fare in FCFA
        const farePerKm = 100; // FCFA per kilometer
        const estimatedFare = (
          baseFare +
          farePerKm * distanceInKm
        ).toFixed(2); // Round to 2 decimal places

        // Display fare and distance in the UI
        document.getElementById(
          "fareAmount"
        ).innerText = `${estimatedFare} FCFA`;
        if (estimatedFare != 0 || estimatedFare != null) {
          buttonDisable(false);
        }
        // document.getElementById('distanceAmount').innerText = `${distanceInKm.toFixed(2)} km`;

        // Optional: console log for debugging
        console.log(
          `Distance: ${distanceInKm} km, Fare: ${estimatedFare} FCFA`
        );
      })
      .addTo(map);
  }
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
  console.log("Checking the approximity");
  if(!picked){
    try {
      proximityCheckInterval = setInterval(() => {
        if (riderMarker) {
          const riderLat = riderMarker.getLatLng().lat;
          const riderLng = riderMarker.getLatLng().lng;
          const distance = getDistance(pickupLat, pickupLng, riderLat, riderLng);
          console.log('====================================');
          console.log(`distance ${distance}`);
          console.log('====================================');
          if (distance <= 1.5) {
            // 1.5 meters radius
            clearInterval(proximityCheckInterval);
            showCustomerPickupConfirmation();
          }
        }
      }, 9000); // Check every 9 seconds
    } catch (error) {
      console.error(`Error checking the distance ${error}`);
    }
  }else{
      console.log("The user has already been picked");
  }
}

// Show popup to confirm pickup
function showCustomerPickupConfirmation() {
  if (confirm("Has the rider picked you up?")) {
    // Mark the ride as picked up
    alert("Ride marked as picked up!");
    // TODO: Send confirmation to the server
  } else {
    alert("Please wait for the rider.");
  }
}