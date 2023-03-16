var web_prefix = '/backend'

// 更新员工自己的个人信息
function updateAccount(params) {
    return $axios({
        url: '/employee/account',
        method: 'post',
        data: { ...params }
    })
}