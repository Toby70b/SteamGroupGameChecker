import React, {useState} from "react";
import 'antd/dist/antd.css';
import {Button, Card, Checkbox, Popconfirm, Row, Table, Col, Modal, Alert, Collapse, Form, Input} from 'antd';
import {Fade} from '@material-ui/core';
import {STEAM_ID_NOT_VALID} from "../../util/Constants";
import {Typography} from 'antd';
import {SearchOutlined, PlusOutlined} from '@ant-design/icons';
import './GroupGameSearchPanel.css'
import '../common.css'

const {Panel} = Collapse;
const {Paragraph} = Typography;

function GroupGameSearchPanel(props) {
    const [dataSource, setDataSource] = useState([]);
    const [multiplayerOnly, setMultiplayerOnly] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);

    const columns = [
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

    const validationRules = [
        {
            required: true,
            message: 'Please enter a Steam Id',
        },
        ({getFieldValue}) => ({
            validator(_, value) {
                if (validateSteamId(value)) {
                    return Promise.resolve();
                }
                return Promise.reject(STEAM_ID_NOT_VALID);
            },
        }),
    ]

    const collectFormData = () => {
        let request = {};
        request.steamIds = getSteamIdsForRequest()
        request.multiplayerOnly = multiplayerOnly;
        return request;
    }

    const getSteamIdsForRequest = () => {
        var steamIds = [];
        dataSource.forEach(function (item) {
            steamIds.push(item.id);
        });
        return steamIds
    }

    const handleSearch = () => {
        props.onSearch(collectFormData())
    }

    const handleDelete = id => {
        setDataSource(dataSource.filter(item => item.id !== id));
    };

    const handleAdd = (value) => {
        const newList = dataSource.concat({key: value.steamId + dataSource.length, id: value.steamId});
        setDataSource(newList);
    }

    const handleModalOpen = () => {
        setModalVisible(true)
    }

    const handleCancel = () => {
        setModalVisible(false);
    }

    const validateSteamId = (value) => {
        if (value) {
            return !(value.length !== 17 || !/^\d+$/.test(value));
        }
        return false
    };

    function handleCheck(e) {
        setMultiplayerOnly(e.target.checked)
    }

    function Header() {
        return (
            <Row>
                <Col xs={2} sm={8}>
                    <h2>Search for Common Games</h2>
                </Col>
                <Col xs={24} sm={{span: 3, offset: 13}}>
                    <Button type="primary" onClick={handleModalOpen}>Help</Button>
                </Col>
            </Row>

        );
    }

    return (
        <div>
            <Modal
                title={<h2>FAQ</h2>}
                visible={modalVisible}
                footer={[null]}
                onCancel={handleCancel}
            >
                <Collapse>
                    <Panel header="What is this?" key="1">
                        <Paragraph>
                            This is a tool that, when provided with two or more Steam Id's will return a list of common
                            games.
                        </Paragraph>
                    </Panel>
                    <Panel header="How do I find steam Id's?" key="2">
                        <Paragraph>
                            <ul>
                                <li>
                                    <Paragraph>Go to a friends profile page on Steam</Paragraph>
                                </li>
                                <li>
                                    <Paragraph>Copy the url</Paragraph>
                                    <Paragraph>
                                        <b>Note</b> If you're using the app right click anywhere on the screen
                                        and select "Copy Page URL" from the menu.
                                    </Paragraph>
                                </li>
                                <li>
                                    <Paragraph>Look for the number at the end, this is a Steam Id</Paragraph>
                                    <Paragraph> https://steamcommunity.com/profiles/<b>76561198045206229</b>/</Paragraph>
                                </li>
                                <li>
                                    <Paragraph>If instead of a number its a name, then that's a vanity url.</Paragraph>
                                    <Paragraph>To resolve this into a Steam Id go to the below URL</Paragraph>
                                    <Paragraph>
                                        http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=B88AF6D15A99EF5A4E01075EF63E5DF2&vanityurl=<b>&lt;VanityURL&gt;</b>
                                    </Paragraph>
                                </li>
                            </ul>
                        </Paragraph>
                    </Panel>
                    <Panel header="Why make this?" key="3">
                        <Paragraph>
                            I belong to a group of 5-6 20 something friends whose primary hobby is gaming, as such we
                            all have steam accounts with each of us owning at least 200 games. We all prefer to play
                            games together, we dont mind replaying games, including older games, if it means we can play
                            together. We would occasionally ask what games we have in common, however since steam only
                            allows users to compare their lists with one other user at a time this task was very
                            tedious. I wanted more excuses to practice on REST API's so I researched Steam's API and
                            once I discovered it was feasible to make a tool that could compare multiple users lists for
                            common games I decided to make this tool.
                        </Paragraph>
                    </Panel>
                    <Panel
                        header="I've searched for multiplayer games only, yet some single player games appear. Are you just an idiot?"
                        key="4">
                        <Paragraph>
                            Wellllllllllll.........
                        </Paragraph>
                        <Paragraph>
                            So over time the Steam API loses information on games still owned by users. This can be
                            because its been pulled off steam,
                            or the developer has accidentally created duplicate entries. Because of my requirements (We
                            wanted all possible multiplayer games showed)
                            I decided that in these cases the game should still appear in the list of multiplayer games.
                        </Paragraph>
                    </Panel>
                </Collapse>

            </Modal>

            <Card title={<Header/>} className={"boxShadow"}>
                <Fade in={props.errorMessage !== ''} timeout={{"enter": 1000, "exit": 0}} unmountOnExit={true}>
                    <Alert
                        showIcon
                        type={"error"}
                        message={"Error"}
                        description={props.errorMessage}
                        className={"alert"}
                    />
                </Fade>
                <div className={"marginBottom"}>
                    <div>
                        <Form
                            name="basic"
                            onFinish={handleAdd}
                            layout={"inline"}
                            id={"steamIdForm"}
                        >
                            <Row>
                                <Form.Item
                                    name="steamId"
                                    rules={validationRules}
                                >
                                    <Input className={"marginBottom"} addonBefore={"Steam Id:"}
                                           placeholder="76561198045206229"/>
                                </Form.Item>
                                <Form.Item>
                                    <Button classicon={<PlusOutlined/>} type="primary" htmlType="submit">
                                        Submit
                                    </Button>
                                </Form.Item>
                            </Row>
                        </Form>
                    </div>
                </div>

                <Table dataSource={dataSource} columns={columns} rowKey={record => record.key} scroll={{y: 300}}
                       pagination={false} style={{marginBottom: 18}}/>
                <Row type="flex" justify="end">
                    <Checkbox onChange={handleCheck}>
                        Multiplayer only?
                    </Checkbox>
                    <Button type="primary" icon={<SearchOutlined/>} onClick={handleSearch}>
                        Search
                    </Button>
                </Row>
            </Card>
        </div>
    );

}

export default GroupGameSearchPanel;
