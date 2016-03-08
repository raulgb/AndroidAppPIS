package edu.ub.pis2016.pis16.strikecom.engine.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;

public class AndroidFileIO implements FileIO {
	Context context;
	AssetManager assets;
	String externalStoragePath;

	public AndroidFileIO(Context context) {
		this.context = context;
		this.assets = context.getAssets();
		this.externalStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
	}

	public InputStream readAsset(String fileName) {
		try {
			return assets.open(fileName);
		} catch (IOException e) {
			Log.e("FILEIO", "ASSET NOT FOUND: " + fileName);
		}
		return null;
	}

	public InputStream readFile(String fileName) throws IOException {
		return new FileInputStream(externalStoragePath + fileName);
	}

	public OutputStream writeFile(String fileName) throws IOException {
		return new FileOutputStream(externalStoragePath + fileName);
	}


	public SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}