document.addEventListener("DOMContentLoaded", function () {
    
     // Function to hide all forms
     function hideAllForms() {
        const forms = ['addMemberForm','addRouteForm', 'addWallForm', 'editWallSection', 'editRouteSection'];
        forms.forEach(formId => {
            document.getElementById(formId).style.display = 'none';
        });
    }    

    // Toggle Add Member Form
    document.getElementById('showAddMemberForm').addEventListener('click', function () {
        hideAllForms();
        const form = document.getElementById('addMemberForm');
        form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
    });

    // Toggle Add Route Form
    document.getElementById('showAddRouteForm').addEventListener('click', function () {
        hideAllForms();
        const form = document.getElementById('addRouteForm');
        form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
    });

    // Toggle Add Wall Form
    document.getElementById('showAddWallForm').addEventListener('click', function () {
        hideAllForms();
        const form = document.getElementById('addWallForm');
        form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
    });

    // Toggle Edit Wall Section
    document.getElementById('showEditWallForm').addEventListener('click', function () {
        hideAllForms();
        const form = document.getElementById('editWallSection');
        form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
    });

    // Toggle Edit Route Section
    document.getElementById('showEditRouteForm').addEventListener('click', function () {
        hideAllForms();
        const form = document.getElementById('editRouteSection');
        form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
    });

    // Dropdown elements
    const wallDropdown = document.getElementById("wallDropdown");
    const routeDropdown = document.getElementById("routeDropdown");

    // Form references
    const editWallForm = document.getElementById("editWallForm");
    const editRouteForm = document.getElementById("editRouteForm");

    // Wall form elements
    const editName = document.getElementById("editName");
    const editIsActive = document.getElementById("editIsActive");
    const editWallId = document.getElementById("editWallId");

    // Route form elements
    const editRouteName = document.getElementById("editRouteName");
    const editSetterName = document.getElementById("editSetterName");
    const editFirstAscent = document.getElementById("editFirstAscent");
    const editGrade = document.getElementById("editGrade");
    const editStyle = document.getElementById("editStyle");
    const editDescription = document.getElementById("editDescription");
    const editRouteId = document.getElementById("editRouteId");

    function toggleFormDisplay(dropdown, form) {
        dropdown.addEventListener("change", function () {
            form.style.display = this.value ? "block" : "none";
        });
    }

    toggleFormDisplay(wallDropdown, editWallForm);
    toggleFormDisplay(routeDropdown, editRouteForm);

    // Listen for changes to the wall dropdown
    wallDropdown.addEventListener("change", function () {
        const selectedWallId = this.value;

        if (selectedWallId) {
            const selectedWall = activeWalls.find(wall => wall.id === selectedWallId);

            if (selectedWall) {
                // Populate the form fields
                editName.value = selectedWall.name;
                editIsActive.checked = selectedWall.isActive;
                editWallId.value = selectedWall.id;

                // Show the form
                editWallForm.style.display = "block";
            }
        } else {
            // Hide the form if no wall is selected
            editWallForm.style.display = "none";
        }
    });

    
    // Listen for changes to the dropdown
    routeDropdown.addEventListener("change", function () {
        const selectedRouteId = this.value;

        if (selectedRouteId) {
            const selectedRoute = routes.find(route => route.id === selectedRouteId);

            if (selectedRoute) {
                // Populate the form fields
                editRouteName.value = selectedRoute.name;
                editSetterName.value = selectedRoute.setterName;
                editFirstAscent.value = selectedRoute.firstAscent;
                editGrade.value = selectedRoute.grade;
                editStyle.value = selectedRoute.style;
                editDescription.value = selectedRoute.description;
                editRouteId.value = selectedRoute.id;
                // Show the form
                editRouteForm.style.display = "block";
            }
        } else {
            // Hide the form if no route is selected
            editRouteForm.style.display = "none";
        }
    });
});
