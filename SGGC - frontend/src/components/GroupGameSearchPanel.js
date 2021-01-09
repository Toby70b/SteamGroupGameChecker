import React, {useState} from "react";
import 'antd/dist/antd.css';
import {Button, Row, Card, Table, Popconfirm, Typography, Checkbox } from 'antd';
import WrappedHorizontalForm from "./WrappedHorizontalForm";
import ErrorList from "./ErrorList";
const { Text } = Typography;



function GroupGameSearchPanel(props) {
    const [dataSource, setDataSource] = useState([]);

    const columns =  [
        {
            title: 'Steam Id',
            dataIndex: 'id',
            key: 'id',
        },

        {
            title: 'Action',
            key: 'action',
            render: (text, record) =>
                dataSource.length >= 1 ? (
                    <Popconfirm title="Are you sure?" onConfirm={() => this.handleDelete(record.id)}>
                        <a>Delete</a>
                    </Popconfirm>
                ) : null,
        },
    ]

    const collectSteamIds = () =>{
        var steamIds = [];
        dataSource.forEach(function (item) {
            steamIds.push(item.id);
        });
        return steamIds
    }

    const handleSearch = () =>{
        props.onSearch(collectSteamIds())
    }

    const handleDelete = id => {
        const dataSource = [...dataSource];
        setDataSource(dataSource.filter(item => item.id !== id) );
    };

    const handleAdd = (value) =>{
        const newList = dataSource.concat({id:value});
        setDataSource(newList);
    }

    const validateSteamId = (rule, value, callback) => {
        const {form} = props;
        if (value) {
            if (value.length !== 17 || !/^\d+$/.test(value)) {
                callback("Steam Id must be a 17 character number e.g. 76561198045206229");
            }
        }
        callback();
    };


    return(
        <div>
            <Card title={<h2>Search for Common Games</h2>} >
                <Row type="flex" justify="center" style={{marginBottom:16}}>
                    <Text level={2} type="danger">{props.errorMessage}</Text>
                </Row>
                <Row type="flex" justify="center" style={{marginBottom:16}}>
                    <ErrorList errorsArr={props.errors}/>
                </Row>
                <div style={{marginBottom:16}}>
                    <WrappedHorizontalForm
                        placeholder={"Please enter a Steam Id"}
                        label = {"Steam Id:"}
                        onSubmit = {handleAdd}
                        required = {true}
                        requiredMessage = {" A Steam Id is required "}
                        validate = {validateSteamId}
                    >
                    </WrappedHorizontalForm>
                </div>
                <Checkbox>
                    Multiplayer only?
                </Checkbox>
                <Table dataSource={dataSource} columns={columns} rowKey={record => record.id} scroll={{y:300}} pagination={false} style={{marginBottom:18}} />
                <Row type="flex" justify="end">
                    <Button type="primary" icon="search" onClick={handleSearch}>
                        Search
                    </Button>
                </Row>
            </Card>
        </div>
    );

}
export default GroupGameSearchPanel;
