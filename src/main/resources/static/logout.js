function logout() {

    const firebaseConfig = {
        apiKey: "AIzaSyA7rFntt5DgCcsGkIr6Udqmb1iN1X3iAgo",
        authDomain: "p-hub-b0290.firebaseapp.com",
        projectId: "p-hub-b0290",
        storageBucket: "p-hub-b0290.appspot.com",
        messagingSenderId: "732002827659",
        appId: "1:732002827659:web:f9f73d1a9203c7457d038e",
        measurementId: "G-J15YZETF15"
    };
    
    // Initialize Firebase
    const app = firebase.initializeApp(firebaseConfig);

    firebase.auth().signOut()
    .then(() => {
        // Sign-out successful, redirect user to server-side logout endpoint
        window.location.href = mainServerUrl + `/login/logout`;
    }).catch((error) => {
        // An error happened.
        console.log("Error logging out")
        console.log(error.message);
    });
}

