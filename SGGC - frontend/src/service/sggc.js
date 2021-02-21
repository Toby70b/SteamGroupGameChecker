import {fetchFromApi, returnPostOptions} from "./index";

export const SGGC_API_URI = "http://localhost:8080/api/sggc/"


export const getCommonGamesBetweenUsers= (requestObj,onSuccess,onError) =>{
    return fetchFromApi(SGGC_API_URI, returnPostOptions(requestObj), onSuccess, onError);
}
