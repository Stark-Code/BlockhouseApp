
// Button Handler
document.addEventListener('DOMContentLoaded', function () {
    const toggleFormBtn = document.getElementById("toggleFormBtn");
    const logbookForm = document.getElementById("logbookForm");

    toggleFormBtn.addEventListener("click", function () {
        logbookForm.style.display = logbookForm.style.display === "none" ? "block" : "none";
    });
});

// Star Rating
document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll(".star");
    let ratingInput = document.getElementById("ratingInput");

    // Initialize star-rating
    let selectedRating = 0;

    stars.forEach((star, index) => {
        star.addEventListener("click", function () {
            selectedRating = index + 1;
            updateStars(selectedRating);
        });
    });

    function updateStars(rating) {
        stars.forEach((star, index) => {
            star.className = index < rating ? "full-star star" : "empty-star star";
        });
        ratingInput.value = rating;  // Store the rating in the hidden input
    }
});




