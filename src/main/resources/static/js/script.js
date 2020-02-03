(function() {
    const resultContent = document.querySelector(".response-section");
    const fileInput = document.querySelector(".file-input");
    const selectedFiles = document.querySelector(".selected-files");
    const fileForm = document.querySelector(".file-form");
    const fileIdForm = document.querySelector(".fileId-form");
    const resultButton = document.querySelector('.response-btn');
    const deleteButton = document.querySelector(".delete");
    const RESULT_SCRIPT_URL = '/api/v1/script/result';
    let DELETE_SCRIPT_URL = `api/v1/delete/script`;
    const SERVER_URL = "/api/v1/script";
    let filesList = [];

    fileForm.addEventListener("submit", e => {
        e.preventDefault();
        Promise.all(
            filesList.map(file => {
                send(file, SERVER_URL);
            })
        );
        filesList = Object.assign([]);
        selectedFiles.innerHTML = " ";
    });

    fileInput.addEventListener("change", e => {
        filesList.push(e.target.files[0]);
        renderFileList();
    });

    resultButton.addEventListener("click", e => {
        e.preventDefault();
        resultRequest(RESULT_SCRIPT_URL);
    });

    deleteButton.addEventListener("click", e => {
        e.preventDefault();
        const fileId = fileIdForm.elements.fileId.value;
        deleteRequest(DELETE_SCRIPT_URL, fileId);
    });

    const renderFileList = () => {
        selectedFiles.innerHTML = " ";
        filesList.forEach(file => {
            let fileDisplayEl = document.createElement("p");
            fileDisplayEl.innerHTML = file.name;
            selectedFiles.appendChild(fileDisplayEl);
        });
    };

    const renderResponseResult = value => {
        let responseElement = document.createElement("p");
        responseElement.innerHTML = value;
        resultContent.appendChild(responseElement);
    };

    const send = (file, url) => {
        readFile(file, url);
    };

    const resultRequest = url => {
        fetch(url, { method: "GET" })
            .then(result => result.text())
            .then(value => renderResponseResult(value));
    };

    const deleteRequest = (url, fileId) => {
        fetch(`/${url}/${fileId}`, { method: "GET" })
            .then(result => result.text())
            .then(value => renderResponseResult(value));
    };

    const readFile = (inputFile, url) => {
        const file = inputFile;
        const reader = new FileReader();
        reader.readAsText(file);
        reader.onload = () => {
            fetch(url, {
                method: "POST",
                body: reader.result
            })
                .then(response => fetch(response.headers.get("Location")))
                .then(response => response.status === 202
                    ? response.text().then(value => renderResponseResult(value))
                    : fetch(response.headers.get("Location")))
                .then(result => result.text())
                .then(value => renderResponseResult(value));
        };
        reader.onerror = function() {
            alert(reader.error);
        };
    };
})();
