package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.KeyEvent;

import com.lt.hm.wovideo.AppContext;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.UpdateModel;
import com.lt.hm.wovideo.widget.IPhoneDialog;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;

import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/4/16
 */
public class UpdateManager {
	private static final String isUpdate="1";
	private static final String updateType="1";//非强制
	public static void checkUpdate(final Context context, final DialogClick click) {
		HashMap<String, Object> maps = new HashMap<>();
		maps.put("platform", AppUtils.getPlatForms());
		maps.put("currVersion", AppUtils.getAppVersionCode());
		HttpApis.updateApp(maps, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log(e.getMessage().toLowerCase());
				if (click != null) {
					click.negitive();
				}
			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log("update_info"+response);
				parseResult(response, context, click);

			}
		});

	}

	private static void parseResult(String t, Context context, DialogClick click) {
		if (!StringUtils.isNullOrEmpty(t)) {
			ResponseObj<UpdateModel, RespHeader> resp = new ResponseObj<>();
			ResponseParser.parse(resp, t, UpdateModel.class, RespHeader.class);
			if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
				if (resp.getBody().getIsUpdate().equals(isUpdate) && AppUtils.getAppVersionCode() < Integer.parseInt(resp.getBody().getLatestVersion().getVersionCode())) {
					alertDialog(resp.getBody(), context, click);
				} else {
					// 弹出提示框，并且下载  有 是否强制更新操作选项
					if (click != null) {
						click.negitive();
					}
				}
			} else {
//					ToastUtils.showToast(context.getResources().getString(R.string.current_lastest_version));
				if (click != null) {
					click.negitive();
				}
			}
		}
	}


	private static void alertDialog(UpdateModel model, Context context, final DialogClick click) {
		String strHtml;
//		String strHtml = "\t\t<font color=\"black\">目前有新版本，有待升级</font><br>\n";
//		strHtml += "<font color=\"black\">版本号为";
//		strHtml += resultVO.getRET_VO().get(0).getVersionName();
//		strHtml += "</font>";
//		strHtml += "<br />\n" +
//				"\t<font color=\"black\">";
//		strHtml += resultVO.getRET_VO().get(0).getIntroduction();
//		strHtml += "</font>";

		strHtml= model.getLatestVersion().getIntroduction();

		IPhoneDialog dialog = new IPhoneDialog(context);
		IPhoneDialog.Builder builder = new IPhoneDialog.Builder(context);
		if (model.getLatestVersion().getUpdateType().equals(updateType)) {
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (click != null) {
						click.negitive();
						click.isDismiss();
					}
				}
			});
		} else {
			builder.setNegativeButton(null, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (click != null) {
						click.negitive();
						click.isDismiss();
					}
				}
			});
		}
		builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (click != null) {
					if (!StringUtils.isNullOrEmpty(model.getLatestVersion().getUrl())) {
						dialog.dismiss();
						if (click != null) {
							click.isDismiss();
						}
						DownloadAPK(model, click, model.getLatestVersion().getUpdateType());
					} else {
//						ToastUtils.showToast("服务器无法链接");
						dialog.dismiss();
						if (click != null) {
							click.isDismiss();
						}
					}
					click.positive();
				} else {
					if (!StringUtils.isNullOrEmpty(model.getLatestVersion().getUrl())) {
						dialog.dismiss();
						if (click != null) {
							click.isDismiss();
						}
//						ToastUtils.showToast("后台下载 中");
						DownloadAPK(model, click, model.getLatestVersion().getUpdateType());
					} else {
//						ToastUtils.showToast("服务器无法链接");
						dialog.dismiss();
						if (click != null) {
							click.isDismiss();
						}
					}
				}
			}
		});
		builder.setMessage(Html.fromHtml(strHtml).toString());
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		dialog = builder.create();
		dialog.show();
		if (click!=null){
			click.isShow();
		}

	}




	/**
	 * 下载APP
	 *
	 * @param model
	 */
	private static void DownloadAPK(UpdateModel model, final DialogClick click, final String type) {
		final String path = AppUtils.getDownloadPath() + "/" + "woVideo.apk";
		TLog.log(path);
		HttpUtils.downFile(model.getLatestVersion().getUrl(), new FileCallBack(AppUtils.getDownloadPath(),"woVideo.apk") {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log(e.getMessage());
				if (click != null) {
					click.onFinish(false, type);
				}
			}

			@Override
			public void onResponse(File response, int id) {
				installApp(AppContext.context(), path);
				System.exit(0);

				if (click != null) {
					click.onFinish(true, type);
				}
			}
		});

	}

	private static void installApp(Context context, String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}


	public interface DialogClick {
		void positive();
		void negitive();
		void onFinish(boolean flag, String tag);
		void isShow();
		void isDismiss();
	}
}