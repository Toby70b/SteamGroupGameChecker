import {fetchFromApi, returnPostOptions} from "./index";

export const SGGC_API_URI = "https://u5m0524nqc.execute-api.eu-west-2.amazonaws.com/Prod/api/sggc"


export const getCommonGamesBetweenUsers= (requestObj,onSuccess,onError) =>{
    return fetchFromApi(SGGC_API_URI, returnPostOptions(requestObj), onSuccess, onError);
}
