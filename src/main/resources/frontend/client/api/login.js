function sendMsg(phone) {
    return $axios({
        'url': '/user/sms',
        'method': 'post',
        params: {phone}
    })
}

function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
  }

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

  