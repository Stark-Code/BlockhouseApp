// Registration process
// User recieves email from admin with registration code
// Registration code contains permissions, organizationd id, email and expiration date
// User submits registration code, email, password, and password confirmation in registration form
// Nodejs recieves registration code, email, password, and password confirmation
// Nodejs checks if registration code is valid via Java API
// If valid, Nodejs creates user in database

function signup() {
    var email = document.getElementById('email').value;
    var registrationCode = document.getElementById('registration_code').value; // (Invitation code)
    var firstName = document.getElementById('first_name').value;
    var lastName = document.getElementById('last_name').value;
    var name = firstName + ' ' + lastName;
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('password_confirm').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match');
        return;
    }

    if (!/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/.test(email)) {
        alert('Please enter a valid email address');
        return;
    }

    var parts = registrationCode.split('.');
    if (parts.length !== 3) {
        alert('Invalid registration code');
        return;
    }

    if (password.length < 8) {
        alert('Password must be at least 8 characters');
        return;
    }

    fetch(authServerUrl + '/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            displayName: name,
            registrationCode: registrationCode,
            password: password,
        })
    })
        .then(
            response => {
                return response.json()
            })
        .then(data => {
            if (data.success) {
                alert('Signup successful. Click OK to be redirected to home page.');
                window.setTimeout(function () {
                    window.location.href = mainServerUrl + '/login/get_login';
                }, 1000);
            } else {
                    alert(data.message);
            }
        })
        .catch((error) => {
            alert('An error occurred while connecting to the server. Please check your internet connection and try again.');
        });
}
