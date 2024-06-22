import {ProForm} from '@ant-design/pro-components';
import {Modal} from 'antd';
import React from 'react';

interface Props
{
    oldData?: API.OrderInfoVO;
    visible: boolean;
    onSubmit: (values: API.UserAddRequest) => void;
    onCancel: () => void;
}

/**
 * 更新弹窗
 * @param props
 * @constructor
 */
const UpdateStatusModal: React.FC<Props> = (props) =>
{
    const { oldData, visible,  onSubmit, onCancel } = props;

    if (!oldData)
    {
        return <></>;
    }

    return (
        <Modal
            destroyOnClose
            title={'更新-状态信息'}
            open={visible}
            footer={null}
            onCancel={() =>
            {
                onCancel?.();
            }}
        >
            <ProForm>

            </ProForm>
        </Modal>
    );
};
export default UpdateStatusModal;
