const handleResponse = (response) => {
    if (!response.ok) {
        throw response;
    }
    return response;
}

export const fetchFromApi = (url, options, onSuccess, onError) => {
    return fetch(url, options)
        .then(handleResponse)
        .then((response) => response.json())
        .then((jsonResponse) => onSuccess(jsonResponse))
        .catch(err => {
            if (err.json) {
                err.json().then(errorJson => {
                    onError(errorJson)
                })
            }
        })
}

export const returnPostOptions = (data) => {
    return returnCommonOptions(data, 'POST');
}

const returnCommonOptions = (data, method) => {
    return {
        method: method,
        headers: {'Content-Type': 'application/json;'},
        body: JSON.stringify(data),
    };
}

