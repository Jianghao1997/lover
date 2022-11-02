<template>
	<view class="product-page">
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		<uni-card>
			<view style="text-align: center;">
				<image style="text-align: center;" mode="scaleToFill"
					src="https://www.loversmission.xyz/images/miniprogramer/Item.gif"></image>
			</view>
			<view v-if="readOnly" style="text-align: center;">
				<text style="font-size: 16px;">此商品由：<text style="color:crimson;margin-right: 5px;">{{ createUser }}</text> 上架！</text>
			</view>
			<view class="form-card">
				<uni-forms ref="valiForm" :modelValue="form" label-position="top" label-width="150" :rules="rules">
					<uni-forms-item label="🛒商品分类" required name="productClassify">
						<picker v-if="!readOnly" :value="index" range-key="label" :range="classifyList" @change="bindPickerChange">						
							<uni-card :is-shadow="false">
								<text> {{ form.productClassify }}</text>
							</uni-card>
						</picker>
						<uni-card v-else :is-shadow="false">
							<text> {{ form.productClassify }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item label="🔗商品标题" required name="productName">
						<uni-easyinput v-if="!readOnly" v-model="form.productName" placeholder="请输入商品标题" />
						<uni-card v-else :is-shadow="false">
							<text> {{ form.productName }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item label="📱商品描述" required name="productDesc">
						<uni-easyinput v-if="!readOnly" type="textarea" v-model="form.productDesc" placeholder="请输入商品描述" />
						<uni-card v-else :is-shadow="false">
							<text> {{ form.productDesc }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item label="🥇商品积分" required name="productCredit">
						<uni-number-box v-if="!readOnly" v-model="form.productCredit" :min="5" :max="100" :step="5" ></uni-number-box>
						<uni-card v-else :is-shadow="false">
							<text> {{ form.productCredit }} 💰</text>
						</uni-card>
					</uni-forms-item>
				</uni-forms>
				<button v-if="!readOnly" type="primary" @click="submit('valiForm')">{{ modify ? '修改商品' : '发布商品' }} </button>
			</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				index: 0,
				form: {
					productClassify : '无预设'
				},
				show: true,
				rules: {
					productName: {
						rules: [{
							required: true,
							errorMessage: '商品标题不能为空'
						}]
					},
					productDesc: {
						rules: [{
							required: true,
							errorMessage: '商品描述不能为空'
						}]
					},
					productClassify: {
						rules: [{
							required: true,
							errorMessage: '商品分类不能为空'
						}]
					},
					productCredit: {
						rules: [{
							required: true,
							errorMessage: '商品分类不能为空'
						}]
					},
				},
				classifyList: [{
					label: "无预设",
					title: "",
					desc: "",
				}, {
					label: "零食券",
					title: "实现零食自由",
					desc: "没有实现财富自由，但是可以实现零食自由！",
				}, {
					label: "饮料券",
					title: "这杯饮料我干了",
					desc: "喝什么白开水，我要喝饮料😁",
				}, {
					label: "夜宵券",
					title: "怎么饿了？我要吃夜宵",
					desc: "晚上吃的有点少了，大半夜想吃点东西！",
				}, {
					label: "洗碗券",
					title: "吃完了吧，去洗碗吧",
					desc: "吃的有点撑，宝贝你去洗个碗吧！😊",
				}, {
					label: "家务券",
					title: "快来拖个地吧",
					desc: "家里有点脏了，但是我不想动，宝贝快来搞卫生咯！",
				}, {
					label: "跑腿券",
					title: "宝贝！给我去跑个腿",
					desc: "好累哦！不想动了！给我去那个快递，倒杯水！",
				}, {
					label: "亲亲券",
					title: "人家要个亲亲",
					desc: "用都用了，还不来个亲亲？",
				}, {
					label: "羞羞券",
					title: "今晚记得关灯哦",
					desc: "麻烦关下灯！躺好了，在床上等我哦！",
				}, {
					label: "按摩券",
					title: "宝贝技师，给按个摩呗",
					desc: "有了这个券，赶紧让对方给你来个全身舒服的马杀鸡！",
				}, {
					label: "宇宙无敌券",
					title: "宇宙无敌券",
					desc: "凭此券，让对方做啥就得做啥（当然也别太过分哦！！！）",
				}],
				openId: '',
				msgType: 'success',
				messageText: '',
				readOnly: false,
				modify: false,
				createUser: ''
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
			if (option.productID) {
				_this.$request.get(`/product/get/${option.productID}`)
					.then(result => {
						if (result.data.code == 0) {
							_this.$set(_this.form, 'productID', result.data.data.productID)
							_this.$set(_this.form, 'productClassify', result.data.data.productClassify)
							_this.$set(_this.form, 'productName', result.data.data.productName)
							_this.$set(_this.form, 'productDesc', result.data.data.productDesc)
							_this.$set(_this.form, 'productCredit', result.data.data.productCredit)
							_this.createUser = result.data.data.createUserName
						} else {
							_this.msgType = 'error'
							_this.messageText = result.data.message
							_this.$refs.message.open()
						}
					})
			}
			if (option.readOnly == 'true' || option.readOnly == true) {
				_this.readOnly = true
			}
			if (option.modify == 'true' || option.modify == true) {
				_this.modify = true
			}
		},
		methods: {
			bindPickerChange: function(e) {
				let _this = this
				_this.index = e.detail.value;
				_this.$set(_this.form, 'productClassify', _this.classifyList[_this.index].label)
				let choice = _this.classifyList.find(p => p.label == _this.classifyList[_this.index].label)
				_this.$set(_this.form, 'productName', choice?.title)
				_this.$set(_this.form, 'productDesc', choice?.desc)
			},
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			submit(ref) {
				let _this = this
				_this.$refs[ref].validate().then(res => {
					let _this = this
					_this.$request.post('/product/saveInfo', {
							..._this.form,
							createUserOpenID: _this.openId
						})
						.then(result => {
							if (result.data.code == 0) {
								uni.showToast({
									title: '创建成功！',
									icon: "success"
								});
								_this.back()
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
		
		.form-card {
			.uni-card {
			    margin: 0px !important;
			    padding: 0 8px;
			    border-radius: 4px;
			    overflow: hidden;
			    font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
			    background-color: #fff;
			    flex: 1;
			}
		}	
	}
</style>
