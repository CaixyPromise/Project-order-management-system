import {createStyles} from "antd-style";

const useStyles = createStyles(({ token }) => ({
    card: {
        marginBottom: '24px',
        '.ant-legacy-form-item .ant-legacy-form-item-control-wrapper': {
            width: '100%',
        },
    },
    editor: {
        border: "1px solid #ccc",
        borderRadius: "6px",
    }
}));

export default useStyles;