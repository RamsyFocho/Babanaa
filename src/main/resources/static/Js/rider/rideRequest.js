// Establish WebSocket connection
//    const notification=false;
     document.addEventListener("DOMContentLoaded", function() {
        fetch('/ride/requests')
        .then(response => response.json())
          .then(data => {
                     console.log('Initial ride requests:', data); // Log to check the response
                     showRideRequests(data,false);
          })
        .catch(error => console.error('Error fetching initial bookings:', error));

        const socket = new SockJS('/ws'); // Match with WebSocket endpoint in Spring
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected to WebSocket: ' + frame);

            // Subscribe to ride requests
            stompClient.subscribe('/all/messages', function (message) {
                const rideRequest = JSON.parse(message.body);
                console.log('Received rideRequest:', rideRequest);
                 // If the WebSocket message is not an array, wrap it in an array
                 if(rideRequest==null){
                    document.getElementById("message").innerHTML="No ride requests for now!!!";
                 }
                 else if (!Array.isArray(rideRequest)) {
                     showRideRequests([rideRequest],true);
                 } else {
                     showRideRequests(rideRequest,true);
                 }
            });
        });
     });

    // Show all ride request in the UI
    function showRideRequests(rideRequest,newRequest) {
        const rideRequestsContainer = document.getElementById('rideRequests');
//        rideRequestsContainer.innerHTML ='';

        // ---------------------loop through all the list----------
      rideRequest.forEach(function(booking){
        console.log(booking.pickupLocation+"\n");
        // Ensure `notification` is set to true/false based on your logic before this point.
                const bgColorClass = newRequest ? 'bg-red-500 text-white' : 'bg-gray-100';

        rideRequestsContainer.innerHTML += `
            <div class="${bgColorClass} p-4 rounded-lg shadow mb-4">
                <p><strong>Pickup:</strong> ${booking.pickupLocation}</p>
                <p><strong>Dropoff:</strong> ${booking.dropoffLocation}</p>
                <p><strong>Time:</strong> ${booking.bookingTime}</p>
                <p><strong>Fare:</strong> ${booking.fare}frs</p>
                <div class="flex justify-between mt-4">
                    <button class="bg-green-500 text-white px-4 py-2 rounded font-bold hover:bg-green-600" onclick="acceptRide('${booking.pickupLocation}', '${booking.dropoffLocation}', '${booking.fare}')">Accept</button>
                    <button class="bg-blue-500 text-white px-4 py-2 rounded font-bold hover:bg-blue-600" onclick="acceptRide('${booking.pickupLocation}', '${booking.dropoffLocation}', '${booking.fare}')">View</button>
                    <button class="bg-red-500 text-white px-4 py-2 rounded font-bold hover:bg-red-600">Reject</button>
                </div>
            </div>
        `;
      })
    }

    // Accept Ride
    function acceptRide(pickup, dropoff, fare) {
        document.getElementById('currentRide').classList.remove('hidden');
        document.getElementById('pickupLocation').textContent = pickup;
        document.getElementById('dropoffLocation').textContent = dropoff;
        document.getElementById('fareAmount').textContent = fare;

        // Update map markers for pickup and dropoff
        const pickupLatLng = pickup.split(',').map(Number);
        const dropoffLatLng = dropoff.split(',').map(Number);
        L.marker(pickupLatLng).addTo(map).bindPopup('Pickup Location').openPopup();
        L.marker(dropoffLatLng).addTo(map).bindPopup('Dropoff Location').openPopup();
        map.setView(pickupLatLng, 13);
    }

    // Toggle rider availability
    document.getElementById('toggleAvailability').addEventListener('click', function() {
        const availabilityStatus = document.getElementById('availabilityStatus');
        if (availabilityStatus.textContent === 'Available') {
            availabilityStatus.textContent = 'Unavailable';
            availabilityStatus.classList.replace('text-green-500', 'text-red-500');
        } else {
            availabilityStatus.textContent = 'Available';
            availabilityStatus.classList.replace('text-red-500', 'text-green-500');
        }
    });

    // Initialize OpenStreetMap
    const map = L.map('map').setView([51.505, -0.09], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);