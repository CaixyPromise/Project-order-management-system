export default [
  { path: '/user', layout: false, routes: [{ path: '/user/login', component: './User/Login' }] },
  { path: '/welcome', icon: 'smile', component: './Welcome', name: "欢迎页" },
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
