import React from 'react';
import 'antd/dist/antd.css';
import { Form, Icon, Input, Button } from 'antd';


function hasErrors(fieldsError) {
    return Object.keys(fieldsError).some(field => fieldsError[field]);
}

class HorizontalForm extends React.Component {
    componentDidMount() {
        this.props.form.validateFields();
    }

    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.props.onSubmit(values.input)
            }
        });
    };


    render() {
        const { getFieldDecorator, getFieldsError, getFieldError, isFieldTouched } = this.props.form;
        // Only show error after a field is touched.
        const inputError = isFieldTouched('input') && getFieldError('input');
        return (
            <Form layout="inline" onSubmit={this.handleSubmit}>
                <Form.Item wrapperCol={{ sm: 24 }} style={{ width: "60%"}} validateStatus={inputError ? 'error' : ''} help={inputError || ''}>
                    {getFieldDecorator('input', {
                        rules: [
                                {required: true , message: this.props.requiredMessage },
                                {validator: this.props.validate}
                               ]
                    })(<Input placeholder={this.props.placeholder} addonBefore={this.props.label} style={{ width: '100%' }} />)}
                </Form.Item>

                <Form.Item >
                    <Button icon="plus" type="primary" htmlType="submit" disabled={hasErrors(getFieldsError())}>
                        Submit
                    </Button>
                </Form.Item>
            </Form>
        );
    }
}

const WrappedHorizontalForm = Form.create({ name: 'horizontal_form' })(HorizontalForm);

export default WrappedHorizontalForm;


