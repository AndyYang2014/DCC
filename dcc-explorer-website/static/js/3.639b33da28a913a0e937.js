webpackJsonp([3],{"4pcB":function(t,a){},"9j4a":function(t,a,e){"use strict";var s={props:["block","loading"],watch:{block:function(t){this.blockData=this.block},loading:function(t){this.loadStatus=t}},data:function(){return{blockData:this.block,loadStatus:this.loading}},components:{Loading:e("T4ej").a}},o={render:function(){var t=this,a=t.$createElement,s=t._self._c||a;return s("div",{staticClass:"top-index container",staticStyle:{position:"relative"}},[s("div",{staticClass:"panel panel-default",staticStyle:{width:"100%"}},[s("div",{staticClass:"panel-heading"},[s("img",{staticClass:"block_img",attrs:{src:e("E2VN"),alt:""}}),t._v(" "),s("h3",{staticClass:"panel-title explorer-panel-title pull-left"},[t._v("  Blocks")]),t._v(" "),t._t("more")],2),t._v(" "),s("Loading",{directives:[{name:"show",rawName:"v-show",value:t.loadStatus,expression:"loadStatus"}]}),t._v(" "),s("div",{staticClass:"panel-body"},[s("table",{staticClass:"table-responsive table table-striped table-hover table-index",attrs:{id:"table-block"}},[t._m(0),t._v(" "),s("tbody",t._l(this.blockData,function(a,e){return s("tr",{key:e},[s("td",[s("router-link",{attrs:{to:{path:"/block/search/"+a.blockNumber}}},[t._v(t._s(a.blockNumber))])],1),t._v(" "),s("td",{attrs:{title:a.hash}},[t._v(t._s(a.hash))]),t._v(" "),s("td",[t._v(t._s(a.transactionCount))]),t._v(" "),s("td",[t._v(t._s(t._f("dateDiff")(a.blockTimestamp)))])])}))])])],1)])},staticRenderFns:[function(){var t=this.$createElement,a=this._self._c||t;return a("thead",[a("tr",[a("th",[this._v(" Height ")]),this._v(" "),a("th",[this._v(" Hash ")]),this._v(" "),a("th",[this._v(" Txn ")]),this._v(" "),a("th",[this._v(" Age ")])])])}]},i=e("VU/8")(s,o,!1,null,null,null);a.a=i.exports},E2VN:function(t,a,e){t.exports=e.p+"static/img/block.951ed3d.svg"},ScDY:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e("9j4a"),o=e("PAn/"),i={data:function(){return{blockData:[],totalElements:0,pageTotal:0,moreBlockLoading:!1}},components:{Block:s.a,Pagination:o.a},methods:{block:function(t){var a=this,e=this;this.$axios.get("/block",t).then(function(t){"SUCCESS"==t.systemCode&&"SUCCESS"==t.businessCode?(a.moreBlockLoading=!1,a.blockData=t.result.items,a.totalElements=t.result.totalElements,a.pageTotal=+t.result.totalPages):a.$toast.top(t.message)}).catch(function(t){e.$toast.top("System is busy, please try again later")})},pagechange:function(t){var a={page:t,pageSize:20};this.moreBlockLoading=!0,this.block(a)}},mounted:function(){this.moreBlockLoading=!0;this.block({page:1,pageSize:20})}},l={render:function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"row indexCon block"},[a("Block",{attrs:{block:this.blockData,loading:this.moreBlockLoading}},[a("span",{staticClass:"blockRecords",attrs:{slot:"more"},slot:"more"},[this._v(this._s(this.totalElements)+" records found")])]),this._v(" "),a("Pagination",{staticClass:"container pageWrap",attrs:{total:this.totalElements,display:20},on:{pagechange:this.pagechange}})],1)},staticRenderFns:[]};var n=e("VU/8")(i,l,!1,function(t){e("4pcB")},null,null);a.default=n.exports}});
//# sourceMappingURL=3.639b33da28a913a0e937.js.map