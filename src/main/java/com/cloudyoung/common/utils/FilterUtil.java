package com.cloudyoung.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FilterUtil {
	/**
	 * 
	 * @param urlPatterns
	 * @return
	 */
	public static List<String> spliteUrlPatterns(String urlPatterns)
	{
		List<String> urls = null; 
		if (StringUtils.isNotBlank(urlPatterns))
		{
			String[] tmp = StringUtils.split(urlPatterns, "\n");
			urls = new ArrayList<String>();
			
			for (String url:tmp)
			{
				if (StringUtils.isNotBlank(url))
				{
					urls.add(url.trim());
				}
			}
		}
		
		return urls;
	}
}
