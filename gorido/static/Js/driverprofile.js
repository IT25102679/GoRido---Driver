function openDeleteModal(){
    var modal = document.getElementById("delete-modal");
    var box = document.getElementById("delete-box");

    modal.classList.remove("hidden");

    setTimeout(() => {
        box.classList.remove("opacity-0", "scale-90");
        box.classList.add("opacity-100", "scale-100");
    }, 10);
}

function closeDeleteModal(){
    var modal = document.getElementById("delete-modal");
    var box = document.getElementById("delete-box");

    box.classList.remove("opacity-100", "scale-100");
    box.classList.add("opacity-0", "scale-90");

    setTimeout(() => {
        modal.classList.add("hidden");
    }, 300);
}

function confirmDelete(){
    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {

        if (request.readyState == 4 && request.status == 200) {
            var response = request.responseText;

            if(response == "success"){
                closeDeleteModal();
                window.location.href = "/signin";
            }else{
                alert(response);
            }
        }
    };

    request.open("GET", "/delete/user", true);
    request.send();
}

function openDriverDeleteModal(){
    var modal = document.getElementById("delete-modal1");
    var box = document.getElementById("delete-box1");

    modal.classList.remove("hidden");

    setTimeout(() => {
        box.classList.remove("opacity-0", "scale-90");
        box.classList.add("opacity-100", "scale-100");
    }, 10);
}

function closeDriverDeleteModal(){
    var modal = document.getElementById("delete-modal1");
    var box = document.getElementById("delete-box1");

    box.classList.remove("opacity-100", "scale-100");
    box.classList.add("opacity-0", "scale-90");

    setTimeout(() => {
        modal.classList.add("hidden");
    }, 300);
}

function confirmDriverDelete(){
    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {

        if (request.readyState == 4 && request.status == 200) {
            var response = request.responseText;

            if(response == "success"){
                closeDeleteModal();
                window.location.href = "/userprofile";
            }else{
                alert(response);
            }
        }
    };

    request.open("GET", "/delete/driver", true);
    request.send();
}

function openModalUpdateExp(){
    var modal = document.getElementById("driver-modal");
    var box = document.getElementById("modal-box2");

    modal.classList.remove("hidden");

    setTimeout(() => {
        box.classList.remove("opacity-0", "scale-95");
        box.classList.add("opacity-100", "scale-100");
    }, 10);
}

function closeModalUpdateExp(){
    var modal = document.getElementById("driver-modal");
    var box = document.getElementById("modal-box2");

    box.classList.remove("opacity-100", "scale-100");
    box.classList.add("opacity-0", "scale-95");

    setTimeout(() => {
        modal.classList.add("hidden");
    }, 300);

    window.location.reload();
}

function updateLicense() {

    var expire_date = document.getElementById("expire_date");
    var licenseImage = document.querySelector("#wrap-nic input");
    var licensemodelmsg = document.getElementById("licensemodelmsg");

    licensemodelmsg.classList.add("hidden");
    resetErrors(licensemodelmsg);

    if (expire_date.value.trim() == "") {
        licensemodelmsg.innerText = "License expiry date is required";
        licensemodelmsg.classList.remove("hidden");
        expire_date.classList.add("border-red-500");
        return;
    }

    var expDate = new Date(expire_date.value);
    var today = new Date();
    today.setHours(0,0,0,0);

    if (expDate < today) {
        licensemodelmsg.innerText = "License is already expired";
        licensemodelmsg.classList.remove("hidden");
        expire_date.classList.add("border-red-500");
        return;
    }

    var maxDate = new Date();
    maxDate.setFullYear(maxDate.getFullYear() + 20);

    if (expDate > maxDate) {
        licensemodelmsg.innerText = "Invalid expiry date";
        licensemodelmsg.classList.remove("hidden");
        expire_date.classList.add("border-red-500");
        return;
    }

    if (!validateImage(licenseImage, "License Image", licensemodelmsg)) return;

    var form = new FormData();
    form.append("license_exp_date", expire_date.value);
    form.append("licenseImage", licenseImage.files[0]);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.readyState == 4 && request.status == 200) {

            var response = request.responseText;

            if(response !== "success"){
                licensemodelmsg.innerText = response;
                licensemodelmsg.classList.remove("hidden");
            }else{
                licensemodelmsg.classList.add("hidden");
                window.location.reload();
            }
        }
    };

    request.open("POST", "/updatelicense", true);
    request.send(form);
}

function validateImage(fileInput, fieldName, licensemodelmsg) {

    licensemodelmsg.classList.remove("hidden");

    var file = fileInput.files[0];

    if (!file) {
        licensemodelmsg.innerText = fieldName + " is required";
        licensemodelmsg.classList.remove("hidden");

        fileInput.closest(".file-drop").classList.add("border-red-500");
        return false;
    }

    var allowedTypes = ["image/jpeg", "image/png", "image/jpg"];

    if (!allowedTypes.includes(file.type)) {
        licensemodelmsg.innerText = fieldName + " must be JPG or PNG";
        licensemodelmsg.classList.remove("hidden");

        fileInput.closest(".file-drop").classList.add("border-red-500");
        return false;
    }

    if (file.size > 2 * 1024 * 1024) {
        licensemodelmsg.innerText = fieldName + " must be less than 2MB";
        licensemodelmsg.classList.remove("hidden");

        fileInput.closest(".file-drop").classList.add("border-red-500");
        return false;
    }

    fileInput.closest(".file-drop").classList.remove("border-red-500");
    return true;
}

function resetErrors(){
    licensemodelmsg.innerText="";
    licensemodelmsg.classList.add("hidden");

    document.querySelectorAll(".file-drop").forEach(function(el){
        el.classList.remove("border-red-500");
    })

    document.querySelectorAll("input,select").forEach(function(el){
        el.classList.remove("border-red-500");
    })
}