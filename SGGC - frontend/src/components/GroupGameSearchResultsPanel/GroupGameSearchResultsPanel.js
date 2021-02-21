import React, {useState} from "react";
import {Button, Card, Input, Space, Table} from "antd";
import {HEADER_IMAGE_FILE_NAME, STEAM_MEDIA_REPO, STEAM_URL} from "../../util/Constants";
import Highlighter from 'react-highlight-words';
import {SearchOutlined} from '@ant-design/icons';
import './GroupGameSearchResultsPanel.css'

function GroupGameSearchResultsPanel(props) {
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    let searchInput;
    const getColumnSearchProps = dataIndex => ({
        filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
            <div style={{padding: 8}}>
                <Input
                    ref={node => {
                        searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    className={"tableFilterInput"}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined/>}
                        size="small"
                        className={"tableFilterButton"}
                    >
                        Search
                    </Button>
                    <Button onClick={() => handleReset(clearFilters)} size="small" className={"tableFilterButton"}>
                        Reset
                    </Button>
                    <Button
                        type="link"
                        size="small"
                        onClick={() => {
                            confirm({closeDropdown: false});
                            setSearchText(selectedKeys[0])
                            setSearchedColumn(dataIndex)
                        }}
                    >
                        Filter
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: filtered => <SearchOutlined style={{color: filtered ? '#1890ff' : undefined}}/>,
        onFilter: (value, record) =>
            record[dataIndex]
                ? record[dataIndex].toString().toLowerCase().includes(value.toLowerCase())
                : '',
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => searchInput.select(), 100);
            }
        },
        render: text =>
            searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{backgroundColor: '#ffc069', padding: 0}}
                    searchWords={[searchText]}
                    autoEscape
                    textToHighlight={text ? text.toString() : ''}
                />
            ) : (
                text
            ),
    });

    const columns = [
        {
            title: '',
            key: 'appid',
            render: (record) => <img className={"gameThumbnail"} alt={"Steam App Thumbnail"}
                                     src={STEAM_MEDIA_REPO + record.appid + "/" + HEADER_IMAGE_FILE_NAME}/>
        },
        {
            title: 'Name',
            key: 'name',
            ...getColumnSearchProps('name'),
            render: (record) => (<a href={STEAM_URL + record.appid + "/"}>{record.name}</a>)
        },
    ]

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0])
        setSearchedColumn(dataIndex)
    };

    const handleReset = clearFilters => {
        clearFilters();
        setSearchText('')
    };

    return (
        <div>
            <Card title={<h2>Search Results</h2>} loading={props.isLoading}
                  className={"boxShadow"}>
                {!props.isLoading &&
                <Table
                    dataSource={props.dataSource}
                    columns={columns}
                    rowKey={record => record.appid}
                    scroll={{y: 400}}
                    pagination={true}
                />
                }
            </Card>
        </div>
    );

}

export default GroupGameSearchResultsPanel



