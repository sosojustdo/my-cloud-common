package com.cloudyoung.common.test;

import com.cloudyoung.common.utils.HttpAsyncUtil;

public class HttpUtilTest {
	
	public static void main(String[] args) {
		String data = HttpAsyncUtil.get("http://121.42.50.13:8081/llb-api/ugc/comment/acceptComment/211455");
		System.out.println(data);
	}

}
