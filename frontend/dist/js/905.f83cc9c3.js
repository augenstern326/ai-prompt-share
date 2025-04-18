"use strict";(self["webpackChunkfrontend"]=self["webpackChunkfrontend"]||[]).push([[905],{723:function(e,a,t){t.d(a,{A:function(){return d}});t(8111),t(2489),t(7588);var l=t(6768),u={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M909.1 209.3l-56.4 44.1C775.8 155.1 656.2 92 521.9 92 290 92 102.3 279.5 102 511.5 101.7 743.7 289.8 932 521.9 932c181.3 0 335.8-115 394.6-276.1 1.5-4.2-.7-8.9-4.9-10.3l-56.7-19.5a8 8 0 00-10.1 4.8c-1.8 5-3.8 10-5.9 14.9-17.3 41-42.1 77.8-73.7 109.4A344.77 344.77 0 01655.9 829c-42.3 17.9-87.4 27-133.8 27-46.5 0-91.5-9.1-133.8-27A341.5 341.5 0 01279 755.2a342.16 342.16 0 01-73.7-109.4c-17.9-42.4-27-87.4-27-133.9s9.1-91.5 27-133.9c17.3-41 42.1-77.8 73.7-109.4 31.6-31.6 68.4-56.4 109.3-73.8 42.3-17.9 87.4-27 133.8-27 46.5 0 91.5 9.1 133.8 27a341.5 341.5 0 01109.3 73.8c9.9 9.9 19.2 20.4 27.8 31.4l-60.2 47a8 8 0 003 14.1l175.6 43c5 1.2 9.9-2.6 9.9-7.7l.8-180.9c-.1-6.6-7.8-10.3-13-6.2z"}}]},name:"reload",theme:"outlined"},r=u,n=t(7526);function s(e){for(var a=1;a<arguments.length;a++){var t=null!=arguments[a]?Object(arguments[a]):{},l=Object.keys(t);"function"===typeof Object.getOwnPropertySymbols&&(l=l.concat(Object.getOwnPropertySymbols(t).filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable})))),l.forEach((function(a){i(e,a,t[a])}))}return e}function i(e,a,t){return a in e?Object.defineProperty(e,a,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[a]=t,e}var o=function(e,a){var t=s({},e,a.attrs);return(0,l.bF)(n.A,s({},t,{icon:r}),null)};o.displayName="ReloadOutlined",o.inheritAttrs=!1;var d=o},4905:function(e,a,t){t.r(a),t.d(a,{default:function(){return m}});var l=t(6768),u=t(144),r=t(4232),n=t(7694),s=t(723),i=t(3397);const o={class:"admin-user"},d={class:"filter-section"},c={key:0,class:"user-info"},v={key:3,class:"action-buttons"};var k={__name:"AdminUser",setup(e){const a=[{title:"ID",dataIndex:"id",key:"id",width:80},{title:"用户信息",key:"user"},{title:"邮箱",dataIndex:"email",key:"email"},{title:"角色",key:"role",width:100},{title:"状态",key:"status",width:100},{title:"注册时间",dataIndex:"createTime",key:"createTime",width:180},{title:"操作",key:"action",width:300}],t=(0,u.KR)(!1),k=(0,u.KR)([]),y=(0,u.KR)(""),f=(0,u.KR)(-1),m=(0,u.KR)(-1),b=(0,u.Kh)({current:1,pageSize:10,total:0,showSizeChanger:!0,showQuickJumper:!0}),p=(0,u.KR)(!1),g=(0,u.KR)(!1),_=(0,u.Kh)({id:null,username:"",email:"",role:"user",status:1}),h=async()=>{t.value=!0;try{const e=await i["default"].admin.getUserList({current:b.current,size:b.pageSize,keyword:y.value||void 0,role:f.value>=0?1===f.value?"admin":"user":void 0,status:m.value>=0?m.value:void 0});200===e.code&&(k.value=e.data.records,b.total=e.data.total)}catch(e){console.error("获取用户列表失败",e),n.Ay.error("获取用户列表失败")}finally{t.value=!1}},F=()=>{b.current=1,h()},w=()=>{b.current=1,h()},A=()=>{b.current=1,h()},W=()=>{h()},C=e=>{b.current=e.current,b.pageSize=e.pageSize,h()},U=e=>{_.id=e.id,_.username=e.username,_.email=e.email,_.role=e.role,_.status=e.status,p.value=!0},z=async()=>{if(_.email){g.value=!0;try{const e=await i["default"].admin.updateUser({id:_.id,email:_.email,role:_.role,status:_.status});200===e.code?(n.Ay.success("更新成功"),p.value=!1,h()):n.Ay.error(e.message||"更新失败")}catch(e){n.Ay.error("更新失败，请稍后重试")}finally{g.value=!1}}else n.Ay.warning("请填写邮箱")},O=async e=>{try{const a=await i["default"].admin.updateUserStatus(e.id,0);200===a.code?(n.Ay.success("用户已禁用"),h()):n.Ay.error(a.message||"操作失败")}catch(a){n.Ay.error("操作失败，请稍后重试")}},R=async e=>{try{const a=await i["default"].admin.updateUserStatus(e.id,1);200===a.code?(n.Ay.success("用户已启用"),h()):n.Ay.error(a.message||"操作失败")}catch(a){n.Ay.error("操作失败，请稍后重试")}},S=async e=>{try{const a=await i["default"].admin.updateUserRole(e.id,"admin");200===a.code?(n.Ay.success("已设为管理员"),h()):n.Ay.error(a.message||"操作失败")}catch(a){n.Ay.error("操作失败，请稍后重试")}},K=async e=>{try{const a=await i["default"].admin.updateUserRole(e.id,"user");200===a.code?(n.Ay.success("已取消管理员权限"),h()):n.Ay.error(a.message||"操作失败")}catch(a){n.Ay.error("操作失败，请稍后重试")}};return(0,l.sV)((()=>{h()})),(e,n)=>{const i=(0,l.g2)("a-input-search"),h=(0,l.g2)("a-col"),X=(0,l.g2)("a-select-option"),j=(0,l.g2)("a-select"),L=(0,l.g2)("a-button"),I=(0,l.g2)("a-row"),Q=(0,l.g2)("a-avatar"),x=(0,l.g2)("a-tag"),E=(0,l.g2)("a-table"),P=(0,l.g2)("a-input"),D=(0,l.g2)("a-form-item"),T=(0,l.g2)("a-form"),q=(0,l.g2)("a-modal");return(0,l.uX)(),(0,l.CE)("div",o,[n[24]||(n[24]=(0,l.Lk)("h1",null,"用户管理",-1)),(0,l.Lk)("div",d,[(0,l.bF)(I,{gutter:16},{default:(0,l.k6)((()=>[(0,l.bF)(h,{span:8},{default:(0,l.k6)((()=>[(0,l.bF)(i,{value:y.value,"onUpdate:value":n[0]||(n[0]=e=>y.value=e),placeholder:"搜索用户名或邮箱","enter-button":"",onSearch:F},null,8,["value"])])),_:1}),(0,l.bF)(h,{span:4},{default:(0,l.k6)((()=>[(0,l.bF)(j,{value:f.value,"onUpdate:value":n[1]||(n[1]=e=>f.value=e),style:{width:"100%"},onChange:w},{default:(0,l.k6)((()=>[(0,l.bF)(X,{value:-1},{default:(0,l.k6)((()=>n[8]||(n[8]=[(0,l.eW)("全部角色")]))),_:1}),(0,l.bF)(X,{value:0},{default:(0,l.k6)((()=>n[9]||(n[9]=[(0,l.eW)("普通用户")]))),_:1}),(0,l.bF)(X,{value:1},{default:(0,l.k6)((()=>n[10]||(n[10]=[(0,l.eW)("管理员")]))),_:1})])),_:1},8,["value"])])),_:1}),(0,l.bF)(h,{span:4},{default:(0,l.k6)((()=>[(0,l.bF)(j,{value:m.value,"onUpdate:value":n[2]||(n[2]=e=>m.value=e),style:{width:"100%"},onChange:A},{default:(0,l.k6)((()=>[(0,l.bF)(X,{value:-1},{default:(0,l.k6)((()=>n[11]||(n[11]=[(0,l.eW)("全部状态")]))),_:1}),(0,l.bF)(X,{value:1},{default:(0,l.k6)((()=>n[12]||(n[12]=[(0,l.eW)("正常")]))),_:1}),(0,l.bF)(X,{value:0},{default:(0,l.k6)((()=>n[13]||(n[13]=[(0,l.eW)("已禁用")]))),_:1})])),_:1},8,["value"])])),_:1}),(0,l.bF)(h,{span:4},{default:(0,l.k6)((()=>[(0,l.bF)(L,{type:"primary",onClick:W},{icon:(0,l.k6)((()=>[(0,l.bF)((0,u.R1)(s.A))])),default:(0,l.k6)((()=>[n[14]||(n[14]=(0,l.eW)(" 刷新 "))])),_:1})])),_:1})])),_:1})]),(0,l.bF)(E,{columns:a,"data-source":k.value,loading:t.value,pagination:b,onChange:C,"row-key":"id"},{bodyCell:(0,l.k6)((({column:e,record:a})=>["user"===e.key?((0,l.uX)(),(0,l.CE)("div",c,[(0,l.bF)(Q,{src:a.avatar||"/avatar-default.png"},null,8,["src"]),(0,l.Lk)("span",null,(0,r.v_)(a.username),1)])):(0,l.Q3)("",!0),"role"===e.key?((0,l.uX)(),(0,l.Wv)(x,{key:1,color:"admin"===a.role?"purple":"blue"},{default:(0,l.k6)((()=>[(0,l.eW)((0,r.v_)("admin"===a.role?"管理员":"普通用户"),1)])),_:2},1032,["color"])):(0,l.Q3)("",!0),"status"===e.key?((0,l.uX)(),(0,l.Wv)(x,{key:2,color:1===a.status?"green":"red"},{default:(0,l.k6)((()=>[(0,l.eW)((0,r.v_)(1===a.status?"正常":"已禁用"),1)])),_:2},1032,["color"])):(0,l.Q3)("",!0),"action"===e.key?((0,l.uX)(),(0,l.CE)("div",v,[(0,l.bF)(L,{type:"link",size:"small",onClick:e=>U(a)},{default:(0,l.k6)((()=>n[15]||(n[15]=[(0,l.eW)(" 编辑 ")]))),_:2},1032,["onClick"]),1===a.status?((0,l.uX)(),(0,l.Wv)(L,{key:0,type:"link",size:"small",danger:"",onClick:e=>O(a)},{default:(0,l.k6)((()=>n[16]||(n[16]=[(0,l.eW)(" 禁用 ")]))),_:2},1032,["onClick"])):((0,l.uX)(),(0,l.Wv)(L,{key:1,type:"link",size:"small",onClick:e=>R(a)},{default:(0,l.k6)((()=>n[17]||(n[17]=[(0,l.eW)(" 启用 ")]))),_:2},1032,["onClick"])),"admin"!==a.role?((0,l.uX)(),(0,l.Wv)(L,{key:2,type:"link",size:"small",onClick:e=>S(a)},{default:(0,l.k6)((()=>n[18]||(n[18]=[(0,l.eW)(" 设为管理员 ")]))),_:2},1032,["onClick"])):((0,l.uX)(),(0,l.Wv)(L,{key:3,type:"link",size:"small",onClick:e=>K(a)},{default:(0,l.k6)((()=>n[19]||(n[19]=[(0,l.eW)(" 取消管理员 ")]))),_:2},1032,["onClick"]))])):(0,l.Q3)("",!0)])),_:1},8,["data-source","loading","pagination"]),(0,l.bF)(q,{visible:p.value,"onUpdate:visible":n[7]||(n[7]=e=>p.value=e),title:"编辑用户信息",onOk:z,confirmLoading:g.value},{default:(0,l.k6)((()=>[(0,l.bF)(T,{model:_,layout:"vertical"},{default:(0,l.k6)((()=>[(0,l.bF)(D,{label:"用户名",name:"username"},{default:(0,l.k6)((()=>[(0,l.bF)(P,{value:_.username,"onUpdate:value":n[3]||(n[3]=e=>_.username=e),disabled:""},null,8,["value"])])),_:1}),(0,l.bF)(D,{label:"邮箱",name:"email",rules:[{required:!0,type:"email",message:"请输入有效的邮箱地址"}]},{default:(0,l.k6)((()=>[(0,l.bF)(P,{value:_.email,"onUpdate:value":n[4]||(n[4]=e=>_.email=e)},null,8,["value"])])),_:1}),(0,l.bF)(D,{label:"角色",name:"role"},{default:(0,l.k6)((()=>[(0,l.bF)(j,{value:_.role,"onUpdate:value":n[5]||(n[5]=e=>_.role=e)},{default:(0,l.k6)((()=>[(0,l.bF)(X,{value:"user"},{default:(0,l.k6)((()=>n[20]||(n[20]=[(0,l.eW)("普通用户")]))),_:1}),(0,l.bF)(X,{value:"admin"},{default:(0,l.k6)((()=>n[21]||(n[21]=[(0,l.eW)("管理员")]))),_:1})])),_:1},8,["value"])])),_:1}),(0,l.bF)(D,{label:"状态",name:"status"},{default:(0,l.k6)((()=>[(0,l.bF)(j,{value:_.status,"onUpdate:value":n[6]||(n[6]=e=>_.status=e)},{default:(0,l.k6)((()=>[(0,l.bF)(X,{value:1},{default:(0,l.k6)((()=>n[22]||(n[22]=[(0,l.eW)("正常")]))),_:1}),(0,l.bF)(X,{value:0},{default:(0,l.k6)((()=>n[23]||(n[23]=[(0,l.eW)("禁用")]))),_:1})])),_:1},8,["value"])])),_:1})])),_:1},8,["model"])])),_:1},8,["visible","confirmLoading"])])}}},y=t(1241);const f=(0,y.A)(k,[["__scopeId","data-v-5343b50d"]]);var m=f}}]);
//# sourceMappingURL=905.f83cc9c3.js.map