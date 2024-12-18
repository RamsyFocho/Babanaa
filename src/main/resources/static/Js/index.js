//let slideIndex = 0;
//  const slides = document.querySelectorAll('.carousel-item');
//  const totalSlides = slides.length;
//
//  function showSlides() {
//    // Hide all slides
//    slides.forEach((slide) => {
//      slide.style.display = 'none';
//    });
//
//    // Increment slide index, looping back to the start if necessary
//    slideIndex = (slideIndex + 1) % totalSlides;
//
//    // Show the current slide
//    slides[slideIndex].style.display = 'block';
//
//    // Repeat the function every 3 seconds
//    setTimeout(showSlides, 3000);
//  }

//----------------------------------------slide show---------------------------------------------------------
let slideIndex = 0;
showSlides();

function showSlides() {
    let slides = document.querySelectorAll('.carousel-item');
    for (let i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    slideIndex++;
    if (slideIndex > slides.length) {
        slideIndex = 1;
    }
    slides[slideIndex - 1].style.display = "block";
    slides[slideIndex - 1].style.transition = "all .5s";

    setTimeout(showSlides, 3000);
}

//  ---------------------modal option------------------------
// Get modal element
const modal = document.getElementById("myModal");
const openModalBtn = document.getElementById("openModal");
const closeModalBtn = document.getElementById("closeModal");

// Function to open modal
openModalBtn.onclick = function () {
    console.log("hidden removed")
    modal.style.display = 'flex';
    modal.form.classList.add('absolute');
    //    form.classList.remove('hidden');

}

// Function to close modal
closeModalBtn.onclick = function () {
    modal.style.display = 'none';
    //    form.classList.add('hidden');

}

// Close modal when clicking outside of modal content
window.onclick = function (event) {
    if (event.target === modal) {
        modal.classList.add("hidden");
    }
}

// Add functionality for buttons (redirect or actions)
document.getElementById("customerButton").onclick = function () {
    // Redirect to customer registration or booking page
    window.location.href = '../templates/customer/credentials.html'; // Change to your customer page
};

document.getElementById("riderButton").onclick = function () {
    // Redirect to rider registration page
    window.location.href = '/rider-registration'; // Change to your rider page
};