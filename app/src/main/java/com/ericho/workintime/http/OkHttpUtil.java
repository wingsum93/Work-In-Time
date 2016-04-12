package com.ericho.workintime.http;

import android.util.Log;

import com.ericho.workintime.BuildConfig;
import com.ericho.workintime.Common;
import com.ericho.workintime.MyApplication;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by EricH on 17/2/2016.
 */
public class OkHttpUtil {
	final String t = "OkHttpUtil";
	final String middleUrl = "/scm-wms/json/";

	//POST constant
	public final String service_ticket = "serviceTicket";
	public final String party_id = "partyId";
	public final String google_token = "google_token";
	public final String device_code = "deviceCode";
	public final String pageNo = "page_no";
	public final String max = "max";
	public final String searchStr = "searchStr";
	public final String photo = "photo";
	public final String operationMetaId = "operationMetaId";
	public final String metaId = "metaId";
	public final String itemId = "itemId";
	private final String username = "username";
	private final String taskId = "taskId";
	private final String deviceCode = "deviceCode";
	public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



	static OkHttpUtil instance;
	public FormBody.Builder builder;

	public static synchronized OkHttpUtil getInstance() {
		if (instance == null) {
			instance = new OkHttpUtil();
		}
		return instance;
	}
	public OkHttpUtil(){
		mOkHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor())
				.build();
	}
	private final OkHttpClient mOkHttpClient ;

	/**
	 * 该不会开启异步线程。
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Response execute(Request request) throws IOException {
		return mOkHttpClient.newCall(request).execute();
	}
	/**
	 * 开启异步线程访问网络
	 * @param request
	 * @param responseCallback
	 */
	public void enqueue(Request request, Callback responseCallback){
		mOkHttpClient.newCall(request).enqueue(responseCallback);
	}
	/**
	 * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
	 * @param request
	 */
	public void enqueue(Request request){
		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

			}
		});
	}
	public String getStringFromServer(String url) throws IOException{
		Request request = new Request.Builder().url(url).build();
		Response response = execute(request);
		if (response.isSuccessful()) {
			String responseUrl = response.body().string();
			return responseUrl;
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}
	private static final String CHARSET_NAME = "UTF-8";


	/**
	 * 为HttpGet 的 url 方便的添加1个name value 参数。
	 * @param url
	 * @param name
	 * @param value
	 * @return
	 */
	public String attachHttpGetParam(String url, String name, String value){
		return url + "?" + name + "=" + value;
	}


	public String sentPostSync(String url,HashMap<String,String> map) throws IOException {
//		mOkHttpClient.interceptors().add(new LoggingInterceptor());
		FormBody.Builder builder = new FormBody.Builder();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			builder.add(entry.getKey(),entry.getValue());
		}

		Request request = new Request.Builder()
				.url(url)
				.post(builder.build())
				.build();

		Response response = mOkHttpClient.newCall(request).execute();
		if(!response.isSuccessful()) throw new IOException("network");
		return response.body().string();

	}
	public String sentPostSync(String url,FormBody.Builder builder) throws IOException {
		FormBody formBody = builder.build();
		Request request = new Request.Builder()
				.url(url)
				.post(formBody)
				.build();
//		build
		Response response = null; StringBuilder bb = new StringBuilder();
		for(int i=0;i<formBody.size();i++) {
			bb.append(formBody.name(i));bb.append(":");bb.append(formBody.value(i));bb.append("\n");
		}
		Log.d(t, bb.toString());

		try{
			response = mOkHttpClient.newCall(request).execute();// connection exception throw
		}catch (IOException ee){
			if(response != null) response.body().close(); throw ee;
		}
		if(!response.isSuccessful() && response.code() != 200) {
			response.body().close();
			throw new IOException("network, Code= "+response.code());
		}
		String result = response.body().string();

		Log.d(t,result);
		return result;
	}


	/** ----------------------------------------------------------------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------------------------------------------------------------------------
	 * --------------------------------------------------------------------------------------------------------------------------------------------------------
	 * --------------------------------------------------------------------------------------------------------------------------------------------------
	 * beloaw are standard web call,
	 *
	 */





	/**
	 * Sync method
	 * @param goo_token
	 * @param device_code
	 * @return
	 * @throws IOException
	 */
	public String registerToken(String goo_token, String device_code ) throws IOException {
		FormBody.Builder builder = new FormBody.Builder();
		builder.add(google_token, goo_token);
		builder.add(this.device_code, device_code);
		builder.add("kk", BuildConfig.FLAVOR);

		return sentPostSync(getUrl(WebUrl.UPLOAD_TOKEN), builder);
	}





	public String getUrl(String suffix){
		String s = Common.getServerAddress(MyApplication.getContext()) + middleUrl + suffix;
		Log.d(t,"serverUrl= "+ s);
		return  s;
	}
}
