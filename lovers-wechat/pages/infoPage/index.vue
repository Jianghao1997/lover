<template>
	<view class="info-page">
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		<uni-section type="line">
			<uni-card margin="10px 15px">
				<view class="image-content">
					<view style="align-items: center;display: inline-flex;">
						<image style="width: 100px; height: 100px; background-color: #eeeeee;border-radius: 50px;"
							mode="scaleToFill" :src="userInfo.wxAvatarUrl" @click="changeAvatar()"></image>
						<image style="width: 50px; height: 50px;margin: 0px 10px;border-radius: 50px;" mode="scaleToFill"
							src="你的域名images/miniprogramer/icons8-heart.gif"></image>
						<image style="width: 100px; height: 100px; background-color: #eeeeee;border-radius: 50px;"
							mode="scaleToFill" :src="cpUserInfo.wxAvatarUrl"></image>
						<image style="width: 50px; height: 50px;" </view>
							<view>
								<text style="font-size: 12px;color: #8c8c8c;">UID:</text>
								<text @click="copyUid(userInfo.wxUserUid)"
									style="font-size: 12px;color: #8c8c8c;">{{ userInfo.wxUserUid || 'LOVER-FOREVER' }}
									(点击即可复制)</text>
							</view>
					</view>

					<uni-collapse>
						<uni-collapse-item title="我的小信息" :open="true">
							<uni-list :border="false">
								<uni-list-item title="🤷‍昵称:" :rightText="userInfo.wxUserName"></uni-list-item>
								<uni-list-item title="🎰性别:"
									:rightText="userInfo.wxUserSex == 0 ? '男' : userInfo.wxUserSex == 1 ? '女' : ''"
									clickable @click="modifySex(userInfo)"></uni-list-item>
								<uni-list-item title="🧧积分:" :rightText="userInfo.wxUserCredit"></uni-list-item>
								<uni-list-item title="👑爱称:" :rightText="userInfo.wxUserNickName"></uni-list-item>
							</uni-list>
						</uni-collapse-item>
						<uni-collapse-item :title="`${cpUserInfo.wxUserNickName || '阿强'}的小信息`">
							<uni-list :border="false">
								<uni-list-item title="🤷‍昵称:" :rightText="cpUserInfo.wxUserName"></uni-list-item>
								<uni-list-item title="🎰性别:"
									:rightText="cpUserInfo.wxUserSex == 0 ? '男' : cpUserInfo.wxUserSex == 1 ? '女' : ''">
								</uni-list-item>
								<uni-list-item title="🧧积分:" :rightText="cpUserInfo.wxUserCredit"></uni-list-item>
								<uni-list-item title="👑爱称:" :rightText="cpUserInfo.wxUserNickName"></uni-list-item>
							</uni-list>
						</uni-collapse-item>
					</uni-collapse>
			</uni-card>
			<view style="text-align: center; padding: 5px 15px">
				<uni-grid :column="3" :square="false" borderColor="#ffffff00" :highlight="true" @change="gridChange">
					<uni-grid-item v-for="(item, index) in gridList" :index="index" :key="index">
						<view class="grid-item-box" style="background-color: #f7f7f773;">
							<uni-icons :type="item.iconType" :size="30" color="#777" />
							<text class="text">{{ item.text }}</text>
							<text style="text-align: left;font-size: 9px;margin: 0px 5px;color: #8c8989;">{{ item.desc }}</text>
						</view>
					</uni-grid-item>
				</uni-grid>
			</view>
			<uni-card v-if="!getInfo" margin="10px 15px">
				<button class="button" type="primary" @tap="getUserProfile">微信授权登录</button>
			</uni-card>
			<view v-if="unUsedStorage.length != 0 || usedStorage.length != 0">
				<view>
					<uni-card title="自己小仓库" thumbnail="你的域名images/miniprogramer/icons8-product.gif" margin="10px 15px">
						<view v-if="unUsedStorage.length != 0">
							<view v-for="(item, index) in unUsedStorage" :key="index">
								<uni-swipe-action>
									<uni-swipe-action-item :right-options="nomalOption"
										@click="e =>{ operation(e, item) }">
										<view style="margin-bottom: 5px;">
											<uni-card margin="0px">
												<uni-row>
													<uni-col :span="18">
														🏆 <text class="mission-name">{{ item.productName}}</text>
													</uni-col>
													<uni-col :span="6">
														<text class="mission-desc">数量：{{ item.count}}</text>
													</uni-col>
												</uni-row>
												<uni-row>
													<uni-col :span="24">
														📒 <text class="mission-desc">{{ item.productDesc}}</text>
													</uni-col>
												</uni-row>
												<uni-row>
													<uni-col :span="24">
														⏰ <text class="mission-desc">{{ item.createTime}}</text>
													</uni-col>
												</uni-row>
											</uni-card>
										</view>
									</uni-swipe-action-item>
								</uni-swipe-action>
							</view>
						</view>
						<view v-else style="text-align: center;">
							<image style="width: 100px; height: 100px; text-align: center;" mode="scaleToFill"
								src="你的域名images/miniprogramer/noData.png"></image>
							<view>
								<text class="font-style"> 暂无哦！</text>
							</view>
						</view>
					</uni-card>

					<view style="padding: 0px 15px;">
						<uni-collapse :border="false" title-border="none">
							<uni-collapse-item title="对方小仓库" thumb="你的域名images/miniprogramer/icons8-product-1.gif" :open="false" :border="false" titleBorder="none">
								<view v-if="cpUnUsedStorage.length != 0">
									<view v-for="(item, index) in cpUnUsedStorage" :key="index" style="padding: 0px 10px;">
										<view style="padding:5px 0px;">
											<uni-card margin="0px">
												<uni-row>
													<uni-col :span="18">
														🏆 <text class="mission-name">{{ item.productName}}</text>
													</uni-col>
													<uni-col :span="6">
														<text class="mission-desc">数量：{{ item.count}}</text>
													</uni-col>
												</uni-row>
												<uni-row>
													<uni-col :span="24">
														📒 <text class="mission-desc">{{ item.productDesc}}</text>
													</uni-col>
												</uni-row>
												<uni-row>
													<uni-col :span="24">
														⏰ <text class="mission-desc">{{ item.createTime}}</text>
													</uni-col>
												</uni-row>
											</uni-card>
										</view>
									</view>
								</view>
								<view v-else style="text-align: center;">
									<image style="width: 100px; height: 100px; text-align: center;" mode="scaleToFill"
										src="你的域名images/miniprogramer/noData.png"></image>
									<view>
										<text class="font-style"> 暂无哦！</text>
									</view>
								</view>
							</uni-collapse-item>
						</uni-collapse>
					</view>
				</view>
				<view style="margin: 10px 0px;">
					<view style="padding: 0px 15px;">
						<uni-collapse>
							<uni-collapse-item title="自己已使用" thumb="你的域名images/miniprogramer/icons8-product-1.gif" :open="false" :border="false" titleBorder="none">
								<view v-if="usedStorage.length != 0">
									<view v-for="(item, index) in usedStorage" :key="index" style="padding: 0px 10px;">
										<uni-swipe-action>
											<uni-swipe-action-item @click="e =>{ operation(e, item) }">
												<view style="padding:5px 0px;">
													<uni-card margin="0px">
														<uni-row>
															<uni-col :span="18">
																🏆 <text class="mission-name">{{ item.productName}}</text>
															</uni-col>
															<uni-col :span="6">
																<text class="mission-desc">数量：{{ item.count}}</text>
															</uni-col>
														</uni-row>
														<uni-row>
															<uni-col :span="24">
																📒 <text class="mission-desc">{{ item.productDesc}}</text>
															</uni-col>
														</uni-row>
														<uni-row>
															<uni-col :span="24">
																⏰ <text class="mission-desc">{{ item.createTime}}</text>
															</uni-col>
														</uni-row>
													</uni-card>
												</view>
											</uni-swipe-action-item>
										</uni-swipe-action>
									</view>
								</view>
								<view v-else style="text-align: center;">
									<image style="width: 100px; height: 100px; text-align: center;" mode="scaleToFill"
										src="你的域名images/miniprogramer/noData.png"></image>
									<view>
										<text class="font-style"> 暂无哦！</text>
									</view>
								</view>
							</uni-collapse-item>
						</uni-collapse>
					</view>
				</view>
			</view>
			<view v-else class="none-conten">
				<view>
					<text class="font-style"> 暂未购买任何商品哦！</text>
				</view>
			</view>
		</uni-section>
		<view style="text-align: center; padding: 0px 15px">
			<button class="button" type="primary" size="mini" open-type="share">分享一下此小程序吧</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				userInfo: {},
				cpUserInfo: {},
				canIUseGetUserProfile: false,
				openId: '',
				avatarUrl: '',
				getInfo: false,
				unUsedStorage: [],
				cpUnUsedStorage: [],
				usedStorage: [],
				nomalOption: [{
					text: '使用',
					style: {
						backgroundColor: '#6c90dd'
					}
				}],
				msgType: 'success',
				messageText: '',
				isSubscribe: false,
				extraIcon: {
					color: '#4cd964',
					size: '22',
					type: 'paperclip'
				},
				sexExtraIcon: {
					color: '#4cd964',
					size: '22',
					type: 'person'
				},
				cpAvatar: '',
				gridList: [
					{
						iconType: 'notification',
						text: '消息订阅',
						desc: '点击订阅消息，第一时间知道对方动态！'
					},
					{
						iconType: 'flag-filled',
						text: '记个小账',
						desc: '生活点点滴滴很美好，但也不要乱花钱哦！'
					},
					{
						iconType: 'staff',
						text: '绑定对象',
						desc: '快点“绑”个对象！点击下方分享一起使用吧！'
					},
				],
				isBind: false
			}
		},
		onShow() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.getUserInfo()
				}
			})
		},
		onLoad() {
			let _this = this
			uni.getSetting({
				withSubscriptions: true,
				success(res) {
					_this.isSubscribe = res.subscriptionsSetting.mainSwitch
				}
			})
			_this.check().then(d => {
					_this.getInfo = true
					_this.userInfo = d
					uni.getStorage({
						key: 'openId',
						success: function(res) {
							_this.openId = res.data
							_this.getStorageList()
							_this.getCpStorageList()
						}
					})
				})
				.catch(e => {
					if (!_this.getInfo) {
						_this.login().then(() => {
							if (uni.getUserProfile) {
								_this.canIUseGetUserProfile = true
							}
						}).catch(err => {
							uni.showToast({
								title: err,
								icon: 'error'
							});
						})
					}
				})
		},
		onPullDownRefresh() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.getUserInfo()
					_this.getStorageList()
					_this.getCpStorageList()
				}
			})
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 1000);
		},
		onShareAppMessage(res) {
		    if (res.from === 'button') {// 来自页面内分享按钮
		        console.log(res.target)
		    }
		    return {
		        title: '快来和我一起使用吧！', //分享的名称
				type: 0,
		        path: '/pages/mainPage/index',
		        mpId:'wx7cf98f884ad76df1' //此处配置微信小程序的AppId
		    }
		},
		methods: {
			check() {
				return new Promise((resolve, reject) => {
					let _this = this
					uni.getStorage({
						key: 'userInfo',
						success: function(res) {
							resolve(res.data)
						},
						fail: function(err) {
							reject()
						}
					})
				})
			},
			login() {
				return new Promise((resolve, reject) => {
					let _this = this
					uni.login({
						provider: 'weixin',
						success: function(loginRes) {
							_this.$request.postForm('/user/login', {
									code: loginRes.code
								})
								.then(result => {
									if (result.data.code == 0) {
										_this.openId = result.data.data
										resolve()
									} else {
										reject(result.data.message)
									}
								})
								.catch(e => {
									reject(e)
								})
						},
						fail: function() {
							reject()
						}
					})
				})
			},
			getUserProfile(e) {
				let _this = this
				if (_this.canIUseGetUserProfile && _this.openId) {
					uni.getUserProfile({
						desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
						success: (res) => {
							_this.userInfo = res.userInfo
							_this.saveUserInfo({
								openId: _this.openId,
							})
						},
						fail: (err) => {
							uni.showToast({
								title: err,
								icon: 'error'
							});
						}
					})
				}
			},
			saveUserInfo(e) {
				let _this = this
				_this.$request.post('/user/saveInfo', {
						wxOpenId: e.openId || e.wxOpenId,
						wxUserName: _this.userInfo.nickName,
						wxUserSex: _this.userInfo.gender,
						wxAvatarUrl: _this.userInfo.avatarUrl
					})
					.then(result => {
						if (result.data.code == 0) {
							_this.getInfo = true
							_this.getUserInfo()
							_this.getStorageList()
							_this.getCpStorageList()
							uni.setStorage({
								key: 'openId',
								data: _this.openId,
							});
							uni.showToast({
								title: '授权成功',
								icon: "success",
								duration: 2000
							});
						}
					})
			},
			getUserInfo() {
				let _this = this
				_this.$request.get(`/user/get/${_this.openId}`)
					.then(result => {
						if (result.data.code == 0) {
							_this.userInfo = result.data.data
							_this.getCpInfo()
							_this.getIsBind()
							uni.setStorage({
								key: 'userInfo',
								data: result.data.data,
							});
						}
					})
			},
			getStorageList() {
				let _this = this
				if (_this.openId) {
					_this.$request.get(`/storage/list?openID=${_this.openId}&status=1`)
						.then(result => {
							_this.unUsedStorage = []
							_this.usedStorage = []
							if (result.data.code == 0) {
								_this.unUsedStorage = result.data.data
							}
						})
					_this.$request.get(`/storage/list?openID=${_this.openId}&status=-1`)
						.then(result => {
							if (result.data.code == 0) {
								_this.usedStorage = result.data.data
							}
						})
				}
			},
			getCpStorageList() {
				let _this = this
				if (_this.openId) {
					_this.$request.get(`/storage/cp/list?openID=${_this.openId}&status=1`)
						.then(result => {
							_this.cpUnUsedStorage = []
							if (result.data.code == 0) {
								_this.cpUnUsedStorage = result.data.data
							}
						})
				}
			},
			operation(e, item) {
				let _this = this
				if (e.content.text == '使用') {
					_this.$request.postForm('/storage/use', {
							storageID: item.storageID,
							openID: _this.openId
						})
						.then(result => {
							if (result.data.code == 0) {
								_this.getStorageList()
								_this.getCpStorageList()
								uni.showToast({
									title: '感谢惠顾',
									icon: "success",
									duration: 2000,
								})
							} else {
								_this.msgType = 'error'
								_this.messageText = result.data.message
								_this.$refs.message.open()
							}
						})
						.catch(e => {
							_this.msgType = 'error'
							_this.messageText = e
							_this.$refs.message.open()
						})
				}
			},
			subscribe() {
				let _this = this
				uni.requestSubscribeMessage({
					// 消息模板id
					tmplIds: ['6kI4R7rSu_BHPHd2TZDGHwqB-OhP7F3xMV9nv7swe70'],
					success(res) {
						if (res['6kI4R7rSu_BHPHd2TZDGHwqB-OhP7F3xMV9nv7swe70'] != 'reject') {
							_this.isSubscribe = true
							uni.showToast({
								title: '订阅成功',
								icon: "success",
								duration: 2000,
							})
						}
					}
				})
			},
			copyUid(uid) {
				uni.setClipboardData({
					data: uid,
					success: function() {
						uni.showToast({
							title: '复制成功',
							icon: "success",
							duration: 1000,
						})
					}
				});
			},
			modifySex(userInfo) {
				let _this = this
				uni.showActionSheet({
					title: '请选择',
					itemList: ['男', '女'],
					success: function(res) {
						if (res.tapIndex == 0) {
							userInfo.wxUserSex = 0
						}
						if (res.tapIndex == 1) {
							userInfo.wxUserSex = 1
						}
						_this.$request.post('/user/saveInfo', userInfo)
							.then(result => {
								if (result.data.code == 0) {
									_this.getUserInfo()
									uni.showToast({
										title: '修改成功',
										icon: "success",
										duration: 1000,
									})
								}
							})
					},
					fail: function(res) {
						console.log(res.errMsg);
					}
				})
			},
			changeAvatar() {
				let _this = this
				uni.chooseImage({
					success: (chooseImageRes) => {
						const tempFilePaths = chooseImageRes.tempFilePaths;
						uni.uploadFile({
							url: '你的域名upload/avatar/image', //仅为示例，非真实的接口地址
							filePath: tempFilePaths[0],
							name: 'file',
							success: (uploadFileRes) => {
								let res = JSON.parse(uploadFileRes.data)
								if (res.code == 0) {
									console.log(res.data)
									_this.userInfo.wxAvatarUrl = res.data
									_this.$request.post('/user/saveInfo', _this.userInfo)
										.then(result => {
											if (result.data.code == 0) {
												_this.getUserInfo()
												uni.showToast({
													title: '修改成功',
													icon: "success",
													duration: 1000,
												})
											}
										})
								}
							},
							fail: (err) => {
								uni.showToast({
									title: '上传失败',
									icon: "error",
									duration: 2000,
									success: function() {

									}
								})
							}
						});
					}
				});
			},
			getCpInfo() {
				let _this = this
				_this.$request.get(
						`/user/getRelations/${_this.openId}`)
					.then(result => {
						if (result.data.code == 0) {
							_this.cpUserInfo = {}
							let cpInfo = result.data.data?.find(p => p.wxOpenId != _this.openId)
							_this.cpUserInfo = cpInfo
						}
					})
			},
			bookkeeping() {
				uni.navigateToMiniProgram({
					appId: 'wx7c86e0c731b9b8ef',
					path: 'pages/index',
					success(res) {

					}
				})
			},
			getIsBind() {
				let _this = this
				_this.$request.get(
						`/user/isBind/${_this.openId}`)
					.then(result => {
						if (result.data.code == 0) {
							_this.isBind = result.data.data
						}
					})
			},
			gridChange(e) {
				let _this = this
				let { index } = e.detail
				if (index == 0) {
					_this.subscribe()
				}
				if (index == 1) {
					_this.bookkeeping()
				}
				if (index == 2) {
					if (_this.isBind) {
						uni.showToast({
							title: '已有对象',
							icon: "error",
							duration: 2000,
							success: function() {
								uni.navigateTo({
									url: './bindRelationPage/index'
								})
							}
						})
					} else {
						uni.navigateTo({
							url: './bindRelationPage/index'
						})
					}
				}
			},
		},
	}
</script>

<style lang="scss">
	page {
		background-image: linear-gradient(-90deg, #D5B1DD, #D6EFFF);
	}

	.info-page {
		padding: 0px 0px 5px 0px;

		.uni-card {
			margin: 10px;
			padding: 0 8px;
			border-radius: 4px;
			overflow: hidden;
			font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
			background-color: #f7f7f773;
			flex: 1;
		}

		.uni-list-item {
		    display: flex;
		    font-size: 16px;
		    position: relative;
		    justify-content: space-between;
		    align-items: center;
		    background-color: #e7ceef47;
		    flex-direction: row;
		}

		.image-content {
			text-align: center;
		}

		.none-conten {
			margin: 10px 0px;
			text-align: center;
			width: 100%;

			.font-style {
				font-size: 14px;
				color: #989090;
			}
		}

		.mission-name {
			font-size: 15px;
			font-weight: 600;
			margin-left: 5px;
		}

		.mission-desc {
			font-size: 12px;
			color: #b3b3b3;
			margin-left: 5px;
		}

		.uni-card .uni-card__content {
			padding: 10px 0px !important;
			font-size: 14px;
			color: #6a6a6a;
			line-height: 22px;
		}

		.uni-swipe_button {
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			padding: 0 20px;
			margin: 0px 6px 5px 0px;
			border-radius: 10px;
		}

		.grid-item-box {
			flex: 1;
			// position: relative;
			/* #ifndef APP-NVUE */
			display: flex;
			/* #endif */
			flex-direction: column;
			align-items: center;
			justify-content: center;
			padding: 15px 0;
		}
	}
</style>
