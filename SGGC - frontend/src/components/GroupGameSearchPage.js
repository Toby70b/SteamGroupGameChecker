import React from "react";
import {Typography,Row} from "antd";
import GroupGameSearchPanel from "./GroupGameSearchPanel";
import GroupGameSearchResultsPanel from "./GroupGameSearchResultsPanel";

const { Title } = Typography;
const notEnoughIdsEnteredErrorMsg = "Please enter at least 2 Steam Id's"
const fetchErrorMessage = "There was an error, please try again..."

class GroupGameSearchPage extends React.Component {
    constructor() {
        super();

        this.state = {
            resultsDataSource: [],
            displayResults: false,
            resultsLoading: false,
            errorMessage: ''
        }
    }

    handleErrors = (response) => {
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response;
    }

    onFetchError = (error) =>{
        console.log(error);
        this.setState({displayResults:false, resultsLoading:false, errorMessage:fetchErrorMessage});
    }

    handleSearch = (steamIds) => {
        if(steamIds.length >=2) {
            var data = {"steamIds": steamIds}
            var options = {
                method: 'POST',
                headers: {'Content-Type': 'application/json;'},
                body: JSON.stringify(data)
            }
            this.setState({displayResults: true, resultsLoading: true, errorMessage: '',})
            fetch('http://localhost:8080/api/sggc/', options)
                .then(this.handleErrors)
                .then(response => {
                    return response.json()
                })
                .then((jsonResponse) => this.setState({resultsDataSource: jsonResponse, resultsLoading: false}))
                .catch(error => this.onFetchError(error));
        }
        else {
            this.setState({ errorMessage:notEnoughIdsEnteredErrorMsg});
        }
    }

    render() {
        return(
            <div>
                <Row type="flex" justify="center">
                    <Title>Steam Group Game Checker</Title>
                </Row>

                <Row type="flex" justify="center" style={{marginBottom:16}}>
                    <GroupGameSearchPanel
                    onSearch = {this.handleSearch}
                    errorMessage ={this.state.errorMessage}>
                    </GroupGameSearchPanel>
                </Row>

                <Row type="flex" justify="center" style={{marginBottom:16}}>
                    { this.state.displayResults ?
                        <GroupGameSearchResultsPanel
                            isLoading={this.state.resultsLoading}
                            dataSource={this.state.resultsDataSource}
                        >
                        </GroupGameSearchResultsPanel> : null }
                </Row>
            </div>
        );
    }
}

export default GroupGameSearchPage
