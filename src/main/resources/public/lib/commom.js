function replaceArray(oldAarray, newArray) {
    oldAarray.splice(0, oldAarray.length, ...newArray);
}

function deleteElement(array, e) {
    const index = array.indexOf(e);
    if (index >= 0)
        array.splice(index, 1);
}

function loadEntity(url, entities) {
    console.log(url);
    axios.get(url)
        .then(response => replaceArray(entities, response.data))
        .catch(reason => {
            console.log(reason);
        })
}

function deleteEntity(url, entities, entity) {
    axios.delete(url)
        .then((response) => {
            if (response.data.status === 200) {
                deleteElement(entities, entity);
            } else {
                alert("无法删除，已被其他对象引用");
            }
        })
        .catch(reason => {
            console.log(reason);
        });
}

function addEntity(url, entity, entyties, modal) {
    axios.post(url, entity)
        .then(response => {
            if (response.data.status === 200) {
                modal.modal('hide');
                entyties.push(response.data.data);
            } else {
                alert(response.data.msg);
            }
        })
        .catch(reason => {
            console.log(reason);
        });
}

