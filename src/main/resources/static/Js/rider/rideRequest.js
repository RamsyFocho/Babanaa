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
// -----------------------------initialize map---------------------------------
     initializeMainMap()
        const socket = new SockJS('/ws'); // Match with WebSocket endpoint in Spring
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected to WebSocket: ' + frame);

            // Subscribe to ride requests
            stompClient.subscribe('/all/bookings', function (message) {
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
                const bgColorClass = newRequest ? 'bg-gray-300 text-white' : 'bg-gray-100';

        rideRequestsContainer.innerHTML += `
            <div class="${bgColorClass} p-4 rounded-lg shadow mb-4">
                <p class="hidden" id="bookingId">${booking.bookingId}</p>
                <p><strong>Pickup:</strong> ${booking.pickupLocation}</p>
                <p><strong>Dropoff:</strong> ${booking.dropoffLocation}</p>
                <p><strong>Time:</strong> ${booking.bookingTime}</p>
                <p><strong>Fare:</strong> ${booking.fare}frs</p>
                <div class="flex justify-between mt-4">
                    <button class="bg-green-500 text-white px-4 py-2 rounded font-bold hover:bg-green-600" onclick="acceptRide('${booking.pickupLocation}', '${booking.dropoffLocation}', '${booking.fare}', '${booking.bookingId}')">Accept</button>
                    <button class="bg-blue-500 text-white px-4 py-2 rounded font-bold hover:bg-blue-600"
                        onclick="viewRideDetails('${booking.pickupLocation}', '${booking.dropoffLocation}')">
                            View
                    </button>
                    <button class="bg-red-500 text-white px-4 py-2 rounded font-bold hover:bg-red-600">Reject</button>
                </div>
            </div>
        `;
      })
    }
    // Initialize OpenStreetMap

        let markers = [];
        let routingControl = null;
        let mainMapInstance;
        let map2Instance;
    function initializeMainMap(){
        if(!mainMapInstance){
             mainMapInstance = L.map('map').setView([51.505, -0.09], 18);
        }
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(mainMapInstance);
    }
//------------------view rides----------------------------------------
//---------------get the rider's actual location---------------

    // Function to clear all markers
    function clearMarkers() {
      markers.forEach(marker => mainMapInstance.removeLayer(marker)); // Remove all markers
      markers = [];
      if (routingControl) {
        mainMapInstance.removeControl(routingControl); // Remove the routing control if it exists
        routingControl = null;
      }
    }

    // Function to get user's current location as he moves
    function getCurrentLocation(callback) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(callback, () => {
//            navigator.geolocation.watchPosition(callback, () => {
                alert("Unable to retrieve your location.");
            }, {
                enableHighAccuracy: true
            });
        } else {
            alert("Geolocation is not supported by this browser.");
        }
    }
    // Function to convert address to coordinates
    function getCoordinates(address, callback) {
        fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}`)
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    const coords = [parseFloat(data[0].lat), parseFloat(data[0].lon)];
                    callback(coords);
                } else {
                    alert("No coordinates found for address: " + address);
                }
            }).catch(error => console.error("Error fetching coordinates: ", error));
    }
    //    customized icons:
       // Define custom icons using FontAwesome and Tailwind CSS
    const driverIcon = L.divIcon({
        html: '<i class="fas fa-biking text-blue-500 text-2xl"></i>',
        className: 'text-center', // Tailwind CSS class for additional styling if needed
        iconSize: [30, 30],
        iconAnchor: [15, 30],
        popupAnchor: [0, -30]
    });
    const userIcon = L.divIcon({
            html: '<i class="fas fa-user text-blue-500 text-2xl"></i>',
            className: 'text-center', // Tailwind CSS class for additional styling if needed
            iconSize: [30, 30],
            iconAnchor: [15, 30],
            popupAnchor: [0, -30]
    });
    // Function to view ride details and plot on map
    const mainMapView = document.getElementById("map");
    function viewRideDetails(pickupAddress, dropoffAddress) {
        initializeMainMap();
//        when clicked, it should scroll till the map
        mainMapView.scrollIntoView({behaviour: 'smooth'})

        getCurrentLocation(function(position) {
            const driverLocation = [position.coords.latitude, position.coords.longitude];

            clearMarkers(); // Clear previous markers

            // Add marker for driver's location
            const driverMarker = L.marker(driverLocation, {
             icon: driverIcon,
             draggable: false,
            })
             .addTo(mainMapInstance)
              .bindPopup('Your Location')
              .openPopup();
             markers.push(driverMarker);

            // Get coordinates for pickup location
            getCoordinates(pickupAddress, function(pickupCoords) {
                const pickupMarker = L.marker(pickupCoords, {icon:userIcon, draggable: false })
                    .addTo(mainMapInstance)
                    .bindPopup('Pickup Location').openPopup();
                markers.push(pickupMarker);

                // Get coordinates for dropoff location
                getCoordinates(dropoffAddress, function(dropoffCoords) {
                    const dropoffMarker = L.marker(dropoffCoords, { draggable: false })
                        .addTo(mainMapInstance)
                        .bindPopup('Dropoff Location').openPopup();
                    markers.push(dropoffMarker);

                    // Adjust mainMapInstance view to include all markers
                    const bounds = L.latLngBounds([driverLocation, pickupCoords, dropoffCoords]);
                    mainMapInstance.fitBounds(bounds);

                    // Draw the route
                    routingControl = L.Routing.control({
                        waypoints: [
                            L.latLng(driverLocation),
                            L.latLng(pickupCoords),
                            L.latLng(dropoffCoords)
                        ],
                        routeWhileDragging: true,
                        createMarker: () => null // Prevent adding default markers
                    }).addTo(mainMapInstance);
                });
            });
        });
    }
    function displayMap2(pickupAddress, dropoffAddress) {
       alert("yo");
       const map = document.querySelector('.mapContainer');
       map.classList.toggle('scale');
       // Check if the class has been toggled
        if (map.classList.contains('scale')) {
            console.log('The scale class has been added.');
            if(mainMapInstance){
                mainMapInstance.remove();
                mainMapInstance = null;
            }
            // ----------------initialize the 2nd map if it doesn't exist---------------------
            if(!map2Instance){
                 map2Instance = L.map('map2').setView([51.505, -0.09], 18);
            }
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                     attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map2Instance);
             // Tile Layer (OpenStreetMap)
            getCurrentLocation(function(position) {
                        const driverLocation = [position.coords.latitude, position.coords.longitude];

                        //clearMarkers(); // Clear previous markers

                        // Add marker for driver's location
                        const driverMarker = L.marker(driverLocation, {
                         icon: driverIcon,
                         draggable: false,
                        })
                         .addTo(map2Instance)
                          .bindPopup('Your Location')
                          .openPopup();
                         markers.push(driverMarker);

                        // Get coordinates for pickup location
                        getCoordinates(pickupAddress, function(pickupCoords) {
                            const pickupMarker = L.marker(pickupCoords, {icon:userIcon, draggable: false })
                                .addTo(map2Instance)
                                .bindPopup('Pickup Location').openPopup();
                            markers.push(pickupMarker);

                            // Get coordinates for dropoff location
                            getCoordinates(dropoffAddress, function(dropoffCoords) {
                                const dropoffMarker = L.marker(dropoffCoords, { draggable: false })
                                    .addTo(map2Instance)
                                    .bindPopup('Dropoff Location').openPopup();
                                markers.push(dropoffMarker);

                                // Adjust map view to include all markers
                                const bounds = L.latLngBounds([driverLocation, pickupCoords, dropoffCoords]);
                                map2Instance.fitBounds(bounds);

                                // Draw the route
                                routingControl = L.Routing.control({
                                    waypoints: [
                                        L.latLng(driverLocation),
                                        L.latLng(pickupCoords),
                                        L.latLng(dropoffCoords)
                                    ],
                                    routeWhileDragging: true,
                                    createMarker: () => null // Prevent adding default markers
                                }).addTo(map2Instance);
                            });
                        });
            });
        }
        else {
            console.log('The scale class has been removed.');
             console.log('Hiding popup map.');
             if (map2Instance) {
                  map2Instance.remove();
                  map2Instance = null; // Clear the reference
             }
             // Optionally reinitialize the main map
             initializeMainMap()
        }
    }
    // Accept Ride
    async function acceptRide(pickup, dropoff, fare,bookingId) {
        console.log("In ride acceptance, the Booking Id is "+bookingId)
        document.getElementById('currentRide').classList.remove('hidden');
        document.getElementById('pickupLocation').textContent = pickup;
        document.getElementById('dropoffLocation').textContent = dropoff;
        document.getElementById('fareAmount').textContent = fare;
        //        display the map
        displayMap2(pickup,dropoff);

        //  TODO: Once accepted, send the information to the db, updating the riderId.
        try{
            const response =  await fetch('/ride/accept',{
                method:'POST',
                headers:{
                  'Content-Type':'application/json' ,
                },
                credentials:'include',
                body: JSON.stringify({bookingId}),
            });
            if(response.ok){
                const res = await response.json();
                console.log("Ride accepted",res);
                //----start sharing location in realtime ------------
                 startLocationSharing(bookingId);
            }else{
                alert("Failed to accept the ride. Please try again.")
            }
        }catch(error){
            console.error("Error accepting ride ",error);
        }

    }
    function startLocationSharing(bookingId){
        if(!navigator.geolocation){
            alert("Geolocation is not supported by your browser!!!");
            return;
        }
        const locationInterval = setInterval(()=>{
        // TODO: Send the current location to the server
            navigator.geolocation.getCurrentPosition(async (position)=>{
                const {latitude, longitude} = position.coords;
                try{
                    await fetch('/ride/location',{
                        method:'POST',
                        headers:{
                          'Content-Type':'application/json' ,
                        },
                        credentials:'include',
                        body: JSON.stringify({latitude, longitude}),
                    });
                }catch(error){
                    console.error("Failed to share location: ",error);
                }
            });
        },5000);//update location every after 5 sec
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

