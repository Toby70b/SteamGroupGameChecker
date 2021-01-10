import React from "react";
import  {Card,Table} from "antd";
class GroupGameSearchResultsPanel extends React.Component {
    constructor() {
        super();
        this.columns = [
            {
                title: '',
                key: 'imageUrl',
                render: (record) => <img alt={"Game"} src={"https://steamcdn-a.akamaihd.net/steam/apps/"+record.appid+"/header_292x136.jpg"} height={100} width={200} />
            },
            {
                title: 'Name',
                key: 'name',
                render: (record) => ( <a href={"https://store.steampowered.com/app/"+record.appid+"/"}>{record.name}</a> )
            },
        ]
    }
    render() {
        return(
            <div>
                <Card title={<h2>Search Results</h2>} loading={this.props.isLoading}>
                    <Table dataSource={this.props.dataSource} columns={this.columns} rowKey={record => record.id} scroll={{y:500}} pagination={false} />
                </Card>
            </div>
        );
    }
}

export default GroupGameSearchResultsPanel



