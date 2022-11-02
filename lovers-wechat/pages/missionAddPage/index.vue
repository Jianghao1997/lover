<template>
	<view class="mission-page">
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		<uni-card>
			<view style="text-align: center;">
				<image style="text-align: center;" mode="scaleToFill"
					src="你的域名images/miniprogramer/Mission.gif"></image>
			</view>
			<view v-if="readOnly" style="text-align: center;">
				<text style="font-size: 16px;">此任务由：<text style="color:crimson;margin-right: 5px;">{{ createUser }}</text> 创建并监督执行！</text>
			</view>
			<view class="form-card">
				<uni-forms ref="valiForm" :modelValue="form" label-position="top" label-width="150" :rules="rules">
					<uni-forms-item label="💻任务类型" required name="missionType">
						<uni-data-checkbox v-if="!readOnly" mode="button" v-model="form.missionType" :localdata="typeList"></uni-data-checkbox>
						<uni-card v-else :is-shadow="false">
							<text> {{ form.missionType == 0 ? '短期任务' : '长期任务' }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item v-if="form.missionType == 1" label="⌚任务截止时间" required name="endTime">
						<uni-datetime-picker v-if="!readOnly" type="date" return-type="date" v-model="form.endTime" />
						<uni-card v-else :is-shadow="false">
							<text> {{ form.endTime }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item label="🛒任务分类" required name="missionClassify">
						<picker :value="index" v-if="!readOnly" range-key="label" :range="classifyList" @change="bindPickerChange">
							<uni-card :is-shadow="false">
								<text> {{ form.missionClassify }}</text>
							</uni-card>
						</picker>
						<uni-card v-else :is-shadow="false">
							<text> {{ form.missionClassify }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item label="🔗任务标题" required name="missionName">
						<uni-easyinput v-if="!readOnly" v-model="form.missionName" placeholder="请输入任务标题" />
						<uni-card v-else :is-shadow="false">
							<text> {{ form.missionName }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item label="📱任务描述" required name="missionDesc">
						<uni-easyinput v-if="!readOnly" type="textarea" v-model="form.missionDesc" placeholder="请输入任务描述" />
						<uni-card v-else :is-shadow="false">
							<text> {{ form.missionDesc }}</text>
						</uni-card>
					</uni-forms-item>
					<uni-forms-item label="🥇任务积分" required name="missionCredit">
						<uni-number-box v-if="!readOnly" :disabled="readOnly" v-model="form.missionCredit" :min="5" :max="100" :step="5"></uni-number-box>
						<uni-card v-else :is-shadow="false">
							<text> {{ form.missionCredit }} 💰</text>
						</uni-card>
					</uni-forms-item>

				</uni-forms>
				<uni-row :gutter="10">
					<uni-col :span="readOnly? 12 : modify ? 12 : 0">
						<button v-if="modify" type="warn" style="margin-bottom: 5px;" @click="deleteMission()"> 删除任务 </button>
					</uni-col>
					<uni-col :span="readOnly? 12 : modify ? 12 : 24">
						<button v-if="!readOnly" type="primary" @click="submit('valiForm')">{{ modify ? '修改任务' : '发布任务' }} </button>
					</uni-col>
				</uni-row>
				<view v-if="readOnly" style="text-align: center;">
					<image style="width: 100px; height: 100px;" mode="scaleToFill"
						:src=" isCompelete ? '你的域名images/miniprogramer/已完成.png' : '你的域名images/miniprogramer/待完成.png'"></image>
				</view>
			</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					missionClassify: '无预设'
				},
				show: false,
				rules: {
					missionName: {
						rules: [{
							required: true,
							errorMessage: '任务标题不能为空'
						}]
					},
					missionDesc: {
						rules: [{
							required: true,
							errorMessage: '任务描述不能为空'
						}]
					},
					missionClassify: {
						rules: [{
							required: true,
							errorMessage: '任务分类不能为空'
						}]
					},
					missionCredit: {
						rules: [{
							required: true,
							errorMessage: '任务分类不能为空'
						}]
					},
				},
				classifyList: [{
					label: "无预设",
					title: "",
					desc: "",
				}, {
					label: "早睡早起",
					title: "今晚早睡，明天早起",
					desc: "熬夜对身体很不好，还是要早点睡觉第二天才能有精神！",
				}, {
					label: "打扫房间",
					title: "打扫房间，整理卫生",
					desc: "有一段时间没有打扫房间了，一屋不扫，何以扫天下？",
				}, {
					label: "运动健身",
					title: "做些运动，强身健体",
					desc: "做一些健身运动吧，跳绳，跑步，训练动作什么的。",
				}, {
					label: "多喝一杯水",
					title: "多喝水，身体更棒棒",
					desc: "多喝一杯水，拒绝手撕嘴巴皮！",
				}, {
					label: "好好吃饭",
					title: "不知道吃啥？那就点个小碗菜吧！",
					desc: "苦恼不知道晚上吃啥？点个小碗菜，吃点下饭的炒菜也不错哦！",
				}, {
					label: "请客吃饭",
					title: "请客吃点好的",
					desc: "好吃的有很多，我可以让你尝到其中之一，好好享受吧！",
				}, {
					label: "买小礼物",
					title: "整点小礼物",
					desc: "买点小礼物，像泡泡马特什么的。",
				}, {
					label: "洗碗洗碟",
					title: "这碗碟我洗了",
					desc: "有我洗碗洗碟子，有你吃饭无它事。",
				}, {
					label: "帮拿东西",
					title: "帮拿一天东西",
					desc: "有了我，你再也不需要移动了。拿外卖，拿零食，开空调，开电视，在所不辞。",
				}, {
					label: "做饭",
					title: "这道美食由我完成",
					desc: "做点可口的饭菜，或者专门被指定的美食。我这个大厨，随便下，都好吃。",
				}],
				typeList: [{
						text: "短期",
						value: 0,
					},
					{
						text: "长期",
						value: 1,
					}
				],
				openId: '',
				msgType: 'success',
				messageText: '',
				index: 0,
				readOnly: false,
				modify: false,
				createUser: '',
				productStatus: ''
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
			if (option.missionID) {
				_this.$request.get(`/mission/get/${option.missionID}`)
					.then(result => {
						if (result.data.code == 0) {
							_this.$set(_this.form, 'missionID', result.data.data.missionID)
							_this.$set(_this.form, 'missionClassify', result.data.data.missionClassify)
							_this.$set(_this.form, 'missionName', result.data.data.missionName)
							_this.$set(_this.form, 'missionDesc', result.data.data.missionDesc)
							_this.$set(_this.form, 'missionCredit', result.data.data.missionCredit)
							_this.$set(_this.form, 'missionType', result.data.data.missionType)
							_this.$set(_this.form, 'endTime', result.data.data.endTime)
							_this.$set(_this.form, 'completeTime', result.data.data.completeTime)
							_this.createUser = result.data.data.createUserName
							_this.productStatus = result.data.data.productStatus
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
		computed: {
			isCompelete() {
				return this.form.completeTime ? true : false
			}
		},
		methods: {
			bindPickerChange: function(e) {
				let _this = this
				_this.index = e.detail.value;
				_this.$set(_this.form, 'missionClassify', _this.classifyList[_this.index].label)
				let choice = _this.classifyList.find(p => p.label == _this.classifyList[_this.index].label)
				_this.$set(_this.form, 'missionName', choice?.title)
				_this.$set(_this.form, 'missionDesc', choice?.desc)
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
					_this.$request.post('/mission/saveInfo', {
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
			deleteMission() {
				let _this = this
				_this.$request.postForm('/mission/delete', {
						missionID: _this.form.missionID,
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
										_this.back()
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
