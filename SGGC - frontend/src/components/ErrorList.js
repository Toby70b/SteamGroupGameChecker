import React from "react";
import {Typography} from 'antd';

const { Text } = Typography;

function ErrorList(props) {
    const listItems = props.errorsArr.map((error,i) =>
        <li key={i}><Text type="danger">{error}</Text></li>
    );
    return (
        <ul>{listItems}</ul>
    );
}

export default ErrorList
