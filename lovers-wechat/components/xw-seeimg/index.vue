<template>
	<view class="">
		<!-- 图片弹窗 -->
		<uni-popup ref="onimg" type="top">
			<view class="information">
				<img :src="imgurl" @longtap="longtap" alt="">
			</view>
		</uni-popup>
	</view>
</template>

<script>
import uniPopup from './uni-popup/uni-popup.vue'
import uniPopupMessage from './uni-popup/uni-popup-message.vue'
import uniPopupDialog from './uni-popup/uni-popup-dialog.vue'
	export default {
		data() {
			return {
				imgurl:"",				//图片路径
			}
		},
		onLoad(){
			
		},
		mounted(){
			
		},
		methods: {
			//子组件调用父组件弹窗信息
			onImg(e){
				this.imgurl = e
				this.$refs.onimg.open()
			},
			//保存图片
			longtap(){
				uni.showModal({
					title: '保存图片',
					content: '是否保存当前图片？',
					success:  (res)=> {
						if (res.confirm) {
							uni.downloadFile({
								url: this.imgurl,
								success: (res) =>{
									if (res.statusCode === 200){
										uni.saveImageToPhotosAlbum({
											filePath: res.tempFilePath,
											success: function() {
												uni.showToast({
													title: "保存成功",
													duration: 2000
												});
											},
											fail: function() {
												uni.showToast({
													title: "保存失败，请稍后重试",
													icon: "none"
												});
											}
										});
									}
								}
							})
						} else if (res.cancel) {
							// uni.showToast({
							// 	title: "你取消了该操作",
							// 	icon:'none',
							// 	duration: 2000
							// });
						}
					}
				});
            }
		},
		components:{
			uniPopup,
			uniPopupMessage,
			uniPopupDialog,
		}
	}
</script>

<style  lang="scss" scoped>
.information{
	width: 700upx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin: 0 auto;
	margin-top: 412upx;
}
.information >img {
	// width: 700upx;
	height: 700upx;
	max-height: 700upx;
	width: auto;
}
</style>
