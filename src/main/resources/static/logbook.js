function toggleExpand(element) {
    const routeList = element.nextElementSibling;
    if (routeList.style.display === "none" || routeList.style.display === "") {
        routeList.style.display = "block";
    } else {
        routeList.style.display = "none";
    }
}