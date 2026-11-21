// StudySync small JavaScript helpers
document.addEventListener('DOMContentLoaded', function () {

    // 1. Register form: check email, password length and matching passwords BEFORE sending
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function (event) {
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const confirmValue = document.getElementById('confirmPassword').value;
            let message = '';
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                message = 'Please enter a valid email address.';
            } else if (password.length < 6) {
                message = 'Password must be at least 6 characters.';
            } else if (password !== confirmValue) {
                message = 'Passwords do not match.';
            }
            if (message) {
                event.preventDefault();
                const box = document.getElementById('formError');
                box.textContent = message;
                box.classList.remove('d-none');
            }
        });
    }

    // 2. Confirmation popup for any button that has data-confirm="..."
    document.querySelectorAll('[data-confirm]').forEach(function (button) {
        button.addEventListener('click', function (event) {
            if (!window.confirm(button.getAttribute('data-confirm'))) {
                event.preventDefault();
            }
        });
    });

    // 3. Schedule-session form: change the fields when the session type changes
    const typeSelect = document.getElementById('sessionType');
    if (typeSelect) {
        const placeLabel = document.getElementById('placeLabel');
        const placeInput = document.getElementById('place');
        const examBox = document.getElementById('examTopicBox');
        const duration = document.getElementById('durationMinutes');

        const updateFields = function () {
            const type = typeSelect.value;
            if (type === 'ONLINE') {
                placeLabel.textContent = 'Meeting link';
                placeInput.placeholder = 'https://meet.google.com/...';
                examBox.classList.add('d-none');
            } else if (type === 'PHYSICAL') {
                placeLabel.textContent = 'Venue';
                placeInput.placeholder = 'Library Room 2';
                examBox.classList.add('d-none');
            } else {
                placeLabel.textContent = 'Venue or link';
                placeInput.placeholder = 'Room 5 or https://...';
                examBox.classList.remove('d-none');
                duration.value = 120;
            }
        };

        typeSelect.addEventListener('change', updateFields);
        updateFields();
    }
});