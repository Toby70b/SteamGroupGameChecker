import React from "react";
import {Typography,Row,Col} from "antd";
import GroupGameSearchPanel from "./GroupGameSearchPanel";
import GroupGameSearchResultsPanel from "./GroupGameSearchResultsPanel";

const { Title } = Typography;
const notEnoughIdsEnteredErrorMsg = "Please enter at least 2 Steam Id's"
const fetchErrorMessage = "The following errors occured:"


class GroupGameSearchPage extends React.Component {
    constructor() {
        super();

        this.state = {
            resultsDataSource: [],
            displayResults: false,
            resultsLoading: false,
            errorMessage: '',
            errors:[]
        }
    }

    addErrorsToErrorList = (error) =>{
        var errors = []
        error.errors.forEach(function(error) { errors.push(error.message); });
        this.setState({
            errors:[...errors]
        });
    }

    onFetchError = (error) =>{
        this.addErrorsToErrorList(error.error);
        this.setState({displayResults:false, resultsLoading:false, errorMessage:fetchErrorMessage});
    }

    async fetchFromApi (url, options)  {
        const response = await fetch(url,options);
        const json = await response.json();
        return response.ok ? json : Promise.reject(json);
    }

    handleSearch = (steamIds) => {
        if(steamIds.length >=2) {
            var data = {"steamIds": steamIds}
            var options = {
                method: 'POST',
                headers: {'Content-Type': 'application/json;'},
                body: JSON.stringify(data)
            }
            this.setState({displayResults: true, resultsLoading: true, errorMessage: '',errors:[]})
            this.fetchFromApi('http://localhost:8080/api/sggc/', options)
                .then((jsonResponse) => {this.setState({resultsDataSource: jsonResponse, resultsLoading: false})})
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
                     <Col xs={24} lg={12}>
                            <GroupGameSearchPanel
                            onSearch = {this.handleSearch}
                            errorMessage ={this.state.errorMessage}
                            errors={this.state.errors}>
                            </GroupGameSearchPanel>
                    </Col>
                </Row>
                <Row type="flex" justify="center" style={{marginBottom:16}}>
                    <Col xs={24} lg={12}>
                        { this.state.displayResults ?
                            <GroupGameSearchResultsPanel
                                isLoading={this.state.resultsLoading}
                                dataSource={this.state.resultsDataSource}
                            >
                            </GroupGameSearchResultsPanel> : null }
                    </Col>
                </Row>
            </div>
        );
    }
}

export default GroupGameSearchPage
