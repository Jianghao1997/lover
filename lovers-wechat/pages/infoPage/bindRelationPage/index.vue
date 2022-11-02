<template>
	<view class="product-page">
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		<uni-card>		
			<view style="text-align: center;">
				<view style="font-size: 18px;">对方信息</view>
				<image style="width: 100px; height: 100px; background-color: #eeeeee;border-radius: 50px;"
					mode="scaleToFill" :src="userInfo.wxAvatarUrl"></image>
				<view>微信名称：{{userInfo.wxUserName || '阿珍'}}</view>
			</view>
			<view>
				<uni-forms ref="valiForm" :modelValue="form" label-position="top" label-width="150" :rules="rules">
					<uni-forms-item label="对方UID" required name="userUid">
						<uni-easyinput v-model="form.userUid" placeholder="请粘贴对方UID" @blur="getUserInfo" />
					</uni-forms-item>
					<uni-forms-item label="对方爱称" name="userNickName">
						<uni-easyinput v-model="form.userNickName" placeholder="请输入对方爱称" />
					</uni-forms-item>
					<uni-forms-item label="在一起的第一天" required name="bindTime">
						<uni-datetime-picker type="date" return-type="date" v-model="form.bindTime" />
					</uni-forms-item>
				</uni-forms>
				<button type="primary" @click="submit('valiForm')">绑定</button>
			</view>
			<view style="text-align: center;">
				<image style="width: 100px; height: 100px;margin: 0px 10px;" mode="scaleToFill"
					src="https://www.loversmission.xyz/images/miniprogramer/icons8-love-letter.gif"></image>
			</view>
		</uni-card>		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				index: 0,
				form: {},
				userInfo:{},
				show: true,
				rules: {
					userUid: {
						rules: [{
							required: true,
							errorMessage: '用户UID不能为空'
						}]
					},
					bindTime: {
						rules: [{
							required: true,
							errorMessage: '时间不能为空'
						}]
					}
				},				
				openId: '',
				msgType: 'success',
				messageText: '',
				readOnly: false,
				modify: false
			}
		},
		onLoad(option) {
			let _this = this
			uni.getStorage({
				key: 'openId',
				success: function(res) {
					_this.openId = res.data
				},
			})		
		},
		methods: {
			getUserInfo() {
				let _this = this
				if (!_this.form.userUid) return
				_this.$request.get(`/user/getByUid/${_this.form.userUid}`)
					 .then(result => {
					 	if (result.data.code == 0) {
					 		_this.userInfo = result.data.data
					 	} else {
					 		_this.msgType = 'error'
					 		_this.messageText = result.data.message
					 		_this.$refs.message.open()
					 	}
					 })
			},
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			submit(ref) {
				let _this = this
				_this.$refs[ref].validate().then(res => {					
					_this.form.openID = _this.openId
					_this.$request.post(
							'/user/create/relation', _this.form)
						.then(result => {
							if (result.data.code == 0) {								
								uni.showToast({
									title: '等待对方同意',
									icon: "success",
									duration: 2000,
									success:function(){
										_this.back()
									}
								})
							} else {
								_this.msgType = 'error'
								_this.messageText = result.data.message
								_this.$refs.message.open()
							}
					
						})
				}).catch(err => {
					console.log('err', err);
				})
			},
		}
	}
</script>

<style lang="scss">
	page {
		background-image: linear-gradient(-90deg, #D5B1DD, #D6EFFF);
	}

	.product-page {
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
	}
</style>
