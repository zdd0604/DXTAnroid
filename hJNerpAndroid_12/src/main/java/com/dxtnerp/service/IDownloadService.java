package com.dxtnerp.service;

import android.os.Handler;

public interface IDownloadService
{
	public void scheduleSilentGetImage(String uri, String fileName, Handler handler);
}
