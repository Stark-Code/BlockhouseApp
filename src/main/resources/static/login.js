
// https://firebase.google.com/docs/web/setup#available-libraries

// Client facing credentials for Firebase SDK
// Your web app's Firebase configuration

// For Firebase JS SDK v7.20.0 and later, measurementId is optional

const firebaseConfig = {
    apiKey: "AIzaSyAfCI5xUMS5-C7FuylxLAJSKo57uk2t5FE",
    authDomain: "blockhouse-c82b3.firebaseapp.com",
    projectId: "blockhouse-c82b3",
    storageBucket: "blockhouse-c82b3.appspot.com",
    messagingSenderId: "806296365173",
    appId: "1:806296365173:web:0dcd650a2fb480034df03c",
    measurementId: "G-JQ7W22Q3LF"
};  

// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);
// const analytics = firebase.getAnalytics(app);

function login() {
    var email = document.getElementById('login-email').value;
    var password = document.getElementById('login-password').value;
    var login_error = document.getElementById('login-error');
    login_error.style.display = "none";

    let user;
    let idToken;
    let data;

    firebase.auth().signInWithEmailAndPassword(email, password)
        .then((userCredential) => {
            user = userCredential.user; // Save to outer scope user
            console.log('User Object:', user);
            return user.getIdToken();
        })
        .then((receivedToken) => {
            idToken = receivedToken; // Save to outer scope idToken
            return fetch(mainServerUrl + '/login/add_session_token', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ jwt: idToken, uid: user.uid, name: user.displayName })
            });
        })
        .then(() => {
            return fetch(authServerUrl + '/decode_token', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ idToken })
            });
        })
        .then(response => response.json())
        .then((receivedData) => {
            data = receivedData; // Save to outer scope data
            return fetch(mainServerUrl + '/user/sync_user', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    uid: user.uid,
                    email: user.email,
                    name: user.displayName,
                    gymId: data.organizationId, // Dont change organizationId to gymId
                    permissions: data.permissions
                })
            });
        })
        .then(() => {
            window.location.href = mainServerUrl + `/nav/get_route_explorer/${data.organizationId}`; // Use outer scope data
        })
        .catch((error) => {
            login_error.style.display = "block";
            var errorCode = error.code || "Unknown error code";
            var errorMessage = error.message || "An unknown error occurred";
            console.log("Error Code:", errorCode);
            console.log("Error Message:", errorMessage);
        });
}

function signup() {
    var email = document.getElementById('signup_email').value;
    var password = document.getElementById('signup_password').value;
    firebase.auth().createUserWithEmailAndPassword(email, password)
        .catch((error) => {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log(errorCode);
            console.log(errorMessage);
        });
}

function resetPassword() {
    var email = document.getElementById('login_email').value;
    firebase.auth().sendPasswordResetEmail(email)
        .catch((error) => {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log(errorCode);
            console.log(errorMessage);
        });
}
