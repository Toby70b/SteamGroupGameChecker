import React, {useState} from "react";
import 'antd/dist/antd.css';
import {Button, Card, Checkbox, Popconfirm, Row, Table, Typography} from 'antd';
import WrappedHorizontalForm from "./WrappedHorizontalForm";
import ErrorList from "./ErrorList";

const { Text } = Typography;



function GroupGameSearchPanel(props) {
    const [dataSource, setDataSource] = useState([]);
    const [multiplayerOnly, setMultiplayerOnly]  = useState(false);

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
                    <Popconfirm title="Are you sure?" onConfirm={() => handleDelete(record.id)}>
                        <a>Delete</a>
                    </Popconfirm>
                ) : null,
        },
    ]

    const collectFormData = () =>{
        let request = {};
        request.steamIds = getSteamIdsForRequest()
        request.multiplayerOnly = multiplayerOnly;
        return request;
    }

    const getSteamIdsForRequest = () =>{
        var steamIds = [];
        dataSource.forEach(function (item) {
            steamIds.push(item.id);
        });
        return steamIds
    }

    const handleSearch = () =>{
        props.onSearch(collectFormData())
    }

    const handleDelete = id => {
        let newDataSource = [...dataSource];
        setDataSource(newDataSource.filter(item => item.id !== id) );
    };

    const handleAdd = (value) =>{
        const newList = dataSource.concat({id:value});
        setDataSource(newList);
    }

    const validateSteamId = (rule, value, callback) => {
        if (value) {
            if (value.length !== 17 || !/^\d+$/.test(value)) {
                callback("Steam Id must be a 17 character number e.g. 76561198045206229");
            }
        }
        callback();
    };

    function onCheck(e) {
        setMultiplayerOnly(e.target.checked)
    }


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

                <Table dataSource={dataSource} columns={columns} rowKey={record => record.id} scroll={{y:300}} pagination={false} style={{marginBottom:18}} />
                <Row type="flex" justify="end">
                    <Checkbox onChange={onCheck}>
                        Multiplayer only?
                    </Checkbox>
                    <Button type="primary" icon="search" onClick={handleSearch}>
                        Search
                    </Button>
                </Row>
            </Card>
        </div>
    );

}
export default GroupGameSearchPanel;
