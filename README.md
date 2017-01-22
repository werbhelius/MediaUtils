# MediaUtils

A demo of record audio and video about Android

一个在 Android 上实现录像和录音功能的小例子。

GitHub 地址：[https://github.com/Werb/MediaUtils](https://github.com/Werb/MediaUtils)

[![download](/app/src/main/res/mipmap-xhdpi/video.png)](https://fir.im/cbas) 也可直接点击下载体验

* Android 的 MediaRecorder 相信用过的同学一定踩过很多坑
* 之前写的代码很乱，现在重构了一下，可以直接放到自己的项目中用
* 本例子将 MediaRecorder 进行了重构，通过 MediaUtils 对外暴露几个必需的方法来实现录像和录音功能
* 录制视频界面参考新版微信小视频，还原了微信的动画效果
* 支持 Android 7.0
* 通过线程及 Exception 等手段避免了常见的 start failed , stop failed 等问题
* 重绘了录制视频和录音时的两个自定义view，可在原本基础上进行二次开发
* 提供了视频截图方法，支持双击放大，支持自动对焦
* 视频录制暂时使用 SurfaceView + Camera , 后续会升级为 TextureView + Camera2

### 效果图

<img src="/screenshots/video.gif" alt="screenshot" title="home" width="360" height="640" />
<img src="/screenshots/audio.gif" alt="screenshot" title="home" width="360" height="640" />

### 核心类
#### MediaUtils

* 重构后对外暴露只有8行代码，可结合实际情况使用
```
      // 初始化
      MediaUtils mediaUtils = new MediaUtils(this);
      mediaUtils.setRecorderType(MediaUtils.MEDIA_VIDEO);
      mediaUtils.setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
      mediaUtils.setTargetName(UUID.randomUUID() + ".mp4");
      mediaUtils.setSurfaceView(surfaceView);
      // 开始录制
      mediaUtils.record()
      // 结束录制 save or unSave
      mediaUtils.stopRecordUnSave();
      mediaUtils.stopRecordSave();
```

#### CameraHelper
* 在 Android 中录制视频时显示的 Size 和实际拍照的 Size 是由 Camera 所决定的，最好的方法是计算出可支持的 PreviewSize 和 VideoSize 计算出一个合适的size，同时根据自己视频的用途，选择合理的 Size
* CameraHelper 参考自 GoogleCameraSample 帮助你计算出合适的 Size
```
  public static Camera.Size getOptimalVideoSize(List<Camera.Size> supportedVideoSizes,
          List<Camera.Size> previewSizes, int w, int h) {
      final double ASPECT_TOLERANCE = 0.1;
      double targetRatio = (double) w / h;
      // sizes
      List<Camera.Size> videoSizes;
      if (supportedVideoSizes != null) {
          videoSizes = supportedVideoSizes;
      } else {
          videoSizes = previewSizes;
      }
      Camera.Size optimalSize = null;
      // Start with max value and refine as we iterate over available video sizes. This is the
      // minimum difference between view and camera height.
      double minDiff = Double.MAX_VALUE;
      // Target view height
      int targetHeight = h;
      // Try to find a video size that matches aspect ratio and the target view size.
      // Iterate over all available sizes and pick the largest size that can fit in the view and
      // still maintain the aspect ratio.
      for (Camera.Size size : videoSizes) {
          double ratio = (double) size.width / size.height;
          if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
              continue;
          if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
              optimalSize = size;
              minDiff = Math.abs(size.height - targetHeight);
          }
      }
      // Cannot find video size that matches the aspect ratio, ignore the requirement
      if (optimalSize == null) {
          minDiff = Double.MAX_VALUE;
          for (Camera.Size size : videoSizes) {
              if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
                  optimalSize = size;
                  minDiff = Math.abs(size.height - targetHeight);
              }
          }
      }
      return optimalSize;
  }
```

#### 自定义View
* 仿造新版微信拍摄视频的界面，通过 Paint，RectF，Canvas 绘制
* 突然喜欢上了在 Android 画动效，开启 dribbble 抄动效模式

好了，知识虽小但五脏俱全

欢迎使用我自己开源的一个 Android 图片选择器
* 【PickPhotoView】 Github 地址：[https://github.com/Werb/PickPhotoSample](https://github.com/Werb/PickPhotoSample)

项目中关于 Android M的权限检查用到了，我的另外一个库
* 【PermissionsChecker】Github 地址 ：[https://github.com/Werb/PermissionsCheckerSample](https://github.com/Werb/PermissionsCheckerSample)



### 很高兴你看到这里

> 有时候啊   你总是在追赶前面的人

> 总是抱怨自己为什么不能再努力一点

>累了你可以停下来   看看原来的自己

>其实你已经很了不起了。

我是 wanbo 。
