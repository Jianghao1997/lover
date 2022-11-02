<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			let updateManager = uni.getUpdateManager();
			if (!updateManager) {
				return
			} else {
				//新版本更新
				if (uni.canIUse('getUpdateManager')) {
					//判断当前微信版本是否支持版本更新
					updateManager.onCheckForUpdate(function(res) {
						if (res.hasUpdate) {
							// 请求完新版本信息的回调
							updateManager.onUpdateReady(function() {
								uni.showModal({
									title: '更新提示',
									content: '新版本已经准备好，是否重启应用？',
									success: function(res) {
										if (res.confirm) {
											// 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
											updateManager.applyUpdate();
										}
									},
								});
							});
							updateManager.onUpdateFailed(function() {
								uni.showModal({
									// 新的版本下载失败
									title: '已经有新版本了哟~',
									content: '新版本已经上线啦~，请您删除当前小程序，重新搜索(晒渔人)打开哟~',
								});
							});
						} else {}
					});
				} else {
					uni.showModal({
						// 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
						title: '提示',
						content: '当前微信版本过低，部分功能无法使用，请升级到最新微信版本后重试。',
					});
				}

			}

		},
		onHide: function() {
			console.log('App Hide')
		}

	}
</script>

<style>
	/*每个页面公共css */
</style>
