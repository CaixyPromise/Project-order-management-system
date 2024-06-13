export default [
    { path: '/user', layout: false, routes: [ { path: '/user/login', component: './User/Login' } ] },
    { path: '/welcome', icon: 'smile', component: './Welcome', name: "欢迎页" },
    { path: '/orderList', icon: 'smile', component: './OrderList', name: "订单列表" },
    {
        path: '/addOrder',
        name: '添加订单',
        icon: 'Form',
        component: './OrderForm',  // 假设你的组件名为 OrderForm
        routes: [
            {
                path: '/addOrder/new',  // 添加订单的路由
                exact: true,
                component: './OrderForm',
            },
            {
                path: '/addOrder/:id',  // 更新订单的路由
                exact: true,
                component: './OrderForm',
                hideInMenu: true,  // 更新的页面不显示在菜单中
            },
            {
                path: '/addOrder/',  // 默认路由，也用于添加，重定向到 '/addOrder/new'
                exact: true,
                redirect: '/addOrder/new',
            },
        ],
    },

    {
        path: '/admin',
        icon: 'crown',
        name: "管理页",
        access: 'canAdmin',
        routes: [
            { path: '/admin', redirect: '/admin/user' },
            { icon: 'table', path: '/admin/user', component: './Admin/User', name: "用户管理" },
            { icon: 'table', path: '/admin/lang', component: './Admin/Lang', name: "编程语言管理" },
            { icon: 'table', path: '/admin/category', component: './Admin/Category', name: "订单分类管理" },
        ],
    },
    { path: "/center", icon: "user", component: "./User/Settings", name: "个人中心" },
    { path: '/', redirect: '/welcome' },
    { path: '*', layout: false, component: './404' },
];
