
// ----------------------MAP-------------------------
 var map = L.map('map').setView([51.505, -0.09], 13);  // Default map view (London)
 var pickupLocation=[];
 var dropOffLocation=[];
  // Load OpenStreetMap tiles
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map);

  // Geolocation: Get the user's current location
  if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function(position) {
          var lat = position.coords.latitude;
          var lng = position.coords.longitude;

          // Update the map view to user's location
          map.setView([lat, lng], 13);

          // Add a marker at the user's location
          L.marker([lat, lng]).addTo(map)
              .bindPopup('You are here!')
              .openPopup();

          // Update the pickup location input with the coordinates
          document.getElementById('pickup').value = lat + ", " + lng;
          pickupLocation = [lat, lng];
      }, function() {
          alert("Unable to fetch your location. Please allow location access.");
      });
  } else {
      alert("Geolocation is not supported by your browser.");
  }
//   JavaScript for Form
     document.getElementById('rideRequestForm').addEventListener('submit', function(e) {
       e.preventDefault();
       document.getElementById('pendingMessage').classList.remove('hidden');
     });
