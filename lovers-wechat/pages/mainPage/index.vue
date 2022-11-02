<template>
	<view class="main-page">
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		<uni-row>
			<carousel :img-list="imageList" url-key="imageUrl" @selected="selectedBanner" />
		</uni-row>
		<view style="text-align: center;padding: 0px 15px;">
			<uni-row :gutter="15">
				<uni-col :span="12">
					<button type="info" @click="upload" style="background-color: #f7f7f773 !important;">
						<uni-icons type="upload-filled"></uni-icons>上传图片
					</button>
				</uni-col>
				<uni-col :span="12">
					<button ref="subscribe" type="info" @tap="subscribe()" style="background-color: #f7f7f773 !important;">
						<uni-icons type="notification"></uni-icons>消息订阅
					</button>
				</uni-col>
			</uni-row>
		</view>
		<uni-card title="每日一句" :thumbnail="avatar" margin="10px 15px">
			<view>
				<view> <text>今天是<text style="color: crimson;margin: 0px 5px;"> {{ userA }} </text> 和 <text
							style="color: crimson;margin: 0px 5px;"> {{ userB }} </text> 相恋的第 <text
							style="color: crimson;margin: 0px 5px;"> 💖{{ day }} </text>天</text> </view>
			</view>
			<view style="margin-top: 10px;">
				<text style="font-size: 16px;font-weight: 500;margin-top: 10px;">💌 {{ loveSentences }}</text>
			</view>
		</uni-card>
		<uni-card title="积分情况" :thumbnail="coinUrl" margin="10px 15px">
			<uni-list :border="false">
				<uni-list-item :title="`${userB} 所拥有积分`" :rightText="`💰 ${userBCredit} 分`">
				</uni-list-item>
				<uni-list-item :title="`${userA} 所拥有积分`" :rightText="`💰 ${userACredit} 分`">
				</uni-list-item>
			</uni-list>
		</uni-card>


		<view style="text-align: center; padding: 0px 15px">
			<uni-grid :column="3" :square="false" borderColor="#ffffff00" :highlight="true" @change="gridChange">
				<uni-grid-item v-for="(item, index) in gridList" :index="index" :key="index">
					<view class="grid-item-box" style="background-color: #f7f7f773;">
						<uni-icons :type="item.iconType" :size="30" color="#777" />
						<text class="text">{{ item.text }}</text>
						<text style="text-align: left;font-size: 9px;margin: 0px 5px;color: #8c8989;">{{ item.desc }}</text>
					</view>
				</uni-grid-item>
			</uni-grid>
			<button class="button" style="margin-top: 5px;" type="primary" size="mini"
				open-type="share">分享一下此小程序吧</button>
		</view>
		<uni-fab :pattern="pattern" :content="content" :horizontal="horizontal" :vertical="vertical"
			:direction="direction" @trigger="trigger"></uni-fab>
	</view>
</template>

<script>
	import carousel from '@/components/vear-carousel/vear-carousel'
	export default {
		components: {
			carousel
		},
		data() {
			return {
				indicatorDots: true,
				autoplay: true,
				interval: 2000,
				duration: 500,
				avatar: 'https://www.loversmission.xyz/images/miniprogramer/icons8-calendar.gif',
				coinUrl: 'https://www.loversmission.xyz/images/miniprogramer/icons8-cheap.gif',
				userA: '阿珍',
				userB: '阿强',
				day: 0,
				userACredit: 0,
				userBCredit: 0,
				horizontal: 'right',
				vertical: 'bottom',
				direction: 'horizontal',
				pattern: {
					color: '#7A7E83',
					backgroundColor: '#fff',
					selectedColor: '#007AFF',
					buttonColor: '#007AFF',
					iconColor: '#fff'
				},
				content: [{
						iconPath: 'https://www.loversmission.xyz/images/miniprogramer/write.png',
						selectedIconPath: 'https://www.loversmission.xyz/images/miniprogramer/write.png',
						text: '小情话',
						active: false
					},
					{
						iconPath: 'https://www.loversmission.xyz/images/miniprogramer/updateNickName.png',
						selectedIconPath: 'https://www.loversmission.xyz/images/miniprogramer/updateNickName.png',
						text: '修改爱称',
						active: false
					},
					{
						iconPath: 'https://www.loversmission.xyz/images/miniprogramer/unBind.png',
						selectedIconPath: 'https://www.loversmission.xyz/images/miniprogramer/unBind.png',
						text: '解除关系',
						active: false
					}
				],
				imageList: [{
					imageUrl: 'https://www.loversmission.xyz/images/miniprogramer/暂无图片.png'
				}],
				loveSentences: '阿珍爱上阿强在一个没有星星的夜晚！',
				msgType: 'success',
				messageText: '',
				gridList: [{
						iconType: 'help',
						text: '今日吃啥',
						desc: '不知道怎么填饱自己的肚子，快来点一下吧！'
					},
					{
						iconType: 'medal',
						text: '拆弹猫',
						desc: '一起玩玩小游戏，增进彼此的感情吧！'
					},
					{
						iconType: 'more-filled',
						text: '开发中...',
						desc: '开发者准备教资考试去了，保佑上岸！'
					},
				],
			}
		},
		onShow() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.loadInfo()
				},
				fail: function() {
					uni.showToast({
						title: '快去授权一下吧！',
						icon: "error",
						duration: 2000,
						success: function() {
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/infoPage/index'
								})	
							}, 1000);
						}
					});
				}
			})
		},
		onLoad() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.loadInfo()
				},
				fail: function() {
					uni.showToast({
						title: '快去授权一下吧！',
						icon: "error",
						duration: 2000,
						success: function() {
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/infoPage/index'
								})	
							}, 1000);
						}
					});
				}
			})
		},
		onPullDownRefresh() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.loadInfo()
					uni.stopPullDownRefresh();
				},
				fail: function() {
					uni.showToast({
						title: '快去授权一下吧！',
						icon: "error",
						duration: 2000,
						success: function() {
							uni.stopPullDownRefresh();
							setTimeout(function() {
								uni.redirectTo({
									url: '../infoPage/index'
								})
							}, 1000);
						}
					});
				}
			})
		},
		onShareAppMessage(res) {
			if (res.from === 'button') { // 来自页面内分享按钮
				console.log(res.target)
			}
			return {
				title: '快来和我一起使用吧！', //分享的名称
				type: 0,
				path: '/pages/mainPage/index',
				mpId: 'wx7cf98f884ad76df1' //此处配置微信小程序的AppId
			}
		},
		methods: {
			subscribe() {
				console.log('开始消息订阅')
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
					},
					fail(error) {
						console.log(error)
					}
				})
			},
			loadInfo() {
				let _this = this
				_this.getUserInfos()
					.then(d => {
						if (d) {
							let infos = d
							let infoA = infos?.find(p => p.wxOpenId == _this.openId)
							_this.userA = '我'
							_this.userACredit = infoA?.wxUserCredit
							let infoB = infos?.find(p => p.wxOpenId != _this.openId)
							_this.userB = infoB?.wxUserNickName
							_this.userBCredit = infoB?.wxUserCredit
						}
						_this.getDays()
						_this.getImages()
						_this.getLoveSentences()
					})
					.catch(e => {
						_this.msgType = 'error'
						_this.messageText = e
						_this.$refs.message.open()
						_this.getNeedAgreeRelations()
					})
			},
			getUserInfos() {
				return new Promise((resolve, reject) => {
					let _this = this
					uni.getStorage({
						key: 'userInfo',
						success: function(res) {
							uni.getStorage({
								key: 'openId',
								success: function(res) {
									_this.openId = res.data
									if (_this.openId) {
										_this.getInfo = true
										_this.$request.get(
												`/user/getRelations/${_this.openId}`)
											.then(result => {
												if (result.data.code == 0) {
													resolve(result.data.data)
												} else {
													reject(result.data.message)
												}
											})
									}
								},
								fail: function(err) {
									reject()
								}
							})
						},
						fail: function(err) {
							reject()
						}
					})
				})
			},
			trigger(e) {
				let _this = this
				this.content[e.index].active = !e.item.active
				if (e.index == 0) {
					uni.showModal({
						title: '写句小情话吧',
						editable: true,
						placeholderText: '输入你想说的话吧！',
						success: function(res) {
							if (res.confirm) {
								_this.$request.postForm(
										'/user/save/loveSentences', {
											openID: _this.openId,
											msg: res.content
										})
									.then(result => {
										if (result.data.code == 0) {
											_this.getLoveSentences()
											uni.showToast({
												title: '爱你哦！',
												icon: "success",
												duration: 2000
											})
										} else {
											_this.msgType = 'error'
											_this.messageText = result.data.message
											_this.$refs.message.open()
										}
									})
							} else if (res.cancel) {
								console.log('用户点击取消')
							}
						}
					})
				}
				if (e.index == 1) {
					uni.showModal({
						title: '改个新的爱称吧',
						editable: true,
						placeholderText: '输入新的爱称',
						success: function(res) {
							if (res.confirm) {
								_this.$request.postForm(
										'/user/update/nickName', {
											openID: _this.openId,
											nickName: res.content
										})
									.then(result => {
										if (result.data.code == 0) {
											_this.loadInfo()
											uni.showToast({
												title: '修改成功',
												icon: "success",
												duration: 2000
											})
										} else {
											_this.msgType = 'error'
											_this.messageText = result.data.message
											_this.$refs.message.open()
										}

									})
							} else if (res.cancel) {
								console.log('用户点击取消')
							}
						}
					})
				}
				if (e.index == 2) {
					uni.showModal({
						title: '确定要解除绑定嘛？',
						success: function(res) {
							if (res.confirm) {
								_this.$request.postForm(
										'/user/update/relationStatus', {
											openID: _this.openId,
											status: -1
										})
									.then(result => {
										if (result.data.code == 0) {
											_this.loadInfo()
											uni.showToast({
												title: '解除成功',
												icon: "success",
												duration: 2000
											})
										} else {
											_this.msgType = 'error'
											_this.messageText = result.data.message
											_this.$refs.message.open()
										}
									})
							} else if (res.cancel) {
								console.log('用户点击取消')
							}
						}
					})
				}
			},
			getDays() {
				let _this = this
				_this.$request.get(`/user/get/day/${_this.openId}`)
					.then(result => {
						_this.day = 0
						if (result.data.code == 0) {
							_this.day = result.data.data
						}
					})
			},
			getLoveSentences() {
				let _this = this
				_this.$request.get(`/user/get/loveSentences/${_this.openId}`)
					.then(result => {
						if (result.data.code == 0) {
							_this.loveSentences = result.data.data
						}
					})
			},
			getNeedAgreeRelations() {
				let _this = this
				_this.$request.postForm(
						'/user/get/relation', {
							openID: _this.openId
						})
					.then(result => {
						if (result.data.code == 0 && result.data.data) {
							let name = result.data.data.createUserName
							let tips = `来自${name}的绑定申请`
							uni.showActionSheet({
								title: tips,
								itemList: ['同意', '拒绝'],
								success: function(res) {
									if (res.tapIndex == 0) {
										_this.$request.postForm(
												'/user/update/relationStatus', {
													openID: _this.openId,
													status: 1
												})
											.then(result => {
												if (result.data.code == 0) {
													_this.loadInfo()
													uni.showToast({
														title: '绑定成功',
														icon: "success",
														duration: 2000
													})
												} else {
													_this.msgType = 'error'
													_this.messageText = result.data.message
													_this.$refs.message.open()
												}
											})
									}
									if (res.tapIndex == 1) {
										_this.$request.postForm(
												'/user/update/relationStatus', {
													openID: _this.openId,
													status: -1
												})
											.then(result => {
												if (result.data.code == 0) {
													_this.loadInfo()
													uni.showToast({
														title: '成功拒绝',
														icon: "success",
														duration: 2000
													})
												} else {
													_this.msgType = 'error'
													_this.messageText = result.data.message
													_this.$refs.message.open()
												}
											})
									}
								},
								fail: function(res) {
									console.log(res.errMsg);
								}
							});
						}
					})
			},
			getImages() {
				let _this = this
				_this.$request.get(`/user/get/images/${_this.openId}`)
					.then(result => {
						_this.imageList = []
						if (result.data.code == 0) {
							_this.imageList = result.data.data
						}
					})
			},
			getTodayEat() {
				let _this = this
				_this.$request.get('/api/getTodayEat')
					.then(result => {
						if (result.data.code == 0) {
							console.log()
							let info = result.data.data.join()
							uni.showModal({
								title: '今日吃',
								content: info,
								confirmText: '就这些',
								cancelText: '重新来',
								success: function(res) {
									if (res.confirm) {
										console.log('用户点击确定');
									} else if (res.cancel) {
										_this.getTodayEat()
									}
								},
							})
						}
					})
			},
			upload() {
				let _this = this
				uni.chooseImage({
					success: (chooseImageRes) => {
						const tempFilePaths = chooseImageRes.tempFilePaths;
						uni.uploadFile({
							url: 'https://www.loversmission.xyz/upload/image', //仅为示例，非真实的接口地址
							filePath: tempFilePaths[0],
							name: 'file',
							formData: {
								'openId': _this.openId
							},
							success: (uploadFileRes) => {
								let res = JSON.parse(uploadFileRes.data)
								if (res.code == 0) {
									uni.showToast({
										title: '上传成功',
										icon: "success",
										duration: 2000,
										success: function() {
											_this.getImages()
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
			selectedBanner(item, index) {
				let _this = this
				let previewImageList = _this.imageList.map(p => p.imageUrl)				
				uni.showActionSheet({
					itemList: ['查看', '删除'],
					success: function(res) {
						if (res.tapIndex == 0) {
							uni.previewImage({
								current: index,
								urls: previewImageList						
							})
						}
						if (res.tapIndex == 1) {
							_this.deleteImage(item)
						}
					},
					fail: function(res) {
						console.log(res.errMsg);
					}
				})
			},
			deleteImage(item) {
				let _this = this
				uni.showModal({
					title: '提示',
					content: '是否删除该张照片',
					success: function(res) {
						if (res.confirm) {
							_this.$request.get(`/user/delete/images/${item.id}`)
								.then(result => {
									if (result.data.code == 0) {
										_this.getImages()
										uni.showToast({
											title: '删除成功',
											icon: "success",
											duration: 2000,
											success: function() {
												_this.getImages()
											}
										})
									}
								})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			},
			game_1() {
				uni.navigateToMiniProgram({
					appId: 'wx8c95d5db0fff0bf1',
					path: 'pages/index',
					success(res) {

					}
				})
			},
			gridChange(e) {
				let _this = this
				let {
					index
				} = e.detail
				if (index == 0) {
					_this.getTodayEat()
				}
				if (index == 1) {
					_this.game_1()
				}
				if (index == 2) {
					uni.showToast({
						title: '开发中',
						icon: "error",
						duration: 2000,
						success: function() {

						}
					})
				}
			},
		}
	}
</script>

<style lang="scss">
	page {
		background-image: linear-gradient(-90deg, #D5B1DD, #D6EFFF);
	}

	.main-page {
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
	}
</style>
