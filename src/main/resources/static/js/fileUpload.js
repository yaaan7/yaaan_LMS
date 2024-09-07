function uploadFile() {
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0];
    var formData = new FormData();
    formData.append('file', file);

    fetch('/upload', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            var fileContainer = document.getElementById('fileContainer');
            var fileBox = document.createElement('div');
            fileBox.className = 'file_box';
            var fileName = document.createElement('p');
            fileName.className = 'file_name';
            fileName.innerText = data.fileName;
            fileBox.appendChild(fileName);
            fileContainer.appendChild(fileBox);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}