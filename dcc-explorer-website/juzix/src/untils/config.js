'use strict'

import axios from 'axios'
import qs from 'qs'


var baseUrl = "http://10.65.209.17:9301";
var baseContextPath = "/juzix-explorer-website/";
if (window.location.host === 'funcexplorer.dcc.finance') {
    baseUrl = 'http://10.65.209.17:9301';
}
if (window.location.host === 'explorer.dcc.finance') {
    baseUrl = 'http://explorer.dcc.finance';
}

axios.interceptors.request.use(config => {    // 这里的config包含每次请求的内容
    // 判断localStorage中是否存在api_token
    if (localStorage.getItem('api_token')) {
        //  存在将api_token写入 request header
        config.headers.apiToken = `${localStorage.getItem('api_token')}`;
    }
    return config;
}, err => {
    return Promise.reject(err);
});

axios.interceptors.response.use(response => {
    return response
}, error => {
    return Promise.resolve(error.response)
});

function checkStatus (response) {
    // 如果http状态码正常，则直接返回数据
    if (response && (response.status === 200 || response.status === 304 ||
            response.status === 400)) {
        return response.data
    }
    // 异常状态下，把错误信息返回去
    // return {
    //     status: -404,
    //     message: 'System is busy, please try again later'
    // }
}

function checkCode (res) {
    // 如果code异常(这里已经包括网络错误，服务器错误，后端抛出的错误)，可以弹出一个错误提示，告诉用户
    if(!res){
        console.log("服务器异常，请刷新重试")
    }
    if (res.status === -404) {
        return  res;
    }
    if (res.data && (!res.data.success)) {
        console.log(res.data.error_msg)
    }
    return res
}
// 请求方式的配置
export default {
    post (url, data) {  //  post
        return axios({
            method: 'post',
            url:baseUrl+"/chain-observer/restful/juzix"+url,
            // url:'/api/chain-observer/restful/juzix'+url,
            data: qs.stringify(data),
            timeout: 5000,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }).then(
            (response) => {
                return checkStatus(response)
            }
        ).then(
            (res) => {
                return checkCode(res)
            }
        )
    },
    get (url, params) {  // get
        var timestamp = new Date().getTime();
        return axios({
            method: 'get',
            url:baseUrl+'/chain-observer/restful/juzix'+url+"?timestamp="+timestamp,
            // url:'/api/chain-observer/restful/juzix'+url,
            params:params, // get 请求时带的参数
            timeout: 5000,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }).then(
            (response) => {
                return checkStatus(response)
            }
        ).then(
            (res) => {
                return checkCode(res)
            }
        )
    }
}