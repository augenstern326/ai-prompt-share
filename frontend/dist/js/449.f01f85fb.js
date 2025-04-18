"use strict";(self["webpackChunkfrontend"]=self["webpackChunkfrontend"]||[]).push([[449],{723:function(e,t,a){a.d(t,{A:function(){return d}});a(8111),a(2489),a(7588);var l=a(6768),n={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M909.1 209.3l-56.4 44.1C775.8 155.1 656.2 92 521.9 92 290 92 102.3 279.5 102 511.5 101.7 743.7 289.8 932 521.9 932c181.3 0 335.8-115 394.6-276.1 1.5-4.2-.7-8.9-4.9-10.3l-56.7-19.5a8 8 0 00-10.1 4.8c-1.8 5-3.8 10-5.9 14.9-17.3 41-42.1 77.8-73.7 109.4A344.77 344.77 0 01655.9 829c-42.3 17.9-87.4 27-133.8 27-46.5 0-91.5-9.1-133.8-27A341.5 341.5 0 01279 755.2a342.16 342.16 0 01-73.7-109.4c-17.9-42.4-27-87.4-27-133.9s9.1-91.5 27-133.9c17.3-41 42.1-77.8 73.7-109.4 31.6-31.6 68.4-56.4 109.3-73.8 42.3-17.9 87.4-27 133.8-27 46.5 0 91.5 9.1 133.8 27a341.5 341.5 0 01109.3 73.8c9.9 9.9 19.2 20.4 27.8 31.4l-60.2 47a8 8 0 003 14.1l175.6 43c5 1.2 9.9-2.6 9.9-7.7l.8-180.9c-.1-6.6-7.8-10.3-13-6.2z"}}]},name:"reload",theme:"outlined"},u=n,r=a(7526);function o(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?Object(arguments[t]):{},l=Object.keys(a);"function"===typeof Object.getOwnPropertySymbols&&(l=l.concat(Object.getOwnPropertySymbols(a).filter((function(e){return Object.getOwnPropertyDescriptor(a,e).enumerable})))),l.forEach((function(t){i(e,t,a[t])}))}return e}function i(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}var s=function(e,t){var a=o({},e,t.attrs);return(0,l.bF)(r.A,o({},a,{icon:u}),null)};s.displayName="ReloadOutlined",s.inheritAttrs=!1;var d=s},1449:function(e,t,a){a.r(t),a.d(t,{default:function(){return y}});a(8111),a(1701);var l=a(6768),n=a(144),u=a(4232),r=a(7694),o=a(723),i=a(3397);const s={class:"admin-prompt"},d={class:"filter-section"},c={key:2,class:"user-info"},v={key:4,class:"action-buttons"};var k={__name:"AdminPrompt",setup(e){const t=[{title:"ID",dataIndex:"id",key:"id",width:80},{title:"标题",dataIndex:"title",key:"title"},{title:"标签",key:"tags"},{title:"发布者",key:"user"},{title:"点赞数",dataIndex:"likeCount",key:"likeCount",width:100},{title:"收藏数",dataIndex:"favoriteCount",key:"favoriteCount",width:100},{title:"状态",key:"status",width:100},{title:"创建时间",dataIndex:"createTime",key:"createTime",width:180},{title:"操作",key:"action",width:150}],a=(0,n.KR)(!1),k=(0,n.KR)([]),g=(0,n.KR)([]),p=(0,n.KR)(""),y=(0,n.KR)([]),f=(0,n.KR)(-1),m=(0,n.Kh)({current:1,pageSize:10,total:0,showSizeChanger:!0,showQuickJumper:!0}),b=(0,n.KR)(!1),h=(0,n.KR)(!1),_=(0,n.Kh)({id:null,title:"",content:"",tagIds:[]}),w=async()=>{try{const e=await i["default"].tag.getTagList();200===e.code&&(g.value=e.data.map((e=>({label:e.name,value:e.id}))))}catch(e){console.error("获取标签列表失败",e)}},F=async()=>{a.value=!0;try{const e=await i["default"].admin.getPromptList({current:m.current,size:m.pageSize,keyword:p.value||void 0,tagIds:y.value.length>0?y.value:void 0,status:f.value>=0?f.value:void 0});200===e.code&&(k.value=e.data.records,m.total=e.data.total)}catch(e){console.error("获取提示词列表失败",e),r.Ay.error("获取提示词列表失败")}finally{a.value=!1}},C=()=>{m.current=1,F()},A=()=>{m.current=1,F()},I=()=>{m.current=1,F()},W=()=>{F()},z=e=>{m.current=e.current,m.pageSize=e.pageSize,F()},K=e=>{_.id=e.id,_.title=e.title,_.content=e.content,_.tagIds=e.tags.map((e=>e.id)),b.value=!0},O=async()=>{if(_.title&&_.content&&0!==_.tagIds.length){h.value=!0;try{const e=await i["default"].prompt.updatePrompt({id:_.id,title:_.title,content:_.content,tagIds:_.tagIds});200===e.code?(r.Ay.success("更新成功"),b.value=!1,F()):r.Ay.error(e.message||"更新失败")}catch(e){r.Ay.error("更新失败，请稍后重试")}finally{h.value=!1}}else r.Ay.warning("请填写完整信息")},R=async e=>{try{const t=await i["default"].prompt.deletePrompt(e.id);200===t.code?(r.Ay.success("删除成功"),F()):r.Ay.error(t.message||"删除失败")}catch(t){r.Ay.error("删除失败，请稍后重试")}},P=async e=>{try{const t=await i["default"].admin.restorePrompt(e.id);200===t.code?(r.Ay.success("恢复成功"),F()):r.Ay.error(t.message||"恢复失败")}catch(t){r.Ay.error("恢复失败，请稍后重试")}};return(0,l.sV)((()=>{w(),F()})),(e,r)=>{const i=(0,l.g2)("a-input-search"),w=(0,l.g2)("a-col"),F=(0,l.g2)("a-select"),X=(0,l.g2)("a-select-option"),x=(0,l.g2)("a-button"),S=(0,l.g2)("a-row"),L=(0,l.g2)("router-link"),U=(0,l.g2)("a-tag"),j=(0,l.g2)("a-avatar"),Q=(0,l.g2)("a-table"),E=(0,l.g2)("a-input"),q=(0,l.g2)("a-form-item"),T=(0,l.g2)("a-textarea"),D=(0,l.g2)("a-form"),B=(0,l.g2)("a-modal");return(0,l.uX)(),(0,l.CE)("div",s,[r[14]||(r[14]=(0,l.Lk)("h1",null,"提示词管理",-1)),(0,l.Lk)("div",d,[(0,l.bF)(S,{gutter:16},{default:(0,l.k6)((()=>[(0,l.bF)(w,{span:8},{default:(0,l.k6)((()=>[(0,l.bF)(i,{value:p.value,"onUpdate:value":r[0]||(r[0]=e=>p.value=e),placeholder:"搜索提示词标题","enter-button":"",onSearch:C},null,8,["value"])])),_:1}),(0,l.bF)(w,{span:8},{default:(0,l.k6)((()=>[(0,l.bF)(F,{value:y.value,"onUpdate:value":r[1]||(r[1]=e=>y.value=e),mode:"multiple",placeholder:"选择标签筛选",style:{width:"100%"},options:g.value,onChange:A},null,8,["value","options"])])),_:1}),(0,l.bF)(w,{span:4},{default:(0,l.k6)((()=>[(0,l.bF)(F,{value:f.value,"onUpdate:value":r[2]||(r[2]=e=>f.value=e),style:{width:"100%"},onChange:I},{default:(0,l.k6)((()=>[(0,l.bF)(X,{value:-1},{default:(0,l.k6)((()=>r[7]||(r[7]=[(0,l.eW)("全部状态")]))),_:1}),(0,l.bF)(X,{value:1},{default:(0,l.k6)((()=>r[8]||(r[8]=[(0,l.eW)("正常")]))),_:1}),(0,l.bF)(X,{value:0},{default:(0,l.k6)((()=>r[9]||(r[9]=[(0,l.eW)("已删除")]))),_:1})])),_:1},8,["value"])])),_:1}),(0,l.bF)(w,{span:4},{default:(0,l.k6)((()=>[(0,l.bF)(x,{type:"primary",onClick:W},{icon:(0,l.k6)((()=>[(0,l.bF)((0,n.R1)(o.A))])),default:(0,l.k6)((()=>[r[10]||(r[10]=(0,l.eW)(" 刷新 "))])),_:1})])),_:1})])),_:1})]),(0,l.bF)(Q,{columns:t,"data-source":k.value,loading:a.value,pagination:m,onChange:z,"row-key":"id"},{bodyCell:(0,l.k6)((({column:e,record:t})=>["title"===e.key?((0,l.uX)(),(0,l.Wv)(L,{key:0,to:`/prompt/detail/${t.id}`,target:"_blank"},{default:(0,l.k6)((()=>[(0,l.eW)((0,u.v_)(t.title),1)])),_:2},1032,["to"])):(0,l.Q3)("",!0),"tags"===e.key?((0,l.uX)(!0),(0,l.CE)(l.FK,{key:1},(0,l.pI)(t.tags,(e=>((0,l.uX)(),(0,l.Wv)(U,{key:e.id,color:"blue"},{default:(0,l.k6)((()=>[(0,l.eW)((0,u.v_)(e.name),1)])),_:2},1024)))),128)):(0,l.Q3)("",!0),"user"===e.key?((0,l.uX)(),(0,l.CE)("div",c,[(0,l.bF)(j,{src:t.userAvatar||"/avatar-default.png",size:"small"},null,8,["src"]),(0,l.Lk)("span",null,(0,u.v_)(t.username),1)])):(0,l.Q3)("",!0),"status"===e.key?((0,l.uX)(),(0,l.Wv)(U,{key:3,color:1===t.status?"green":"red"},{default:(0,l.k6)((()=>[(0,l.eW)((0,u.v_)(1===t.status?"正常":"已删除"),1)])),_:2},1032,["color"])):(0,l.Q3)("",!0),"action"===e.key?((0,l.uX)(),(0,l.CE)("div",v,[(0,l.bF)(x,{type:"link",size:"small",onClick:e=>K(t)},{default:(0,l.k6)((()=>r[11]||(r[11]=[(0,l.eW)(" 编辑 ")]))),_:2},1032,["onClick"]),1===t.status?((0,l.uX)(),(0,l.Wv)(x,{key:0,type:"link",size:"small",danger:"",onClick:e=>R(t)},{default:(0,l.k6)((()=>r[12]||(r[12]=[(0,l.eW)(" 删除 ")]))),_:2},1032,["onClick"])):((0,l.uX)(),(0,l.Wv)(x,{key:1,type:"link",size:"small",onClick:e=>P(t)},{default:(0,l.k6)((()=>r[13]||(r[13]=[(0,l.eW)(" 恢复 ")]))),_:2},1032,["onClick"]))])):(0,l.Q3)("",!0)])),_:1},8,["data-source","loading","pagination"]),(0,l.bF)(B,{visible:b.value,"onUpdate:visible":r[6]||(r[6]=e=>b.value=e),title:"编辑提示词",onOk:O,confirmLoading:h.value,width:"800px"},{default:(0,l.k6)((()=>[(0,l.bF)(D,{model:_,layout:"vertical"},{default:(0,l.k6)((()=>[(0,l.bF)(q,{label:"标题",name:"title",rules:[{required:!0,message:"请输入标题"}]},{default:(0,l.k6)((()=>[(0,l.bF)(E,{value:_.title,"onUpdate:value":r[3]||(r[3]=e=>_.title=e)},null,8,["value"])])),_:1}),(0,l.bF)(q,{label:"提示词内容",name:"content",rules:[{required:!0,message:"请输入提示词内容"}]},{default:(0,l.k6)((()=>[(0,l.bF)(T,{value:_.content,"onUpdate:value":r[4]||(r[4]=e=>_.content=e),rows:6},null,8,["value"])])),_:1}),(0,l.bF)(q,{label:"标签",name:"tagIds",rules:[{required:!0,type:"array",message:"请选择标签"}]},{default:(0,l.k6)((()=>[(0,l.bF)(F,{value:_.tagIds,"onUpdate:value":r[5]||(r[5]=e=>_.tagIds=e),mode:"multiple",placeholder:"选择标签",options:g.value},null,8,["value","options"])])),_:1})])),_:1},8,["model"])])),_:1},8,["visible","confirmLoading"])])}}},g=a(1241);const p=(0,g.A)(k,[["__scopeId","data-v-ce303478"]]);var y=p}}]);
//# sourceMappingURL=449.f01f85fb.js.map