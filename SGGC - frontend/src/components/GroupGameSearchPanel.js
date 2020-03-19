import React from "react";
import 'antd/dist/antd.css';
import { Input, Button, Row, Card, Table, Popconfirm, Typography, Form } from 'antd';
import WrappedHorizontalForm from "./WrappedHorizontalForm";

const { Search } = Input;
const { Text } = Typography;




class GroupGameSearchPanel extends React.Component {
    constructor() {
        super();
        this.columns = [
            {
                title: 'Steam Id',
                dataIndex: 'id',
                key: 'id',
            },

            {
                title: 'Action',
                key: 'action',
                render: (text, record) =>
                    this.state.dataSource.length >= 1 ? (
                        <Popconfirm title="Are you sure?" onConfirm={() => this.handleDelete(record.id)}>
                            <a>Delete</a>
                        </Popconfirm>
                    ) : null,
            },
        ]

        this.state = {
            dataSource: [
            ],
        }
    }

    collectSteamIds = () =>{
        var steamIds = [];
        this.state.dataSource.forEach(function (item) {
            steamIds.push(item.id);
        });
        return steamIds
    }

    handleSearch = () =>{
        this.props.onSearch(this.collectSteamIds())
    }

    handleDelete = id => {
        const dataSource = [...this.state.dataSource];
        this.setState({ dataSource: dataSource.filter(item => item.id !== id) });
    };

    handleAdd = (value) =>{
        const { count, dataSource } = this.state;
        const newData = {
           id:value
        };
        this.setState({
            dataSource: [...dataSource, newData],
        });
    }

    validateSteamId = (rule, value, callback) => {
        const {form} = this.props;
        if (value) {
            if (value.length !== 17 || !/^\d+$/.test(value)) {
                callback("Steam Id must be a 17 character number e.g. 76561198045206229");
            }
        }
        callback();
    };


    render() {
        return(
        <div>
            <Card title={<h2>Search for Common Games</h2>}  style={{ width: 1000 }}>
                <Row type="flex" justify="center" style={{marginBottom:16}}>
                    <Text level={2} type="danger">{this.props.errorMessage}</Text>
                </Row>
                <div style={{marginBottom:16}}>
                    <WrappedHorizontalForm
                        placeholder={"Please enter a Steam Id"}
                        label = {"Steam Id:"}
                        onSubmit = {this.handleAdd}
                        required = {true}
                        requiredMessage = {" A Steam Id is required "}
                        validate = {this.validateSteamId}
                    >
                    </WrappedHorizontalForm>
                </div>
                <Table dataSource={this.state.dataSource} columns={this.columns} rowKey={record => record.id} scroll={{y:300}} pagination={false} style={{marginBottom:18}} />
                <Row type="flex" justify="end">
                    <Button type="primary" icon="search" onClick={this.handleSearch}>
                        Search
                    </Button>
                </Row>
            </Card>
        </div>
        );
    }
}
export default GroupGameSearchPanel;
