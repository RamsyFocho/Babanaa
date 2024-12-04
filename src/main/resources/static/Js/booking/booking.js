       alert("Clicked");

//   JavaScript for Form to collect data and send to the backend
     document.getElementById('rideRequestForm').addEventListener('submit',async function(e) {
       e.preventDefault();
       alert("Clicked");
       const formData = new FormData(this);
       const formObject = {
            pickup: formData.get("pickupLoc"),
            dropOff: formData.get("dropOff"),
            bikeType: formData.get("bikeType"),
//            TODO collect the fare from the UI
       }
       try{
            const response = await fetch('/ride/request',{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(formObject),
            });
            if(response.ok){
                const res = await response.json();
                console.log("Ride requested successfully:", res);
                document.getElementById('pendingMessage').classList.remove('hidden');

                //------------------ start listening for rider acceptance----------
                listenForRiderAcceptance(res.bookingId);
            }else {
                console.error("Error in response:", response);
            }
       } catch (error) {
            console.error("Request failed:", error);
       }

     });
function listenForRiderAcceptance(bookingId){
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected to WebSocket: ' + frame);
        stompClient.subscribe(`/all/riderAccepted/${bookingId}`, function (message) {
            const data = JSON.parse(message.body);
            console.log("Rider accepted the booking:", data);
            if(data !=null) {
                listenForRiderLocation(bookingId);
            }
            // Update UI with rider info
            document.getElementById("pendingMessage").classList.add("hidden");
            //TODO: displayRiderInfo(data);
        }
    }
}
function listenForRiderLocation(bookingId){
    const socket = new SockJs('/ws');
    const stompClient =  Stomp.over(socket);
    stompClient.connect({},function(frame){
        console.log("Connected to Websocket "+ frame);
        stompClient.subscribe('/all/location/'+bookingId, (data)=>{
            const location = JSON.parse(data.body);
//           update Rider Marker on the map
            updateRiderMarker(location.latitude,location.longitude);
         });
    });
}
//let rideMarker; // Keep track of the rider's marker on the map'
//export function updateRiderMarker(lat, lng){
//    if(!rideMarker){
//        rideMarker = L.marker([lat, lng)).addTo(map).bindPopup("Rider's position");
//
//    }else{
//        riderMarker.setLatLng([lat,lng]);
//    }
//}