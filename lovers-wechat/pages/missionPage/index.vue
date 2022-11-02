<template>
	<view class="mission-page">
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		<uni-search-bar @confirm="search" :focus="false" placeholder="任务名称" v-model="searchValue">
		</uni-search-bar>
		<view v-if="finishedMission.length != 0 || unFinishedMission.length != 0">
			<view class="info-mission">
				<uni-card title="未完成任务" :thumbnail="unFinishedMissionAvatar" margin="10px 15px">
					<uni-badge class="badge-num" :text="unFinishedMissionCount" type="success" />
					<view v-if="unFinishedMission.length != 0">
						<view v-for="(item, index) in unFinishedMission" :key="index">
							<uni-swipe-action>
								<uni-swipe-action-item
									:right-options="item.missionType == 0 ? nomalOption : clockOptions"
									@click="e =>{ operation(e, item) }">
									<view style="margin-bottom: 5px;">
										<uni-card :is-shadow="false" margin="0px" @click="goMissionDetails(item)">
											<view
												:style=" 'background-image:url('+ getUrl(item) +');    background-repeat: no-repeat;background-position: right;background-size: contain;' ">
												<uni-row>
													<uni-col :span="18">
														<text class="mission-name">🏆 {{ item.missionName}}</text>
													</uni-col>
													<uni-col :span="6">
														<text class="mission-desc"><text
																style="color: #de482d;margin-right: 5px;font-weight: 800;">
																💰 {{ item.missionCredit}} </text> 积分</text>
													</uni-col>
												</uni-row>
												<!-- <uni-row>
													<uni-col>
														<text class="mission-content">{{ item.missionDesc}}</text>
													</uni-col>
												</uni-row> -->
												<uni-row>
													<uni-col :span="12">
														<text class="mission-desc">⏰ {{ item.createTime }}</text>
													</uni-col>
													<uni-col :span="12">
														<text class="mission-desc">💪 By：{{ item.createUserName}}</text>
													</uni-col>
												</uni-row>
												<uni-row>
													<uni-col :span="12" v-if="item.missionType == 1">
														<text class="mission-desc"
															:style="isNeedWarning(item.endTime) ? 'color: #de0f00;' : ''">{{ item.endTime}} 截至</text>
													</uni-col>
													<uni-col :span="12">
														<text
															class="mission-desc">{{ item.missionType == 0 ? '' : `🥇 (打卡：${item.clockDay} 天)`}}</text>
													</uni-col>
												</uni-row>
											</view>
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
							<text class="font-style"> 暂无任务哦！</text>
						</view>
					</view>
				</uni-card>
			</view>
			<view style="padding: 0px 15px; margin:10px 0px;">
				<uni-collapse :border="false" title-border="none">
					<uni-collapse-item title="已完成任务(点击查看)" thumb="你的域名images/miniprogramer/icons8-in-progress.gif" :open="false" :border="false" titleBorder="none">
						<view v-if="finishedMission.length != 0">
							<view v-for="(item, index) in finishedMission" :key="index" style="padding: 0px 10px;">
								<uni-swipe-action>
									<uni-swipe-action-item
										:right-options="deleteOption"
											@click="e =>{ operation(e, item) }">
											<view style="margin-bottom: 5px;">
												<uni-card margin="0px" @click="goMissionDetails(item)">
													<view
														:style=" 'background-image:url('+ getUrl(item) +');    background-repeat: no-repeat;background-position: right;background-size: contain;' ">
														<uni-row>
															<uni-col :span="18">
																<text class="mission-name">🏆 {{ item.missionName}}</text>
															</uni-col>
															<uni-col :span="6">
																<text class="mission-desc"><text
																		style="color: #de482d;margin-right: 5px;font-weight: 800;">
																		💰 {{ item.missionCredit}} </text> 积分</text>
															</uni-col>
														</uni-row>
														<!-- <uni-row>
															<uni-col>
																<text class="mission-content">{{ item.missionDesc}}</text>
															</uni-col>
														</uni-row> -->
														<uni-row>
															<uni-col :span="24">
																<text class="mission-desc">⏰ {{ item.completeTime }} 完成</text>
															</uni-col>
														</uni-row>
														<uni-row>
															<uni-col :span="12">
																<text class="mission-desc">💪 By：{{ item.createUserName}}</text>
															</uni-col>
															<uni-col :span="12">
																<text
																	class="mission-desc">{{ item.missionType == 0 ? '' : `🥇 (打卡：${item.clockDay} 天)`}}</text>
															</uni-col>
														</uni-row>
													</view>
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
								<text class="font-style"> 暂无任务哦！</text>
							</view>
						</view>
					</uni-collapse-item>
				</uni-collapse>
			</view>
		</view>

		<view v-else class="none-conten">
			<image style="height: 320px;" mode="scaleToFill" src="你的域名images/miniprogramer/None.png"></image>
			<view>
				<text class="font-style"> 暂无任务哦！</text>
			</view>
		</view>
		<uni-fab :pattern="pattern" :content="content" :horizontal="horizontal" :vertical="vertical"
			:direction="direction" @trigger="trigger"></uni-fab>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				searchValue: '',
				unFinishedMission: [],
				finishedMission: [],
				openId: '',
				unFinishedMissionAvatar: '你的域名images/miniprogramer/icons8-task.gif',
				nomalOption: [{
					text: '完成',
					style: {
						backgroundColor: '#6c90dd',
					}
				}, {
					text: '修改',
					style: {
						backgroundColor: '#ccddc3'
					}
				}],
				deleteOption: [ {
					text: '删除',
					style: {
						backgroundColor: '#dd503d'
					}
				}],
				clockOptions: [{
					text: '打卡',
					style: {
						backgroundColor: '#ffe5a1'
					}
				}, {
					text: '完成',
					style: {
						backgroundColor: '#55aaff'
					}
				}, {
					text: '修改',
					style: {
						backgroundColor: '#ccddc3'
					}
				}],
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
					iconPath: '你的域名images/miniprogramer/write.png',
					selectedIconPath: '你的域名images/miniprogramer/write.png',
					text: '写个任务',
					active: false
				}],
				msgType: 'success',
				messageText: '',
				unFinishedMissionCount: 0
			}
		},
		onLoad() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.getMissionList()
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
		onShow() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.getMissionList()
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
			_this.getMissionList()
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
			isNeedWarning(time) {
				let now = new Date()
				now.setTime(now.setHours(now.getHours() + 24))
				let _time = new Date(Date.parse(time))
				return _time < now
			},
			getUrl(item) {
				let _this = this
				return item.createUserOpenID != _this.openId ? '你的域名images/miniprogramer/MarketA.png' : '你的域名images/miniprogramer/MarketB.png'
			},
			operation(e, item) {
				let _this = this
				if (e.content.text == '完成') {
					_this.$request.postForm('/mission/complete', {
							missionID: item.missionID,
							openID: _this.openId,
							bForce: false
						})
						.then(result => {
							if (result.data.code == 0) {
								uni.showToast({
									title: '任务完成',
									icon: "success",
									duration: 2000,
									success: function() {
										setTimeout(function() {
											_this.getMissionList()
										}, 1000);
									}
								})
							} else if (result.data.code == 10001) {
								uni.showModal({
									title: '系统提示',
									content: result.data.message + '是否确认完成？',
									cancelText:'取消',
									confirmText: '确认',
									success: function(res) {
										if (res.confirm) {
											_this.$request.postForm('/mission/complete', {
													missionID: item.missionID,
													openID: _this.openId,
													bForce: true
												})
												.then(result => {
													if (result.data.code == 0) {
														uni.showToast({
															title: '任务完成',
															icon: "success",
															duration: 2000,
															success: function() {
																setTimeout(function() {
																	_this.getMissionList()
																}, 1000);
															}
														})
													} else {
														uni.showModal({
															title: '系统提示',
															content: result.data.message,
															showCancel: false,
															confirmText: '关闭'
														})
													}
												})
												.catch(e => {
													_this.msgType = 'error'
													_this.messageText = e
													_this.$refs.message.open()
												})
										}
									},
								})
							} else {
								uni.showModal({
									title: '系统提示',
									content: result.data.message,
									showCancel: false,
									confirmText: '关闭'
								})
							}
						})
						.catch(e => {
							_this.msgType = 'error'
							_this.messageText = e
							_this.$refs.message.open()
						})
				}
				if (e.content.text == '打卡') {
					_this.$request.postForm('/mission/clock', {
							missionId: item.missionID,
							openID: _this.openId
						})
						.then(result => {
							if (result.data.code == 0) {
								uni.showToast({
									title: '打卡成功',
									icon: "success",
									duration: 2000,
									success: function() {
										setTimeout(function() {
											_this.getMissionList()
										}, 1000);
									}
								})
							} else {
								uni.showModal({
									title: '系统提示',
									content: result.data.message,
									showCancel: false,
									confirmText: '关闭'
								})
							}
						})
						.catch(e => {
							_this.msgType = 'error'
							_this.messageText = e
							_this.$refs.message.open()
						})
				}
				if (e.content.text == '修改') {
					if (_this.openId != item.createUserOpenID) {
						uni.showModal({
							title: '系统提示',
							content: '不能修改对方创建的任务哦！',
							showCancel: false,
							confirmText: '关闭',
						})
						return
					}
					uni.navigateTo({
						url: `../missionAddPage/index?missionID=${item.missionID}&readOnly=false&modify=true`
					})
				}
				if (e.content.text == '删除') {
					_this.$request.postForm('/mission/delete', {
							missionID: item.missionID,
							openID: _this.openId
						})
						.then(result => {
							if (result.data.code == 0) {
								uni.showToast({
									title: '删除成功',
									icon: "success",
									duration: 2000,
									success: function() {
										setTimeout(function() {
											_this.getMissionList()
										}, 1000);
									}
								})
							} else {
								uni.showModal({
									title: '系统提示',
									content: result.data.message,
									showCancel: false,
									confirmText: '关闭'
								})
							}
						})
						.catch(e => {
							_this.msgType = 'error'
							_this.messageText = e
							_this.$refs.message.open()
						})
				}
			},
			search() {
				this.getMissionList()
			},
			getMissionList() {
				let _this = this
				uni.getStorage({
					key: 'userInfo',
					success: async function(res) {
						if (_this.openId) {
							uni.showLoading({
								title: '任务加载中'
							});
							await _this.$request.get(
									`/mission/get?openID=${_this.openId}&status=0&searchValue=${_this.searchValue}`
								)
								.then(result => {
									if (result.data.code == 0) {
										_this.unFinishedMission = result.data.data
										_this.unFinishedMissionCount = _this.unFinishedMission.length
									}
								})
							await _this.$request.get(
									`/mission/get?openID=${_this.openId}&status=1&searchValue=${_this.searchValue}`
								)
								.then(result => {
									if (result.data.code == 0) {
										_this.finishedMission = result.data.data
									}
								})
							uni.hideLoading()
							uni.stopPullDownRefresh();
						}
					}
				})
			},
			trigger(e) {
				if (!this.content[e.index].active) {
					uni.navigateTo({
						url: '../missionAddPage/index'
					})
				}
			},
			goMissionDetails(item) {
				uni.navigateTo({
					url: `../missionAddPage/index?missionID=${item.missionID}&readOnly=true`
				})
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-image: linear-gradient(-90deg, #D5B1DD, #D6EFFF);
	}

	.mission-page {
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

		.uni-card .uni-card__content {
			padding: 10px 0px !important;
			font-size: 14px;
			color: #6a6a6a;
			line-height: 22px;
		}

		.none-conten {
			margin-top: 50px;
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
			display: inline-block;
			width: 200px;
			/* 设置文本不进行换行 */
			white-space: nowrap;
			/* 让超出部分省略 */
			overflow: hidden;
			/* 超出部分使用省略号 */
			text-overflow: ellipsis;
		}

		.mission-desc {
			font-size: 12px;
			color: #656565;
			margin-left: 5px;
		}

		.mission-content {
			font-size: 12px;
			color: #b3b3b3;
			margin-left: 5px;
			display: inline-block;
			width: 280px;
			/* 设置文本不进行换行 */
			white-space: nowrap;
			/* 让超出部分省略 */
			overflow: hidden;
			/* 超出部分使用省略号 */
			text-overflow: ellipsis;
		}

		.info-mission {
			position: relative;

			.badge-num {
				position: absolute;
				top: 20px;
				right: 10px;
			}
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
	}
</style>
