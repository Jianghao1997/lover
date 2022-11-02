<template>
	<view class="mission-page">
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		<view class="page-title">
		    <div style="position: absolute; top: 5px; left: 10px;">欢迎来到专属小商店, {{ user }}!</div>
		    <div style="position: absolute; top: 5px; right: 10px;">当前 💰{{ credit }} 积分</div>
		</view>
		<uni-search-bar style="margin-top: 10px;" @confirm="search" :focus="false" placeholder="商品名称" v-model="searchValue">
		</uni-search-bar>
		<view v-if="saleProducts.length != 0 || unSaleProducts.length != 0">
			<view class="market-list">
				<uni-card title="上架商品" :thumbnail="saleProductsAvatar" margin="10px 15px">
					<uni-badge class="badge-num" :text="saleProductsCount" type="success" />
					<view v-if="saleProducts.length != 0">
						<view v-for="(item, index) in saleProducts" :key="index">
							<uni-swipe-action>
								<uni-swipe-action-item :right-options="options2" @click="e =>{ operation(e, item) }">
									<view style="margin-bottom: 5px;">
										<uni-card margin="0px" @click="goProductDetails(item)">
											<view
												:style=" 'background-image:url('+ getUrl(item) +');    background-repeat: no-repeat;background-position: right;background-size: contain;' ">
												<uni-row>
													<uni-col :span="18">
														🏆 <text class="mission-name">{{ item.productName}}</text>
													</uni-col>
													<uni-col :span="6">
														💰 <text class="mission-desc"><text
																style="color: #de482d;margin-right: 5px;font-weight: 800;">
																{{ item.productCredit}} </text> 积分</text>
													</uni-col>
												</uni-row>
												<uni-row>
													<uni-col>
														<text class="mission-content">{{ item.productDesc}}</text>
													</uni-col>
												</uni-row>
												<uni-row>
													<uni-col :span="12">
														💪 <text class="mission-desc">By：{{ item.createUserName}}</text>
													</uni-col>
													<uni-col :span="12">
														⏰ <text class="mission-desc">{{ item.createTime}}</text>
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
							src="https://www.loversmission.xyz/images/miniprogramer/noData.png"></image>
						<view>
							<text class="font-style"> 暂无商品哦！</text>
						</view>
					</view>
				</uni-card>
			</view>
			<view style="padding: 0px 15px; margin:10px 0px;">
				<uni-collapse :border="false" title-border="none">
					<uni-collapse-item title="下架商品(点击查看)"
						thumb="https://www.loversmission.xyz/images/miniprogramer/icons8-add-shopping-cart.gif"
						:open="false" :border="false" titleBorder="none">
						<view v-if="unSaleProducts.length != 0">
							<view v-for="(item, index) in unSaleProducts" :key="index" style="padding: 0px 10px;">
								<uni-swipe-action>
									<uni-swipe-action-item :right-options="options1"
										@click="e =>{ operation(e, item) }">
										<view style="margin-bottom: 5px;">
											<uni-card margin="0px" @click="goProductDetails(item)">
												<view
													:style=" 'background-image:url('+ getUrl(item) +');    background-repeat: no-repeat;background-position: right;background-size: contain;' ">
													<uni-row>
														<uni-col :span="18">
															🏆 <text class="mission-name">{{ item.productName}}</text>
														</uni-col>
														<uni-col :span="6">
															💰 <text class="mission-desc"><text
																	style="color: #de482d;margin-right: 5px;font-weight: 800;">
																	{{ item.productCredit}} </text> 积分</text>
														</uni-col>
													</uni-row>
													<uni-row>
														<uni-col>
															📒 <text class="mission-desc">{{ item.productDesc}}</text>
														</uni-col>
													</uni-row>
													<uni-row>
														<uni-col :span="12">
															💪 <text
																class="mission-desc">By：{{ item.createUserName}}</text>
														</uni-col>
														<uni-col :span="12">
															⏰ <text class="mission-desc">{{ item.createTime}}</text>
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
								src="https://www.loversmission.xyz/images/miniprogramer/noData.png"></image>
							<view>
								<text class="font-style"> 暂无商品哦！</text>
							</view>
						</view>
					</uni-collapse-item>
				</uni-collapse>
			</view>
		</view>
		<view v-else class="none-conten">
			<image style="height: 320px;" mode="scaleToFill"
				src="https://www.loversmission.xyz/images/miniprogramer/None.png"></image>
			<view>
				<text class="font-style"> 暂无商品哦！</text>
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
				unSaleProducts: [],
				saleProducts: [],
				openId: '',
				saleProductsAvatar: 'https://www.loversmission.xyz/images/miniprogramer/icons8-wallet.gif',
				options2: [{
					text: '购买',
					style: {
						backgroundColor: '#6c90dd'
					}
				}, {
					text: '修改',
					style: {
						backgroundColor: '#bfd4dd'
					}
				}, {
					text: '下架',
					style: {
						backgroundColor: '#ccddc3'
					}
				}],
				options1: [{
					text: '上架',
					style: {
						backgroundColor: '#ccddc3'
					}
				}, ],
				horizontal: 'right',
				vertical: 'bottom',
				direction: 'horizontal',
				pattern: {
					color: '#7A7E83',
					backgroundColor: '#fff',
					selectedColor: '#f7f7f773',
					buttonColor: '#007AFF',
					iconColor: '#fff'
				},
				content: [{
					iconPath: 'https://www.loversmission.xyz/images/miniprogramer/write.png',
					selectedIconPath: 'https://www.loversmission.xyz/images/miniprogramer/write.png',
					text: '发布商品',
					active: false
				}],
				msgType: 'success',
				messageText: '',
				user: '阿强',
				credit: 0,
				saleProductsCount: 0
			}
		},
		onLoad() {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
					_this.getProductList()
					_this.getUserInfo()
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
					_this.getProductList()
					_this.getUserInfo()
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
			_this.getProductList()
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
			getUserInfo() {
				let _this = this
				_this.$request.get(`/user/get/${_this.openId}`)
					.then(result => {
						if (result.data.code == 0) {							
							_this.user = result.data.data.wxUserNickName
							_this.credit = result.data.data.wxUserCredit	
							uni.setStorage({
								key: 'userInfo',
								data: result.data.data,
							});
						}
					})
			},
			getUrl(item) {
				let _this = this
				return item.createUserOpenID != _this.openId ?
					'https://www.loversmission.xyz/images/miniprogramer/MarketA.png' :
					'https://www.loversmission.xyz/images/miniprogramer/MarketB.png'
			},
			operation(e, item) {
				let _this = this
				if (e.content.text == '购买') {
					_this.$request.postForm('/product/buy', {
							productId: item.productID,
							openID: _this.openId,
						})
						.then(result => {
							if (result.data.code == 0) {
								uni.showToast({
									title: '购买完成',
									icon: "success",
									duration: 2000,
									success: function() {
										setTimeout(function() {
											_this.getProductList()
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
				if (e.content.text == '上架') {
					_this.$request.postForm('/product/updateStatus', {
							productId: item.productID,
							openID: _this.openId,
							status: 1
						})
						.then(result => {
							if (result.data.code == 0) {
								uni.showToast({
									title: '上架成功',
									icon: "success",
									duration: 2000,
									success: function() {
										setTimeout(function() {
											_this.getProductList()
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
				if (e.content.text == '下架') {
					_this.$request.postForm('/product/updateStatus', {
							productId: item.productID,
							openID: _this.openId,
							status: -1
						})
						.then(result => {
							if (result.data.code == 0) {
								uni.showToast({
									title: '下架成功',
									icon: "success",
									duration: 2000,
									success: function() {
										setTimeout(function() {
											_this.getProductList()
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
							content: '不能修改对方上架的商品哦！',
							showCancel: false,
							confirmText: '关闭',
						})
						return
					}					
					uni.navigateTo({
						url: `../marketAddPage/index?productID=${item.productID}&readOnly=false&modify=true`
					})
				}
			},
			search() {
				this.getProductList()
			},
			getProductList() {
				let _this = this
				uni.getStorage({
					key: 'userInfo',
					success: async function(res) {
						if (_this.openId) {
							uni.showLoading({
								title: '商品加载中'
							});
							await _this.$request.get(
									`/product/get?openID=${_this.openId}&status=1&searchValue=${_this.searchValue}`
								)
								.then(result => {
									if (result.data.code == 0) {
										_this.saleProducts = result.data.data
										_this.saleProductsCount = _this.saleProducts.length
									}
								})
							await _this.$request.get(
									`/product/get?openID=${_this.openId}&status=-1&searchValue=${_this.searchValue}`
								)
								.then(result => {
									if (result.data.code == 0) {
										_this.unSaleProducts = result.data.data
									}
								})
							uni.hideLoading();
							uni.stopPullDownRefresh();
						}
					}
				})
			},
			trigger(e) {
				if (!this.content[e.index].active) {
					uni.navigateTo({
						url: '../marketAddPage/index'
					})
				}
			},
			goProductDetails(item) {
				uni.navigateTo({
					url: `../marketAddPage/index?productID=${item.productID}&readOnly=true`
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
		
		.page-title{
			position: fixed;
			font-size: 14px;
			width: 100%;
			height: 35px;
			top: 0px;
			color: white;
			box-shadow: 1px 1px 10px black;
			background-color: rgba(0, 0, 0, 0.8);
			z-index: 10;
		}
		
		.uni-searchbar {
		    display: flex;
		    flex-direction: row;
		    position: relative;
		    padding: 10px;
		    margin-top: 30px;
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

		.uni-swipe_button {
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			padding: 0 20px;
			margin: 0px 6px 5px 0px;
			border-radius: 10px;
		}
		.market-list {
			position: relative;		
			.badge-num {
				position: absolute;
				top: 20px;
				right: 10px;
			}
		}
	}
</style>
