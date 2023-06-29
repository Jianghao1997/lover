// request.js
// 通常可以吧 baseUrl 单独放在一个 js 文件了，部署云服务器时注意修改请求地址
const baseUrl = 'http://localhost:8080'

const request = (options = {}) => {
	return new Promise((resolve, reject) => {
		uni.request({
			url: baseUrl + options.url || '',
			method: options.type || 'GET',
			data: options.data || {},
			header: options.header || {}
		}).then(data => {
			let [err, res] = data;
			resolve(res);
		}).catch(error => {
			reject(error)
		})
	});
}

const get = (url, data, options = {}) => {

	options.type = 'GET';
	options.data = data;
	options.url = url;
	const value = uni.getStorageSync('openId') || '';
	options.header = {
		"openID": value
	};
	return request(options)
}

const post = (url, data, options = {}) => {
	options.type = 'POST';
	options.data = data;
	options.url = url;
	const value = uni.getStorageSync('openId') || '';
	options.header = {
		"openID": value
	};
	return request(options)
}

const postForm = (url, data, options = {}) => {
	const value = uni.getStorageSync('openId') || '';
	options.type = 'POST';
	options.data = data;
	options.url = url;
	options.header = {
		"Content-Type": "application/x-www-form-urlencoded",
		"openID": value
	};
	return request(options)
}


export default {
	request,
	get,
	post,
	postForm
}
